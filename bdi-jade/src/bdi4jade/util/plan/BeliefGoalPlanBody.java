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
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.util.plan;

import bdi4jade.plan.AbstractPlanBody;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.util.goal.BeliefGoal;

/**
 * @author ingrid
 * 
 */
public abstract class BeliefGoalPlanBody extends AbstractPlanBody {

	private static final long serialVersionUID = -2512248999988800844L;

	@Override
	public final void action() {
		if (!isGoalAchieved()) {
			execute();
		}
	}

	protected abstract void execute();

	protected boolean isGoalAchieved() {
		BeliefGoal goal = (BeliefGoal) getGoal();
		if (goal.isAchieved(getBeliefBase())) {
			setEndState(EndState.SUCCESSFUL);
			return true;
		}
		return false;
	}

	@Override
	public void onStart() {
		if (!(getGoal() instanceof BeliefGoal))
			throw new IllegalArgumentException("BeliefGoal expected.");
	}

}
