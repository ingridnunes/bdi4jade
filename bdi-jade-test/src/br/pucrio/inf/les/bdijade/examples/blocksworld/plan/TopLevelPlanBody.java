/*
 * Created on 26/01/2010 21:19:50 
 */
package br.pucrio.inf.les.bdijade.examples.blocksworld.plan;

import jade.core.behaviours.Behaviour;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.pucrio.inf.les.bdijade.examples.blocksworld.BlocksWorldCapability;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.On;
import br.pucrio.inf.les.bdijade.examples.blocksworld.goal.AchieveBlocksStacked;
import br.pucrio.inf.les.bdijade.plan.PlanBody;
import br.pucrio.inf.les.bdijade.plan.PlanInstance;
import br.pucrio.inf.les.bdijade.plan.PlanInstance.EndState;
import br.pucrio.inf.les.bdijade.util.goal.BeliefSetValueGoal;

/**
 * @author ingrid
 * 
 */
public class TopLevelPlanBody extends Behaviour implements PlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	private int counter;
	private Log log;
	private PlanInstance planInstance;
	private On[] target;

	public TopLevelPlanBody() {
		this.counter = 0;
		this.log = LogFactory.getLog(this.getClass());
	}

	@Override
	public void action() {
		if (counter != 0) {
			if ((planInstance.getGoalEvent() == null)) {
				return;
			}
		}
		if (counter != target.length) {
			planInstance.dispatchSubgoalAndListen(new BeliefSetValueGoal<On>(
					BlocksWorldCapability.BELIEF_ON, target[counter]));
		}
		counter++;
	}

	@Override
	public boolean done() {
		return counter > target.length;
	}

	@Override
	public EndState getEndState() {
		return (counter > target.length) ? EndState.SUCCESSFUL : null;
	}

	@Override
	public void init(PlanInstance planInstance) {
		this.planInstance = planInstance;
		this.target = ((AchieveBlocksStacked) planInstance.getGoal())
				.getTarget();
	}

	@Override
	public int onEnd() {
		log.info("World Model at end is:");
		log.info(planInstance.getBeliefBase());
		return super.onEnd();
	}

	@Override
	public void onStart() {
		log.info("World Model at start is:");
		log.info(planInstance.getBeliefBase());
	}

}
