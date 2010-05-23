/*
 * Created on 04/02/2010 22:21:30 
 */
package br.pucrio.inf.les.bdijade.examples.compositegoal;

import jade.core.behaviours.Behaviour;

import java.util.Random;

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

	private static final long serialVersionUID = -220345270457161508L;
	
	private EndState endState = null;
	private PlanInstance planInstance;
	private Log log = LogFactory.getLog(this.getClass());

	public void action() {
		long random = new Random().nextLong();
		log.info("Random: " + random);
		endState = (random % 3 != 0) ? EndState.SUCCESSFUL : EndState.FAILED;
		log.info(planInstance.getGoal() + " Plan#"
				+ planInstance.getPlan().getId() + " EndState: " + endState);
	}

	public boolean done() {
		return true;
	}

	public EndState getEndState() {
		return endState;
	}

	public void init(PlanInstance planInstance) {
		this.planInstance = planInstance;
	}
}
