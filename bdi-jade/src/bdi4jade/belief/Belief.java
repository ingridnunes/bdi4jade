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

package bdi4jade.belief;

import jade.content.Concept;

import java.io.Serializable;
import java.util.Set;

import bdi4jade.core.MetadataElement;

/**
 * Represents a belief of the belief base. It has a name and a value associate
 * with it.
 * 
 * @author ingrid
 */
public interface Belief<T> extends MetadataElement, Serializable, Concept {

	/**
	 * Adds a belief base that contains this belief. The agent whose capability
	 * contains this belief in the belief base believes in this belief.
	 * 
	 * @param beliefBase
	 *            the belief base to be added.
	 */
	public void addBeliefBase(BeliefBase beliefBase);

	/**
	 * @return the beliefBases
	 */
	public Set<BeliefBase> getBeliefBases();

	public String getName();

	/**
	 * Gets the current value of the Belief.
	 * 
	 * @return the belief value.
	 */
	public T getValue();

	/**
	 * Removes a belief base that does not contain this belief anymore. The
	 * agent whose capability does not contain this belief in the belief base
	 * does not believe in this belief anymore.
	 * 
	 * @param beliefBases
	 *            the belief base to be removed.
	 */
	public void removeBeliefBase(BeliefBase beliefBase);

	/**
	 * Sets a new value to the belief.
	 * 
	 * @param value
	 *            the new value.
	 */
	public void setValue(T value);

}
