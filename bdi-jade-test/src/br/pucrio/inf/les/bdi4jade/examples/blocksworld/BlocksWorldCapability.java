//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes, et al.
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
// http://www.inf.puc-rio.br/~ionunes/
//
//----------------------------------------------------------------------------

package br.pucrio.inf.les.bdi4jade.examples.blocksworld;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.pucrio.inf.les.bdi4jade.belief.Belief;
import br.pucrio.inf.les.bdi4jade.belief.BeliefSet;
import br.pucrio.inf.les.bdi4jade.belief.TransientBeliefSet;
import br.pucrio.inf.les.bdi4jade.core.BeliefBase;
import br.pucrio.inf.les.bdi4jade.core.Capability;
import br.pucrio.inf.les.bdi4jade.core.PlanLibrary;
import br.pucrio.inf.les.bdi4jade.event.GoalEvent;
import br.pucrio.inf.les.bdi4jade.event.GoalListener;
import br.pucrio.inf.les.bdi4jade.examples.blocksworld.domain.Clear;
import br.pucrio.inf.les.bdi4jade.examples.blocksworld.domain.On;
import br.pucrio.inf.les.bdi4jade.examples.blocksworld.domain.Thing;
import br.pucrio.inf.les.bdi4jade.examples.blocksworld.goal.AchieveBlocksStacked;
import br.pucrio.inf.les.bdi4jade.examples.blocksworld.goal.PerformMove;
import br.pucrio.inf.les.bdi4jade.examples.blocksworld.plan.AchieveOnPlanBody;
import br.pucrio.inf.les.bdi4jade.examples.blocksworld.plan.ClearPlanBody;
import br.pucrio.inf.les.bdi4jade.examples.blocksworld.plan.PerformMovePlanBody;
import br.pucrio.inf.les.bdi4jade.examples.blocksworld.plan.TopLevelPlanBody;
import br.pucrio.inf.les.bdi4jade.goal.Goal;
import br.pucrio.inf.les.bdi4jade.plan.Plan;
import br.pucrio.inf.les.bdi4jade.util.goal.BeliefSetValueGoal;
import br.pucrio.inf.les.bdi4jade.util.plan.SimplePlan;

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
		plans.add(new SimplePlan(BeliefSetValueGoal.class, ClearPlanBody.class) {
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
							.getCapability().getBeliefBase()
							.getBelief(BlocksWorldCapability.BELIEF_CLEAR);
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
