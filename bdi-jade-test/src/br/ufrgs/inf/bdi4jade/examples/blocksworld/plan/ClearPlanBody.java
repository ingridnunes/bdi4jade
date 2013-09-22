//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes, et al.
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// 
// To contact the authors:
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package br.ufrgs.inf.bdi4jade.examples.blocksworld.plan;

import jade.core.behaviours.Behaviour;
import br.ufrgs.inf.bdi4jade.belief.BeliefSet;
import br.ufrgs.inf.bdi4jade.examples.blocksworld.BlocksWorldCapability;
import br.ufrgs.inf.bdi4jade.examples.blocksworld.domain.Clear;
import br.ufrgs.inf.bdi4jade.examples.blocksworld.domain.On;
import br.ufrgs.inf.bdi4jade.examples.blocksworld.domain.Thing;
import br.ufrgs.inf.bdi4jade.plan.PlanBody;
import br.ufrgs.inf.bdi4jade.plan.PlanInstance;
import br.ufrgs.inf.bdi4jade.plan.PlanInstance.EndState;
import br.ufrgs.inf.bdi4jade.util.goal.BeliefSetValueGoal;

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
