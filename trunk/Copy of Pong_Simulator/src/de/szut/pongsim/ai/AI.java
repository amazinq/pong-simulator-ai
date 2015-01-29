package de.szut.pongsim.ai;

import de.szut.pongsim.physics.PadMovement;
import de.szut.pongsim.physics.Point;

public class AI implements User {

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

}
