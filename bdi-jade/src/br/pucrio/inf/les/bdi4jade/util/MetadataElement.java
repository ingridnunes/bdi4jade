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

package br.pucrio.inf.les.bdi4jade.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ingrid
 * 
 */
public abstract class MetadataElement {

	protected Map<String, Object> metadata;

	public MetadataElement() {
		this.metadata = new HashMap<String, Object>();
	}

	/**
	 * @return the metadata
	 */
	public Map<String, Object> getMetadata() {
		return metadata;
	}

	/**
	 * Gets a value of a metadata.
	 * 
	 * @param name
	 *            the name of the metadata.
	 * @return the existing value of this metadata.
	 */
	public Object getMetadata(String name) {
		return this.metadata.get(name);
	}

	/**
	 * Verifies if a metadata is associated with this element.
	 * 
	 * @param name
	 *            the name of the metadata.
	 * @return true if the element has this metadata.
	 */
	public boolean hasMetadata(String name) {
		return this.hasMetadata(name);
	}

	/**
	 * Put a metadata in this element. If it does not exists, it is added, and it
	 * is update otherwise.
	 * 
	 * @param name
	 *            the name of the metadata.
	 * @param value
	 *            the value associated with this metadata.
	 */
	public void putMetadata(String name, Object value) {
		this.metadata.put(name, value);
	}

	/**
	 * Removes a metadata of this element.
	 * 
	 * @param name
	 *            the name of the metadata.
	 * @return the existing value of this metadata.
	 */
	public Object removeMetadata(String name) {
		return this.metadata.remove(name);
	}

}
