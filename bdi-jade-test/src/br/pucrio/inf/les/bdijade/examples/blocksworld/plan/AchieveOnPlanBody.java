/*
 * Created on 26/01/2010 21:17:50 
 */
package br.pucrio.inf.les.bdijade.examples.blocksworld.plan;

import jade.core.behaviours.Behaviour;
import br.pucrio.inf.les.bdijade.examples.blocksworld.BlocksWorldCapability;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.Clear;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.On;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.Thing;
import br.pucrio.inf.les.bdijade.examples.blocksworld.goal.PerformMove;
import br.pucrio.inf.les.bdijade.plan.PlanBody;
import br.pucrio.inf.les.bdijade.plan.PlanInstance;
import br.pucrio.inf.les.bdijade.plan.PlanInstance.EndState;
import br.pucrio.inf.les.bdijade.util.goal.BeliefSetValueGoal;

/**
 * @author ingrid
 * 
 */
public class AchieveOnPlanBody extends Behaviour implements PlanBody {
	private static final long serialVersionUID = -5919677537834351951L;

	private int counter;
	private PlanInstance planInstance;
	private Thing thing1;
	private Thing thing2;

	public AchieveOnPlanBody() {
		this.counter = 0;
	}

	@Override
	public void action() {
		switch (counter) {
		case 0:
			if (new BeliefSetValueGoal<On>(BlocksWorldCapability.BELIEF_ON,
					new On(thing1, thing2)).isAchieved(planInstance
					.getBeliefBase())) {
				counter = 6;
			} else {
				counter = checkClearAndDispatch(thing1) ? 1 : 2;
			}
			break;
		case 2:
			counter = checkClearAndDispatch(thing2) ? 3 : 4;
			break;
		case 4:
			planInstance.dispatchSubgoalAndListen(new PerformMove(thing1,
					thing2));
			counter = 5;
		case 1:
		case 3:
		case 5:
			if (planInstance.getGoalEvent() != null)
				counter++;
			break;
		}
	}

	private boolean checkClearAndDispatch(Thing thing) {
		BeliefSetValueGoal<Clear> clearBelief = new BeliefSetValueGoal<Clear>(
				BlocksWorldCapability.BELIEF_CLEAR, new Clear(thing1));
		if (!clearBelief.isAchieved(planInstance.getBeliefBase())) {
			planInstance.dispatchSubgoalAndListen(clearBelief);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean done() {
		return counter == 6;
	}

	@Override
	public EndState getEndState() {
		return (counter == 6) ? EndState.SUCCESSFUL : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void init(PlanInstance planInstance) {
		this.planInstance = planInstance;
		BeliefSetValueGoal<On> achieveOn = (BeliefSetValueGoal<On>) planInstance
				.getGoal();
		this.thing1 = achieveOn.getValue().getThing1();
		this.thing2 = achieveOn.getValue().getThing2();
	}

}
