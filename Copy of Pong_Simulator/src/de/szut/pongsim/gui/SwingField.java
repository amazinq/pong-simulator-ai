package de.szut.pongsim.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.szut.pongsim.physics.PadMovement;
import de.szut.pongsim.physics.Point;

/**
 * Represents the gamefield
 * @author Steffen Wiﬂmann
 *
 */
public class SwingField extends JPanel implements Field {

	private static final long serialVersionUID = 2818877177291544110L;
	private final int HORIZONTALOFFSET = 75;
	private final int VERTICALOFFSET = 150;
	private final int PANELWIDTH = 800;
	private final int PANELHEIGHT = 800;
	private final int PIXELMULTIPLICATOR = 10;
	private final Point BALLSIZE = new Point(1, 1);
	private final Point PADSIZE = new Point(1, 6);
	private final Font COUNTERFONT = new Font("Arial", Font.BOLD, 100);
	private final Font VICTORYLABELFONT = new Font("Arial", Font.BOLD, 60);
	private Point ballPosition;
	private Point leftPadPosition;
	private Point rightPadPosition;
	private JLabel leftCounterLabel;
	private JLabel rightCounterLabel;
	private JLabel stateLabel;
	private int leftCounter;
	private int rightCounter;

	public SwingField() {
		setSize(PANELWIDTH, PANELHEIGHT);
		setLayout(null);
		leftPadPosition = new Point(0, 27);
		rightPadPosition = new Point(65, 27);
		ballPosition = new Point(32, 29);
		leftCounter = 0;
		rightCounter = 0;
		leftCounterLabel = new JLabel(String.valueOf(leftCounter));
		leftCounterLabel.setBounds(HORIZONTALOFFSET + 100,
				VERTICALOFFSET - 110, 125, 125);
		leftCounterLabel.setFont(COUNTERFONT);
		leftCounterLabel.setAlignmentX(LEFT_ALIGNMENT);
		add(leftCounterLabel);
		rightCounterLabel = new JLabel(String.valueOf(rightCounter));
		rightCounterLabel.setBounds(HORIZONTALOFFSET + 650 - 150,
				VERTICALOFFSET - 110, 125, 125);
		rightCounterLabel.setFont(COUNTERFONT);
		rightCounterLabel.setAlignmentX(RIGHT_ALIGNMENT);
		add(rightCounterLabel);
		stateLabel = new JLabel("", JLabel.CENTER);
		stateLabel.setBounds(HORIZONTALOFFSET + 25, VERTICALOFFSET + 200, 600, 200);
		stateLabel.setFont(VICTORYLABELFONT);
		stateLabel.setOpaque(true);
		stateLabel.setVisible(false);
		add(stateLabel);
		

	}

	@Override
	public void moveBall(Point point) {
		ballPosition = point;
		showMoves();
	}

	@Override
	public void moveLeftPad(PadMovement padMovement) {
		switch (padMovement) {
		case UP:
			leftPadPosition = new Point(leftPadPosition.getX(),
					leftPadPosition.getY() + 1);
			showMoves();
			break;
		case DOWN:
			leftPadPosition = new Point(leftPadPosition.getX(),
					leftPadPosition.getY() - 1);
			showMoves();
			break;
		case STOP:
			break;
		}
	}

	@Override
	public void moveRightPad(PadMovement padMovement) {
		switch (padMovement) {
		case UP:
			rightPadPosition = new Point(rightPadPosition.getX(),
					rightPadPosition.getY() + 1);
			showMoves();
			break;
		case DOWN:
			rightPadPosition = new Point(rightPadPosition.getX(),
					rightPadPosition.getY() - 1);
			showMoves();
			break;
		case STOP:
			break;
		}
	}

	@Override
	public void incrementLeftCounter() {
		leftCounter++;
		leftCounterLabel.setText(String.valueOf(leftCounter));

	}

	@Override
	public void incrementRightCounter() {
		rightCounter++;
		rightCounterLabel.setText(String.valueOf(rightCounter));

	}

