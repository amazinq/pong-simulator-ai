package de.szut.pongsim.physics;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import de.szut.pongsim.ai.User;
import de.szut.pongsim.gui.Field;

/**
 * Represents the game logic
 * @author Marc Huisinga
 *
 */
public class Model implements Runnable {
	
	/**
	 * Direction the ball starts into
	 */
	private static final HorizontalDirection START_DIRECTION = HorizontalDirection.LEFT;
	/**
	 * Time the halftime label is displayed
	 */
	private static final int HALFTIME_DISPLAY_TIME_MS = 2000;
	/**
	 * Speed the ball starts at
	 */
	private static final int START_SPEED = 1;
	
	private Field field;
	private Config config;
	private User leftUser = null;
	private User rightUser = null;
	
	/**
	 * Constructor
	 * @param field - view
	 * @param config - physics configuration
	 */
	public Model(Field field, Config config) {
		this.field = field;
		this.config = config;
	}
	
	/**
	 * Gets the movement decision from the AI in a limited time.
	 * @param movementTask - task to move the pad with
	 * @param aiTimeoutMS - time the AI is allowed use to calculate the next step
	 * @return pad movement the AI decided
	 */
	private PadMovement getAIMovementDecision(Callable<PadMovement> movementTask, int aiTimeoutMS) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			List<Future<PadMovement>> movements = executor.invokeAll(Arrays.asList(movementTask), 
					aiTimeoutMS, 
					TimeUnit.MILLISECONDS);
			executor.shutdownNow();
			Future<PadMovement> future = movements.get(0);
			if (!future.isCancelled()) {
				try {
					PadMovement movement = future.get();
					return movement;
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Starts a round.
	 * @param startDirection - direction to start into
	 * @param leftUser - left AI
	 * @param rightUser - right AI
	 * @return direction the round ended at
	 */
	private HorizontalDirection startRound(HorizontalDirection startDirection, User leftUser, User rightUser) {
		// Config inits
		int speedIncreaseSteps = config.get(Config.SPEED_INCREASE_DIRECTION_TOGGLE_STEPS_KEY);
		int aiTimeoutMS = config.get(Config.AI_TIMEOUT_KEY);
		int movementDelayMS = config.get(Config.MOVEMENT_DELAY_KEY);
		int ballSpeed = START_SPEED;
		HorizontalDirection direction = startDirection;
		Board board = Board.createDefaultBoard(direction);
		int totalDirectionToggles = 0;
		Point startPos = board.getBallPos();
		BallFunction func = BallFunction.createAtRandomAngle(startPos);
		leftUser.updateBallPos(startPos);
		rightUser.updateBallPos(startPos);
		field.respawn(startPos);
		// Game loop
		while (true) {
			// Information for pad movement
			int leftPadBottomY = board.getLeftPadBottomY();
			int rightPadBottomY = board.getRightPadBottomY();
			// Pad movements
			PadMovement leftMovement = getAIMovementDecision(() -> {
				return leftUser.nextStep(leftPadBottomY, rightPadBottomY);
			}, aiTimeoutMS);
			if (leftMovement != null) {
				leftMovement = board.moveLeftPad(leftMovement);
				field.moveLeftPad(leftMovement);
			}
			PadMovement rightMovement = getAIMovementDecision(() -> {
				return rightUser.nextStep(rightPadBottomY, leftPadBottomY);
			}, aiTimeoutMS);
			if (rightMovement != null) {
				rightMovement = board.moveRightPad(rightMovement);
				field.moveRightPad(rightMovement);
			}
			// Ball movements
			for (int i = 0; i < ballSpeed; i++) {
				Point currentPos = func.next(direction, board.getBallPos());
				Collision collision = board.moveBall(currentPos);
				if (collision == Collision.END) {
					currentPos = func.next(direction, currentPos);
					board.moveBall(currentPos);
					// Take unchecked value of x to properly 
					// display the end state, because the ball
					// being on the same x level as a pad
					// would be considered out of bounds
					field.moveBall(new Point(currentPos.getX(), board.getBallPos().getY()));
					return direction;
				}
				// Refresh current pos should the input have
				// been out of bounds
				currentPos = board.getBallPos();
				HorizontalDirection oldDirection = direction;
				// Refresh direction should the direction
				// have changed during the collision
				// (happens when colliding with pads)
				direction = collision.collide(func, direction, currentPos);
				if (direction != oldDirection) {
					totalDirectionToggles++;
					if (totalDirectionToggles % speedIncreaseSteps == 0) {
						ballSpeed++;
					}
				}
				leftUser.updateBallPos(currentPos);
				rightUser.updateBallPos(currentPos);
				field.moveBall(currentPos);
				try {
					Thread.sleep(movementDelayMS/ballSpeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Starts a match.
	 */
	public void start() {
		if (leftUser == null || rightUser == null) {
			field.needUsersError();
			return;
		}
		User leftUser = this.leftUser;
		User rightUser = this.rightUser;
		field.reset();
		int leftVictories = 0;
		int rightVictories = 0;
		int halftimeRounds = config.get(Config.HALFTIME_ROUNDS_KEY);
		HorizontalDirection direction = START_DIRECTION;
		for (int halftime = 0; halftime < 2; halftime++) {
			for (int round = 0; round < halftimeRounds; round++) {
				HorizontalDirection finalDirection = startRound(direction, leftUser, rightUser);
				if (finalDirection == HorizontalDirection.LEFT) {
					field.incrementRightCounter();
					rightVictories++;
				} else {
					field.incrementLeftCounter();
					leftVictories++;
				}
				leftUser.reset();
				rightUser.reset();
			}
			if (halftime < 1) {
				field.displayHalftime();
				direction = direction.toggled();
				try {
					Thread.sleep(HALFTIME_DISPLAY_TIME_MS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (rightVictories > leftVictories) {
			field.displayVictoryRight();
		} else if (leftVictories > rightVictories) {
			field.displayVictoryLeft();
		} else {
			field.displayDraw();
		}
	}

	@Override
	public void run() {
		start();
	}
	
	public void setLeftUser(User leftUser) {
		leftUser.setLeftSide();
		this.leftUser = leftUser;
	}

	public void setRightUser(User rightUser) {
		rightUser.setRightSide();
		this.rightUser = rightUser;
	}

	public Config getConfig() {
		return config;
	}
	
}
