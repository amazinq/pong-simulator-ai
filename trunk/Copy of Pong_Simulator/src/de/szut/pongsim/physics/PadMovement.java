package de.szut.pongsim.physics;

/**
 * Decisions the AI is allowed to make in regard to moving the pad.
 * @author Marc Huisinga
 *
 */
public enum PadMovement {
	/**
	 * Move the pad upwards
	 */
	UP(1), 
	/**
	 * Move the pad downwards
	 */
	DOWN(-1), 
	/**
	 * Don't move the pad
	 */
	STOP(0);
	
	private int nextStep;
	
	/**
	 * Constructor
	 * @param nextStep - next step of the pad, usually -1 <= nextStep <= 1
	 */
	PadMovement(int nextStep) {
		this.nextStep = nextStep;
	}
	
	/**
	 * Gets the next step of the pad.
	 * @return -1 <= nextStep <= 1
	 */
	public int getNextStep() {
		return nextStep;
	}
	
}
