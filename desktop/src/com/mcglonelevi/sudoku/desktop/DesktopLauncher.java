package com.mcglonelevi.sudoku.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mcglonelevi.sudoku.SudokuGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 606;
		config.width = 606;
		new LwjglApplication(new SudokuGame(), config);
	}
}
