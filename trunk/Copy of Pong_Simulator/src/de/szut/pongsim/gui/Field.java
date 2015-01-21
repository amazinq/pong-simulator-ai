package de.szut.pongsim.gui;

import de.szut.pongsim.physics.PadMovement;
import de.szut.pongsim.physics.Point;

/**
 * Field Interface
 * 
 * @author Steffen Wiﬂmann
 *
 */
public interface Field {

	/**
	 * Sets the new position of the ball
	 * 
	 * @param point
	 */
	void moveBall(Point point);

	/**
	 * Sets the new position of the left pad
	 * 
	 * @param padMovement
	 */
	void moveLeftPad(PadMovement padMovement);

	/**
	 * Sets the new position of the right pad
	 * 
	 * @param padMovement
	 */
	void moveRightPad(PadMovement padMovement);

	/**
	 * If invalid AIs are chosen, an error will be displayed by this method
	 */
	void needUsersError();

	/**
	 * Increments the point counter for the left player
	 */
	void incrementLeftCounter();

	/**
	 * Increments the point counter for the right player
	 */
	void incrementRightCounter();

	/**
	 * Displays if halftime is reached
	 */
	void displayHalftime();

	/**
	 * Displays victory for the left player
	 */
	void displayVictoryLeft();

	/**
	 * Displays victory for the right player
	 */
	void displayVictoryRight();

	/**
	 * Displays draw
	 */
	void displayDraw();

	/**
	 * Resets the positions of the pads and the ball after a round has ended
	 * 
	 * @param point
	 */
	void respawn(Point point);

	/**
	 * Resets the whole gamefield also resets counters
	 */
	void reset();
	
}
