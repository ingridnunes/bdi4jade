package br.pucrio.inf.les.bdijade.examples.blocksworld.domain;

/*
 * Created on 26/01/2010 10:08:37 
 */

/**
 * @author ingrid
 * 
 */
public class On {

	private Thing thing1;
	private Thing thing2;

	public On(Thing thing1, Thing thing2) {
		this.thing1 = thing1;
		this.thing2 = thing2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof On) {
			On other = (On) obj;
			return this.thing1.equals(other.thing1)
					&& this.thing2.equals(other.thing2);
		} else {
			return false;
		}
	}

	public Thing getThing1() {
		return thing1;
	}

	public Thing getThing2() {
		return thing2;
	}

	@Override
	public int hashCode() {
		return (int) Math.pow(thing1.hashCode(), thing2.hashCode());
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("ON");
		sb.append("_").append(thing1);
		sb.append("_").append(thing2);
		return sb.toString();
	}

}