	@Override
	public void displayHalftime() {
		stateLabel.setText("Halftime!");
		stateLabel.setForeground(Color.BLACK);
		stateLabel.setVisible(true);
		showMoves();
	}

	@Override
	public void displayVictoryLeft() {
		stateLabel.setText("Left Player won!");
		stateLabel.setForeground(Color.GREEN);
		stateLabel.setVisible(true);
		showMoves();
	}

	@Override
	public void displayVictoryRight() {
		stateLabel.setText("Right Player won!");
		stateLabel.setForeground(Color.GREEN);
		stateLabel.setVisible(true);
		showMoves();
	}

	@Override
	public void displayDraw() {
		stateLabel.setText("Draw!");
		stateLabel.setForeground(Color.BLACK);
		stateLabel.setVisible(true);
		showMoves();
	}

	@Override
	public void reset() {
		leftPadPosition = new Point(0, 27);
		rightPadPosition = new Point(65, 27);
		ballPosition = new Point(32, 29);
		leftCounter = 0;
		rightCounter = 0;
		leftCounterLabel.setText(String.valueOf(leftCounter));
		rightCounterLabel.setText(String.valueOf(rightCounter));
		stateLabel.setVisible(false);
		showMoves();

	}
	@Override
	public void respawn(Point point) {
		leftPadPosition = new Point(0, 27);
		rightPadPosition = new Point(65, 27);
		ballPosition = point;
		stateLabel.setVisible(false);
		showMoves();
		
	}

	/**
	 * Repaints the frame
	 */
	private void showMoves() {
		getParent().repaint();
		
	}
	
	@Override
	public void needUsersError() {
		stateLabel.setText("Please set valid AIs!");
		stateLabel.setForeground(Color.RED);
		stateLabel.setVisible(true);
		showMoves();
	}

	@Override
	/**
	 * paints all components 
	 * Y coordinates are inverted so 0/0 equals
	 * the bottom left field and 0/60 the top
	 * left field
	 */
	public void paintComponent(Graphics g) {
		// Color: Black
		g.setColor(new Color(0, 0, 0));
		
		// Fieldborders
		g.fillRect(HORIZONTALOFFSET - 2, VERTICALOFFSET, 2, 600);
		g.fillRect(PANELWIDTH - HORIZONTALOFFSET, VERTICALOFFSET, 2, 600);
		g.fillRect(HORIZONTALOFFSET - 2, VERTICALOFFSET - 2, 654, 2);
		g.fillRect(HORIZONTALOFFSET - 2, PANELHEIGHT - 50, 654, 2);

		// Left pad drawing
		g.fillRect(HORIZONTALOFFSET + 1, (600 + VERTICALOFFSET)
				- (leftPadPosition.getY() * PIXELMULTIPLICATOR)
				- (PADSIZE.getY() * PIXELMULTIPLICATOR), PADSIZE.getX()
				* PIXELMULTIPLICATOR - 1, PADSIZE.getY() * PIXELMULTIPLICATOR);

		// Right pad drawing
		g.fillRect(HORIZONTALOFFSET + 650 - (PADSIZE.getX() * PIXELMULTIPLICATOR),
				(600 + VERTICALOFFSET)
						- (rightPadPosition.getY() * PIXELMULTIPLICATOR)
						- (PADSIZE.getY() * PIXELMULTIPLICATOR), PADSIZE.getX()
						* PIXELMULTIPLICATOR - 1, PADSIZE.getY()
						* PIXELMULTIPLICATOR);

		// Ball drawing
		g.fillOval(HORIZONTALOFFSET + ballPosition.getX() * PIXELMULTIPLICATOR,
				(600 + VERTICALOFFSET) - (ballPosition.getY() * PIXELMULTIPLICATOR)
						- (BALLSIZE.getY() * PIXELMULTIPLICATOR), BALLSIZE.getX()
						* PIXELMULTIPLICATOR, BALLSIZE.getY() * PIXELMULTIPLICATOR);

	}




}
