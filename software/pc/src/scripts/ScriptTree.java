package scripts;

import robot.RobotChrono;
import smartMath.Vec2;
import table.Table;
import container.Service;
import java.util.ArrayList;
public class ScriptTree extends Script{

	public ScriptTree(Service pathfinding, Service threadtimer,
			Service robotvrai, Service robotchrono, Service hookgenerator,
			Service table, Service config, Service log) {
		super(pathfinding, threadtimer, robotvrai, robotchrono, hookgenerator, table,
				config, log);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Integer> version() {
		int i;
		ArrayList<Integer> versionsList = new ArrayList<Integer>();
		for (i=0; i<=3; i++)
		{
			if (table.isTreeTaken(i) == false)
				{
					versionsList.add(i);
				}
		}
		return versionsList;
	}

	@Override
	/* ici, pour chaque version on va avoir un unique point d'entrée donné, donc la fonction
	  de récupération des points d'entrée possibles est inutile pour les arbres */
	public Vec2 point_entree(int id_version) {
		Vec2 entree = new Vec2();

		// Hardcoder les positions
		
		/*
		if (id_version == 0) 
		{
			entree.x = (table.getATree()[0].getPosition().x)-(150+335); //les constantes sont respectivement le rayon de l'arbre, la taille nécessaire à allonger le rateau sans toucher la cime par rapport à la position du centre de la bat-mobile.
			entree.y = (table.getATree()[0].getPosition().y) ; // les positions des arbres étant constantes, pourquoi ne pas les marquer en dur ?
		}
		if (id_version == 1)
		{
			entree.x = (table.getATree()[1].getPosition().x);
			entree.y = (table.getATree()[1].getPosition().y)+(150+335);
		}
		if (id_version == 2)
		{
			entree.x = (table.getATree()[2].getPosition().x);
			entree.y = (table.getATree()[2].getPosition().y)+(150+335);
		}
		if (id_version ==3)
		{
			entree.x = (table.getATree()[3].getPosition().x)+(150+335);
			entree.y = (table.getATree()[3].getPosition().y);
		}
		*/
		return entree;
	}

	@Override
	public int score(int id_version) {
		int res = 0;
		if (id_version <= 1)
		{
			res = table.nbrTotal(0) + table.nbrTotal(1);
		}
		else
		{
			res = table.nbrTotal(2) + table.nbrTotal(3);
		}
		return res;
	}

	@Override
	public int poids() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void execute(int id_version) {
		
		
	}

	@Override
	protected void termine() {
		// TODO Auto-generated method stub
		
	}

}
