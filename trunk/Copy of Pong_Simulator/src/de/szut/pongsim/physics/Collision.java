package de.szut.pongsim.physics;

/**
 * Represents collisions during ball movement
 * @author Marc Huisinga
 *
 */
public enum Collision {
	
	/**
	 * No collision occured
	 */
	NONE {
		@Override
		public HorizontalDirection collide(BallFunction func, HorizontalDirection dir, Point at) {
			return dir;
		}
	}, 
	
	/**
	 * The ball hit the bottom of the pad
	 */
	PAD_BOTTOM {
		@Override
		public HorizontalDirection collide(BallFunction func, HorizontalDirection dir, Point at) {
			// Angles are inverted for right pad
			if (dir == HorizontalDirection.LEFT) {
				func.lowerRebound(at);
			} else {
				func.upperRebound(at);
			}
			
			return dir.toggled();
		}
	},
	
	/**
	 * The ball hit the middle of the pad
	 */
	PAD_MIDDLE {
		@Override
		public HorizontalDirection collide(BallFunction func, HorizontalDirection dir, Point at) {
			func.rebound(at);
			return dir.toggled();
		}
	}, 
	
	/**
	 * The ball hit the top of the pad
	 */
	PAD_TOP {
		@Override
		public HorizontalDirection collide(BallFunction func, HorizontalDirection dir, Point at) {
			// Angles are inverted for right pad
			if (dir == HorizontalDirection.LEFT) {
				func.upperRebound(at);
			} else {
				func.lowerRebound(at);
			}
			
			return dir.toggled();
		}
	}, 
	
	/**
	 * The ball hit the upper or the lower side of the field
	 */
	SIDE {
		@Override
		public HorizontalDirection collide(BallFunction func, HorizontalDirection dir, Point at) {
			func.rebound(at);
			return dir;
		}
	},	
	
	/**
	 * The ball hit the outside of the board without a pad
	 */
	END {
		@Override
		public HorizontalDirection collide(BallFunction func, HorizontalDirection dir, Point at) {
			return null;
		}
	};
	
	/**
	 * Runs the appropriate operations for the collision.
	 * @param func - function of the ball
	 * @param dir - direction the ball is flying in
	 * @param at - point at which the collision occured
	 * @return new direction of the ball
	 */
	public abstract HorizontalDirection collide(BallFunction func, HorizontalDirection dir, Point at);
}
