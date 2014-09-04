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

import jade.lang.acl.ACLMessage;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalTemplateFactory;
import bdi4jade.goal.ParallelGoal;
import bdi4jade.goal.SequentialGoal;
import bdi4jade.plan.planbody.ParallelGoalPlanBody;
import bdi4jade.plan.planbody.SequentialGoalPlanBody;

/**
 * This class represents the plan library of a capability. It aggregates the
 * plans that can be used to achieve goals.
 * 
 * @author Ingrid Nunes
 */
public class PlanLibrary implements Serializable {

	private static final long serialVersionUID = 3038533629659859857L;

	private Capability capability;
	private final Set<Plan> plans;

	/**
	 * The default constructor. It should be only used if persistence frameworks
	 * are used.
	 */
	protected PlanLibrary() {
		this.plans = new HashSet<Plan>();
	}

	/**
	 * Creates a plan library associated with a capability.
	 * 
	 * @param capability
	 *            the capability with which this plan library is associated.
	 */
	public PlanLibrary(final Capability capability) {
		this(capability, null);
	}

	/**
	 * Creates a plan library base associated with a capability and adds the
	 * plans in the provided set.
	 * 
	 * @param capability
	 *            the capability with which this plan library is associated.
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
	 * sequential and parallel goals. This method may be overridden by children
	 * capabilities.
	 */
	protected void addDefaultPlans() {
		addPlan(new DefaultPlan(
				GoalTemplateFactory.goalOfType(SequentialGoal.class),
				SequentialGoalPlanBody.class));
		addPlan(new DefaultPlan(
				GoalTemplateFactory.goalOfType(ParallelGoal.class),
				ParallelGoalPlanBody.class));
	}

	/**
	 * Adds a plan to the plan library.
	 * 
	 * @param plan
	 *            the plan to be added.
	 */
	public void addPlan(Plan plan) {
		if (plan.getPlanLibrary() != null) {
			plan.getPlanLibrary().removePlan(plan);
		}
		plan.setPlanLibrary(this);
		this.plans.add(plan);
	}

	/**
	 * Returns true if there is a plan that can achieve the given goal.
	 * 
	 * @param goal
	 *            the goal to be checked.
	 * @return true if a plan can achieve the goal, false otherwise.
	 */
	public boolean canAchieve(Goal goal) {
		for (Plan plan : this.plans) {
			if (plan.canAchieve(goal)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if there is a plan that can handle the given message.
	 * 
	 * @param message
	 *            the message to be checked.
	 * @return true if a plan can handle the message, false otherwise.
	 */
	public boolean canHandle(ACLMessage message) {
		for (Plan plan : this.plans) {
			if (plan.canProcess(message)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the set of plans that can achieve the given goal.
	 * 
	 * @param goal
	 *            the goal to be achieved.
	 * @return the set of plans that can achieve the goal.
	 */
	public Set<Plan> getCandidatePlans(Goal goal) {
		Set<Plan> plans = new HashSet<Plan>();
		for (Plan plan : this.plans) {
			if (plan.canAchieve(goal)) {
				plans.add(plan);
			}
		}
		return plans;
	}

	/**
	 * Returns the capability with which this plan library is associated.
	 * 
	 * @return the capability.
	 */
	public Capability getCapability() {
		return capability;
	}

	/**
	 * Returns the set of plans that are part of this plan library.
	 * 
	 * @return the plans.
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
	 * @return true if the plan was removed, false otherwise.
	 */
	public boolean removePlan(Plan plan) {
		boolean removed = this.plans.remove(plan);
		if (removed) {
			plan.setPlanLibrary(null);
		}
		return removed;
	}

}
