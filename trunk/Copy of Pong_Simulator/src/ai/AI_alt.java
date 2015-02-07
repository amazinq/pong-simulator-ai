package ai;

import de.szut.pongsim.ai.User;
import de.szut.pongsim.physics.PadMovement;
import de.szut.pongsim.physics.Point;

public class AI_alt implements User {

	private Point firstBallPosition;
	private boolean isFirstStep;
	private boolean isLeftPlayer;
	private Point padCollisionPoint;

	public AI_alt() {
		isFirstStep = true;
		padCollisionPoint = new Point(0,27);
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
	// System.out.println((Math.abs(flightRoute.getF(64)) % 120)%60);
	// if((Math.abs(flightRoute.getF(64)) % 120)%60 < ownPadBottomY+2) {
	// return PadMovement.DOWN;
	// }
	// return PadMovement.UP;
	// // if(flightRoute.getM() > 0) {
	// // //oben
	// // } else {
	// // //unten
	// // }
	// } else {
	// System.out.println((Math.abs(flightRoute.getF(0)) % 120)%60);
	// if((Math.abs(flightRoute.getF(0)) % 120)%60 < ownPadBottomY+2) {
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
		if(padCollisionPoint.getY() < ownPadBottomY+2) {
			return PadMovement.DOWN;
		} else if(padCollisionPoint.getY() > ownPadBottomY+2) {
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
			LinearFunction flightRoute = generateLinearFunction(
					firstBallPosition, ballPos);
			if (isLeftPlayer) {
				// is left player
				if (ballPos.getX() < firstBallPosition.getX()) {
					// is defender
					if (flightRoute.getM() > 0) {
						// collision bottom
						LinearFunction tempFunction = new LinearFunction(flightRoute.getM(), flightRoute.getB());
						while(tempFunction.getF(1) < 0) {
							// still collisions left
						}
						// no further collisions
						padCollisionPoint = new Point(0,(int)(tempFunction.getF(1)+0.5));
					} else {
						// collision top
						LinearFunction tempFunction = new LinearFunction(flightRoute.getM(), flightRoute.getB());
						while(tempFunction.getF(1) > 59) {
							// still collisions left
						}
						// no further collisions
						padCollisionPoint = new Point(0,(int)(tempFunction.getF(1)+0.5));
					}
				} else {
					// is not defender
					padCollisionPoint = new Point(0,27);
				}
			} else {
				// is right player
				if (ballPos.getX() > firstBallPosition.getX()) {
					// is defender
					if (flightRoute.getM() > 0) {
						// collision top
						LinearFunction tempFunction = new LinearFunction(flightRoute.getM(), flightRoute.getB());
						while(tempFunction.getF(63) > 59) {
							// still collisions left
						}
						// no further collisions
						padCollisionPoint = new Point(64,(int)(tempFunction.getF(63)+0.5));
					} else {
						// collision bottom
						LinearFunction tempFunction = new LinearFunction(flightRoute.getM(), flightRoute.getB());
						while(tempFunction.getF(63) < 0) {
							// still collisions left
						}
						// no further collisions
						padCollisionPoint = new Point(64,(int)(tempFunction.getF(63)+0.5));
					}
				} else {
					// is not defender
				}
			}
		}
	}

	private static LinearFunction generateLinearFunction(
			Point firstBallPosition, Point currentBallPosition) {
		double m = ((double) currentBallPosition.getY() - (double) firstBallPosition
				.getY())
				/ ((double) currentBallPosition.getX() - (double) firstBallPosition
						.getX());
		double b = (double) currentBallPosition.getY() - m
				* (double) currentBallPosition.getX();
		return new LinearFunction(m, b);
	}
}
