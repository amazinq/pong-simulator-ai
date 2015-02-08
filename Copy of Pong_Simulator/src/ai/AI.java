package ai;

import de.szut.pongsim.ai.User;
import de.szut.pongsim.physics.PadMovement;
import de.szut.pongsim.physics.Point;

public class AI implements User {

	private Point firstBallPosition;
	private boolean isFirstStep;
	private boolean isLeftPlayer;
	private boolean alreadyCollided;
	private Point padCollisionPoint;
	
	private static final int FIELD_WIDTH = 65;
	private static final int FIELD_HEIGHT = 60;

	public AI() {
		isFirstStep = true;
		alreadyCollided = false;
		padCollisionPoint = new Point(0, 27);
	}

	// @Override
	// public PadMovement nextStep(int ownPadBottomY, int enemyPadBottomY,
	// Point ballPos, int ballSpeed, boolean isDefender) {
	//
	// if(isDefender) {
	// if(isFirstStep) {
	// firstBallPosition = ballPos;
	// isFirstStep = false;
	// } else {
	// LinearFunction flightRoute = generateLinearFunction(firstBallPosition,
	// ballPos);
	// //System.out.println(flightRoute.getM());
	//
	// if(ballPos.getX() > firstBallPosition.getX()) {
	// System.out.println((Math.abs(flightRoute.getF(64)) % 120)%FIELD_HEIGHT);
	// if((Math.abs(flightRoute.getF(64)) % 120)%FIELD_HEIGHT < ownPadBottomY+2) {
	// return PadMovement.DOWN;
	// }
	// return PadMovement.UP;
	// // if(flightRoute.getM() > 0) {
	// // //oben
	// // } else {
	// // //unten
	// // }
	// } else {
	// System.out.println((Math.abs(flightRoute.getF(0)) % 120)%FIELD_HEIGHT);
	// if((Math.abs(flightRoute.getF(0)) % 120)%FIELD_HEIGHT < ownPadBottomY+2) {
	// return PadMovement.DOWN;
	// }
	// return PadMovement.UP;
	// // if(flightRoute.getM() > 0) {
	// // //unten
	// // } else {
	// // //oben
	// // }
	// }
	// }
	// } else {
	//
	// }
	// return PadMovement.STOP;
	// }

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

	@Override
	public PadMovement nextStep(int ownPadBottomY, int enemyPadBottomY) {
		if (padCollisionPoint.getY() < ownPadBottomY + 1) {
			return PadMovement.DOWN;
		} else if (padCollisionPoint.getY() > ownPadBottomY + 4) {
			return PadMovement.UP;
		}
		return PadMovement.STOP;
	}

	// Padcollision beachten! firstpoint zurücksetzen!
	@Override
	public void updateBallPos(Point ballPos) {
		if (isFirstStep) {
			firstBallPosition = ballPos;
			isFirstStep = false;
		} else {
			if (collisionDetected(ballPos)) {
				isFirstStep = true;
			} else {
				LinearFunction flightRoute = generateLinearFunction(
						firstBallPosition, ballPos);
				if (isLeftPlayer) {
					// is left player
					if (ballPos.getX() < firstBallPosition.getX()) {
						// is defender
						double functionLength = flightRoute.getF(1);
						if (calculateAmountOfCollisions(functionLength) % 2 == 1) {
							padCollisionPoint = new Point(0,
									FIELD_HEIGHT - Math.abs((int) functionLength % FIELD_HEIGHT));
						} else {
							padCollisionPoint = new Point(0,
									Math.abs((int) functionLength % FIELD_HEIGHT));
						}
					} else {
						// is not defender
						padCollisionPoint = new Point(0, 27);
					}
				} else {
					// is right player
					if (ballPos.getX() > firstBallPosition.getX()) {
						// is defender
						double functionLength = flightRoute.getF(FIELD_WIDTH -2);
						if (calculateAmountOfCollisions(functionLength) % 2 == 1) {
							padCollisionPoint = new Point(FIELD_WIDTH -2,
									FIELD_HEIGHT - Math.abs((int) functionLength % FIELD_HEIGHT));
						} else {
							padCollisionPoint = new Point(FIELD_WIDTH -2,
									Math.abs((int) functionLength % FIELD_HEIGHT));
						}
					} else {
						// is not defender
						padCollisionPoint = new Point(FIELD_WIDTH -2, 27);
					}
				}
			}
		}
	}

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

	private int calculateAmountOfCollisions(double functionLength) {
		if (functionLength < 0 && functionLength / FIELD_HEIGHT > -1 && functionLength / FIELD_HEIGHT < -2) {
			return (int) (functionLength / FIELD_HEIGHT) + 1;
		}
		return (int) functionLength / FIELD_HEIGHT;
	}
	
	private boolean collisionDetected(Point currentBallPosition) {
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
