package br.pucrio.inf.les.bdijade.examples.blocksworld.domain;

/*
 * Created on 26/01/2010 10:16:20 
 */

/**
 * @author ingrid
 * 
 */
public class Clear {

	private Thing thing;

	public Clear(Thing thing) {
		this.thing = thing;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Clear) {
			Clear other = (Clear) obj;
			return this.thing.equals(other.thing);
		} else {
			return false;
		}
	}

	public Thing getThing() {
		return thing;
	}

	@Override
	public int hashCode() {
		return thing.hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CLEAR");
		sb.append("_").append(thing);
		return sb.toString();
	}

}
