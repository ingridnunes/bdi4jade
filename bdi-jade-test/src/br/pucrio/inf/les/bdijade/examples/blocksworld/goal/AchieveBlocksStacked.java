package br.pucrio.inf.les.bdijade.examples.blocksworld.goal;

import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.On;
import br.pucrio.inf.les.bdijade.goal.Goal;

/*
 * Created on 26/01/2010 10:55:09 
 */

/**
 * @author ingrid
 * 
 */
public class AchieveBlocksStacked implements Goal {

	private static final long serialVersionUID = -8126833927953226126L;

	private On[] target;

	public AchieveBlocksStacked(On[] target) {
		this.target = target;
	}

	public On[] getTarget() {
		return target;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("AchieveBlocksStacked: ");
		for (On on : target) {
			sb.append(on).append(" ");
		}
		return sb.toString();
	}

}
