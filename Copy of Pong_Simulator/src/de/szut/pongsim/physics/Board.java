package de.szut.pongsim.physics;

public class Board {

	/**
	 * Height of the field
	 */
	private static final int HEIGHT = 60;
	/**
	 * Width of the field
	 */
	private static final int WIDTH = 65;
	/**
	 * Highest point on the field
	 */
	private static final int UPPER_BOUND = HEIGHT - 1;
	/**
	 * Lowest point on the field
	 */
	private static final int LOWER_BOUND = 0;
	/**
	 * Height of both pads
	 */
	private static final int PAD_HEIGHT = 6;
	/**
	 * Start y-position of the pad
	 */
	private static final int PAD_BOTTOM_Y = HEIGHT/2 - PAD_HEIGHT/2;
	/**
	 * Size of each three sections of the pad
	 */
	private static final int PAD_SECTION_SIZE = PAD_HEIGHT/3;
	/**
	 * Upper bound of the lower section (exclusive)
	 */
	private static final int PAD_BOTTOM_SECTION = PAD_SECTION_SIZE;
	/**
	 * Upper bound of the middle section (exclusive)
	 */
	private static final int PAD_MIDDLE_SECTION = 2*PAD_SECTION_SIZE;
	/**
	 * Upper bound of the upper section (exclusive)
	 */
	private static final int PAD_TOP_SECTION = 3*PAD_SECTION_SIZE;
	/**
	 * Highest point the pad can be moved to on the field
	 */
	private static final int UPPER_PAD_BOUND = HEIGHT - PAD_HEIGHT;
	/**
	 * Lowest point the pad can be moved to on the field
	 */
	private static final int LOWER_PAD_BOUND = 0;
	/**
	 * Start y-position of the ball
	 */
	private static final int BALL_Y = HEIGHT/2;
	/**
	 * Column before the left pad
	 */
	private static final int BEFORE_LEFT_PAD_X = 1;
	/**
	 * Column before the right pad
	 */
	private static final int BEFORE_RIGHT_PAD_X = WIDTH - 2;
	
	private int leftPadBottomY = PAD_BOTTOM_Y;
	private int rightPadBottomY = PAD_BOTTOM_Y;
	private boolean hitBottom = false;
	private boolean hitTop = false;
	private Point ballPos;
	
	private Board() {}
	
	/**
	 * Moves the left pad in the given direction.
	 * Ignores the movement if the movement moves
	 * outside of the area in which the pad is allowed to move.
	 * @param movement - direction to move the pad in
	 * @return actual PadMovement, STOP if the movement was ignored
	 */
	public PadMovement moveLeftPad(PadMovement movement) {
		int newPadBottomY = leftPadBottomY + movement.getNextStep();
		if (newPadBottomY < LOWER_PAD_BOUND || newPadBottomY > UPPER_PAD_BOUND) {
			return PadMovement.STOP;
		}
		leftPadBottomY = newPadBottomY;
		return movement;
	}
	
	/**
	 * Moves the right pad in the given direction.
	 * Ignores the movement if the movement moves
	 * outside of the area in which the pad is allowed to move.
	 * @param movement - direction to move the pad in
	 * @return actual PadMovement, STOP if the movement was ignored
	 */
	public PadMovement moveRightPad(PadMovement movement) {
		int newPadBottomY = rightPadBottomY + movement.getNextStep();
		if (newPadBottomY < LOWER_PAD_BOUND || newPadBottomY > UPPER_PAD_BOUND) {
			return PadMovement.STOP;
		}
		rightPadBottomY = newPadBottomY;
		return movement;
	}
	
	/**
	 * Adjusts the movement of the ball in a way
	 * that disallows the ball to leave the bounds
	 * of the board.
	 * This method makes the assumption that
	 * -1 <= delta(ballpos_x) <= 1 is always
	 * fulfilled when moving the ball.
	 * @param to - point to move the ball to
	 * @return adjusted movement
	 */
	private Point adjustBallMovement(Point to) {
		int ballPosX = to.getX();
		if (ballPosX < BEFORE_LEFT_PAD_X || ballPosX > BEFORE_RIGHT_PAD_X) {
			// (no specific assignment because ball is not
			// expected to move faster than 1 x position
			// per cycle)
			ballPosX = ballPos.getX();
		}
		int ballPosY = to.getY();
		if (ballPosY < LOWER_BOUND) {
			ballPosY = LOWER_BOUND;
		} else if (ballPosY > UPPER_BOUND) {
			ballPosY = UPPER_BOUND;
		}
		return new Point(ballPosX, ballPosY);
	}
	
