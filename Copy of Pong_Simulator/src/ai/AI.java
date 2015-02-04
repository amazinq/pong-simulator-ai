package ai;

import de.szut.pongsim.physics.PadMovement;
import de.szut.pongsim.physics.Point;

public class AI implements User {

	private Point firstBallPosition;
	private Point currentBallPosition;
	
	
	@Override
	public PadMovement nextStep(int ownPadBottomY, int enemyPadBottomY,
			Point BallPos, int BallSpeed, boolean isDefender) {
		
		if(isDefender) {
			
		} else {
			
		}
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	private static LinearFunction generateLinearFunction(Point firstBallPosition, Point currentBallPosition) {
		double m = ((double)currentBallPosition.getX() - (double)firstBallPosition.getX())/((double)currentBallPosition.getY() - (double)firstBallPosition.getY());
		double b = (double)currentBallPosition.getY() - m * (double)currentBallPosition.getX();
		return new LinearFunction(m,b);
	}

}
