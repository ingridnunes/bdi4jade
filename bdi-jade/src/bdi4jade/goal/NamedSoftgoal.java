//----------------------------------------------------------------------------
// Copyright (C) 2013  Ingrid Nunes
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

import bdi4jade.core.MetadataElement;
import bdi4jade.core.MetadataElementImpl;

/**
 * This class provides a default implementation for a softgoal, representing it
 * just with a given name.
 * 
 * It implements the {@link MetadataElement} interface, allowing to associate
 * metadata with softgoals.
 * 
 * @author Ingrid Nunes
 * 
 */
public class NamedSoftgoal extends MetadataElementImpl implements Softgoal,
		MetadataElement {

	private static final long serialVersionUID = 3958189054716876043L;

	private String name;

	/**
	 * The default constructor. It should be only used if persistence frameworks
	 * are used.
	 */
	protected NamedSoftgoal() {

	}

	/**
	 * Initializes a softgoal with its name.
	 * 
	 * @param name
	 *            the softgoal name.
	 */
	public NamedSoftgoal(String name) {
		this.name = name;
	}

	/**
	 * Returns true of the object is a named softgoal and has the same name of
	 * this named softgoal.
	 * 
	 * @param obj
	 *            to object to be tested if it is equal to this named softgoal.
	 * 
	 * @see Object#equals(Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof NamedSoftgoal) {
			NamedSoftgoal sg = (NamedSoftgoal) obj;
			return this.name.equals(sg.name);
		}
		return false;
	}

	/**
	 * Returns the name of this softgoal.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the hash code of this named softgoal.
	 * 
	 * @return the hash code of the name of this softgoal.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return name == null ? 0 : this.name.hashCode();
	}

	/**
	 * Sets the name of this softgoal. Ideally, the name should be final and
	 * initialized in the constructor. This method should be only used if
	 * persistence frameworks are used.
	 * 
	 * @param name
	 *            the name to set.
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the string representation of this softgoal, which is its name.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}
