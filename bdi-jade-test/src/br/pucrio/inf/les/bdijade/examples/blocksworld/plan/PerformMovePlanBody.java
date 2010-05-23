/*
 * Created on 26/01/2010 21:19:08 
 */
package br.pucrio.inf.les.bdijade.examples.blocksworld.plan;

import jade.core.behaviours.Behaviour;
import br.pucrio.inf.les.bdijade.belief.BeliefSet;
import br.pucrio.inf.les.bdijade.examples.blocksworld.BlocksWorldCapability;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.Clear;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.On;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.Thing;
import br.pucrio.inf.les.bdijade.examples.blocksworld.goal.PerformMove;
import br.pucrio.inf.les.bdijade.plan.PlanBody;
import br.pucrio.inf.les.bdijade.plan.PlanInstance;
import br.pucrio.inf.les.bdijade.plan.PlanInstance.EndState;

/**
 * @author ingrid
 * 
 */
public class PerformMovePlanBody extends Behaviour implements PlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	private BeliefSet<Clear> clearSet;
	private boolean done;
	private BeliefSet<On> onSet;
	private Thing thing1;
	private Thing thing2;

	public PerformMovePlanBody() {
		this.done = false;
	}

	@Override
	public void action() {
		if (!thing2.equals(Thing.TABLE)) {
			clearSet.remove(new Clear(thing2));
		}

		for (Thing thing : Thing.THINGS) {
			On on = new On(thing1, thing);
			if (onSet.hasValue(on)) {
				onSet.remove(on);
				if (!Thing.TABLE.equals(thing)) {
					clearSet.addValue(new Clear(thing));
				}
			}
		}

		onSet.addValue(new On(thing1, thing2));
		this.done = true;
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
		this.clearSet = (BeliefSet<Clear>) planInstance.getBeliefBase()
				.getBelief(BlocksWorldCapability.BELIEF_CLEAR);
		PerformMove goal = (PerformMove) planInstance.getGoal();
		this.thing1 = goal.getThing1();
		this.thing2 = goal.getThing2();
	}

}
