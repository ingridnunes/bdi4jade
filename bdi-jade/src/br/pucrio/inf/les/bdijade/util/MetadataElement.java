/*
 * Created on 28/01/2010 20:29:58 
 */
package br.pucrio.inf.les.bdijade.util;

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
