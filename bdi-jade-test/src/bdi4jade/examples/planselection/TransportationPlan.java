//----------------------------------------------------------------------------
// Copyright (C) 2013 Ingrid Nunes
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

package bdi4jade.examples.planselection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bdi4jade.examples.planselection.TransportationAgent.TransportationGoal;
import bdi4jade.extension.planselection.utilitybased.PlanContribution;
import bdi4jade.goal.GoalTemplateFactory;
import bdi4jade.goal.Softgoal;
import bdi4jade.plan.DefaultPlan;

/**
 * @author Ingrid Nunes
 */
public class TransportationPlan extends DefaultPlan {

	public static final double MAX_TIME_TAKEN = 90;

	private double beingRobbedProbability;
	private double comfort;
	private double cost;
	private boolean costConstant;
	private double crashProbability;
	private int maxTime;
	private int minTime;

	public TransportationPlan(String id, double crashProbability,
			double beingRobbedProbability, boolean costConstant, double cost,
			double comfort, int minTime, int maxTime) {
		super(id, GoalTemplateFactory.goalOfType(TransportationGoal.class),
				TransportationPlanBody.class);

		this.beingRobbedProbability = beingRobbedProbability;
		this.comfort = comfort;
		this.cost = cost;
		this.costConstant = costConstant;
		this.crashProbability = crashProbability;
		this.maxTime = maxTime;
		this.minTime = minTime;

		Map<Softgoal, List<PlanContribution>> contributions = new HashMap<Softgoal, List<PlanContribution>>();

		List<PlanContribution> sgContributions = new ArrayList<PlanContribution>();
		sgContributions.add(new PlanContribution(Softgoals.SAFETY,
				crashProbability, 0.0));
		sgContributions.add(new PlanContribution(Softgoals.SAFETY,
				1 - crashProbability, 1.0));
		contributions.put(Softgoals.SAFETY, sgContributions);

		sgContributions = new ArrayList<PlanContribution>();
		if (costConstant) {
			sgContributions.add(new PlanContribution(Softgoals.COST, 1.0,
					1 - cost));
		} else {
			sgContributions.add(new PlanContribution(Softgoals.COST,
					crashProbability, 0.0));
			sgContributions.add(new PlanContribution(Softgoals.COST,
					1 - crashProbability, 1 - cost));
		}
		contributions.put(Softgoals.COST, sgContributions);

		sgContributions = new ArrayList<PlanContribution>();
		sgContributions.add(new PlanContribution(Softgoals.COMFORT, 1.0,
				comfort));
		contributions.put(Softgoals.COMFORT, sgContributions);

		sgContributions = new ArrayList<PlanContribution>();
		sgContributions.add(new PlanContribution(Softgoals.SECURITY,
				beingRobbedProbability, 0.0));
		sgContributions.add(new PlanContribution(Softgoals.SECURITY,
				1 - beingRobbedProbability, 1.0));
		contributions.put(Softgoals.SECURITY, sgContributions);

		sgContributions = new ArrayList<PlanContribution>();
		sgContributions.add(new PlanContribution(Softgoals.PERFORMANCE,
				crashProbability, 0.0));
		sgContributions.add(new PlanContribution(Softgoals.PERFORMANCE,
				(1 - crashProbability) * 0.5, 1 - (minTime / MAX_TIME_TAKEN)));
		sgContributions.add(new PlanContribution(Softgoals.PERFORMANCE,
				(1 - crashProbability) * 0.5, 1 - (maxTime / MAX_TIME_TAKEN)));
		contributions.put(Softgoals.PERFORMANCE, sgContributions);

		putMetadata(PlanContribution.METADATA_NAME, contributions);
	}

	/**
	 * @return the beingRobbedProbability
	 */
	public double getBeingRobbedProbability() {
		return beingRobbedProbability;
	}

	/**
	 * @return the comfort
	 */
	public double getComfort() {
		return comfort;
	}

	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @return the crashProbability
	 */
	public double getCrashProbability() {
		return crashProbability;
	}

	/**
	 * @return the maxTime
	 */
	public int getMaxTime() {
		return maxTime;
	}

	/**
	 * @return the minTime
	 */
	public int getMinTime() {
		return minTime;
	}

	/**
	 * @return the costConstant
	 */
	public boolean isCostConstant() {
		return costConstant;
	}

}
