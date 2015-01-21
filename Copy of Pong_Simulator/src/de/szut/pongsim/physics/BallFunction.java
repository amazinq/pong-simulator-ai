package de.szut.pongsim.physics;

import java.util.Random;

/**
 * Function to calculate the path of the ball with
 * @author Marc Huisinga
 *
 */
public class BallFunction {

	/**
	 * Lower bound of the range in which the ball can rebound
	 */
	public static final int LOWER_LAUNCH_BOUND = -70;
	/**
	 * Upper bound of the range in which the ball can rebound
	 */
	public static final int UPPER_LAUNCH_BOUND = 70;
	
	private double m;
	private double b;
	
	/**
	 * Constructor
	 * @param m - slope
	 * @param b - intersection at x=0
	 */
	private BallFunction(double m, double b) {
		this.m = m;
		this.b = b;
	}
	
	/**
	 * Calculates the next ball position in the given direction.
	 * @param dir - direction of the ball movement
	 * @param current - current position of the ball
	 * @return next position
	 */
	public Point next(HorizontalDirection dir, Point current) {
		int nextX = current.getX() + dir.getNextStep();
		int nextY = (int) (m*nextX + b);
		Point next = new Point(nextX, nextY);
		return next;
	}
	
	/**
	 * Converts an angle to a slope.
	 * @param angle - in deg
	 * @return slope
	 */
	private static double angleToSlope(double angle) {
		return Math.tan(Math.toRadians(angle));
	}
	
	/**
	 * Calculates the x=0 intersection at a given point 
	 * with a given slope.
	 * @param impact - point of impect at which to calculate the intersection
	 * @param m - slope
	 * @return intersection
	 */
	private static double intersection(Point impact, double m) {
		return impact.getY() - m*impact.getX();
	}
	
	/**
	 * Converts a slope to an angle.
	 * @param m - slope
	 * @return angle in deg
	 */
	private static double slopeToAngle(double m) {
		return Math.toDegrees(Math.atan(m));
	}
	
	/**
	 * Calculates a random angle in the given bounds.
	 * @param from - lower bound (inclusive)
	 * @param to - upper bound (exclusive)
	 * @return randomized angle
	 */
	private static double randomAngle(double from, double to) {
		Random ran = new Random();
		return ran.doubles(from, to).findFirst().getAsDouble();
	}
	
	/**
	 * Default rebound from the side of the field
	 * or the middle of the pad.
	 * @param impact - point at which the collision occured
	 */
	public void rebound(Point impact) {
		m = -m;
		b = intersection(impact, m);
	}
	
	/**
	 * Generates a random rebound in the given
	 * range of angles.
	 * @param impact - point at which the collision occured
	 * @param from - lower bound (inclusive)
	 * @param to - upper bound (exclusive)
	 */
	private void randomRebound(Point impact, double from, double to) {
		double randomizedAngle = randomAngle(from, to);
		m = angleToSlope(randomizedAngle);
		b = intersection(impact, m);
	}
	
	/**
	 * Rebound from the lower area of the pad.
	 * @param impact - point at which the collision occured
	 */
	public void lowerRebound(Point impact) {
		double reflectionAngle = -slopeToAngle(m);
		randomRebound(impact, LOWER_LAUNCH_BOUND, reflectionAngle);
	}
	
	/**
	 * Rebound from the upper area of the pad.
	 * @param impact - point at which the collision occured
	 */
	public void upperRebound(Point impact) {
		double reflectionAngle = -slopeToAngle(m);
		randomRebound(impact, reflectionAngle, UPPER_LAUNCH_BOUND);
	}
	
	/**
	 * Creates a ball function at a random start angle.
	 * @param startPos - position to start at
	 * @return function
	 */
	public static BallFunction createAtRandomAngle(Point startPos) {
		double startAngle = randomAngle(LOWER_LAUNCH_BOUND, UPPER_LAUNCH_BOUND);
		double m = angleToSlope(startAngle);
		double b = intersection(startPos, m);
		BallFunction function = new BallFunction(m, b);
		return function;
	}
}
