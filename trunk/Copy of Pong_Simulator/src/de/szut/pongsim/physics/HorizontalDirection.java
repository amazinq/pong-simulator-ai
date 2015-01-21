package de.szut.pongsim.physics;

/**
 * Represents the direction of the ball.
 * @author Marc Huisinga
 *
 */
public enum HorizontalDirection {
	/**
	 * Ball is moving to the left
	 */
	LEFT(-1),
	/**
	 * Ball is moving to the right
	 */
	RIGHT(1);
	
	private int nextStep;
	
	/**
	 * Constructor
	 * @param nextStep - alteration of the ball position (-1 or 1)
	 */
	HorizontalDirection(int nextStep) {
		this.nextStep = nextStep;
	}
	
	/**
	 * Toggles the direction.
	 * @return toggled direction
	 */
	public HorizontalDirection toggled() {
		if (this == HorizontalDirection.LEFT) {
			return HorizontalDirection.RIGHT;
		}
		return HorizontalDirection.LEFT;
	}
	
	/**
	 * Gets the next step of the ball.
	 * @return -1 or 1, depending on the direction
	 */
	public int getNextStep() {
		return nextStep;
	}
	
}
