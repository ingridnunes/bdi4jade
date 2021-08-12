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

import java.util.Set;

import bdi4jade.annotation.Belief;
import bdi4jade.annotation.GoalOwner;
import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.annotation.Plan;
import bdi4jade.belief.BeliefSet;
import bdi4jade.belief.TransientBeliefSet;
import bdi4jade.core.Capability;
import bdi4jade.examples.blocksworld.domain.Clear;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.examples.blocksworld.plan.AchieveOnPlanBody;
import bdi4jade.examples.blocksworld.plan.ClearPlanBody;
import bdi4jade.examples.blocksworld.plan.PerformMovePlanBody;
import bdi4jade.examples.blocksworld.plan.TopLevelPlanBody;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalTemplateFactory;
import bdi4jade.plan.DefaultPlan;

/**
 * @author Ingrid Nunes
 */
public class BlocksWorldCapability extends Capability {

	@GoalOwner(capability = BlocksWorldCapability.class, internal = true)
	public static class PerformMove implements Goal {
		private static final long serialVersionUID = 8286023371969088149L;

		private Thing thing1;
		private Thing thing2;

		public PerformMove(Thing thing1, Thing thing2) {
			this.thing1 = thing1;
			this.thing2 = thing2;
		}

		@Parameter(direction = Direction.IN)
		public Thing getThing1() {
			return thing1;
		}

		@Parameter(direction = Direction.IN)
		public Thing getThing2() {
			return thing2;
		}

		@Override
		public String toString() {
			return "PerformMove: " + thing1 + " to " + thing2;
		}

	}

	public static final String BELIEF_CLEAR = "clear";
	public static final String BELIEF_ON = "on";
	private static final long serialVersionUID = 2298178213927064463L;

	@Plan
	private bdi4jade.plan.Plan achieveBlocksStackedPlan = new DefaultPlan(
			GoalTemplateFactory.hasBeliefValueOfType(BELIEF_ON, Set.class),
			TopLevelPlanBody.class);

	@Plan
	private bdi4jade.plan.Plan achieveOnPlan = new DefaultPlan(
			GoalTemplateFactory.hasValueOfTypeInBeliefSet(BELIEF_ON, On.class),
			AchieveOnPlanBody.class);

	@Belief
	private BeliefSet<String, Clear> clear = new TransientBeliefSet<>(
			BELIEF_CLEAR);

	@Plan
	private bdi4jade.plan.Plan clearPlan = new DefaultPlan(
			GoalTemplateFactory.hasValueOfTypeInBeliefSet(BELIEF_CLEAR,
					Clear.class), ClearPlanBody.class);

	@Belief
	private BeliefSet<String, On> on = new TransientBeliefSet<>(BELIEF_ON);

	@Plan
	private bdi4jade.plan.Plan performMovePlan;

	public BlocksWorldCapability() {
		this.performMovePlan = new DefaultPlan(PerformMove.class,
				PerformMovePlanBody.class) {
			@Override
			public boolean isContextApplicable(Goal goal) {
				if (goal instanceof PerformMove) {
					PerformMove performMove = (PerformMove) goal;
					return clear.hasValue(new Clear(performMove.getThing1()))
							&& clear.hasValue(new Clear(performMove.getThing2()));
				}
				return false;
			}
		};
	}

	@Override
	protected void setup() {
		clear.addValue(new Clear(Thing.BLOCK_4));
		clear.addValue(new Clear(Thing.TABLE));

		on.addValue(new On(Thing.BLOCK_1, Thing.TABLE));
		on.addValue(new On(Thing.BLOCK_3, Thing.BLOCK_1));
		on.addValue(new On(Thing.BLOCK_2, Thing.BLOCK_3));
		on.addValue(new On(Thing.BLOCK_5, Thing.BLOCK_2));
		on.addValue(new On(Thing.BLOCK_4, Thing.BLOCK_5));
	}

}
