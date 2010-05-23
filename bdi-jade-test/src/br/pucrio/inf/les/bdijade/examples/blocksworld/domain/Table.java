/*
 * Created on 27/01/2010 16:23:23 
 */
package br.pucrio.inf.les.bdijade.examples.blocksworld.domain;

/**
 * @author ingrid
 * 
 */
public class Table implements Thing {

	public Table() {

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Table) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Table";
	}

}
