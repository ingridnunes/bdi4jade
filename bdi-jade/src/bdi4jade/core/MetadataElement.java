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

package bdi4jade.core;

import java.util.Map;

/**
 * This interface defines methods that a certain element should implement to
 * store metadata. Such metadata may be used in reasoning processes.
 * 
 * @author Ingrid Nunes
 * 
 */
public interface MetadataElement {

	/**
	 * @return the metadata
	 */
	public Map<?, ?> getMetadata();

	/**
	 * Gets a value of a metadata.
	 * 
	 * @param name
	 *            the name of the metadata.
	 * @return the existing value of this metadata.
	 */
	public Object getMetadata(Object name);

	/**
	 * Verifies if a metadata is associated with this element.
	 * 
	 * @param name
	 *            the name of the metadata.
	 * @return true if the element has this metadata.
	 */
	public boolean hasMetadata(Object name);

	/**
	 * Put a metadata in this element. If it does not exists, it is added, and
	 * it is update otherwise.
	 * 
	 * @param name
	 *            the name of the metadata.
	 * @param value
	 *            the value associated with this metadata.
	 */
	public void putMetadata(Object name, Object value);

	/**
	 * Removes a metadata of this element.
	 * 
	 * @param name
	 *            the name of the metadata.
	 * @return the existing value of this metadata.
	 */
	public Object removeMetadata(Object name);

}
