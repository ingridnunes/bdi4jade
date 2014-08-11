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

package bdi4jade.plan;

import bdi4jade.goal.BeliefGoal;
import bdi4jade.goal.BeliefSetValueGoal;
import bdi4jade.goal.BeliefValueGoal;
import bdi4jade.goal.Goal;

/**
 * This abstract class specifies a goal template that is used to match a goal to
 * be achieved to a template of goals. This template is used to indicate the set
 * of goals that a plan is able to handle.
 * 
 * This class also provides a collection of static methods to create common goal
 * templates.
 * 
 * @author Ingrid Nunes
 * 
 */
public abstract class GoalTemplate {

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefGoal} and has the given belief name.
	 * 
	 * @param beliefName
	 *            the belief name to be matched.
	 * @return the goal template that checks if the goal is a {@link BeliefGoal}
	 *         with the given name.
	 */
	public static GoalTemplate createBeliefGoalTemplate(final String beliefName) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefGoal) {
					BeliefGoal bg = (BeliefGoal) goal;
					return bg.getBeliefName().equals(beliefName);
				}
				return false;
			}
		};
	}

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefSetValueGoal}, has the given belief name, and
	 * its value is of the given type.
	 * 
	 * @param beliefName
	 *            the belief name to be matched.
	 * @param beliefValueClass
	 *            the value class name to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefSetValueGoal} with the given name and value of the
	 *         given type.
	 */
	public static GoalTemplate createBeliefSetTypeGoalTemplate(
			final String beliefName, final Class<?> beliefValueClass) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefSetValueGoal<?> bg = (BeliefSetValueGoal<?>) goal;
					return bg.getBeliefName().equals(beliefName)
							&& beliefValueClass.isInstance(bg.getValue());
				}
				return false;
			}
		};
	}

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefSetValueGoal}, has the given belief name, and
	 * has the given value.
	 * 
	 * @param beliefName
	 *            the belief name to be matched.
	 * @param beliefValue
	 *            the value to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefSetValueGoal} with the given name and value.
	 */
	public static GoalTemplate createBeliefSetValueGoalTemplate(
			final String beliefName, final Object beliefValue) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefSetValueGoal<?> bg = (BeliefSetValueGoal<?>) goal;
					return bg.getBeliefName().equals(beliefName)
							&& beliefValue.equals(bg.getValue());
				}
				return false;
			}
		};
	}

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefValueGoal}, has the given belief name, and
	 * its value is of the given type.
	 * 
	 * @param beliefName
	 *            the belief name to be matched.
	 * @param beliefValueClass
	 *            the value class name to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefValueGoal} with the given name and value of the
	 *         given type.
	 */
	public static GoalTemplate createBeliefTypeGoalTemplate(
			final String beliefName, final Class<?> beliefValueClass) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefValueGoal<?> bg = (BeliefValueGoal<?>) goal;
					return bg.getBeliefName().equals(beliefName)
							&& beliefValueClass.isInstance(bg.getValue());
				}
				return false;
			}
		};
	}

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefValueGoal}, has the given belief name, and
	 * has the given value.
	 * 
	 * @param beliefName
	 *            the belief name to be matched.
	 * @param beliefValue
	 *            the value to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefValueGoal} with the given name and value.
	 */
	public static GoalTemplate createBeliefValueGoalTemplate(
			final String beliefName, final Object beliefValue) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefValueGoal<?> bg = (BeliefValueGoal<?>) goal;
					return bg.getBeliefName().equals(beliefName)
							&& beliefValue.equals(bg.getValue());
				}
				return false;
			}
		};
	}

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the given type.
	 * 
	 * @param goalClass
	 *            the goal class to be matched.
	 * @return the goal template that checks if the goal is of the given type.
	 */
	public static GoalTemplate createGoalTypeTemplate(
			final Class<? extends Goal> goalClass) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				return goalClass.isInstance(goal);
			}
		};
	}

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefValueGoal}, has the given belief name, and is
	 * associated with a null value.
	 * 
	 * @param beliefName
	 *            the belief name to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefValueGoal} with the given name and null value.
	 */
	public static GoalTemplate createNullBeliefValueGoalTemplate(
			final String beliefName) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefValueGoal<?> bg = (BeliefValueGoal<?>) goal;
					return bg.getBeliefName().equals(beliefName)
							&& bg.getValue() == null;
				}
				return false;
			}
		};
	}

	/**
	 * This methods checks if the given goal matches this template.
	 * 
	 * @param goal
	 *            the goal to be checked.
	 * @return true if the goal matches this template, false otherwise.
	 */
	public abstract boolean match(Goal goal);

}
