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

package bdi4jade.goal;

/**
 * This class provides a collection of static methods to create common goal
 * templates.
 * 
 * @author Ingrid Nunes
 * 
 */
public abstract class GoalTemplateFactory {

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefGoal} and has the given belief name.
	 * 
	 * @param beliefName
	 *            the belief name to be matched.
	 * @return the goal template that checks if the goal is a {@link BeliefGoal}
	 *         with the given name.
	 */
	public static GoalTemplate beliefGoal(final String beliefName) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefGoal) {
					BeliefGoal bg = (BeliefGoal) goal;
					return bg.getBeliefName().equals(beliefName);
				}
				return false;
			}

			public String toString() {
				return "belief(" + beliefName + ")";
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
	public static GoalTemplate beliefSetTypeGoal(final String beliefName,
			final Class<?> beliefValueClass) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefSetValueGoal) {
					BeliefSetValueGoal<?> bg = (BeliefSetValueGoal<?>) goal;
					return bg.getBeliefName().equals(beliefName)
							&& beliefValueClass.isInstance(bg.getValue());
				}
				return false;
			}

			public String toString() {
				return "beliefSet<" + beliefValueClass.getName() + ">("
						+ beliefName + "(?))";
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
	public static GoalTemplate beliefSetValueGoal(final String beliefName,
			final Object beliefValue) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefSetValueGoal) {
					BeliefSetValueGoal<?> bg = (BeliefSetValueGoal<?>) goal;
					return bg.getBeliefName().equals(beliefName)
							&& beliefValue.equals(bg.getValue());
				}
				return false;
			}

			public String toString() {
				return "beliefSet(" + beliefName + "(" + beliefValue + "))";
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
	public static GoalTemplate beliefTypeGoal(final String beliefName,
			final Class<?> beliefValueClass) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefValueGoal<?> bg = (BeliefValueGoal<?>) goal;
					return bg.getBeliefName().equals(beliefName)
							&& beliefValueClass.isInstance(bg.getValue());
				}
				return false;
			}

			public String toString() {
				return "belief<" + beliefValueClass.getName() + ">("
						+ beliefName + "(?))";
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
	public static GoalTemplate beliefValueGoal(final String beliefName,
			final Object beliefValue) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefValueGoal<?> bg = (BeliefValueGoal<?>) goal;
					return bg.getBeliefName().equals(beliefName)
							&& beliefValue.equals(bg.getValue());
				}
				return false;
			}

			public String toString() {
				return "belief(" + beliefName + "(" + beliefValue + "))";
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
	public static GoalTemplate goalType(final Class<? extends Goal> goalClass) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				return goalClass.isInstance(goal);
			}

			public String toString() {
				return "goal(" + goalClass.getName() + ")";
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
	public static GoalTemplate nullBeliefValueGoal(final String beliefName) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefValueGoal<?> bg = (BeliefValueGoal<?>) goal;
					return bg.getBeliefName().equals(beliefName)
							&& bg.getValue() == null;
				}
				return false;
			}

			public String toString() {
				return "belief(" + beliefName + "(null))";
			}
		};
	}

}
