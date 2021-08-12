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

package bdi4jade.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import bdi4jade.event.GoalListener;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;

/**
 * This class is a data structure to used to specify updates on goals. In a
 * certain agent state, it has a set of current goals. During the agent
 * reasoning cycle, some of these goals may be dropped, and new goals may be
 * generated. This class has the set with current goals, and allows informing
 * which goals should be dropped, and which should be generated. Current goals
 * may contain goals dispatched by a single capability.
 * 
 * @author Ingrid Nunes
 */
public class GoalUpdateSet {

	/**
	 * This class is a data structure to describe a goal. This description
	 * contains the goal itself, together with its status and the capability
	 * that dispatched the goal (if it was dispatched in a plan of a
	 * capability).
	 * 
	 * @author Ingrid Nunes
	 */
	public class GoalDescription {

		private final Capability dispatcher;
		private final Goal goal;
		private final Intention intention;
		private final GoalListener listener;
		private final GoalStatus status;

		private GoalDescription(Goal goal) {
			this(goal, null, null);
		}

		private GoalDescription(Goal goal, Capability dispatcher,
				GoalListener listener) {
			this.goal = goal;
			this.status = null;
			this.dispatcher = dispatcher;
			this.listener = listener;
			this.intention = null;
		}

		private GoalDescription(Intention intention) {
			this.goal = intention.getGoal();
			this.status = intention.getStatus();
			this.dispatcher = intention.getDispatcher();
			this.listener = null;
			this.intention = intention;
		}

		/**
		 * Returns the capability that dispatched the goal.
		 * 
		 * @return the capability.
		 */
		public Capability getDispatcher() {
			return dispatcher;
		}

		/**
		 * Returns the goal described by this descriptor.
		 * 
		 * @return the goal.
		 */
		public Goal getGoal() {
			return goal;
		}

		/**
		 * Returns the intention associated with the goal described by this
		 * descriptor.
		 * 
		 * @return the intention.
		 */
		Intention getIntention() {
			return intention;
		}

		/**
		 * Returns a listener of the goal.
		 * 
		 * @return the listener.
		 */
		public GoalListener getListener() {
			return listener;
		}

		/**
		 * Returns the status of the goal described by this descriptor.
		 * 
		 * @return the goal status.
		 */
		public GoalStatus getStatus() {
			return status;
		}

	}

	private final Set<GoalDescription> currentGoals;
	private final Set<GoalDescription> droppedGoals;
	private final Set<GoalDescription> generatedGoals;

	GoalUpdateSet() {
		this.currentGoals = new HashSet<>();
		this.droppedGoals = new HashSet<>();
		this.generatedGoals = new HashSet<>();
	}

	GoalUpdateSet(Collection<Intention> intentions) {
		this();
		for (Intention intention : intentions) {
			this.currentGoals.add(new GoalDescription(intention));
		}
	}

	void addIntention(Intention intention) {
		this.currentGoals.add(new GoalDescription(intention));
	}

	/**
	 * Indicates that a goal should be dropped. The goal is added to the set of
	 * dropped goals.
	 * 
	 * @param goal
	 *            the goal to be dropped.
	 */
	public void dropGoal(GoalDescription goal) {
		this.droppedGoals.add(goal);
	}

	/**
	 * Indicates that a goal should be added to the agent. The goal is added to
	 * the set of generated goals.
	 * 
	 * @param goal
	 *            the goal to be added.
	 */
	public void generateGoal(Goal goal) {
		this.generatedGoals.add(new GoalDescription(goal));
	}

	/**
	 * Indicates that a goal should be added to the agent, with the capability
	 * that dispatched the goal. The goal is added to the set of generated
	 * goals.
	 * 
	 * @param goal
	 *            the goal to be added.
	 * @param dispatcher
	 *            the capability that dispatched the goal.
	 */
	public void generateGoal(Goal goal, Capability dispatcher) {
		this.generatedGoals.add(new GoalDescription(goal, dispatcher, null));
	}

	/**
	 * Indicates that a goal should be added to the agent, with the capability
	 * that dispatched the goal and provided listener. The goal is added to the
	 * set of generated goals.
	 * 
	 * @param goal
	 *            the goal to be added.
	 * @param dispatcher
	 *            the capability that dispatched the goal.
	 * @param listener
	 *            a goal listener.
	 */
	public void generateGoal(Goal goal, Capability dispatcher,
			GoalListener listener) {
		this.generatedGoals
				.add(new GoalDescription(goal, dispatcher, listener));
	}

	/**
	 * Indicates that a goal should be added to the agent, with the provided
	 * listener. The goal is added to the set of generated goals.
	 * 
	 * @param goal
	 *            the goal to be added.
	 * @param listener
	 *            a goal listener.
	 */
	public void generateGoal(Goal goal, GoalListener listener) {
		this.generatedGoals.add(new GoalDescription(goal, null, listener));
	}

	/**
	 * Returns the set of current agent goals.
	 * 
	 * @return the set of current goals.
	 */
	public Set<GoalDescription> getCurrentGoals() {
		return new HashSet<>(currentGoals);
	}

	/**
	 * Returns the set of dropped goals.
	 * 
	 * @return the set of dropped goals.
	 */
	public Set<GoalDescription> getDroppedGoals() {
		return new HashSet<>(droppedGoals);
	}

	/**
	 * Returns the set of generated goals.
	 * 
	 * @return the set of generated goals.
	 */
	public Set<GoalDescription> getGeneratedGoals() {
		return new HashSet<>(generatedGoals);
	}

	boolean removeIntention(GoalDescription goalDescription) {
		return this.currentGoals.remove(goalDescription);
	}

}
