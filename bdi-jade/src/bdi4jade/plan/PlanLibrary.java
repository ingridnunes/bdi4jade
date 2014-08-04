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

package bdi4jade.plan;

import jade.lang.acl.ACLMessage;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.util.goal.ParallelGoal;
import bdi4jade.util.goal.SequentialGoal;
import bdi4jade.util.plan.ParallelGoalPlanBody;
import bdi4jade.util.plan.SequentialGoalPlanBody;

/**
 * This class represents the plan library of a capability. It aggregates the
 * plans that can be used to achieve goals.
 * 
 * @author ingrid
 */
// XXX PlanLibrary - create indexes to optimize plan matches
public final class PlanLibrary implements Serializable {

	private static final long serialVersionUID = 3038533629659859857L;

	private final Capability capability;
	private final Set<Plan> plans;

	/**
	 * Creates a plan library.
	 */
	public PlanLibrary(final Capability capability) {
		this(capability, null);
	}

	/**
	 * Creates a plan library base associated with a capability and adds the
	 * plans in the provided set.
	 * 
	 * @param plans
	 *            the initial plans
	 */
	public PlanLibrary(final Capability capability, Set<Plan> plans) {
		if (capability == null)
			throw new NullPointerException("Capability must be not null.");

		this.capability = capability;
		this.plans = new HashSet<Plan>();
		if (plans != null) {
			for (Plan plan : plans) {
				addPlan(plan);
			}
		}
		addDefaultPlans();
	}

	/**
	 * Adds a set of default plans to this library. It adds plans to achieve the
	 * sequential and parallel goals. This method may be overriden by children
	 * capabilities.
	 */
	protected void addDefaultPlans() {
		addPlan(new SimplePlan(SequentialGoal.class,
				SequentialGoalPlanBody.class));
		addPlan(new SimplePlan(ParallelGoal.class, ParallelGoalPlanBody.class));
	}

	/**
	 * Adds a plan to the plan library.
	 * 
	 * @param plan
	 *            the plan to be added.
	 */
	public void addPlan(Plan plan) {
		plan.setPlanLibrary(this);
		this.plans.add(plan);
	}

	/**
	 * Returns the set of plans that can achieve the given goal.
	 * 
	 * @param goal
	 *            the goal to be achieved.
	 * @return the set of plans that can achieve the goal.
	 */
	public Set<Plan> canAchievePlans(Goal goal) {
		Set<Plan> plans = new HashSet<Plan>();
		for (Plan plan : this.plans) {
			if (plan.canAchieve(goal)) {
				plans.add(plan);
			}
		}
		return plans;
	}

	/**
	 * Returns true if there is a plan that can process the given message.
	 * 
	 * @param message
	 *            the message to be processed.
	 * @return true if a plan can process the message.
	 */
	public boolean canProcessPlans(ACLMessage message) {
		for (Plan plan : this.plans) {
			if (plan.canProcess(message)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the capability
	 */
	public Capability getCapability() {
		return capability;
	}

	/**
	 * @return the plans
	 */
	public Set<Plan> getPlans() {
		return plans;
	}

	/**
	 * Checks if a plan is part of the plan library.
	 * 
	 * @param plan
	 *            the plan to be checked
	 * @return true if the plan library contains the plan.
	 */
	public boolean hasPlan(Plan plan) {
		return this.plans.contains(plan);
	}

	/**
	 * Removes a plan from the plan library.
	 * 
	 * @param plan
	 *            the plan to be removed.
	 * @return true if the plan was removed.
	 */
	public boolean removePlan(Plan plan) {
		boolean removed = this.plans.remove(plan);
		if (removed) {
			plan.setPlanLibrary(null);
		}
		return removed;
	}

}
