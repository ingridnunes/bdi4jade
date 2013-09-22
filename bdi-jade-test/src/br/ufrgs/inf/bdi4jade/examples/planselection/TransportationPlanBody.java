/**
 * 
 */
package br.ufrgs.inf.bdi4jade.examples.planselection;

import jade.core.behaviours.OneShotBehaviour;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.ufrgs.inf.bdi4jade.belief.TransientBelief;
import br.ufrgs.inf.bdi4jade.plan.PlanBody;
import br.ufrgs.inf.bdi4jade.plan.PlanInstance;
import br.ufrgs.inf.bdi4jade.plan.PlanInstance.EndState;
import br.ufrgs.inf.bdi4jade.preference.SoftgoalPreferences;

/**
 * @author ingrid
 * 
 */
public class TransportationPlanBody extends OneShotBehaviour implements
		PlanBody {

	class Scenario {

		Double comfort;
		Double cost;
		Boolean crashed;
		Random rand;
		Boolean robbed;
		Double timeTaken;

		public Scenario() {
			this.rand = new Random(System.currentTimeMillis());
			this.crashed = occurred(rand, plan.getCrashProbability());
			this.comfort = plan.getComfort();
			this.timeTaken = TransportationPlan.MAX_TIME_TAKEN;
			this.cost = plan.getCost();
			if (!crashed) {
				this.timeTaken = new Double(rand.nextInt(plan.getMaxTime()
						- plan.getMinTime())
						+ plan.getMinTime());
			} else {
				if (!plan.isCostConstant())
					this.cost = 1.0;
			}
			this.robbed = occurred(rand, plan.getBeingRobbedProbability());
		}

		public Double getSatisfaction() {
			double safetySatisfaction = crashed ? 0.0 : 1.0;
			log.debug("safetySatisfaction = " + safetySatisfaction);

			double costSatisfaction = 1 - cost;
			log.debug("costSatisfaction = " + costSatisfaction);

			double comfortSatisfaction = comfort;
			log.debug("comfortSatisfaction = " + comfortSatisfaction);

			double performanceSatisfaction = crashed ? 0.0
					: 1 - (timeTaken / TransportationPlan.MAX_TIME_TAKEN);
			log.debug("performanceSatisfaction = " + performanceSatisfaction);

			double securitySatisfaction = robbed ? 0.0 : 1.0;
			log.debug("securitySatisfaction = " + securitySatisfaction);

			double satisfaction = preferences
					.getPreferenceForSoftgoal(Softgoals.SAFETY)
					* safetySatisfaction
					+ preferences.getPreferenceForSoftgoal(Softgoals.COST)
					* costSatisfaction
					+ preferences.getPreferenceForSoftgoal(Softgoals.COMFORT)
					* comfortSatisfaction
					+ preferences
							.getPreferenceForSoftgoal(Softgoals.PERFORMANCE)
					* performanceSatisfaction
					+ preferences.getPreferenceForSoftgoal(Softgoals.SECURITY)
					* securitySatisfaction;
			log.debug("Total Satisfaction = " + satisfaction);
			return satisfaction;
		}

		private boolean occurred(Random rand, double probability) {
			double number = rand.nextDouble();
			return (number < probability);
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[ crashed = ").append(crashed).append(", cost = ")
					.append(cost).append(", time taken = ").append(timeTaken)
					.append(", robbed = ").append(robbed).append(" ]");
			return sb.toString();
		}

	}

	private static final long serialVersionUID = -9039447524062487795L;

	private Log log;
	private TransportationPlan plan;
	private SoftgoalPreferences preferences;
	private GenericValueFunction<Integer> satisfaction;
	private EndState endState = null;

	public void action() {
		log.debug("Plan executed: " + this.plan.getId());
		Scenario scenario = new Scenario();
		double satisfaction = scenario.getSatisfaction();
		this.satisfaction.addValue(this.satisfaction.getCount() + 1,
				satisfaction);
		log.debug("Plan finished!");
		this.endState = EndState.SUCCESSFUL;
	}

	public EndState getEndState() {
		return endState;
	}

	@SuppressWarnings("unchecked")
	public void init(PlanInstance planInstance) {
		this.log = LogFactory.getLog(this.getClass());
		this.plan = (TransportationPlan) planInstance.getPlan();
		this.satisfaction = ((TransientBelief<GenericValueFunction<Integer>>) planInstance
				.getBeliefBase().getBelief(TransportationAgent.SATISFACTION))
				.getValue();
		this.preferences = (SoftgoalPreferences) planInstance.getBeliefBase()
				.getBelief(SoftgoalPreferences.NAME);
	}
}
