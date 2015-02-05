package ai;

import de.szut.pongsim.physics.PadMovement;
import de.szut.pongsim.physics.Point;

public class AI implements User {

	private Point firstBallPosition;
	private boolean isFirstStep;
	
	public AI() {
		isFirstStep = true;
	}
	
	@Override
	public PadMovement nextStep(int ownPadBottomY, int enemyPadBottomY,
			Point ballPos, int ballSpeed, boolean isDefender) {
		
		if(isDefender) {
			if(isFirstStep) {
				firstBallPosition = ballPos;
				isFirstStep = false;
			} else {
				LinearFunction flightRoute = generateLinearFunction(firstBallPosition, ballPos);
				//System.out.println(flightRoute.getM());
				Point padCollisionPoint;
				if(ballPos.getX() > firstBallPosition.getX()) {
					System.out.println((Math.abs(flightRoute.getF(64)) % 120)%60);
					if((Math.abs(flightRoute.getF(64)) % 120)%60 < ownPadBottomY+2) {
						return PadMovement.DOWN;
					} 
					return PadMovement.UP;
//					if(flightRoute.getM() > 0) {
//						//oben
//					} else {
//						//unten
//					}
				} else {
					System.out.println((Math.abs(flightRoute.getF(0)) % 120)%60);
					if((Math.abs(flightRoute.getF(0)) % 120)%60 < ownPadBottomY+2) {
						return PadMovement.DOWN;
					}
					return PadMovement.UP;
//					if(flightRoute.getM() > 0) {
//						//unten
//					} else {
//						//oben
//					}
				}
			}
		} else {
			
		}
		return PadMovement.STOP;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	private static LinearFunction generateLinearFunction(Point firstBallPosition, Point currentBallPosition) {
		double m = ((double)currentBallPosition.getY() - (double)firstBallPosition.getY())/((double)currentBallPosition.getX() - (double)firstBallPosition.getX());
		double b = (double)currentBallPosition.getY() - m * (double)currentBallPosition.getX();
		return new LinearFunction(m,b);
	}

}
