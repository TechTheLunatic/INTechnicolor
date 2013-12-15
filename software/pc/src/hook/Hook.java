package hook;

import java.util.ArrayList;

import robot.Robot;
import utils.Log;
import utils.Read_Ini;

/**
 * Classe-mère abstraite des hooks, utilisés pour la programmation évènementielle
 * @author pf
 *
 */

abstract public class Hook {

	private ArrayList<Callback> callbacks = new ArrayList<Callback>();
	
	protected Read_Ini config;
	protected Log log;

	public Hook(Read_Ini config, Log log)
	{
		this.config = config;
		this.log = log;
	}
	
	
	public void ajouter_callback(Callback callback)
	{
		callbacks.add(callback);
	}
	
	protected void declencher()
	{
		for(Callback callback : callbacks)
			callback.appeler();
	}

	public abstract void evaluate(final Robot robot);

}

