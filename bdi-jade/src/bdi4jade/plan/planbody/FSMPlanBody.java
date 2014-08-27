//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes
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
// http://inf.ufrgs.br/prosoft/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.plan.planbody;

import jade.core.behaviours.FSMBehaviour;
import bdi4jade.belief.BeliefBase;
import bdi4jade.core.Capability;
import bdi4jade.core.Intention;
import bdi4jade.event.GoalEvent;
import bdi4jade.exception.PlanInstantiationException;
import bdi4jade.goal.Goal;
import bdi4jade.plan.Plan;
import bdi4jade.plan.Plan.EndState;

/**
 * This allows a plan body to inherit from both {@link AbstractPlanBody} and
 * {@link FSMBehaviour}.
 * 
 * @author Ingrid Nunes
 * 
 */
public class FSMPlanBody extends FSMBehaviour implements PlanBody {

	private static final long serialVersionUID = -7659781172897309684L;

	private AbstractPlanBody abstractPlanBody;

	public FSMPlanBody() {
		this.abstractPlanBody = new AbstractPlanBody() {
			private static final long serialVersionUID = -6874445280312398784L;

			@Override
			public void action() {
			}
		};
	}

	public boolean dispatchGoal(Goal goal) {
		return abstractPlanBody.dispatchGoal(goal);
	}

	public boolean dispatchSubgoal(Goal subgoal) {
		return abstractPlanBody.dispatchSubgoal(subgoal);
	}

	public boolean dispatchSubgoalAndListen(Goal subgoal) {
		return abstractPlanBody.dispatchSubgoalAndListen(subgoal);
	}

	public BeliefBase getBeliefBase() {
		return abstractPlanBody.getBeliefBase();
	}

	public Capability getCapability() {
		return abstractPlanBody.getCapability();
	}

	public EndState getEndState() {
		return abstractPlanBody.getEndState();
	}

	public Goal getGoal() {
		return abstractPlanBody.getGoal();
	}

	public GoalEvent getGoalEvent() {
		return abstractPlanBody.getGoalEvent();
	}

	public GoalEvent getGoalEvent(boolean block) {
		return abstractPlanBody.getGoalEvent(block);
	}

	public GoalEvent getGoalEvent(long ms) {
		return abstractPlanBody.getGoalEvent(ms);
	}

	public Plan getPlan() {
		return abstractPlanBody.getPlan();
	}

	@Override
	public void goalPerformed(GoalEvent event) {
		abstractPlanBody.goalPerformed(event);
	}

	public void init(Plan plan, Intention intention)
			throws PlanInstantiationException {
		abstractPlanBody.init(plan, intention);
	}

	public void start() {
		abstractPlanBody.getIntention().getMyAgent().addBehaviour(this);
	}

	public void stop() {
		abstractPlanBody.dropSubgoals();
		abstractPlanBody.getIntention().getMyAgent().removeBehaviour(this);
		if (this instanceof DisposablePlanBody) {
			((DisposablePlanBody) this).onAbort();
		}
	}

}
