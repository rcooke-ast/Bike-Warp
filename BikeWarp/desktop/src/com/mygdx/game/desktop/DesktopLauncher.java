package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.BikeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = BikeGame.TITLE;
		config.width = BikeGame.V_WIDTH * BikeGame.SCALE;
		config.height = BikeGame.V_HEIGHT * BikeGame.SCALE;
		config.fullscreen = true;

		new LwjglApplication(new BikeGame(), config);
	}
}
