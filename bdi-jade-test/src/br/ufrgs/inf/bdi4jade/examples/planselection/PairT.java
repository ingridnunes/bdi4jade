/*
 * Created on 30 Jul 2011 10:29:17 
 */
package br.ufrgs.inf.bdi4jade.examples.planselection;

/**
 * @author ingrid
 * 
 */
public class PairT<T1, T2> {

	protected T1 value1;
	protected T2 value2;

	public PairT() {
		this.value1 = null;
		this.value2 = null;
	}

	public PairT(T1 value1, T2 value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PairT other = (PairT) obj;
		if (value1 == null) {
			if (other.value1 != null)
				return false;
		} else if (!value1.equals(other.value1))
			return false;
		if (value2 == null) {
			if (other.value2 != null)
				return false;
		} else if (!value2.equals(other.value2))
			return false;
		return true;
	}

	public T1 getValue1() {
		return value1;
	}

	public T2 getValue2() {
		return value2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value1 == null) ? 0 : value1.hashCode());
		result = prime * result + ((value2 == null) ? 0 : value2.hashCode());
		return result;
	}

	public void setValue1(T1 value1) {
		this.value1 = value1;
	}

	public void setValue2(T2 value2) {
		this.value2 = value2;
	}

	@Override
	public String toString() {
		return "<" + value1 + ", " + value2 + ">";
	}

}