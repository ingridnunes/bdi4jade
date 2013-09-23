/**
 * 
 */
package br.ufrgs.inf.bdi4jade.examples.template;

import br.ufrgs.inf.bdi4jade.softgoal.NamedSoftgoal;
import br.ufrgs.inf.bdi4jade.softgoal.Softgoal;

/**
 * @author ingrid
 * 
 */
public interface Constants {

	// Softgoals
	public static final Softgoal Softgoal1 = new NamedSoftgoal("Softgoal1");
	public static final Softgoal Softgoal2 = new NamedSoftgoal("Softgoal2");

	public static final Softgoal ALL_SOFTGOALS[] = { Softgoal1, Softgoal2 };

}
