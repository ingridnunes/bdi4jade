/**
 * 
 */
package br.ufrgs.inf.bdi4jade.examples.planselection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufrgs.inf.bdi4jade.plan.PlanContribution;
import br.ufrgs.inf.bdi4jade.softgoal.Softgoal;
import br.ufrgs.inf.bdi4jade.util.plan.SimplePlan;

/**
 * @author ingrid
 * 
 */
public class TransportationPlan extends SimplePlan {

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
		super(id, TransportationGoal.class, TransportationPlanBody.class);

		this.beingRobbedProbability = beingRobbedProbability;
		this.comfort = comfort;
		this.cost = cost;
		this.costConstant = costConstant;
		this.crashProbability = crashProbability;
		this.maxTime = maxTime;
		this.minTime = minTime;

		Map<Softgoal, List<PlanContribution>> contributions = (Map<Softgoal, List<PlanContribution>>) getMetadata(DefaultMetadata.CONTRIBUTIONS);

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
