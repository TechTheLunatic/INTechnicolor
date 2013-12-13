package table;

import smartMath.Vec2;

class Tree {

	private int tree_id; //depuis le haut vers le bas, de 0 à 3;
	// 0 et 1 : c�t� droit
	// 2 et 3 : c�t� gauche
	private float radius = 150 ;
	private boolean taken = false;
	Fruit arrayFruit[] = new Fruit[6];

	// combien gauche()
	// combien droit()

	/**
	 * Constructeur public
	 * @param position
	 * @param id
	 */
	public Tree (int id)
	{
		this.tree_id = id;
		this.arrayFruit[0] = new Fruit(0);
		this.arrayFruit[1] = new Fruit(1);
		this.arrayFruit[2] = new Fruit(2);
		this.arrayFruit[3] = new Fruit(3);
		this.arrayFruit[4] = new Fruit(4);
		this.arrayFruit[5] = new Fruit(5);	
		// Convention par Guy (spammez moi si je me trompe) :
		// 0-2 : partie gauche de l'arbre
		// 3-5 : partie droite de l'arbre
		// De plus, 0 et 3 sont les fruits les plus proches du robot.
	}
	
	/**
	 * Constructeur utilisé par clone()
	 * @param position
	 * @param tree_id
	 * @param radius
	 * @param arrayFruit
	 */
	private Tree(int tree_id, float radius, Fruit[] arrayFruit) {
		this.tree_id = tree_id;
		this.radius = radius;
		this.arrayFruit = arrayFruit;
	}

	public Tree clone()
	{
		Fruit[] cloned_arrayFruit = new Fruit[6];
		for(int i = 0; i < 6; i++)		
			cloned_arrayFruit[i] = arrayFruit[i].clone();
		return new Tree(tree_id, radius, cloned_arrayFruit);
	}
	
	public int nbrLeft ()
	{
		int var = 0;
		int i = 0;
		if (!(this.taken))
		{
			while (i < 3 && this.arrayFruit[i].isGood()) 
			{
				if (this.arrayFruit[i].isTaken() == false) 
				{
					var++;
				}
				i++;
			}
		}
		return var;
	}
	
	public int nbrRight ()
	{
		int var = 0;
		int i = 3;
		if (!(this.taken))
		{
			while (i < 6 && this.arrayFruit[i].isGood()) 
			{
				if (this.arrayFruit[i].isTaken() == false) 
				{
					var++;
				}
				i++;
			}
		}
		return var;
	}
	
	public int nbrTotal ()
	{
		return (nbrLeft()+nbrRight());
	}
	
	public void pickFruit(int id)
	{
		this.arrayFruit[id].setTaken();
	}
	
	public boolean isTaken()
	{
		return this.taken;
	}
	
	public void setTaken()
	{
		this.taken = true;
	}
}
