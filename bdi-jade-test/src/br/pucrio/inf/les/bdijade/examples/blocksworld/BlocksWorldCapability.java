/*
 * Created on 26/01/2010 16:40:28 
 */
package br.pucrio.inf.les.bdijade.examples.blocksworld;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.pucrio.inf.les.bdijade.belief.Belief;
import br.pucrio.inf.les.bdijade.belief.BeliefSet;
import br.pucrio.inf.les.bdijade.belief.TransientBeliefSet;
import br.pucrio.inf.les.bdijade.core.BeliefBase;
import br.pucrio.inf.les.bdijade.core.Capability;
import br.pucrio.inf.les.bdijade.core.PlanLibrary;
import br.pucrio.inf.les.bdijade.event.GoalEvent;
import br.pucrio.inf.les.bdijade.event.GoalListener;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.Clear;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.On;
import br.pucrio.inf.les.bdijade.examples.blocksworld.domain.Thing;
import br.pucrio.inf.les.bdijade.examples.blocksworld.goal.AchieveBlocksStacked;
import br.pucrio.inf.les.bdijade.examples.blocksworld.goal.PerformMove;
import br.pucrio.inf.les.bdijade.examples.blocksworld.plan.AchieveOnPlanBody;
import br.pucrio.inf.les.bdijade.examples.blocksworld.plan.ClearPlanBody;
import br.pucrio.inf.les.bdijade.examples.blocksworld.plan.PerformMovePlanBody;
import br.pucrio.inf.les.bdijade.examples.blocksworld.plan.TopLevelPlanBody;
import br.pucrio.inf.les.bdijade.goal.Goal;
import br.pucrio.inf.les.bdijade.plan.Plan;
import br.pucrio.inf.les.bdijade.util.goal.BeliefSetValueGoal;
import br.pucrio.inf.les.bdijade.util.plan.SimplePlan;

/**
 * @author ingrid
 * 
 */
public class BlocksWorldCapability extends Capability implements GoalListener {

	public static final String BELIEF_CLEAR = "clear";
	public static final String BELIEF_ON = "on";

	private static final Log log = LogFactory
			.getLog(BlocksWorldCapability.class);
	private static final long serialVersionUID = -4800805796961540570L;

	private static final On[] target = { new On(Thing.BLOCK_9, Thing.TABLE),
			new On(Thing.BLOCK_8, Thing.BLOCK_9),
			new On(Thing.BLOCK_7, Thing.BLOCK_8),
			new On(Thing.BLOCK_6, Thing.BLOCK_7),
			new On(Thing.BLOCK_5, Thing.BLOCK_6),
			new On(Thing.BLOCK_4, Thing.BLOCK_5),
			new On(Thing.BLOCK_3, Thing.BLOCK_4),
			new On(Thing.BLOCK_2, Thing.BLOCK_3),
			new On(Thing.BLOCK_1, Thing.BLOCK_2) };

	private static Set<Belief<?>> getBeliefs() {
		Set<Belief<?>> beliefs = new HashSet<Belief<?>>();

		BeliefSet<On> on = new TransientBeliefSet<On>(BELIEF_ON);
		on.addValue(new On(Thing.BLOCK_5, Thing.TABLE));
		on.addValue(new On(Thing.BLOCK_2, Thing.BLOCK_5));
		on.addValue(new On(Thing.BLOCK_7, Thing.BLOCK_2));
		on.addValue(new On(Thing.BLOCK_1, Thing.BLOCK_7));
		on.addValue(new On(Thing.BLOCK_8, Thing.BLOCK_1));
		on.addValue(new On(Thing.BLOCK_4, Thing.BLOCK_8));
		on.addValue(new On(Thing.BLOCK_3, Thing.BLOCK_4));
		on.addValue(new On(Thing.BLOCK_6, Thing.BLOCK_3));
		on.addValue(new On(Thing.BLOCK_9, Thing.BLOCK_6));
		beliefs.add(on);

		BeliefSet<Clear> clear = new TransientBeliefSet<Clear>(BELIEF_CLEAR);
		clear.addValue(new Clear(Thing.BLOCK_9));
		clear.addValue(new Clear(Thing.TABLE));
		beliefs.add(clear);

		return beliefs;
	}

	private static Set<Plan> getPlans() {
		Set<Plan> plans = new HashSet<Plan>();

		plans.add(new SimplePlan(BeliefSetValueGoal.class,
				AchieveOnPlanBody.class) {
			@Override
			protected boolean matchesContext(Goal goal) {
				return BlocksWorldCapability.BELIEF_ON
						.equals(((BeliefSetValueGoal<?>) goal)
								.getBeliefSetName());
			}
		});
		plans
				.add(new SimplePlan(BeliefSetValueGoal.class,
						ClearPlanBody.class) {
					@Override
					protected boolean matchesContext(Goal goal) {
						return BlocksWorldCapability.BELIEF_CLEAR
								.equals(((BeliefSetValueGoal<?>) goal)
										.getBeliefSetName());
					}
				});
		plans.add(new SimplePlan(PerformMove.class, PerformMovePlanBody.class) {
			@Override
			@SuppressWarnings("unchecked")
			protected boolean matchesContext(Goal goal) {
				if (goal instanceof PerformMove) {
					PerformMove performMove = (PerformMove) goal;
					BeliefSet<Clear> set = (BeliefSet<Clear>) getPlanLibrary()
							.getCapability().getBeliefBase().getBelief(
									BlocksWorldCapability.BELIEF_CLEAR);
					return set.hasValue(new Clear(performMove.getThing1()))
							&& set.hasValue(new Clear(performMove.getThing2()));
				}
				return false;
			}
		});
		plans.add(new SimplePlan(AchieveBlocksStacked.class,
				TopLevelPlanBody.class));

		return plans;
	}

	public BlocksWorldCapability() {
		super(new BeliefBase(getBeliefs()), new PlanLibrary(getPlans()));
	}

	@Override
	public void goalPerformed(GoalEvent event) {
		if (event.getGoal() instanceof AchieveBlocksStacked) {
			log.info("Goal achieved!! Removing capability of this agent...");
			myAgent.removeCapability(this);
		}
	}

	@Override
	protected void setup() {
		myAgent.addGoal(new AchieveBlocksStacked(target), this);
	}

}
