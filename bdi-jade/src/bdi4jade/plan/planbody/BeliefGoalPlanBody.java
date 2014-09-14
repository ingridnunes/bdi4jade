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

import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.belief.Belief;
import bdi4jade.goal.BeliefGoal;
import bdi4jade.plan.Plan.EndState;

/**
 * This plan body aims to achieve a {@link BeliefGoal}. It verifies when the
 * plan begins its execution if the goal is already achieved. If so, nothing is
 * performed, otherwise the method {@link #execute()} is invoked, as a
 * replacement of the {@link #action()} method.
 * 
 * @author Ingrid Nunes
 * 
 */
public abstract class BeliefGoalPlanBody extends AbstractPlanBody {

	private static final long serialVersionUID = -2512248999988800844L;

	/**
	 * This final implementation of the action method verifies the belief goal
	 * that triggered this plan body execution is already achieved (
	 * {@link #isGoalAchieved()}). If not, it invokes the {@link #execute()}
	 * method;
	 */
	@Override
	public final void action() {
		if (!isGoalAchieved()) {
			execute();
		}
	}

	/**
	 * This method is a placeholder for subclasses that should implement the set
	 * of steps needed to achieve this plan body goal.
	 */
	protected abstract void execute();

	@Parameter(direction = Direction.OUT)
	public Belief<?, ?> getOutputBelief() {
		return getBeliefBase().getBelief(
				((BeliefGoal<?>) getGoal()).getBeliefName());
	}

	/**
	 * This method is a placeholder for subclasses. It is invoked by the
	 * {@link #onStart()} method, after it performs some pre-processing.
	 */
	protected void init() {

	}

	/**
	 * Returns true if the goal of this plan body was achieved. If so, it sets
	 * the end state to successful, which cases this plan body to complete its
	 * execution.
	 * 
	 * @return true if goal was achieved, false otherwise.
	 */
	protected boolean isGoalAchieved() {
		BeliefGoal<?> goal = (BeliefGoal<?>) getGoal();
		if (goal.isAchieved(getBeliefBase())) {
			setEndState(EndState.SUCCESSFUL);
			return true;
		}
		return false;
	}

	/**
	 * Verifies if the goal that triggered this plan body execution is a
	 * {@link BeliefGoal}. If not, it throws an {@link IllegalArgumentException}
	 * .
	 */
	@Override
	public final void onStart() {
		if (!(getGoal() instanceof BeliefGoal))
			throw new IllegalArgumentException("BeliefGoal expected.");
		init();
	}

}
