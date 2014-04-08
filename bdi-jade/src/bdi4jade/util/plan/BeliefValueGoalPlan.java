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

import bdi4jade.util.goal.BeliefGoal;
import bdi4jade.util.goal.BeliefValueGoal;

/**
 * @author ingrid
 * 
 */
public class BeliefValueGoalPlan extends BeliefGoalPlan {

	private final Class<?> beliefValueClass;

	public BeliefValueGoalPlan(
			Class<? extends BeliefValueGoal> beliefGoalClass,
			String beliefName, Class<?> beliefValueClass,
			Class<? extends BeliefGoalPlanBody> planBodyClass) {
		this(planBodyClass.getSimpleName(), beliefGoalClass, beliefName,
				beliefValueClass, planBodyClass);
	}

	public BeliefValueGoalPlan(String id,
			Class<? extends BeliefValueGoal> beliefGoalClass,
			String beliefName, Class<?> beliefValueClass,
			Class<? extends BeliefGoalPlanBody> planBodyClass) {
		super(id, beliefGoalClass, beliefName, planBodyClass);
		this.beliefValueClass = beliefValueClass;
	}

	@Override
	protected boolean checkBeliefGoal(BeliefGoal beliefGoal) {
		if (super.checkBeliefGoal(beliefGoal)) {
			return beliefValueClass
					.isInstance(((BeliefValueGoal<?>) beliefGoal).getValue());
		} else {
			return false;
		}
	}

}
