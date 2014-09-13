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
	 * is of the given type.
	 * 
	 * @param goalClass
	 *            the goal class to be matched.
	 * @return the goal template that checks if the goal is of the given type.
	 */
	public static GoalTemplate goalOfType(final Class<? extends Goal> goalClass) {
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
	 * is of the type {@link BeliefPresentGoal} and has the given belief name.
	 * 
	 * @param beliefName
	 *            the belief name to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefPresentGoal} with the given name.
	 */
	public static GoalTemplate hasBelief(final Object beliefName) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefPresentGoal) {
					BeliefPresentGoal<?> bg = (BeliefPresentGoal<?>) goal;
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
	 * is of the type {@link BeliefPresentGoal} and has a belief name of the
	 * given type.
	 * 
	 * @param beliefNameClass
	 *            the belief name class to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefPresentGoal} with a belief name of the given type.
	 */
	public static GoalTemplate hasBeliefOfType(final Class<?> beliefNameClass) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefPresentGoal) {
					BeliefPresentGoal<?> bg = (BeliefPresentGoal<?>) goal;
					return beliefNameClass.isInstance(bg.getBeliefName());
				}
				return false;
			}

			public String toString() {
				return "belief<" + beliefNameClass + ">(?))";
			}
		};
	}

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefNotNullValueGoal}, has the a belief name of
	 * the given type.
	 * 
	 * @param beliefNameClass
	 *            the belief name class to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefNotNullValueGoal} with the given name.
	 */
	public static GoalTemplate hasBeliefOfTypeWithNotNullValue(
			final Class<?> beliefNameClass) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefNotNullValueGoal) {
					BeliefNotNullValueGoal<?, ?> bg = (BeliefNotNullValueGoal<?, ?>) goal;
					return beliefNameClass.isInstance(bg.getBeliefName());
				}
				return false;
			}

			public String toString() {
				return "belief<" + beliefNameClass + ">(!null))";
			}
		};
	}

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefValueGoal}, has the a belief name of the
	 * given type, and is associated with a null value.
	 * 
	 * @param beliefNameClass
	 *            the belief name class to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefValueGoal} with the given name and null value.
	 */
	public static GoalTemplate hasBeliefOfTypeWithNullValue(
			final Class<?> beliefNameClass) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefValueGoal<?, ?> bg = (BeliefValueGoal<?, ?>) goal;
					return beliefNameClass.isInstance(bg.getBeliefName())
							&& bg.getValue() == null;
				}
				return false;
			}

			public String toString() {
				return "belief<" + beliefNameClass + ">(null))";
			}
		};
	}

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefValueGoal}, is of the class of given belief
	 * name class, and has the given value.
	 * 
	 * @param beliefNameClass
	 *            the belief name class to be matched.
	 * @param beliefValue
	 *            the value to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefValueGoal} with the given name and value.
	 */
	public static GoalTemplate hasBeliefOfTypeWithValue(
			final Class<?> beliefNameClass, final Object beliefValue) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefValueGoal<?, ?> bg = (BeliefValueGoal<?, ?>) goal;
					return beliefNameClass.isInstance(bg.getBeliefName())
							&& beliefValue.equals(bg.getValue());
				}
				return false;
			}

			public String toString() {
				return "belief(<" + beliefNameClass.getName() + ">("
						+ beliefValue + "))";
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
	public static GoalTemplate hasBeliefValue(final Object beliefName,
			final Object beliefValue) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefValueGoal<?, ?> bg = (BeliefValueGoal<?, ?>) goal;
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
	public static GoalTemplate hasBeliefValueOfType(final Object beliefName,
			final Class<?> beliefValueClass) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefValueGoal<?, ?> bg = (BeliefValueGoal<?, ?>) goal;
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
	 * is of the type {@link BeliefNotNullValueGoal}, has the given belief name.
	 * 
	 * @param beliefName
	 *            the belief name to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefNotNullValueGoal} with the given name.
	 */
	public static GoalTemplate hasBeliefWithNotNullValue(final Object beliefName) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefNotNullValueGoal) {
					BeliefNotNullValueGoal<?, ?> bg = (BeliefNotNullValueGoal<?, ?>) goal;
					return bg.getBeliefName().equals(beliefName);
				}
				return false;
			}

			public String toString() {
				return "belief(" + beliefName + "(!null))";
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
	public static GoalTemplate hasBeliefWithNullValue(final Object beliefName) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefValueGoal) {
					BeliefValueGoal<?, ?> bg = (BeliefValueGoal<?, ?>) goal;
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

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefNotPresentGoal} and has the given belief
	 * name.
	 * 
	 * @param beliefName
	 *            the belief name to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefNotPresentGoal} with the given name.
	 */
	public static GoalTemplate hasNoBelief(final Object beliefName) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefNotPresentGoal) {
					BeliefNotPresentGoal<?> bg = (BeliefNotPresentGoal<?>) goal;
					return bg.getBeliefName().equals(beliefName);
				}
				return false;
			}

			public String toString() {
				return "!belief(" + beliefName + ")";
			}
		};
	}

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefNotPresentGoal} and has the given belief
	 * name.
	 * 
	 * @param beliefNameClass
	 *            the belief name class to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefNotPresentGoal} with a belief name of the given
	 *         type.
	 */
	public static GoalTemplate hasNoBeliefOfType(final Class<?> beliefNameClass) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefNotPresentGoal) {
					BeliefNotPresentGoal<?> bg = (BeliefNotPresentGoal<?>) goal;
					return beliefNameClass.isInstance(bg.getBeliefName());
				}
				return false;
			}

			public String toString() {
				return "!belief<" + beliefNameClass + ">(?))";
			}
		};
	}

	/**
	 * This method creates a goal template that positively matches a goal if it
	 * is of the type {@link BeliefSetHasValueGoal}, has the given belief name,
	 * and has the given value.
	 * 
	 * @param beliefName
	 *            the belief name to be matched.
	 * @param beliefValue
	 *            the value to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefSetHasValueGoal} with the given name and value.
	 */
	public static GoalTemplate hasValueInBeliefSet(final Object beliefName,
			final Object beliefValue) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefSetHasValueGoal) {
					BeliefSetHasValueGoal<?, ?> bg = (BeliefSetHasValueGoal<?, ?>) goal;
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
	 * is of the type {@link BeliefSetHasValueGoal}, has the given belief name,
	 * and its value is of the given type.
	 * 
	 * @param beliefName
	 *            the belief name to be matched.
	 * @param beliefValueClass
	 *            the value class name to be matched.
	 * @return the goal template that checks if the goal is a
	 *         {@link BeliefSetHasValueGoal} with the given name and value of
	 *         the given type.
	 */
	public static GoalTemplate hasValueOfTypeInBeliefSet(
			final Object beliefName, final Class<?> beliefValueClass) {
		return new GoalTemplate() {
			public boolean match(Goal goal) {
				if (goal instanceof BeliefSetHasValueGoal) {
					BeliefSetHasValueGoal<?, ?> bg = (BeliefSetHasValueGoal<?, ?>) goal;
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

}
