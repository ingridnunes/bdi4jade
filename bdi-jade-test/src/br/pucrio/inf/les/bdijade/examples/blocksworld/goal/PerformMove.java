/*
 * Created on 26/01/2010 11:02:03 
 */
package br.pucrio.inf.les.bdijade.examples.blocksworld.goal;

import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.Thing;
import br.pucrio.inf.les.bdijade.goal.Goal;

/**
 * @author ingrid
 * 
 */
public class PerformMove implements Goal {

	private static final long serialVersionUID = 8286023371969088149L;

	private Thing thing1;
	private Thing thing2;

	public PerformMove(Thing thing1, Thing thing2) {
		this.thing1 = thing1;
		this.thing2 = thing2;
	}

	public Thing getThing1() {
		return thing1;
	}

	public Thing getThing2() {
		return thing2;
	}

	@Override
	public String toString() {
		return "PerformMove: " + thing1 + " to " + thing2;
	}

}
