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

package bdi4jade.examples.blocksworld;

import bdi4jade.belief.BeliefSet;
import bdi4jade.belief.TransientBeliefSet;
import bdi4jade.core.BDIAgent;
import bdi4jade.examples.blocksworld.domain.Clear;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.examples.blocksworld.goal.AchieveBlocksStacked;
import bdi4jade.examples.blocksworld.goal.PerformMove;
import bdi4jade.examples.blocksworld.plan.AchieveOnPlanBody;
import bdi4jade.examples.blocksworld.plan.ClearPlanBody;
import bdi4jade.examples.blocksworld.plan.PerformMovePlanBody;
import bdi4jade.examples.blocksworld.plan.TopLevelPlanBody;
import bdi4jade.goal.Goal;
import bdi4jade.plan.SimplePlan;
import bdi4jade.util.goal.BeliefSetValueGoal;
import bdi4jade.util.plan.BeliefValueGoalPlan;

/**
 * @author ingrid
 * 
 */
public class BlocksWorldAgent extends BDIAgent {

	public static final String BELIEF_CLEAR = "clear";
	public static final String BELIEF_ON = "on";

	private static final long serialVersionUID = -4800805796961540570L;

	public BlocksWorldAgent() {
		// Beliefs
		BeliefSet<On> on = new TransientBeliefSet<On>(BELIEF_ON);
		on.addValue(new On(Thing.BLOCK_1, Thing.TABLE));
		on.addValue(new On(Thing.BLOCK_3, Thing.BLOCK_1));
		on.addValue(new On(Thing.BLOCK_2, Thing.BLOCK_3));
		on.addValue(new On(Thing.BLOCK_5, Thing.BLOCK_2));
		on.addValue(new On(Thing.BLOCK_4, Thing.BLOCK_5));
		getRootCapability().getBeliefBase().addBelief(on);

		BeliefSet<Clear> clear = new TransientBeliefSet<Clear>(BELIEF_CLEAR);
		clear.addValue(new Clear(Thing.BLOCK_4));
		clear.addValue(new Clear(Thing.TABLE));
		getRootCapability().getBeliefBase().addBelief(clear);

		// Plans
		getRootCapability().getPlanLibrary().addPlan(
				new BeliefValueGoalPlan(BeliefSetValueGoal.class,
						BlocksWorldAgent.BELIEF_ON, On.class,
						AchieveOnPlanBody.class));
		getRootCapability().getPlanLibrary().addPlan(
				new BeliefValueGoalPlan(BeliefSetValueGoal.class,
						BlocksWorldAgent.BELIEF_CLEAR, Clear.class,
						ClearPlanBody.class));
		getRootCapability().getPlanLibrary().addPlan(
				new SimplePlan(PerformMove.class, PerformMovePlanBody.class) {
					@Override
					@SuppressWarnings("unchecked")
					protected boolean matchesContext(Goal goal) {
						if (goal instanceof PerformMove) {
							PerformMove performMove = (PerformMove) goal;
							BeliefSet<Clear> set = (BeliefSet<Clear>) getPlanLibrary()
									.getCapability().getBeliefBase()
									.getBelief(BlocksWorldAgent.BELIEF_CLEAR);
							return set.hasValue(new Clear(performMove
									.getThing1()))
									&& set.hasValue(new Clear(performMove
											.getThing2()));
						}
						return false;
					}
				});
		getRootCapability().getPlanLibrary().addPlan(
				new SimplePlan(AchieveBlocksStacked.class,
						TopLevelPlanBody.class));
	}

}
