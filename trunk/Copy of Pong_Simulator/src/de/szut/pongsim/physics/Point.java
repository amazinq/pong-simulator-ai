package de.szut.pongsim.physics;

/**
 * Represents a 2D Point on the board
 * @author Marc Huisinga
 *
 */
public final class Point {
	
	private final int x;
	private final int y;
	
	/**
	 * Constructor
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
}
