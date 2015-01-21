package de.szut.pongsim.physics;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

	/**
	 * Path to the default config file
	 */
	private static final String PATH = "cfg/physics.ini";
	/**
	 * Amount of rounds each halftime
	 */
	private static final int HALFTIME_ROUNDS = 5;
	public static final String HALFTIME_ROUNDS_KEY = "rounds_per_halftime";
	/**
	 * Amount of pad collisions until the ball speed is incremented
	 */
	private static final int SPEED_INCREASE_DIRECTION_TOGGLE_STEPS = 8;
	public static final String SPEED_INCREASE_DIRECTION_TOGGLE_STEPS_KEY = "pad_rebounds_until_speed_increase";
	/**
	 * Time the AI is allowed to calculate the next step
	 */
	private static final int AI_TIMEOUT_MS = 200;
	public static final String AI_TIMEOUT_KEY = "ai_time_limit_ms";
	/**
	 * Delay between ball movements
	 */
	private static final int MOVEMENT_DELAY_MS = 20;
	public static final String MOVEMENT_DELAY_KEY = "movement_delay_ms";
	
	
	private final Properties properties = new Properties();
	
	private final File config;
	
	/**
	 * Constructor
	 * @param location - path to the config
	 */
	private Config(String location) {
		this.config = new File(location);
	}

	/**
	 * Saves the config.
	 * Creates a new config file should the config file be missing.
	 */
	private void save() {
		try {
			config.createNewFile();
		} catch (IOException e) {
			// ignore save
			return;
		}
		try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(config))) {
			properties.store(writer, "");
		} catch (FileNotFoundException e) {
			// ignore save
		} catch (IOException e) {
			// ignore save
		}
		
	}
	
	/**
	 * Loads the default config into properties.
	 */
	private void loadDefaultConfig() {
		properties.putIfAbsent(HALFTIME_ROUNDS_KEY, String.valueOf(HALFTIME_ROUNDS));
		properties.putIfAbsent(SPEED_INCREASE_DIRECTION_TOGGLE_STEPS_KEY, String.valueOf(SPEED_INCREASE_DIRECTION_TOGGLE_STEPS));
		properties.putIfAbsent(AI_TIMEOUT_KEY, String.valueOf(AI_TIMEOUT_MS));
		properties.putIfAbsent(MOVEMENT_DELAY_KEY, String.valueOf(MOVEMENT_DELAY_MS));
	}
	
	/**
	 * Loads the config.
	 * Loads the default config instead should the default config be missing.
	 * Additionally, this also saves the default config for future reference
	 * as main config.
	 */
	private void load() {
		try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(config))) {
			properties.load(reader);
		} catch (FileNotFoundException e) {
			loadDefaultConfig();
			save();
		} catch (IOException e) {
			loadDefaultConfig();
		}
	}
	
	/**
	 * Gets the value of a key.
	 * This method loads the default value
	 * of the key and stores an entry with the
	 * default value in the config should the value 
	 * not be present in the config..
	 * @param key
	 * @return value
	 */
	public int get(String key) {
		String value = properties.getProperty(key);
		if (value == null) {
			loadDefaultConfig();
			save();
			value = properties.getProperty(key);
		}
		return Math.abs(Integer.valueOf(value));
	}
	
	/**
	 * Sets the value of a key and saves
	 * the value to the config.
	 * @param key
	 * @param value
	 */
	public void set(String key, int value) {
		int oldValue = (int) Integer.valueOf((String) properties.setProperty(key, String.valueOf(value)));
		if (value != oldValue) {
			save();
		}
	}
	
	public static Config createLoadedConfig() {
		Config config = new Config(PATH);
		config.load();
		return config;
	}
	
}
