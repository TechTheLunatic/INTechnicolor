package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import exception.MouvementImpossibleException;
import robot.*;
import robot.cartes.*;
import smartMath.Vec2;

	/**
	 * Tests unitaires pour RobotVrai (non, sans blague...), lorsqu'il est jaune
	 * @author pf
	 *
	 */
public class JUnit_RobotVraiJauneTest extends JUnit_Test {

	private RobotVrai robotvrai;
	private Deplacements deplacements;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		log.debug("JUnit_RobotVraiJauneTest.setUp()", this);
		config.set("couleur", "jaune");
		robotvrai = (RobotVrai) container.getService("RobotVrai");
		deplacements = (Deplacements)container.getService("Deplacements");
		deplacements.set_x(0);
		deplacements.set_y(1500);
		deplacements.set_orientation(0);
		deplacements.set_vitesse_translation(80);
		deplacements.set_vitesse_rotation(130);
	}

	@Test
	public void test_setPosition() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_setPosition()", this);
		robotvrai.setPosition(new Vec2(300, 400));
		float[] infos_float = deplacements.get_infos_x_y_orientation();
		Assert.assertTrue(infos_float[0] == 300);
		Assert.assertTrue(infos_float[1] == 400);
		Assert.assertTrue(infos_float[2] == 0);
	}

	@Test
	public void test_setOrientation() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_setOrientation()", this);
		robotvrai.setOrientation((float)1.2);
		float[] infos_float = deplacements.get_infos_x_y_orientation();
		assertEquals(0, infos_float[0], 0);
		assertEquals(1500, infos_float[1], 0);
		assertEquals(1200, infos_float[2], 0);
	}
	
	@Test
	public void test_getOrientation() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_getOrientation()", this);
		deplacements.tourner((float)1.2);
		Thread.sleep(500);
		robotvrai.update_x_y_orientation();
		assertEquals(robotvrai.getOrientation(), 1.2, 0.001);
	}

	@Test
	public void test_update_x_y_orientation() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_update_x_y_orientation()", this);
		deplacements.set_x(0);
		deplacements.set_y(500);
		robotvrai.update_x_y_orientation();
		Assert.assertTrue(robotvrai.getPosition().equals(new Vec2(0,500)));
	}
	
	@Test
	public void test_avancer() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_avancer()", this);
		robotvrai.setOrientation(0);
		container.getService("threadPosition");
		container.demarreThreads();
		Thread.sleep(100);
		robotvrai.update_x_y_orientation();
		robotvrai.avancer(10);
		robotvrai.update_x_y_orientation();
		Assert.assertTrue(robotvrai.getPosition().equals(new Vec2(10,1500)));
	}

	@Test
	public void test_stopper() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_stopper()", this);
		robotvrai.setOrientation(0);
		container.getService("threadPosition");
		container.demarreThreads();
		Thread.sleep(100);
		robotvrai.update_x_y_orientation();
		robotvrai.avancer(100);
		Thread.sleep(10);
		robotvrai.stopper();
		robotvrai.update_x_y_orientation();
		Assert.assertTrue(!robotvrai.getPosition().equals(new Vec2(10,1500)));
	}

	@Test
	public void test_va_au_point() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_va_au_point()", this);
		container.getService("threadPosition");
		container.demarreThreads();
		Thread.sleep(100);
		robotvrai.va_au_point(new Vec2(10, 1400));
		robotvrai.update_x_y_orientation();
		log.debug(robotvrai.getPosition(), this);
		Assert.assertTrue(robotvrai.getPosition().distance(new Vec2(10,1400)) < 2);
	}

	@Test
	public void test_trajectoire_courbe() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_correction_trajectoire()", this);
		container.getService("threadPosition");
		container.demarreThreads();
		Thread.sleep(100);
		ArrayList<Vec2> chemin = new ArrayList<Vec2>();
		chemin.add(new Vec2(1150, 1050));
		chemin.add(new Vec2(900, 400));
		robotvrai.suit_chemin(chemin, true, true);		
	}
	
	@Test
	public void test_correction_trajectoire() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_correction_trajectoire()", this);
		container.getService("threadPosition");
		container.demarreThreads();
		Thread.sleep(100);
		deplacements.avancer(400);
		robotvrai.va_au_point(new Vec2(1100, 900));
		robotvrai.update_x_y_orientation();
		log.debug(robotvrai.getPosition(), this);
		Assert.assertTrue(robotvrai.getPosition().distance(new Vec2(10,1400)) < 2);
	}

	@Test
	public void test_tourner() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_tourner()", this);
		container.getService("threadPosition");
		container.demarreThreads();
		Thread.sleep(100);
		robotvrai.tourner((float)1.2);
		assertEquals(robotvrai.getOrientation(), 1.2, 0.001);
	}

	@Test
	public void test_tourner_symetrie_2() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_tourner_symetrie_2()", this);
		container.getService("threadPosition");
		container.demarreThreads();
		Thread.sleep(100);
		robotvrai.tourner((float)1.2, true);
		log.debug(robotvrai.getOrientation(), this);
		assertEquals(robotvrai.getOrientation(), 1.2, 0.001);
	}

	@Test
	public void test_suit_chemin() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_suit_chemin()", this);
		container.getService("threadPosition");
