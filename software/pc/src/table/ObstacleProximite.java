package table;

import smartMath.Vec2;

/**
 * Obstacles détectés par capteurs de proximité (ultrasons et infrarouges)
 * @author pf
 *
 */
class ObstacleProximite extends ObstacleCirculaire {

	public long death_date;
	
	public ObstacleProximite (Vec2 position, float rad, long death_date)
	{
		super(position,rad);
		this.death_date = death_date;
	}
	
	public ObstacleProximite clone()
	{
		return new ObstacleProximite(position.clone(), radius, death_date);
	}
	
}