package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
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
		// Just in case the user wishes to have windowed+borderless
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width/3;
		config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height/3;

//		config.r = config.g = config.b = config.a = 8;
//
//		config.useGL20 = false;
//
//		View view = initializeForView(new LineDrawing(), config);
//
//		if (graphics.getView() instanceof SurfaceView) {
//			SurfaceView glView = (SurfaceView) graphics.getView();
//			glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//			glView.setZOrderOnTop(true);
//		}

		new LwjglApplication(new BikeGame(), config);
	}
}
