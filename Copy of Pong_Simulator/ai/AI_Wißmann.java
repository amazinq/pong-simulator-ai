package ai;

import de.szut.pongsim.ai.User;
import de.szut.pongsim.physics.PadMovement;
import de.szut.pongsim.physics.Point;

/**
 * AI placed as a player in the pong simulator
 * @author Steffen Wiﬂmann
 *
 */
public class AI_Wiﬂmann implements User {

	private Point firstBallPosition;
	private boolean isFirstStep;
	private boolean isLeftPlayer;
	private boolean alreadyCollided;
	private Point padCollisionPoint;
	
	private static final int FIELD_WIDTH = 65;
	private static final int FIELD_HEIGHT = 60;

	public AI_Wiﬂmann() {
		isFirstStep = true;
		alreadyCollided = false;
		padCollisionPoint = new Point(0, 27);
	}

	@Override
	public void reset() {
		firstBallPosition = null;
		isFirstStep = true;

	}

	@Override
	public void setLeftSide() {
		isLeftPlayer = true;

	}

	@Override
	public void setRightSide() {
		isLeftPlayer = false;

	}


	/**
	 * inteface between the AI and model
	 * @param ownPadBottomY lowest point of your own gamepad
	 * @param enemyPadBottomY lowest point of the enemys gamepad
	 * @return next padmovement (UP, DOWN, STOP)
	 */
	@Override
	public PadMovement nextStep(int ownPadBottomY, int enemyPadBottomY) {
		if (padCollisionPoint.getY() < ownPadBottomY + 1) {
			return PadMovement.DOWN;
		} else if (padCollisionPoint.getY() > ownPadBottomY + 4) {
			return PadMovement.UP;
		}
		return PadMovement.STOP;
	}

	/**
	 * method updates the ballposition
	 * and also calculates the next
	 * padcollision -> route prediction
	 * @param ballPos current ballposition
	 */
	@Override
	public void updateBallPos(Point ballPos) {
		if (isFirstStep) {
			// resets the current calculation
			// after each collision
			firstBallPosition = ballPos;
			isFirstStep = false;
		} else {
			if (collisionDetected(ballPos)) {
				isFirstStep = true;
			} else {
				// calculates flightroute
				LinearFunction flightRoute = generateLinearFunction(
						firstBallPosition, ballPos);
				if (isLeftPlayer) {
					// is left player
					if (ballPos.getX() < firstBallPosition.getX()) {
						// is defender
						double y = flightRoute.getY(1);
						// calculates the final point of collision
						if (calculateAmountOfCollisions(y) % 2 == 1) {
							padCollisionPoint = new Point(0,
									FIELD_HEIGHT - Math.abs((int) y % FIELD_HEIGHT));
						} else {
							padCollisionPoint = new Point(0,
									Math.abs((int) y % FIELD_HEIGHT));
						}
					} else {
						// is not defender
						// moves the pad to the mid if hes not defender
						padCollisionPoint = new Point(0, 27);
					}
				} else {
					// is right player
					if (ballPos.getX() > firstBallPosition.getX()) {
						// is defender
						double y = flightRoute.getY(FIELD_WIDTH -2);
						// calculates the final point of collision
						if (calculateAmountOfCollisions(y) % 2 == 1) {
							padCollisionPoint = new Point(FIELD_WIDTH -2,
									FIELD_HEIGHT - Math.abs((int) y % FIELD_HEIGHT));
						} else {
							padCollisionPoint = new Point(FIELD_WIDTH -2,
									Math.abs((int) y % FIELD_HEIGHT));
						}
					} else {
						// is not defender
						// moves the pad to the mid if hes not defender
						padCollisionPoint = new Point(FIELD_WIDTH -2, 27);
					}
				}
			}
		}
	}

	/**
	 * Generates a linear function by using 2 points
	 * @param firstBallPosition first position after a collision
	 * @param currentBallPosition current ballposition
	 * @return linearfunction reperesents a linear function
	 */
	private LinearFunction generateLinearFunction(
			Point firstBallPosition, Point currentBallPosition) {
		double m = ((double) currentBallPosition.getY() - (double) firstBallPosition
				.getY())
				/ ((double) currentBallPosition.getX() - (double) firstBallPosition
						.getX());
		double b = (double) currentBallPosition.getY() - m
				* (double) currentBallPosition.getX();
		return new LinearFunction(m, b);
	}

	/**
	 * calculates the amount of collisions by
	 * dividing the actual y value with the
	 * field height
	 * if the y value is negative, another collision
	 * needs to be added
	 * at the bottom and top of the gamefield
	 * @param y represents the f(x) value 
	 * @return amount of collisions 
	 */
	private int calculateAmountOfCollisions(double y) {
		if (y < 0 && y / FIELD_HEIGHT > -1 && y / FIELD_HEIGHT < -2) {
			return (int) (y / FIELD_HEIGHT) + 1;
		}
		return (int) y / FIELD_HEIGHT;
	}
	/**
	 * detects collisions
	 * @param currentBallPosition current ball position
	 * @return boolean whether a collision has been detected or not
	 */
	private boolean collisionDetected(Point currentBallPosition) {
		// alreadycollided is necessary because the ball might 
		// be at y = 0 or y = 59 for 2 or even more consecutive steps
		// this would result in a double detected collision which causes fatal errors
		if((currentBallPosition.getX() == 1 || currentBallPosition.getX() == 63 || currentBallPosition.getY() == 0 || currentBallPosition.getY() == 59) && !alreadyCollided) {
			alreadyCollided = true;
			return true;
		}
		if(currentBallPosition.getY() > 0 && currentBallPosition.getY() < 59) {
			alreadyCollided = false;
		}
		return false;
	}
}
