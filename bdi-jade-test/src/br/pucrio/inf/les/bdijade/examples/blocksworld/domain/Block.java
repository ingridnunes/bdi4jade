/*
 * Created on 27/01/2010 16:21:43 
 */
package br.pucrio.inf.les.bdijade.examples.blocksworld.domain;

/**
 * @author ingrid
 * 
 */
public class Block implements Thing {

	private int id;

	public Block(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Block) {
			Block b = (Block) obj;
			return this.id == b.id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new Integer(id).hashCode();
	}

	@Override
	public String toString() {
		return "Block#" + id;
	}

}
