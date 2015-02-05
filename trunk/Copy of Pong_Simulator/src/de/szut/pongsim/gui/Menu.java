package de.szut.pongsim.gui;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import ai.AILoader;
import ai.User;
import de.szut.pongsim.physics.Config;
import de.szut.pongsim.physics.Model;
import ai.AI;

/**
 * 
 * @author Steffen Wiﬂmann
 *
 */
public class Menu extends JMenuBar {

	private static final String CHOOSER_TITLE = "Please select an AI class in a folder called 'ai' which implements User and has 'ai' as package.";
	private static final String AI_FOLDER = "ai";
	private static final FileFilter AI_FILTER = new FileNameExtensionFilter("Compiled Java class files", "class");
	
	private static final long serialVersionUID = 6986686811317721045L;
	private JMenu menuGame;
	private JMenu menuOptions;
	private JMenu menuChooseAI;
	private JMenu menuChoosePhysicValues;
	private JMenuItem menuItemNewGame;
	private JMenuItem menuItemLeftAiselection;
	private JMenuItem menuItemRightAiselection;
	private JMenuItem menuItemHalfTimeRoundSelection;
	private JMenuItem menuItemSpeedIncreaseStepSelection;
	private JMenuItem menuItemAITimeOutSelection;
	private JMenuItem menuItemMovementDelaySelection;
	private AILoader aiLoader;
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private Future<?> task = null;

