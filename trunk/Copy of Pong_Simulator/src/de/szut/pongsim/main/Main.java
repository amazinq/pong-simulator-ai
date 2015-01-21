package de.szut.pongsim.main;

import de.szut.pongsim.gui.Field;
import de.szut.pongsim.gui.Frame;
import de.szut.pongsim.gui.Menu;
import de.szut.pongsim.gui.SwingField;
import de.szut.pongsim.physics.Config;
import de.szut.pongsim.physics.Model;

public class Main {

	public static void main(String[] args) {
		Field field = new SwingField();
		Config config = Config.createLoadedConfig();
		Model model = new Model(field, config);
		new Frame(field, new Menu(model));
	}

}
