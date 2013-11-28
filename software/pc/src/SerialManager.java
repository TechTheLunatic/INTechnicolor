import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


public class SerialManager 
{
	//Series à instancier
	public Serial serieAsservissement;
	public Serial serieCapteursActionneurs;
	public Serial serieLaser;
	
	//Pour chaque carte, on connait à l'avance son nom, son ping et son baudrate
	private SpecificationCard asservissement = new SpecificationCard("deplacements", 0, 9600);
	private SpecificationCard capteurs_actionneurs = new SpecificationCard("capteurs_actionneurs", 3, 9600);
	private SpecificationCard laser = new SpecificationCard("laser", 4, 9600); //à decommenter 38400);
	
	//On stock les cartes dans une liste
	private Hashtable<String, SpecificationCard> cards = new Hashtable<String, SpecificationCard>();
	
	//Liste pour stocker les series qui sont connectees au pc 
	private ArrayList<String> connectedSerial = new ArrayList<String>();
	
	//Liste pour stocker les baudrates des differentes serie
	private ArrayList<Integer> baudrate = new ArrayList<Integer>();

	//Recuperation de toutes les cartes dans cards et des baudrates dans baudrate
	SerialManager()
	{
		cards.put("asservissement", asservissement);
		cards.put("capteurs_actionneurs", capteurs_actionneurs);
		cards.put("laser", laser);

		Enumeration<SpecificationCard> e = cards.elements();
		while (e.hasMoreElements())
		{
			int baud = e.nextElement().baudrate;
			if (!this.baudrate.contains(baud))
				this.baudrate.add(baud);
		}
	}

	//Regarde toutes les series qui sont branchees dans /dev/ttyUSB*
	public  void checkSerial()
	{
		Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();
		while (ports.hasMoreElements())
		{
			CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
			this.connectedSerial.add(port.getName());
		}
	}

	public void createSerial()
	{
		int id = -1;
		//Liste des series dejà attribues
		ArrayList<Integer> deja_attribues = new ArrayList<Integer>();
		String pings[] = new String[4];
		for (int baudrate : this.baudrate)
		{
			System.out.println("liste des pings pour le baudrate " + baudrate);

			for(int k = 0; k < this.connectedSerial.size(); k++)
			{
				if (!deja_attribues.contains(k))
				{
					Serial serialTest = new Serial();
					try
					{
						serialTest.initialize(this.connectedSerial.get(k), baudrate);
					}
					catch (NoSuchPortException e1)
					{
						e1.printStackTrace();
					}
					id = Integer.parseInt(serialTest.ping());
					if(!isKnownPing(id))
						continue;

					//On stock le port de la serie dans le tabeau qui stock les pings
					pings[id] = this.connectedSerial.get(k);

					//Après les tests de pings sur la serie, on ferme la communication
					serialTest.close();

					deja_attribues.add(k);

					System.out.println(id + " sur: " + connectedSerial.get(k));

				}
			}
		}
		//Association de chaque serie à son port
		Enumeration<SpecificationCard> e = cards.elements();
		while (e.hasMoreElements())
		{
			SpecificationCard serial = e.nextElement();
			if(serial.id == 0 && pings[id] != "")
			{
				try
				{
					this.serieAsservissement.initialize(pings[id], serial.baudrate);
				}
				catch (NoSuchPortException e1)
				{
					e1.printStackTrace();
				}
			}
			else if(serial.id == 1 && pings[id] != "")
			{
				try
				{
					this.serieAsservissement.initialize(pings[id], serial.baudrate);
				}
				catch (NoSuchPortException e1)
				{
					e1.printStackTrace();
				}
			}
			else if(serial.id == 2 && pings[id] != "")
			{
				try
				{
					this.serieAsservissement.initialize(pings[id], serial.baudrate);
				}
				catch (NoSuchPortException e1)
				{
					e1.printStackTrace();
				}
			}
			else if(serial.id == 3 && pings[id] != "")
			{
				try
				{
					this.serieAsservissement.initialize(pings[id], serial.baudrate);
				}
				catch (NoSuchPortException e1)
				{
					e1.printStackTrace();
				}
			}
		}
	}
	
	private boolean isKnownPing(int id)
	{
		for(SpecificationCard spec : cards)
		{
			if(id == spec.id)
				return true;
		}
		return false;
	}
}