	/**
	 * Determines the type of collision the ball had with a pad.
	 * @param ballPosY - y position of the ball
	 * @param padBottomY - y position of the lowest point of the pad
	 * @return collision
	 */
	private static Collision determinePadCollision(int ballPosY, int padBottomY) {
		int distFromPadBottom = ballPosY - padBottomY;
		if (distFromPadBottom >= 0 && distFromPadBottom < PAD_BOTTOM_SECTION) {
			return Collision.PAD_BOTTOM;
		} else if (distFromPadBottom >= PAD_BOTTOM_SECTION && distFromPadBottom < PAD_MIDDLE_SECTION) {
			return Collision.PAD_MIDDLE;
		} else if (distFromPadBottom >= PAD_MIDDLE_SECTION && distFromPadBottom < PAD_TOP_SECTION) {
			return Collision.PAD_TOP;
		}
		return Collision.END;
	}
	
	/**
	 * Moves the ball to the specified point.
	 * It is assumed that 
	 * delta(ballpos_x) == -1 || delta(ballpos_x) == 1 
	 * is always fulfilled when moving the ball.
	 * @param to - point to move the ball to
	 * @return collision that occured when moving the ball
	 */
	public Collision moveBall(Point to) {
		to = adjustBallMovement(to);
		int ballPosX = to.getX();
		int ballPosY = to.getY();
		// Hack to avoid the ball getting stuck
		// in a collision loop, should the ball
		// be in y = LOWER_BOUND or y = UPPER_BOUND
		// twice in a row.
		// No hitLeft and hitRight are necessary
		// because the ball is expected to always
		// move 1 x position each turn.
		if (ballPosY > LOWER_BOUND) {
			hitBottom = false;
		}
		if (ballPosY < UPPER_BOUND) {
			hitTop = false;
		}
		Collision collision = Collision.NONE;
		if (ballPosX <= BEFORE_LEFT_PAD_X) {
			collision = determinePadCollision(ballPosY, leftPadBottomY);
			// Reset the hit indicators
			// to avoid the ball getting
			// stuck if it hits the pad
			// with a very small slope
			// close to a wall into the direction
			// of a wall
			hitBottom = false;
			hitTop = false;
		} else if (ballPosX >= BEFORE_RIGHT_PAD_X) {
			collision = determinePadCollision(ballPosY, rightPadBottomY);
			hitBottom = false;
			hitTop = false;
		} else if (ballPosY <= LOWER_BOUND) {
			if (!hitBottom) {
				collision = Collision.SIDE;
				hitBottom = true;
			} else {
				collision = Collision.NONE;
			}
		} else if (ballPosY >= UPPER_BOUND) {
			if (!hitTop) {
				collision = Collision.SIDE;
				hitTop = true;
			} else {
				collision = Collision.NONE;
			}
		}
		ballPos = new Point(ballPosX, ballPosY);
		return collision;
	}
	
	/**
	 * Creates the board with the pads and the ball
	 * being centered as well as the ball starting
	 * at the appropriate side in the specified direction.
	 * @param startDirection - direction to start the ball into (inversion of its starting position)
	 * @return created board
	 */
	public static Board createDefaultBoard(HorizontalDirection startDirection) {
		Board board = new Board();
		int ballX = BEFORE_LEFT_PAD_X;
		if (startDirection == HorizontalDirection.LEFT) {
			ballX = BEFORE_RIGHT_PAD_X;
		}
		board.ballPos = new Point(ballX, BALL_Y);
		return board;
	}
	
	public Point getBallPos() {
		return ballPos;
	}

	public int getLeftPadBottomY() {
		return leftPadBottomY;
	}

	public int getRightPadBottomY() {
		return rightPadBottomY;
	}
	
}
