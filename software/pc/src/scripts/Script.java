package scripts;

import smartMath.Vec2;
import strategie.GameState;
import hook.Callback;
import hook.Executable;
import hook.Hook;
import robot.RobotChrono;
import robot.RobotVrai;
import utils.Log;
import utils.Read_Ini;
import container.Service;
import hook.methodes.DisparitionTorche;
import hook.methodes.TakeFire;
import hook.sortes.HookGenerator;
import java.util.ArrayList;

import enums.Cote;
import exceptions.deplacements.MouvementImpossibleException;
import exceptions.serial.SerialException;
import exceptions.strategie.PathfindingException;
import exceptions.strategie.ScriptException;
/**
 * Classe abstraite dont hériteront les différents scripts. S'occupe le robotvrai et robotchrono de manière à ce que ce soit transparent pour les différents scripts
 * @author pf
 */

public abstract class Script implements Service {

	// Ces services resteront toujours les mêmes, on les factorise avec un static
	protected static HookGenerator hookgenerator;
	protected static Read_Ini config;
	protected static Log log;
	
	private ArrayList<Hook> hooks_chemin = new ArrayList<Hook>();
	
	protected String couleur; 
	private int rayon_robot;
	
	public Script(HookGenerator hookgenerator, Read_Ini config, Log log)
	{
		Script.hookgenerator = hookgenerator;
		Script.config = config;
		Script.log = log;
		
		couleur = config.get("couleur");
		rayon_robot = Integer.parseInt(config.get("rayon_robot"));
}
		
	/**
	 * Exécute vraiment un script
	 */
	public void agit(int id_version, GameState<RobotVrai> state, boolean retenter_si_blocage) throws ScriptException
	{
		Vec2 point_entree = point_entree(id_version);

		state.robot.set_vitesse_translation("entre_scripts");
		state.robot.set_vitesse_rotation("entre_scripts");

		Executable takefire = new TakeFire(state.robot, Cote.GAUCHE);
		Hook hook = hookgenerator.hook_feu(Cote.GAUCHE);
		hook.ajouter_callback(new Callback(takefire, true));		
		hooks_chemin.add(hook);

		takefire = new TakeFire(state.robot, Cote.DROIT);
		hook = hookgenerator.hook_feu(Cote.DROIT);
		hook.ajouter_callback(new Callback(takefire, true));
		hooks_chemin.add(hook);

		Executable torche_disparue = new DisparitionTorche(state.table, Cote.DROIT);
		hook = hookgenerator.hook_position(state.table.getPositionTorche(Cote.DROIT), state.table.getRayonTorche(Cote.DROIT)+rayon_robot);
		hook.ajouter_callback(new Callback(torche_disparue, true));
		hooks_chemin.add(hook);
	
		torche_disparue = new DisparitionTorche(state.table, Cote.GAUCHE);
		hook = hookgenerator.hook_position(state.table.getPositionTorche(Cote.GAUCHE), state.table.getRayonTorche(Cote.GAUCHE)+rayon_robot);
		hook.ajouter_callback(new Callback(torche_disparue, true));
		hooks_chemin.add(hook);

		
		
		// on doit appeller score avant de faire le script (sinon, une fois le script fait, il ne reste plus de points a prendre)
		int scorePotentiel = score(id_version, state);
		
		try
		{
		    state.robot.va_au_point_pathfinding(state.pathfinding, point_entree, hooks_chemin);
			execute(id_version, state);
			

			// Prends en compte le nombre de points que l'on marque par ce script
			state.pointsObtenus += scorePotentiel;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// Si on rencontre un obstacle en allant exécuter un script et qu'il reste d'autres scripts, alors on change de script
			throw new ScriptException();
		}
		finally
		{
			termine(state);
		}

		
	}

	/**
	 * Calcule le temps d'exécution de ce script (grâce à robotChrono)
	 * @return le temps d'exécution
	 * @throws PathfindingException 
	 */
	public long metacalcule(int id_version, GameState<RobotChrono> state, boolean use_cache) throws PathfindingException
	{	    
		long duree = calcule(version_asso(id_version).get(0), state, use_cache);
		state.time_depuis_debut += duree;
        state.time_depuis_racine += duree;
		return duree;
	}
	
	/**
	 * Calcule le temps d'exécution de ce script (grâce à robotChrono)
	 * @return le temps d'exécution
	 * @throws PathfindingException 
	 */
	public long calcule(int id_version, GameState<RobotChrono> state, boolean use_cache) throws PathfindingException
	{
		Vec2 point_entree = point_entree(id_version);
		state.robot.set_vitesse_translation("entre_scripts");
		state.robot.set_vitesse_rotation("entre_scripts");


		state.robot.initialiser_compteur(state.pathfinding.distance(state.robot.getPositionFast(), point_entree, use_cache));
		state.robot.setPosition(point_entree);

		try {
			execute(id_version, state);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return state.robot.get_compteur();
	}
	/**
	 * Renvoie les versions associées associée à une méta-version
	 * @return le tableau des versions associée
	 */
	public abstract ArrayList<Integer> version_asso(int id_meta);
	
	/**
	 * Renvoie le tableau des méta-verions d'un script
	 * @return le tableau des méta-versions possibles
	 */
	public abstract ArrayList<Integer> meta_version(final GameState<?> state);
		

	/**
	 * Retourne la position d'entrée associée à la version id
	 * @param id de la version
	 * @return la position du point d'entrée
	 */
	public abstract Vec2 point_entree(int id);
	/**
	 * Renvoie le score que peut fournir une méta-version d'un script
	 * @return le score
	 */
	
	public int meta_score(int id_metaversion, GameState<?> state)
	{
	    ArrayList<Integer> versions = version_asso(id_metaversion);
        if(versions == null)
            return -1;
	    int max = versions.get(0);
	    for(Integer v: versions)
	        if(score(v, state) > score(max, state))
	            max = v;
		return score(max, state);
	}

	/**
	 * Renvoie le score que peut fournir une version d'un script
	 * @return le score
	 */
	public abstract int score(int id_version, final GameState<?> state);
	
	/**
 	 * Donne le poids du script, utilisé pour calculer sa note
	 * @return le poids
	 */
	public abstract int poids(final GameState<?> state);

	/**
	 * Exécute le script, avec RobotVrai ou RobotChrono
	 * @throws SerialException 
	 */
	abstract protected void execute(int id_version, GameState<?> state) throws MouvementImpossibleException, SerialException;

	/**
	 * Méthode toujours appelée à la fin du script (via un finally). Repli des actionneurs.
	 */
	abstract protected void termine(GameState<?> state);
	
	public void maj_config()
	{
	}

	
}
