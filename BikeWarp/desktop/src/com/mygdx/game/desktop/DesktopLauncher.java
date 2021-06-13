package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.BikeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
		config.title = BikeGame.TITLE;
		config.width = BikeGame.V_WIDTH * BikeGame.SCALE;
		config.height = BikeGame.V_HEIGHT * BikeGame.SCALE;
		config.addIcon("data/images/finish_whirl_64x64.png", Files.FileType.Internal);
		// The following is just temporary until we implement a user option to choose fullscreen or windowed borderless
		boolean fullscreen = true;
		if (fullscreen) {
			config.fullscreen = true;
		} else {
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
			config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
			config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		}

		new LwjglApplication(new BikeGame(), config);
	}
}
