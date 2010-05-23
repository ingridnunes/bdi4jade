/*
 * Created on 31/01/2010 18:25:42 
 */
package br.pucrio.inf.les.bdijade.examples.subgoal;

import jade.core.behaviours.Behaviour;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.pucrio.inf.les.bdijade.plan.PlanBody;
import br.pucrio.inf.les.bdijade.plan.PlanInstance;
import br.pucrio.inf.les.bdijade.plan.PlanInstance.EndState;

/**
 * @author ingrid
 * 
 */
public class ParentPlan extends Behaviour implements PlanBody {

	private static final long serialVersionUID = -5432560989511973914L;

	private int counter;
	private Log log = LogFactory.getLog(this.getClass());
	private PlanInstance planInstance;

	@Override
	public void action() {
		if (counter == 0) {
			this.planInstance.dispatchSubgoal(new Subgoal());
		}
		log.info("ParentPlan executing... counter " + counter);
		counter++;
	}

	@Override
	public boolean done() {
		return counter >= 10;
	}

	@Override
	public EndState getEndState() {
		return done() ? EndState.FAILED : null;
	}

	@Override
	public void init(PlanInstance planInstance) {
		this.planInstance = planInstance;
		this.counter = 0;
	}

}
