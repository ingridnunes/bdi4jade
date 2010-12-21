/*
 * Created on 26/01/2010 21:18:32 
 */
package br.pucrio.inf.les.bdijade.examples.blocksworld.plan;

import jade.core.behaviours.Behaviour;
import br.pucrio.inf.les.bdijade.belief.BeliefSet;
import br.pucrio.inf.les.bdijade.examples.blocksworld.BlocksWorldCapability;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.Clear;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.On;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.Thing;
import br.pucrio.inf.les.bdijade.plan.PlanBody;
import br.pucrio.inf.les.bdijade.plan.PlanInstance;
import br.pucrio.inf.les.bdijade.plan.PlanInstance.EndState;
import br.pucrio.inf.les.bdijade.util.goal.BeliefSetValueGoal;

/**
 * @author ingrid
 * 
 */
public class ClearPlanBody extends Behaviour implements PlanBody {
	private static final long serialVersionUID = -5919677537834351951L;

	private boolean done;
	private int index;
	private Thing thing;
	private PlanInstance planInstance;
	private On on;
	private boolean waiting;
	private BeliefSet<On> onSet;

	public ClearPlanBody() {
		this.done = false;
		this.waiting = false;
		this.index = 0;
	}

	@Override
	public void action() {
		if (!waiting) {
			for (; index < Thing.THINGS.length; index++) {
				Thing t = Thing.THINGS[index];
				on = new On(t, thing);
				if (onSet.hasValue(on)) {
					planInstance
							.dispatchSubgoalAndListen(new BeliefSetValueGoal<On>(
									BlocksWorldCapability.BELIEF_ON, new On(t,
											Thing.TABLE)));
					waiting = true;
					break;
				}
			}
		} else if (planInstance.getGoalEvent() != null) {
			onSet.removeValue(on);
			on = null;
			waiting = false;
			index++;
		}

		if (index >= Thing.THINGS.length) {
			done = true;
		}
	}

	@Override
	public boolean done() {
		return done;
	}

	@Override
	public EndState getEndState() {
		return done ? EndState.SUCCESSFUL : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void init(PlanInstance planInstance) {
		this.onSet = (BeliefSet<On>) planInstance.getBeliefBase().getBelief(
				BlocksWorldCapability.BELIEF_ON);
		this.planInstance = planInstance;
		BeliefSetValueGoal<Clear> achieveClear = (BeliefSetValueGoal<Clear>) planInstance
				.getGoal();
		this.thing = achieveClear.getValue().getThing();
	}

}
