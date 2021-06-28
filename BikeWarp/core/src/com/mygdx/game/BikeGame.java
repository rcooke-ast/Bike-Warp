package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.codedisaster.steamworks.SteamAPI;
import com.mygdx.game.handlers.*;

public class BikeGame implements ApplicationListener {
	public static final String TITLE = "AstroBike";
	// 1920 x 1080 = 1.77777R  # STEVE
	// 1440 x 900 = 1.6   # RYAN
	// 512 x 320 = 1.6
	// 512 x 288 = 1.77777R
	// 768 x 432 = 1.77777R
	public static final int V_WIDTH = 768;
	public static final int V_HEIGHT = 432;
//	public static final int V_WIDTH = 512;
//	public static final int V_HEIGHT = 288;
	// Following are 1.6
//	public static final int V_WIDTH = 512;
//	public static final int V_HEIGHT = 320;
	// Following are 1.3
//	public static final int V_WIDTH = 512;//480;
//	public static final int V_HEIGHT = 384;//360;
	private static final float ASPECT_RATIO = (float)V_WIDTH/(float)V_HEIGHT;
	public static final int SCALE = 1;  // Probably best to not change this.

	public static final float STEP = 1 / 100f;
	public float accum;
	private float gn_width, gn_height;

	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	public static Rectangle viewport;

	private GameStateManager gsm;
	private Sprite stars, logo, tube, fluid;
	private boolean load_stars, load_logo, load_tube, load_fluid;
	private float finAngle, finishRad, tube_length, tube_height;
	private float progress;

