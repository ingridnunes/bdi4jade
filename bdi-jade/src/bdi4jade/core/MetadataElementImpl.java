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

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides an implementation to the {@link MetadataElement}. It is
 * able to store metadata.
 * 
 * @author Ingrid Nunes
 * 
 */
public abstract class MetadataElementImpl implements MetadataElement {

	protected Map<Object, Object> metadata;

	public MetadataElementImpl() {
		this.metadata = new HashMap<>();
	}

	/**
	 * @see MetadataElement#getMetadata()
	 */
	@Override
	public Map<?, ?> getMetadata() {
		return metadata;
	}

	/**
	 * @see MetadataElement#getMetadata(Object)
	 */
	@Override
	public Object getMetadata(Object name) {
		return this.metadata.get(name);
	}

	/**
	 * @see MetadataElement#hasMetadata(Object)
	 */
	@Override
	public boolean hasMetadata(Object name) {
		return this.hasMetadata(name);
	}

	/**
	 * @see MetadataElement#putMetadata(Object, Object)
	 */
	@Override
	public void putMetadata(Object name, Object value) {
		this.metadata.put(name, value);
	}

	/**
	 * @see MetadataElement#removeMetadata(Object)
	 */
	@Override
	public Object removeMetadata(Object name) {
		return this.metadata.remove(name);
	}

}
