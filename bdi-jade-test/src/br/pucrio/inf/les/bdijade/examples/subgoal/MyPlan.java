/*
 * Created on 31/01/2010 18:42:05 
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
public class MyPlan extends Behaviour implements PlanBody {

	private static final long serialVersionUID = -5432560989511973914L;

	private int counter;
	private Log log = LogFactory.getLog(this.getClass());

	@Override
	public void action() {
		log.info("Plan executing... counter " + counter);
		counter++;
	}

	@Override
	public boolean done() {
		return counter >= 10;
	}

	@Override
	public EndState getEndState() {
		return done() ? EndState.SUCCESSFUL : null;
	}

	@Override
	public void init(PlanInstance planInstance) {
		this.counter = 0;
	}

}
