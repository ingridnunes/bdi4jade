//----------------------------------------------------------------------------
// Copyright (C) 2013  Ingrid Nunes
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

package bdi4jade.extension.planselection.utilitybased;

import java.util.ArrayList;
import java.util.List;

import bdi4jade.goal.Goal;
import bdi4jade.plan.Plan;

/**
 * This class represents an AND dependency between a plan and a set of goals. It
 * means that this plan needs that all goals that it depends on must be achieved
 * so that the plan can be completed.
 * 
 * @author Ingrid Nunes
 */
public class AndPlanGoalDependency extends PlanGoalDependency {

	private List<Goal> goals;

	public AndPlanGoalDependency(Plan root) {
		this(root, new ArrayList<Goal>());
	}

	public AndPlanGoalDependency(Plan root, List<Goal> goals) {
		super(root);
		this.goals = goals;
	}

	/**
	 * Adds a goal to this dependency, indicating that the root plan depends on
	 * it.
	 * 
	 * @param goal
	 *            a goal that the root plan depends on.
	 */
	public void addGoal(Goal goal) {
		this.goals.add(goal);
	}

	/**
	 * Returns the set of goals that the root plan depends on.
	 * 
	 * @return the goals the goal that the root plan depends on.
	 */
	public List<Goal> getGoals() {
		return goals;
	}

	/**
	 * Remove a goal from this dependency.
	 * 
	 * @param goal
	 *            the goal to be removed.
	 */
	public void removeGoal(Goal goal) {
		this.goals.remove(goal);
	}

}
