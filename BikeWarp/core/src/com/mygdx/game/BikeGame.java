package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameInputProcessor;
import com.mygdx.game.handlers.GameStateManager;

public class BikeGame implements ApplicationListener {
	public static final String TITLE = "Bike Warp";
//	public static final int V_WIDTH = 512;//512;//480;
//	public static final int V_HEIGHT = 320;//384;//360;
	public static final int V_WIDTH = 512;//480;
	public static final int V_HEIGHT = 384;//360;
	private static final float ASPECT_RATIO = (float)V_WIDTH/(float)V_HEIGHT;
	public static final int SCALE = 2;

	public static final float STEP = 1 / 100f;
	public float accum;

	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private Rectangle viewport;

	private GameStateManager gsm;

	public SpriteBatch getSpriteBatch() { return sb; }
	public OrthographicCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hudCam; }

	@Override
	public void create () {
		// Load the textures and sounds
		BikeGameTextures.InitiateTextures();
		BikeGameSounds.InitiateSounds();

		// Set the Input Processor to the key input I've written
		Gdx.input.setInputProcessor(new GameInputProcessor());

		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		//cam.setToOrtho(false);
		float SCRWIDTH = ((float) V_HEIGHT*Gdx.graphics.getDisplayMode().width)/((float) Gdx.graphics.getDisplayMode().height);
		cam.setToOrtho(false, SCRWIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		//hudCam.setToOrtho(false);
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		gsm = new GameStateManager(this);
	}

	@Override
	public void render () {
		accum += Gdx.graphics.getDeltaTime();
		while (accum >= STEP) {
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
			GameInput.update();
		}
	}

	@Override
	public void dispose () {
		sb.dispose();
	}

	@Override
	public void resize (int w, int h) {
		// calculate new viewport
//		float aspectRatio = (float)w/(float)h;
//		float scale = 1f;
//		Vector2 crop = new Vector2(0f, 0f);
//
//		if(aspectRatio > ASPECT_RATIO)
//		{
//			scale = (float)h/(float)V_HEIGHT;
//			crop.x = (w - V_WIDTH*scale)/2f;
//		}
//		else if(aspectRatio < ASPECT_RATIO)
//		{
//			scale = (float)w/(float)V_WIDTH;
//			crop.y = (h - V_HEIGHT*scale)/2f;
//		}
//		else
//		{
//			scale = (float)w/(float)V_WIDTH;
//		}
//
//		float wid = (float)V_WIDTH*scale;
//		float hei = (float)V_HEIGHT*scale;
//		viewport = new Rectangle(crop.x, crop.y, wid, hei);
	}

	@Override
	public void pause () {

	}

	@Override
	public void resume () {

	}
}
