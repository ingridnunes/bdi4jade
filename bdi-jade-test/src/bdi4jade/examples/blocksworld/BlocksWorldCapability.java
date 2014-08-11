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

package bdi4jade.examples.blocksworld;

import bdi4jade.annotation.Belief;
import bdi4jade.annotation.Plan;
import bdi4jade.belief.BeliefSet;
import bdi4jade.belief.TransientBeliefSet;
import bdi4jade.core.Capability;
import bdi4jade.examples.blocksworld.domain.Clear;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.examples.blocksworld.goal.AchieveBlocksStacked;
import bdi4jade.examples.blocksworld.goal.PerformMove;
import bdi4jade.examples.blocksworld.plan.AchieveOnPlanBody;
import bdi4jade.examples.blocksworld.plan.PerformMovePlanBody;
import bdi4jade.examples.blocksworld.plan.TopLevelPlanBody;
import bdi4jade.goal.Goal;
import bdi4jade.plan.GoalTemplate;
import bdi4jade.plan.SimplePlan;

/**
 * @author ingrid
 * 
 */
public class BlocksWorldCapability extends Capability {

	public static final String BELIEF_CLEAR = "clear";

	public static final String BELIEF_ON = "on";
	private static final long serialVersionUID = 2298178213927064463L;

	@Plan
	private bdi4jade.plan.Plan achieveBlocksStackedPlan;

	@Plan
	private bdi4jade.plan.Plan achieveOnPlan;

	@Belief
	private BeliefSet<Clear> clear;

	@Plan
	private bdi4jade.plan.Plan clearPlan;

	@Belief
	private BeliefSet<On> on;

	@Plan
	private bdi4jade.plan.Plan performMovePlan;

	public BlocksWorldCapability() {
		this.on = new TransientBeliefSet<On>(BELIEF_ON);
		on.addValue(new On(Thing.BLOCK_1, Thing.TABLE));
		on.addValue(new On(Thing.BLOCK_3, Thing.BLOCK_1));
		on.addValue(new On(Thing.BLOCK_2, Thing.BLOCK_3));
		on.addValue(new On(Thing.BLOCK_5, Thing.BLOCK_2));
		on.addValue(new On(Thing.BLOCK_4, Thing.BLOCK_5));

		this.clear = new TransientBeliefSet<Clear>(BELIEF_CLEAR);
		clear.addValue(new Clear(Thing.BLOCK_4));
		clear.addValue(new Clear(Thing.TABLE));

		this.achieveOnPlan = new SimplePlan(
				GoalTemplate.createBeliefSetTypeGoalTemplate(
						BlocksWorldAgent.BELIEF_ON, On.class),
				AchieveOnPlanBody.class);
		this.clearPlan = new SimplePlan(
				GoalTemplate.createBeliefSetTypeGoalTemplate(
						BlocksWorldAgent.BELIEF_CLEAR, Clear.class),
				AchieveOnPlanBody.class);
		this.performMovePlan = new SimplePlan(PerformMove.class,
				PerformMovePlanBody.class) {
			@Override
			@SuppressWarnings("unchecked")
			public boolean isContextApplicable(Goal goal) {
				if (goal instanceof PerformMove) {
					PerformMove performMove = (PerformMove) goal;
					BeliefSet<Clear> set = (BeliefSet<Clear>) getPlanLibrary()
							.getCapability().getBeliefBase()
							.getBelief(BlocksWorldAgent.BELIEF_CLEAR);
					return set.hasValue(new Clear(performMove.getThing1()))
							&& set.hasValue(new Clear(performMove.getThing2()));
				}
				return false;
			}
		};
		this.achieveBlocksStackedPlan = new SimplePlan(
				AchieveBlocksStacked.class, TopLevelPlanBody.class);
	}

}
