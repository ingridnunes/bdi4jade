package br.pucrio.inf.les.bdijade.examples.blocksworld.domain;

/*
 * Created on 26/01/2010 10:09:12 
 */

/**
 * @author ingrid
 * 
 */
public interface Thing {

	public static final Table TABLE = new Table();
	public static final Block BLOCK_1 = new Block(1);
	public static final Block BLOCK_2 = new Block(2);
	public static final Block BLOCK_3 = new Block(3);
	public static final Block BLOCK_4 = new Block(4);
	public static final Block BLOCK_5 = new Block(5);
	public static final Block BLOCK_6 = new Block(6);
	public static final Block BLOCK_7 = new Block(7);
	public static final Block BLOCK_8 = new Block(8);
	public static final Block BLOCK_9 = new Block(9);
	public static final Thing[] THINGS = { TABLE, BLOCK_1, BLOCK_2, BLOCK_3,
			BLOCK_4, BLOCK_5, BLOCK_6, BLOCK_7, BLOCK_8, BLOCK_9 };

}
