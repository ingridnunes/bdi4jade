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

package bdi4jade.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;

/**
 * @author ingridnunes
 * 
 */
public class GoalUpdateSet {

	public class GoalDescription {

		private final Capability dispatcher;
		private final Goal goal;
		private final Intention intention;
		private final GoalStatus status;

		private GoalDescription(Goal goal) {
			this(goal, null);
		}

		private GoalDescription(Goal goal, Capability dispatcher) {
			this.goal = goal;
			this.status = null;
			this.dispatcher = dispatcher;
			this.intention = null;
		}

		private GoalDescription(Intention intention) {
			this.goal = intention.getGoal();
			this.status = intention.getStatus();
			this.dispatcher = intention.getOwner();
			this.intention = intention;
		}

		public Capability getDispatcher() {
			return dispatcher;
		}

		public Goal getGoal() {
			return goal;
		}

		Intention getIntention() {
			return intention;
		}

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

	public void dropGoal(GoalDescription goal) {
		this.droppedGoals.add(goal);
	}

	public void generateGoal(Goal goal) {
		this.generatedGoals.add(new GoalDescription(goal));
	}

	public void generateGoal(Goal goal, Capability dispatcher) {
		this.generatedGoals.add(new GoalDescription(goal, dispatcher));
	}

	public Set<GoalDescription> getCurrentGoals() {
		return new HashSet<>(currentGoals);
	}

	public Set<GoalDescription> getDroppedGoals() {
		return new HashSet<>(droppedGoals);
	}

	public Set<GoalDescription> getGeneratedGoals() {
		return new HashSet<>(generatedGoals);
	}

	boolean removeIntention(GoalDescription goalDescription) {
		return this.currentGoals.remove(goalDescription);
	}

}
