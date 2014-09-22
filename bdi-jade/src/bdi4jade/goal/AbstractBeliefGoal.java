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

package bdi4jade.goal;

import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.belief.Belief;

/**
 * This is an abstract implementation of a belief goal.
 * 
 * @author Ingrid Nunes
 */
public abstract class AbstractBeliefGoal<K> implements BeliefGoal<K> {

	private static final long serialVersionUID = 2493877854717226283L;

	protected K beliefName;
	protected Belief<K, ?> outputBelief;

	/**
	 * Default constructor.
	 */
	public AbstractBeliefGoal() {

	}

	/**
	 * Creates a new AbstractBeliefGoal with the provided belief.
	 * 
	 * @param beliefName
	 *            the belief name.
	 */
	public AbstractBeliefGoal(K beliefName) {
		this.beliefName = beliefName;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractBeliefGoal) {
			AbstractBeliefGoal<?> bg = (AbstractBeliefGoal<?>) obj;
			return beliefName.equals(bg.beliefName);
		}
		return false;
	}

	/**
	 * Returns the name of the belief associated with this goal.
	 * 
	 * @return the belief name.
	 */
	@Parameter(direction = Direction.IN)
	public K getBeliefName() {
		return beliefName;
	}

	/**
	 * Returns the belief which is the output of this goal achievement.
	 * 
	 * @return the belief.
	 */
	@Override
	@Parameter(direction = Direction.OUT)
	public Belief<K, ?> getOutputBelief() {
		return outputBelief;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = AbstractBeliefGoal.class.hashCode();
		result = prime * result
				+ ((beliefName == null) ? 0 : beliefName.hashCode());
		return result;
	}

	/**
	 * Sets the name of the belief associated with this goal.
	 * 
	 * @param beliefName
	 *            the belief name.
	 */
	public void setBeliefName(K beliefName) {
		this.beliefName = beliefName;
	}

	/**
	 * Sets the belief which is the output of this goal achievement.
	 * 
	 * @param belief the belief.
	 */
	@Override
	public void setOutputBelief(Belief<K, ?> belief) {
		this.outputBelief = belief;
	}

}