	/**
	 * Menu to select some values like Ais and gamevalues
	 * 
	 * @param model
	 */
	public Menu(Model model) {
		aiLoader = new AILoader();
		menuGame = new JMenu("Game");
		add(menuGame);
		menuOptions = new JMenu("Options");
		add(menuOptions);
		menuChooseAI = new JMenu("AIs");
		menuOptions.add(menuChooseAI);
		menuChoosePhysicValues = new JMenu("Physics");
		menuOptions.add(menuChoosePhysicValues);
		menuItemNewGame = new JMenuItem("New Game");
		menuGame.add(menuItemNewGame);
		menuItemLeftAiselection = new JMenuItem("Left AI");
		menuChooseAI.add(menuItemLeftAiselection);
		menuItemRightAiselection = new JMenuItem("Right AI");
		menuChooseAI.add(menuItemRightAiselection);
		menuItemHalfTimeRoundSelection = new JMenuItem(
				"Amount of halftime rounds");
		menuChoosePhysicValues.add(menuItemHalfTimeRoundSelection);
		menuItemSpeedIncreaseStepSelection = new JMenuItem(
				"Speed increase steps");
		menuChoosePhysicValues.add(menuItemSpeedIncreaseStepSelection);
		menuItemAITimeOutSelection = new JMenuItem("AI timeout");
		menuChoosePhysicValues.add(menuItemAITimeOutSelection);
		menuItemMovementDelaySelection = new JMenuItem("Movement delay");
		menuChoosePhysicValues.add(menuItemMovementDelaySelection);

		menuItemNewGame.addActionListener(e -> {
			if (task == null || task.isDone()) {
				task = executor.submit(model);
			}
		});

		// Left AI selection
		menuItemLeftAiselection.addActionListener(e -> {
//			JFileChooser fileChooser = new JFileChooser();
//			fileChooser.setDialogTitle(CHOOSER_TITLE);
//			fileChooser.setCurrentDirectory(new File(AI_FOLDER));
//			fileChooser.setFileFilter(AI_FILTER);
//			int option = fileChooser.showOpenDialog(null);
//			if (option == JFileChooser.APPROVE_OPTION) {
//				User ai = aiLoader.loadClassObject(fileChooser.getSelectedFile());
//				model.setLeftUser(ai);
//			}
			model.setLeftUser(new AI());
		});

		// Right AI selection
		menuItemRightAiselection.addActionListener(e -> {
//			JFileChooser fileChooser = new JFileChooser();
//			fileChooser.setDialogTitle(CHOOSER_TITLE);
//			fileChooser.setCurrentDirectory(new File(AI_FOLDER));
//			fileChooser.setFileFilter(AI_FILTER);
//			int option = fileChooser.showOpenDialog(null);
//			if (option == JFileChooser.APPROVE_OPTION) {
//				User ai = aiLoader.loadClassObject(fileChooser.getSelectedFile());
//				model.setRightUser(ai);
//			}
			model.setRightUser(new AI());
		});

		Config config = model.getConfig();
		
		// Halftime round selection
		menuItemHalfTimeRoundSelection.addActionListener(e -> {
			String input = showInputDialog("Choose amount of rounds per halftime",
					config.get(Config.HALFTIME_ROUNDS_KEY));
			int integerInput;
			if (input != null) {
				if (isAnInteger(input)) {
					integerInput = Integer.parseInt(input);
					if (isInExclusiveRange(integerInput)) {
						config.set(Config.HALFTIME_ROUNDS_KEY, integerInput);
					} else {
						showErrorDialog("Value needs to be >0!");
					}
				} else {
					showErrorDialog("Value needs to be a number!");
				}
			}
		});

		// Speed increase step selection
		menuItemSpeedIncreaseStepSelection.addActionListener(e -> {
			String input = showInputDialog("Choose pad rebounds needed until speed increase",
					config.get(Config.SPEED_INCREASE_DIRECTION_TOGGLE_STEPS_KEY));
			int integerInput;
			if (input != null) {
				if (isAnInteger(input)) {
					integerInput = Integer.parseInt(input);
					if (isInExclusiveRange(integerInput)) {
						config.set(Config.SPEED_INCREASE_DIRECTION_TOGGLE_STEPS_KEY, integerInput);
					} else {
						showErrorDialog("Value needs to be >0!");
					}
				} else {
					showErrorDialog("Value needs to be a number!");
				}
			}
		});

		// AI timeout selection
		menuItemAITimeOutSelection.addActionListener(e -> {
			String input = showInputDialog("Choose AI time limit (in ms)",
					config.get(Config.AI_TIMEOUT_KEY));
			int integerInput;
			if (input != null) {
				if (isAnInteger(input)) {
					integerInput = Integer.parseInt(input);
					if (isInInclusiveRange(integerInput)) {
						config.set(Config.AI_TIMEOUT_KEY, integerInput);
					} else {
						showErrorDialog("Value needs to be >=0!");
					}
				} else {
					showErrorDialog("Value needs to be a number!");
				}
			}
		});

		// Movementdelay selection
		menuItemMovementDelaySelection.addActionListener(e -> {
			String input = showInputDialog("Choose movement delay (in ms)",
					config.get(Config.MOVEMENT_DELAY_KEY));
			int integerInput;
			if (input != null) {
				if (isAnInteger(input)) {
					integerInput = Integer.parseInt(input);
					if (isInInclusiveRange(integerInput)) {
						config.set(Config.MOVEMENT_DELAY_KEY, integerInput);
					} else {
						showErrorDialog("Value needs to be >=0!");
					}
				} else {
					showErrorDialog("Value needs to be a number!");
				}
			}
		});
	}

	/**
	 * Shows an inputdialog with a defined text and pre setted value
	 * @param inputText
	 * @param currentValue
	 * @return
	 */
	private static String showInputDialog(String inputText, int currentValue) {
		return (String) JOptionPane.showInputDialog(null, inputText, inputText,
				JOptionPane.PLAIN_MESSAGE, null, null,
				String.valueOf(currentValue));
	}

	/**
	 * Shows an errormessage
	 * @param errorMessage
	 */
	private static void showErrorDialog(String errorMessage) {
		JOptionPane.showMessageDialog(null, errorMessage, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Checks whether the inputvalue is an Integer
	 * @param input
	 * @return
	 */
	private static boolean isAnInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Checks whether the inputvalue is >0
	 * @param input
	 * @return
	 */
	private static boolean isInExclusiveRange(int input) {
		if (input > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether the inputvalue is >=0
	 * @param input
	 * @return
	 */
	private static boolean isInInclusiveRange(int input) {
		if (input >= 0) {
			return true;
		}
		return false;
	}
}