	public SpriteBatch getSpriteBatch() { return sb; }
	public OrthographicCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hudCam; }

	@Override
	public void create () {
		// Load the splash screen textures
		load_logo = false;
		load_tube = false;
		load_fluid = false;
		finAngle = 0.0f;
		progress = 0.0f;

		// Initialise Steam connection
		SteamVars.initAndConnect();
/*
		SteamVars.PrepareLeaderboards();
		// Parse the leaderboards -- this seems to need the callbacks running in order to work
		if (SteamVars.GetProgress() < 0.7) {
			SteamVars.ParseLeaderboards();
			totalTime += dt;
		} else if (totalTime != -1.0) {
			System.out.println(totalTime);
			totalTime = -1.0f;
		}
 */
		// Load the textures and sounds
		BikeGameSounds.InitiateSounds();
		BikeGameTextures.InitiateTextures();

		// Set the Input Processor to the key input I've written
		Gdx.input.setInputProcessor(new GameInputProcessor());

		// Initialise the viewport
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Initialise the Sprite batch
		sb = new SpriteBatch();
		sb.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam = new OrthographicCamera();
		//cam.setToOrtho(false);
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		gsm = null;
		hudCam.setToOrtho(false, viewport.width, viewport.height);
		hudCam.position.set(viewport.width/2,viewport.height/2,0);
		tube_length = viewport.width*0.7f;
		tube_height = viewport.height*0.02f;
//		finishRad = viewport.height*0.03f;
	}

	@Override
	public void render () {
		if (BikeGameTextures.textureManager.update()) {
			// we are done loading, let's move to another screen!
			if (gsm == null) {
				gsm = new GameStateManager(this);
				Gdx.input.setCursorCatched(true);
			}
			accum += Gdx.graphics.getDeltaTime();
			while (accum >= STEP) {
				accum -= STEP;
				gsm.update(STEP);
				gsm.render();
				GameInput.update();
				// Make sure Steam is getting callbacks
				if (SteamAPI.isSteamRunning()) {
					SteamAPI.runCallbacks();
				}
			}
		} else {
			progress = BikeGameTextures.textureManager.getProgress();
			// clear screen
			Gdx.gl.glClearColor(0, 0, 0, 0);  // Black
			Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
			Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);
			// Check if everything is loaded
			if (load_stars && load_logo && load_tube && load_fluid) {
				sb.setProjectionMatrix(hudCam.combined);
				sb.begin();
				// Draw stars
				sb.draw(stars, hudCam.position.x-viewport.width/2, hudCam.position.y-viewport.height/2, 0, 0, viewport.width, viewport.height, 1.0f, 1.0f, 0.0f);
				// Draw logo
//				sb.draw(logo, hudCam.position.x-viewport.width/2, hudCam.position.y-viewport.height/2, 0, 0, viewport.width, viewport.height, 1.0f, 1.0f, 0.0f);
				sb.draw(logo, hudCam.position.x-gn_width/2, hudCam.position.y+(viewport.height/2-gn_height*1.5f), 0, 0, gn_width, gn_height, 1.0f, 1.0f, 0.0f);
				// Draw whirl
//				sb.draw(warp, hudCam.position.x-finishRad, hudCam.position.y-finishRad, finishRad, finishRad, 2.0f*finishRad, 2.0f*finishRad, 1.0f, 1.0f, finAngle);
//				finAngle += 5.0f;
//				if (finAngle >= 360.0f) finAngle -= 360.0f;
				// Draw Fluid
				sb.draw(fluid, hudCam.position.x-tube_length/2, hudCam.position.y-0.4f*viewport.height, 0, 0, tube_length*progress, tube_height, 1.0f, 1.0f, 0.0f);
				// Draw Tube
				sb.draw(tube, hudCam.position.x-tube_length/2, hudCam.position.y-0.4f*viewport.height, 0, 0, tube_length, tube_height, 1.0f, 1.0f, 0.0f);
				sb.end();
			} else {
				if(BikeGameTextures.textureManager.isLoaded(BikeGameTextures.GetTextureName("bg_StarsSparse"))) {
					// texture is available, let's fetch it and do something interesting
//					stars = new Sprite(BikeGameTextures.LoadTexture("bg_StarsBlueGreen"));
//					stars = new Sprite(BikeGameTextures.LoadTexture("bg_StarsBlue"));
					stars = new Sprite(BikeGameTextures.LoadTexture("bg_StarsSparse"));
//					stars = new Sprite(BikeGameTextures.LoadTexture("bg_Starfield"));
//					stars = new Sprite(BikeGameTextures.LoadTexture("bg_GalaxyDusty"));
					load_stars = true;
				}
				if(BikeGameTextures.textureManager.isLoaded(BikeGameTextures.GetTextureName("menu_gamename"))) {
					// texture is available, let's fetch it and do something interesting
					logo = new Sprite(BikeGameTextures.LoadTexture("menu_gamename"));
					gn_width = viewport.width*0.7f;
					gn_height = gn_width*logo.getHeight()/logo.getWidth();
					load_logo = true;
				}
				if(BikeGameTextures.textureManager.isLoaded(BikeGameTextures.GetTextureName("nitrous_tube"))) {
					// texture is available, let's fetch it and do something interesting
					tube = new Sprite(BikeGameTextures.LoadTexture("nitrous_tube"));
					load_tube = true;
				}
				if(BikeGameTextures.textureManager.isLoaded(BikeGameTextures.GetTextureName("nitrous_fluid"))) {
					// texture is available, let's fetch it and do something interesting
					fluid = new Sprite(BikeGameTextures.LoadTexture("nitrous_fluid"));
					load_fluid = true;
				}
			}
		}
	}

	@Override
	public void dispose () {
		sb.dispose();
	}

	@Override
	public void resize (int w, int h) {
		// calculate new viewport
		float aspectRatio = (float)w/(float)h;
		float scale = 1f;
		Vector2 crop = new Vector2(0f, 0f);

		if (aspectRatio > ASPECT_RATIO) {
			scale = (float)h/(float)V_HEIGHT;
			crop.x = (w - V_WIDTH*scale)/2f;
		} else if (aspectRatio < ASPECT_RATIO) {
			scale = (float)w/(float)V_WIDTH;
			crop.y = (h - V_HEIGHT*scale)/2f;
		} else {
			scale = (float)w/(float)V_WIDTH;
		}

		float wid = (float)V_WIDTH*scale;
		float hei = (float)V_HEIGHT*scale;
		viewport = new Rectangle(crop.x, crop.y, wid, hei);
	}

	public static void UpdateDisplay() {
		if (!GameVars.GetPlayerFullscreen()) {
			Gdx.graphics.setWindowedMode((int) BikeGame.viewport.width, (int) BikeGame.viewport.height);
			Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
		} else {
			Graphics.DisplayMode m = null;
			for(Graphics.DisplayMode mode: Gdx.graphics.getDisplayModes()) {
				if(m == null) {
					m = mode;
				} else {
					if(m.width < mode.width) {
						m = mode;
					}
				}
			}

			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
		}
	}

	@Override
	public void pause () {

	}

	@Override
	public void resume () {

	}
}
