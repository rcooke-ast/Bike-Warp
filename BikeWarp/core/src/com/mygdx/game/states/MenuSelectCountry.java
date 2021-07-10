package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameSounds;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.GameVars;
import com.mygdx.game.handlers.SteamVars;

public class MenuSelectCountry extends GameState {
    private int currentOption, numOptions, updateCountry;
    private Sprite stars, shade, gamename, tube, fluid;
    private float gn_width, gn_height, tube_length, tube_height, progress;
    private BitmapFont question;
	private static GlyphLayout glyphLayout = new GlyphLayout();
	private float qWidth, qHeight, SCRWIDTH, SCRHEIGHT, sborder;
    private float fadeIn, fadeOut, alpha, fadeTime = 0.5f;
    private final String header = "Select your nationality";
    private boolean doFade, popState, enableSelect, updateSteam;
	private SelectBox selectCountry;
	private Button buttonCountry;
	private Skin skin;
	private Stage stage;
	private Window windowTBar;
	private InputMultiplexer inputMultiplexer;

	public MenuSelectCountry(GameStateManager gsm, int inFading) {
		super(gsm);
		// If we have no information on the players, load it
		if (GameVars.currentPlayer == -1) {
			GameVars.LoadPlayers();
		} else {
	        // Set the starting option
	        currentOption = GameVars.GetCurrentPlayer();
		}
		doFade = false;
		popState = false;
		if (inFading==1) doFade = true;
		if (inFading==2) popState = true;
		// Create the canvas
        create();
	}

