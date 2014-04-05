import hook.HookGenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.runner.JUnitCore;

import container.Container;
import exception.ContainerException;
import exception.ScriptException;
import robot.Cote;
import robot.PositionRateau;
import robot.RobotChrono;
import robot.RobotVrai;
import scripts.Script;
import scripts.ScriptManager;
import smartMath.Vec2;
import table.Table;
import threads.ThreadTimer;
import utils.Log;
import utils.Read_Ini;

		
public class lanceur
{
	
	private static Test test = new Test();
	
	public static void main(String[] args)
	{
		Container container;
		try {
			container = new Container();
			Read_Ini config = (Read_Ini) container.getService("Read_Ini");
			Log log = (Log) container.getService("Log");
		ThreadTimer threadtimer = (ThreadTimer) container.getService("threadTimer");
		// compléter avec: vitesse, position, ...
		ScriptManager scriptmanager = (ScriptManager)container.getService("ScriptManager");
		RobotVrai robotvrai = (RobotVrai)container.getService("RobotVrai");
		RobotChrono robotchrono = new RobotChrono(config, log);
		//config.set("couleur", "jaune");
		robotchrono.majRobotChrono(robotvrai);
		Table table = (Table)container.getService("Table");
		HookGenerator hookgenerator = (HookGenerator)container.getService("HookGenerator");
		//robotvrai.setPosition(new Vec2(1300, 1700));
		robotvrai.setPosition(new Vec2(1251, 1695));
		robotvrai.setOrientation((float)(-Math.PI/2));
		robotvrai.set_vitesse_rotation("entre_scripts");
		robotvrai.set_vitesse_translation("entre_scripts");
		container.getService("threadPosition");
		container.demarreThreads();
		//robotvrai.set_vitesse_translation("30");
		/*On aura 3 inputs 
		Le premier pour la couleur du robot avec 0 pour jaune et 1 pour rouge
		Le deuxième pour les arbres 0 et 3 (on donne pour 0 et pour le 3 ça sera calculé facilement)
		Le troisième pour les arbres 1 et 2 (one donne pour 1 et pour les 2 ça sera calculé facilement) 
		La position des fruits dans un arbre est expliqué dans la classe Tree
		*/
		//--------------------------------------------------------------
				//Début des paramétrages
				String couleur = "";
				while(!couleur.contains("0") && !couleur.contains("1"))
				{
					System.out.println("Rentrez 0 pour jaune et 1 pour rouge : ");
					BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in)); 
					 
					couleur = keyboard.readLine(); 
					
					
					if(couleur.contains("0"))
						config.set("couleur","jaune");
					else if(couleur.contains("1"))
						config.set("couleur", "rouge");
					robotvrai.update_couleur();
				}
				
				
				// On initialise des trucs
				
				
				
				
				//Pour les fruits noirs
				String pos_noir1 = "";
				String pos_noir2 = "";
				while(!(pos_noir1.contains("0")|| pos_noir1.contains("1")|| pos_noir1.contains("2")||pos_noir1.contains("3")||pos_noir1.contains("4")||pos_noir1.contains("5")))
				{
					System.out.println("Donnez la position des fruits noirs pour les arbres 0 et 3 : ");
					BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in)); 
					pos_noir1 = keyboard.readLine(); 
					if(pos_noir1.contains("0"))
					{
						table.setFruitNoir(0, 0);
						table.setFruitNoir(3, 3);
					}
					if(pos_noir1.contains("1"))
					{
						table.setFruitNoir(0, 1);
						table.setFruitNoir(3, 4);
					}
					if (pos_noir1.contains("2"))
					{
						table.setFruitNoir(0, 2);
						table.setFruitNoir(3, 5);
					}
					if (pos_noir1.contains("3"))
					{
						table.setFruitNoir(0, 3);
						table.setFruitNoir(3, 0);
					}
					if (pos_noir1.contains("4"))
					{
						table.setFruitNoir(0, 4);
						table.setFruitNoir(3, 1);
					}
					if (pos_noir1.contains("5"))
					{
						table.setFruitNoir(0, 5);
						table.setFruitNoir(3, 2);
					}
				}		
				while(!(pos_noir2.contains("0")|| pos_noir2.contains("1")|| pos_noir2.contains("2")||pos_noir2.contains("3")||pos_noir2.contains("4")||pos_noir2.contains("5")))
				{
					System.out.println("Donnez la position des fruits noirs pour les arbres 1 et 2 : ");
					BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in)); 
					pos_noir2 = keyboard.readLine(); 
					if(pos_noir2.contains("0"))
					{
						table.setFruitNoir(0, 0);
						table.setFruitNoir(3, 3);
					}
					if(pos_noir2.contains("1"))
					{
						table.setFruitNoir(0, 1);
						table.setFruitNoir(3, 4);
					}
					if (pos_noir2.contains("2"))
					{
						table.setFruitNoir(0, 2);
						table.setFruitNoir(3, 5);
					}
					if (pos_noir2.contains("3"))
					{
						table.setFruitNoir(0, 3);
						table.setFruitNoir(3, 0);
					}
					if (pos_noir2.contains("4"))
					{
						table.setFruitNoir(0, 4);
						table.setFruitNoir(3, 1);
					}
					if (pos_noir2.contains("5"))
					{
						table.setFruitNoir(0, 5);
						table.setFruitNoir(3, 2);
					}
						
				}
				//Fin paramétrage terrain
				//------------------------------------------------------
		

		while(!threadtimer.match_demarre)
		{
			Thread.sleep(100);
		}
		
		robotvrai.initialiser_actionneurs_deplacements();
		robotvrai.recaler();
		//Le dégager
		robotvrai.avancer(200);
		robotvrai.tourner((float)(-Math.PI/2-Math.PI/6));
		robotvrai.avancer(300);
		
		
			if(couleur.contains("0"))
				//C'est jaune
			{
				while (true)
				{
					try{
						//On va lancer des balles sur le mammouth
						Script s_lances0 = (Script)scriptmanager.getScript("ScriptLances");
						s_lances0.agit(0, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try{
						//On va déposer la fresque
						
						Script s_fresque = (Script)scriptmanager.getScript("ScriptFresque");
						s_fresque.agit(2, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
						//On va prendre des fruits dans l'arbre 0
						Script s_arbre0 = (Script)scriptmanager.getScript("ScriptTree");
						s_arbre0.agit(0, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
						
						//dans l'arbre 1
						Script s_arbre1 = (Script)scriptmanager.getScript("ScriptTree");
						s_arbre1.agit(1, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
						//On va lancer des balles sur l'autre mammouth
						Script s_lances1 = (Script)scriptmanager.getScript("ScriptLances");
						s_lances1.agit(1, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
						//On dépose les fruits
						Script s_depot0 = (Script)scriptmanager.getScript("ScriptDeposerFruits");
						s_depot0.agit(1, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					
					try
					{
						//On prend des fruits sur l'arbre 3
						Script s_arbre3 = (Script)scriptmanager.getScript("ScriptTree");
						s_arbre3.agit(3, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
						//On prend des fruits sur l'arbre 2
						Script s_arbre2 = (Script)scriptmanager.getScript("ScriptTree");
						s_arbre2.agit(2, robotvrai, table, true);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}		
					try
					{
						//On dépose encore des fruits
						Script s_depot1 = (Script)scriptmanager.getScript("ScriptDeposerFruits");
						s_depot1.agit(1, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					
					
					
					
				}
			}
			else
			{
				//Si la couleur est rouge
				while (true)
				{
					try{
						//On va lancer des balles sur le mammouth
						Script s_lances1 = (Script)scriptmanager.getScript("ScriptLances");
						s_lances1.agit(0, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try{
						//On va déposer la fresque
						
						Script s_fresque = (Script)scriptmanager.getScript("ScriptFresque");
						s_fresque.agit(2, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					
					try
					{
						//On va prendre des fruits dans l'arbre 0
						Script s_arbre3 = (Script)scriptmanager.getScript("ScriptTree");
						s_arbre3.agit(0, robotvrai, table, true);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
						//On va prendre des fruits dans l'arbre 1
						Script s_arbre2 = (Script)scriptmanager.getScript("ScriptTree");
						s_arbre2.agit(1, robotvrai, table, true);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
						//On va lancer des balles sur l'autre mammouth
						Script s_lances0 = (Script)scriptmanager.getScript("ScriptLances");
						s_lances0.agit(1, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
						//On dépose les fruits
						Script s_depot0 = (Script)scriptmanager.getScript("ScriptDeposerFruits");
						s_depot0.agit(1, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					
					try
					{
						//On prend des fruits sur l'arbre 3
						Script s_arbre3 = (Script)scriptmanager.getScript("ScriptTree");
						s_arbre3.agit(3, robotvrai, table, true);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
						//On prend des fruits sur l'arbre 2
						Script s_arbre2 = (Script)scriptmanager.getScript("ScriptTree");
						s_arbre2.agit(2, robotvrai, table, true);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					
					
					
					try
					{
						//On dépose encore des fruits
						Script s_depot1 = (Script)scriptmanager.getScript("ScriptDeposerFruits");
						s_depot1.agit(1, robotvrai, table, false);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
					try
					{
					if(System.currentTimeMillis() - threadtimer.date_debut > threadtimer.duree_match-15)
					{
						Script s = (Script)scriptmanager.getScript("ScriptFunnyAction");
						s.agit(0, robotvrai, table, false);
					}
					} catch (ScriptException e) {
						e.printStackTrace();
					}
				}
			}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}

	}
