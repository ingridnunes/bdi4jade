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

package bdi4jade.belief;

/**
 * This class represents a belief whose value is derived from other beliefs of a
 * belief base. As a belief may be part of many belief bases, a main belief base
 * is associated with this belief. The evaluation of the value of this belief is
 * performed considering the belief of the main belief base.
 * 
 * @author Ingrid Nunes
 */
public abstract class DerivedBelief<K, V> extends AbstractBelief<K, V> {

	private static final long serialVersionUID = 6923761036847007160L;

	private BeliefBase mainBeliefBase;

	/**
	 * The default constructor. It should be only used if persistence frameworks
	 * are used.
	 */
	public DerivedBelief() {
		super();
	}

	/**
	 * Creates a new derived belief. The value of this belief cannot be set as
	 * it is derived from other values of a belief base.
	 * 
	 * @param name
	 *            the belief name.
	 */
	public DerivedBelief(K name) {
		super(name);
	}

	/**
	 * Adds a belief base with which this belief is associated. If the main
	 * belief base of this belief is null, it sets the given belief base as the
	 * main belief base of this belief.
	 * 
	 * @see bdi4jade.belief.AbstractBelief#addBeliefBase(bdi4jade.belief.BeliefBase)
	 */
	@Override
	public void addBeliefBase(BeliefBase beliefBase) {
		if (mainBeliefBase == null) {
			this.mainBeliefBase = beliefBase;
		}
		super.addBeliefBase(beliefBase);
	}

	/**
	 * Evaluates the value of this belief, which is derived from beliefs of the
	 * main belief base associated with this belief.
	 * 
	 * @return the value of this belief.
	 */
	protected abstract V evaluate();

	/**
	 * Returns the main belief base of this belief.
	 * 
	 * @return the main belief base.
	 */
	public BeliefBase getMainBeliefBase() {
		return mainBeliefBase;
	}

	/**
	 * Returns the value of this belief. If the main belief base is null, it
	 * returns null. Otherwise, the method {@link #evaluate()} is invoked.
	 * 
	 * @return the value of this belief.
	 * 
	 * @see bdi4jade.belief.Belief#getValue()
	 */
	@Override
	public V getValue() {
		if (mainBeliefBase == null)
			return null;
		return evaluate();

	}

	/**
	 * Removes a belief base associated with this belief. If the belief base was
	 * the main belief base of this belief, this method sets a random belief
	 * base from the belief bases associated with this belief as the main belief
	 * base. If the removed belief base was the last belief base associated with
	 * this belief, the main belief base is set to null.
	 * 
	 * @see bdi4jade.belief.AbstractBelief#removeBeliefBase(bdi4jade.belief.BeliefBase)
	 */
	@Override
	public void removeBeliefBase(BeliefBase beliefBase) {
		super.removeBeliefBase(beliefBase);
		if (beliefBase.equals(mainBeliefBase)) {
			this.mainBeliefBase = getBeliefBases().isEmpty() ? null
					: getBeliefBases().iterator().next();
		}
	}

	/**
	 * Sets the main belief base of this belief. This method does not verify if
	 * the main belief base is one of the belief bases associated with this
	 * belief.
	 * 
	 * @param mainBeliefBase
	 *            the main belief base to set.
	 */
	public void setMainBeliefBase(BeliefBase mainBeliefBase) {
		this.mainBeliefBase = mainBeliefBase;
	}

	/**
	 * This method throws a {@link NullPointerException}, as this operation is
	 * invalid for this belief. The value of this belief cannot be set as it is
	 * derived from other beliefs.
	 */
	@Override
	protected void updateValue(V value) {
		throw new NullPointerException("Invalid operation.");
	}

}