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

import jade.content.Concept;
import jade.content.ContentElement;

import java.io.Serializable;
import java.util.Set;

import bdi4jade.core.MetadataElement;

/**
 * This interface represents a belief of the belief base. It has a name (or a
 * key) and a value associate with it. It is parameterized by the types of the
 * name/key and value. For example, a name may be an object representing a
 * propositional formula, and the value is a boolean indicating whether the
 * formula is true or false.
 * 
 * It extends the {@link MetadataElement} interface, allowing to associate
 * metadata with beliefs.
 * 
 * @param <K>
 *            the type of the belief name or key.
 * 
 * @param <V>
 *            the type of the belief value.
 * 
 * @author Ingrid Nunes
 */
public interface Belief<K, V> extends Serializable, Concept, ContentElement,
		MetadataElement, Cloneable {

	/**
	 * Adds a belief base that contains this belief. The agent whose capability
	 * contains this belief in the belief base believes in this belief.
	 * 
	 * @param beliefBase
	 *            the belief base to be added.
	 */
	public void addBeliefBase(BeliefBase beliefBase);

	/**
	 * Clones a belief. This may be particularly useful to share this belief
	 * with other agents, copying the information of this belief without sharing
	 * other information, such as belief bases associated with a belief.
	 * 
	 * @return the clone of this belief.
	 * @throws CloneNotSupportedException
	 *             if an implementation of a belief does not support clone.
	 */
	public Object clone() throws CloneNotSupportedException;

	/**
	 * Returns the belief bases with which this belief is associated.
	 * 
	 * @return the belief bases.
	 */
	public Set<BeliefBase> getBeliefBases();

	/**
	 * Returns the name of this belief.
	 * 
	 * @return the string that is the belief name.
	 */
	public K getName();

	/**
	 * Gets the current value of the belief.
	 * 
	 * @return the belief value.
	 */
	public V getValue();

	/**
	 * Removes a belief base that does not contain this belief anymore. The
	 * agent whose capability does not contain this belief in the belief base
	 * does not believe in this belief anymore.
	 * 
	 * @param beliefBase
	 *            the belief base to be removed.
	 */
	public void removeBeliefBase(BeliefBase beliefBase);

	/**
	 * Sets the new value of the belief.
	 * 
	 * @param value
	 *            the new value.
	 */
	public void setValue(V value);

}
