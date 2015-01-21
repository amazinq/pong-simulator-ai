package de.szut.pongsim.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class Frame extends JFrame {

	private static final long serialVersionUID = 3699383660884961597L;

	/**
	 * Inherits all GUI components
	 * @param field
	 * @param menu
	 */
	public Frame(Field field, JMenuBar menu) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Pong Simulator");
		setJMenuBar(menu);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(800, 850);
		setLocation((int) ((dim.getWidth() - getWidth()) / 2), (int) ((dim.getHeight() - getHeight()) / 2));
		setResizable(false);
		setVisible(true);
		setContentPane((Container) field);
	}
}