//		container.demarreThreads();
		Thread.sleep(100);
		ArrayList<Vec2> chemin = new ArrayList<Vec2>();
		chemin.add(new Vec2(20, 1400));
		chemin.add(new Vec2(1200, 1500));
		robotvrai.suit_chemin(chemin);
		robotvrai.update_x_y_orientation();
		log.debug(robotvrai.getPosition(), this);
		Assert.assertTrue(robotvrai.getPosition().distance(new Vec2(1200,1500)) < 5);
	}

	@Test(expected=MouvementImpossibleException.class)
	public void test_exception_collision() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_exception_collision()", this);
		container.getService("threadPosition");
		container.demarreThreads();
		Thread.sleep(100);
		robotvrai.setPosition(new Vec2(0, 1800));
		robotvrai.setOrientation((float)Math.PI/2);
		robotvrai.avancer(500, false, false);
	}

	@Test
	public void test_sans_exception_collision() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_sans_exception_collision()", this);
		container.getService("threadPosition");
		container.demarreThreads();
		Thread.sleep(100);
		robotvrai.setPosition(new Vec2(0, 1800));
		robotvrai.setOrientation((float)Math.PI/2);
		robotvrai.avancer(500, false, true);
	}

	@Test
	public void test_detection_ennemi_sans_exception() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_detection_ennemi_sans_exception()", this);
		robotvrai.setPosition(new Vec2(0, 900));
		robotvrai.setOrientation(0);
		container.getService("threadPosition");
		container.getService("threadCapteurs");
		container.demarreThreads();
		Thread.sleep(300);
		robotvrai.avancer(500, false, true);
	}

	@Test(expected=MouvementImpossibleException.class)
	public void test_detection_ennemi() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_detection_ennemi()", this);
		robotvrai.setPosition(new Vec2(0, 900));
		robotvrai.setOrientation(0);
		container.getService("threadPosition");
		container.getService("threadCapteurs");
		container.demarreThreads();
		Thread.sleep(300);
		robotvrai.avancer(500, false);
	}

	@Test
	public void test_degager_mur() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_degager_mur()", this);
		test_sans_exception_collision();
		robotvrai.avancer(-500);
	}

	@Test
	public void test_actionneurs() throws Exception
	{
		log.debug("JUnit_RobotVraiJauneTest.test_actionneurs()", this);
		robotvrai.initialiser_actionneurs_deplacements();
		robotvrai.recaler();
		robotvrai.bac_bas();
		robotvrai.bac_haut();
		robotvrai.deposer_fresques();
		robotvrai.isFresquesPosees();
		robotvrai.rateau(PositionRateau.BAS, Cote.DROIT);
		robotvrai.rateau(PositionRateau.BAS, Cote.GAUCHE);
		robotvrai.rateau(PositionRateau.HAUT, Cote.DROIT);
		robotvrai.rateau(PositionRateau.HAUT, Cote.GAUCHE);
		robotvrai.rateau(PositionRateau.RANGER, Cote.DROIT);
		robotvrai.rateau(PositionRateau.RANGER, Cote.GAUCHE);
		robotvrai.rateau(PositionRateau.SUPER_BAS, Cote.DROIT);
		robotvrai.rateau(PositionRateau.SUPER_BAS, Cote.GAUCHE);
		robotvrai.tirerBalle();
		robotvrai.takefire();
		robotvrai.sleep(100);
	}

}