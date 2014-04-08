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

import jade.lang.acl.ACLMessage;
import bdi4jade.goal.Goal;
import bdi4jade.message.MessageGoal;
import bdi4jade.plan.AbstractPlan;
import bdi4jade.plan.SimplePlan;
import bdi4jade.util.goal.BeliefGoal;

/**
 * @author ingrid
 * 
 */
public class BeliefGoalPlan extends SimplePlan {

	private final String beliefName;

	protected BeliefGoalPlan(Class<? extends BeliefGoal> beliefGoalClass,
			String beliefName, Class<? extends BeliefGoalPlanBody> planBodyClass) {
		super(beliefGoalClass, planBodyClass);
		this.beliefName = beliefName;
	}

	protected BeliefGoalPlan(String id,
			Class<? extends BeliefGoal> beliefGoalClass, String beliefName,
			Class<? extends BeliefGoalPlanBody> planBodyClass) {
		super(id, beliefGoalClass, planBodyClass);
		this.beliefName = beliefName;
	}

	/**
	 * Constructs a new Plan. It sets the plan library and plan body class of
	 * this plan, and initializes the goals that it can achieve and messages it
	 * can process. The goals are initialized with the provided goal class.
	 * 
	 * @param id
	 *            plan identifier
	 * @param goalClass
	 *            the goal that this plan can achieve
	 */
	public BeliefGoalPlan(String beliefName,
			Class<? extends BeliefGoalPlanBody> planBodyClass) {
		this(planBodyClass.getSimpleName(), BeliefGoal.class, beliefName,
				planBodyClass);
	}

	/**
	 * Constructs a new Plan. It sets the plan library and plan body class of
	 * this plan, and initializes the goals that it can achieve and messages it
	 * can process. The goals are initialized with the provided goal class.
	 * 
	 * @param id
	 *            plan identifier
	 * @param goalClass
	 *            the goal that this plan can achieve
	 */
	public BeliefGoalPlan(String id, String beliefName,
			Class<? extends BeliefGoalPlanBody> planBodyClass) {
		this(id, BeliefGoal.class, beliefName, planBodyClass);
	}

	/**
	 * Verifies if a given goal can be achieved by this plan. When the goal is a
	 * {@link MessageGoal}, it invokes the method
	 * {@link AbstractPlan#canProcess(ACLMessage)}. Otherwise, it checks if the
	 * class of this goal is contained in the goal set of this plan.
	 * 
	 * @param goal
	 *            the goal to be verified.
	 * @return true if the given goal can be achieved by this plan, false
	 *         otherwise.
	 */
	public boolean canAchieve(Goal goal) {
		boolean ok = false;
		for (Class<? extends Goal> goalClass : getGoals()) {
			if (goalClass.equals(goal.getClass())) {
				if (checkBeliefGoal((BeliefGoal) goal)) {
					ok = true;
				}
			}
		}
		return ok ? matchesContext(goal) : false;
	}

	protected boolean checkBeliefGoal(BeliefGoal beliefGoal) {
		return beliefGoal.getBeliefName().equals(beliefName);
	}

}