    public void create() {
		updateSteam = false;
//        float SCTOSCRW = ((float) Gdx.graphics.getHeight()*Gdx.graphics.getDisplayMode().width)/((float) Gdx.graphics.getDisplayMode().height);
		this.game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        SCRWIDTH = BikeGame.viewport.width;
		SCRHEIGHT = BikeGame.viewport.height;
		//background = new Sprite(BikeGameTextures.LoadTexture("sky_bluesky"));
		//background = new Sprite(BikeGameTextures.LoadTexture("menu_black"));
		stars = new Sprite(BikeGameTextures.LoadTexture("bg_StarsSparse"));
		shade = new Sprite(BikeGameTextures.LoadTexture("ground_shade"));
		tube = new Sprite(BikeGameTextures.LoadTexture("nitrous_tube"));
		fluid = new Sprite(BikeGameTextures.LoadTexture("nitrous_fluid"));
		tube_length = SCRWIDTH*0.7f;
		tube_height = SCRHEIGHT*0.02f;

		gamename = new Sprite(BikeGameTextures.LoadTexture("menu_gamename"));
		gn_width = SCRWIDTH*0.7f;
		gn_height = gn_width*gamename.getHeight()/gamename.getWidth();

		// Set the fading variables
        fadeOut = -1.0f;
        if (doFade) fadeIn = 0.0f;
        else fadeIn = 2.0f;
		// Prepare the stage
		stage = new Stage();
		Gdx.input.setCursorCatched(false);
		// Set the viewport
		stage.getViewport().setScreenX((int) (BikeGame.viewport.x));
		stage.getViewport().setScreenY((int) (BikeGame.viewport.y));
		stage.getViewport().setScreenWidth((int) (SCRWIDTH));
		stage.getViewport().setScreenHeight((int) SCRHEIGHT);
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(Gdx.input.getInputProcessor());
		Gdx.input.setInputProcessor(inputMultiplexer);
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		enableSelect = false;
		selectCountry = new SelectBox(skin);
		selectCountry.setItems(GameVars.countryNames.clone());
		numOptions = GameVars.countryNames.length;
		selectCountry.setSelectedIndex(0);
		selectCountry.setMaxListCount(0);
		selectCountry.setAlignment(Align.center);
		buttonCountry = new TextButton("Select", skin);
		buttonCountry.align(Align.center);
		// Now setup everything in a window
		windowTBar = new Window("", skin);
		windowTBar.align(Align.center);
		//windowTBar.setPosition(BikeGame.viewport.x, BikeGame.viewport.y, Align.center);
		windowTBar.defaults().spaceBottom(3);
		windowTBar.row();//.fill().expandX().colspan(2);
		windowTBar.add(selectCountry);
		windowTBar.row();//.fill().expandX().colspan(2);
		windowTBar.add(buttonCountry);
		windowTBar.pack();
		windowTBar.setPosition(BikeGame.viewport.x + BikeGame.viewport.width/2, BikeGame.viewport.y + BikeGame.viewport.height/2, Align.center);
//		windowTBar.setHeight(0.2f*SCRHEIGHT);
		stage.addActor(windowTBar);

		// Grab the bitmap fonts
		question = new BitmapFont(Gdx.files.internal("data/font-48.fnt"), false);
		question.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		question.setColor(1, 1, 1, 1);
		float scaleVal = 1.0f;
		question.getData().setScale(scaleVal);
		glyphLayout.setText(question, header);
		qWidth = glyphLayout.width;
		if (qWidth > windowTBar.getPrefWidth()) scaleVal = windowTBar.getPrefWidth()/qWidth;
		question.getData().setScale(scaleVal);
		glyphLayout.setText(question, header);
		qWidth = glyphLayout.width;
		qHeight = glyphLayout.height;

		sborder = 0.1f*windowTBar.getPrefWidth();

		selectCountry.addListener(new ChangeListener() {
			@SuppressWarnings("unchecked")
			public void changed (ChangeEvent event, Actor actor) {
				currentOption = selectCountry.getSelectedIndex();
				enableSelect = currentOption != 0;
			}
		});

		buttonCountry.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if (enableSelect) {
					// Set the player country
					updateSteam = GameVars.SetPlayerCountry(currentOption);
					if (updateSteam) {
						updateCountry = 1;
					} else {
						BikeGameSounds.PlayMenuSelect();
						fadeOut=1.0f;
					}
				}
				buttonCountry.setChecked(false);
			}
		});
	}

    @Override
	public void handleInput() {
    	if (GameInput.isPressed(GameInput.KEY_UP)) {
    		currentOption--;
    		if (currentOption < 0) currentOption = numOptions - 1;
			BikeGameSounds.PlayMenuSwitch();
        } else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
    		currentOption++;
    		if (currentOption >= numOptions) currentOption = 0;
			BikeGameSounds.PlayMenuSwitch();
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (enableSelect) & (fadeOut==-1.0f)) {
			// Set the player country
			updateSteam = GameVars.SetPlayerCountry(currentOption);
			if (updateSteam) {
				updateCountry = 1;
			} else {
				BikeGameSounds.PlayMenuSelect();
				fadeOut=1.0f;
			}
        } else if (fadeOut==0.0f) {
        	// Go to the main menu
    		fadeOut=-1.0f;
			Gdx.input.setCursorCatched(true);
			if (popState) gsm.setState(GameStateManager.PEEK, false, null, -1, 0);
			else gsm.setState(GameStateManager.MAINMENU, false, "none", -1, 0);
        }
	}

	@Override
	public void update(float dt) {
    	// Always make sure the camera is in the correct location and zoom for this screen
		cam.setToOrtho(false, SCRWIDTH, SCRHEIGHT);
		cam.zoom = 1.0f;
    	cam.update();
    	handleInput();
		if (updateCountry != 0) UpdatePlyrCountry();
    	if (fadeOut > 0.0f) {
    		fadeOut -= dt/fadeTime;
    		if (fadeOut < 0.0f) fadeOut = 0.0f;
    	} else if (fadeIn <= 1.0f) {
    		fadeIn += dt/fadeTime;
    		if (fadeIn > 1.0f) fadeIn = 2.0f;
    	}
	}

	private void UpdatePlyrCountry() {
		if (updateCountry == 1) {
			SteamVars.PrepareAllLeaderboards(1);
			updateCountry++;
		} else if (updateCountry == 2) {
			if (SteamVars.readyForNextLeaderboard) {
				// Leaderboards are now prepared
				updateCountry++;
			}
		} else if (updateCountry == 3) {
			// Start the long process of updating the player's country
			SteamVars.UpdatePlayerCountry();
			progress = SteamVars.GetProgress();
			if (!SteamVars.updateCountry) {
				updateCountry++;
			}
		} else if (updateCountry == 4) {
			// Reset
			updateCountry = 0;
			fadeOut=0.00001f; // Don't bother fading - change to 1.0f to fade
		}
	}

	@Override
	public void render() {
    	// clear screen
    	Gdx.gl.glClearColor(0, 0, 0, 1);
    	Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glViewport((int) BikeGame.viewport.x, (int) BikeGame.viewport.y, (int) BikeGame.viewport.width, (int) BikeGame.viewport.height);
    	//Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	sb.setProjectionMatrix(cam.combined);
		if (fadeOut >= 0.0f) alpha=fadeOut;
		else if (fadeIn < 1.0f) alpha=fadeIn;
		else alpha=1.0f;
		sb.setColor(1, 1, 1, alpha);
		sb.begin();
		sb.draw(stars, cam.position.x-SCRWIDTH/2, cam.position.y-SCRHEIGHT/2, 0, 0, SCRWIDTH, SCRHEIGHT, 1.0f, 1.0f, 0.0f);
		sb.draw(shade, cam.position.x-sborder-windowTBar.getPrefWidth()/2, cam.position.y-windowTBar.getHeight()/2-sborder*0.3f, 0, 0, 2*sborder+windowTBar.getPrefWidth(), 3*sborder*0.3f+windowTBar.getHeight()+qHeight, 1.0f, 1.0f, 0.0f);
		sb.draw(gamename, cam.position.x-gn_width/2, cam.position.y+(SCRHEIGHT/2-gn_height*1.5f), 0, 0, gn_width, gn_height, 1.0f, 1.0f, 0.0f);
		// Draw the header
		float alpha=1.0f;
		if (fadeOut >= 0.0f) alpha = fadeOut;
		else if (fadeIn < 1.0f) alpha = fadeIn;
		windowTBar.setColor(1,1,1,alpha);
		question.setColor(1, 1, 1, alpha);
		question.draw(sb, header, (SCRWIDTH-qWidth)/2.0f, cam.position.y + windowTBar.getHeight()/2+sborder*0.3f + qHeight);
		// Draw progress bar, if needed
		if (updateSteam) {
			// Draw Fluid
			sb.draw(fluid, hudCam.position.x-tube_length/2, hudCam.position.y-0.4f*SCRHEIGHT, 0, 0, tube_length*progress, tube_height, 1.0f, 1.0f, 0.0f);
			// Draw Tube
			sb.draw(tube, hudCam.position.x-tube_length/2, hudCam.position.y-0.4f*SCRHEIGHT, 0, 0, tube_length, tube_height, 1.0f, 1.0f, 0.0f);
		}
		sb.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void dispose() {
		if (question != null) question.dispose();
		if (stage != null) stage.dispose();
		if (skin != null) skin.dispose();
	}

}
