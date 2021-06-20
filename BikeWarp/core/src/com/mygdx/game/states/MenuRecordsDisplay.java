package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameSounds;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.*;

public class MenuRecordsDisplay extends GameState {
	private static final String option1 = "Return to Records Menu";
    private int currentOption, numLevelShow, numMin, numOptions;
    private Sprite background;
    private BitmapFont question, levelFont;
	private static GlyphLayout glyphLayout = new GlyphLayout();
	private float qWidth, qHeight, SCRWIDTH, SCRHEIGHT, sheight, recordWidth, recordHeight, optWidth;
    private float fadeIn, fadeOut, alpha, fadeTime = 0.5f;
    private final String header = "Select a level to display records";
    private static String[] levelList;
	private static final int numExtra = 1;
	private static int totalLevels;
	private static boolean diamond;
	private static String dispText = "";

	public MenuRecordsDisplay(GameStateManager gsm, int mode) {
		super(gsm);
		// Create the canvas
        create();
		diamond = mode != 0;
	}

    public void create() {
    	// Setup the canvas
		this.game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		SCRWIDTH = BikeGame.viewport.width;
		SCRHEIGHT = BikeGame.viewport.height;
		sheight = 0.7f*SCRHEIGHT;
        background = new Sprite(BikeGameTextures.LoadTexture("bg_StarsBlueGreen"));
        // Grab the bitmap fonts
        question = new BitmapFont(Gdx.files.internal("data/font-48.fnt"), false);
        question.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        question.setColor(1, 1, 1, 1);
        float scaleVal = 1.0f;
        question.getData().setScale(scaleVal);
		glyphLayout.setText(question, header);
        qWidth = glyphLayout.width;
        if (qWidth > 0.25f*SCRWIDTH) scaleVal = 0.25f*SCRWIDTH/qWidth;
        question.getData().setScale(scaleVal);
        glyphLayout.setText(question, header);
        qWidth = glyphLayout.width;
        qHeight = glyphLayout.height;
        // Now grab the fonts for the player names
        levelFont = new BitmapFont(Gdx.files.internal("data/recordsmenu.fnt"), false);
        levelFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        SetupMenuFont();
        // Set the fading variables
        fadeOut = -1.0f;
        fadeIn = 0.0f;
    }

    private void SetNumRecordShow() {
		glyphLayout.setText(levelFont, "My");
        recordHeight = glyphLayout.height;
        numLevelShow = (int) Math.floor(sheight/(1.5f* recordHeight));
        if (numLevelShow > numOptions) numLevelShow = numOptions;
    }

    private void SetupMenuFont() {
		totalLevels = LevelsListGame.NUMGAMELEVELS;
		float scaleVal = 1.0f;
		levelFont.getData().setScale(scaleVal);
		glyphLayout.setText(levelFont, option1);
		recordWidth = glyphLayout.width;
		// Remaining options
		float tstReplayWidth;
		numOptions = numExtra + totalLevels;
		for (int i=0; i<totalLevels; i++) {
			glyphLayout.setText(levelFont, LevelsListGame.getLevelName(i+1)); // +1 for Main Menu
			tstReplayWidth = glyphLayout.width;
			if (tstReplayWidth > recordWidth) recordWidth = tstReplayWidth;
		}
		scaleVal = 0.25f*(SCRWIDTH-0.075f*SCRHEIGHT)/ recordWidth;
		levelFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		levelFont.getData().setScale(scaleVal);
		SetNumRecordShow();
	}

    @Override
	public void handleInput() {
		if (GameInput.isPressed(GameInput.KEY_UP)) {
			currentOption--;
			if (currentOption < 0) currentOption = numOptions - 1;
			if (currentOption >= numExtra) SteamVars.LoadPBWR(currentOption);
			BikeGameSounds.PlayMenuSwitch();
		} else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
			currentOption++;
			if (currentOption >= numOptions) currentOption = 0;
			if (currentOption >= numExtra) SteamVars.LoadPBWR(currentOption);
			BikeGameSounds.PlayMenuSwitch();
		} else if ((GameInput.isPressed(GameInput.KEY_ESC)) & (fadeOut == -1.0f)) {
			fadeOut = 1.0f;
			BikeGameSounds.PlayMenuSelect();
		} else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut == -1.0f)) {
			if (currentOption == 0) {
				fadeOut = 1.0f;
				BikeGameSounds.PlayMenuSelect();
			}  else {
				// Refresh the leaderboard (again!)
				SteamVars.LoadPBWR(currentOption);
			}
		} else if (fadeOut == 0.0f) {
			// Go to the replay menu
			fadeOut = -1.0f;
			gsm.setState(GameStateManager.PEEK, false, "none", -1, 0);
		}
		if ((currentOption > numLevelShow / 2) & (currentOption < numOptions - numLevelShow / 2))
			numMin = currentOption - numLevelShow / 2;
		else if (currentOption <= numLevelShow / 2) numMin = 0;
		else if (currentOption >= numOptions - numLevelShow / 2) numMin = numOptions - numLevelShow;
	}

	@Override
	public void update(float dt) {
    	// Always make sure the camera is in the correct location and zoom for this screen
		cam.setToOrtho(false, SCRWIDTH, SCRHEIGHT);
		cam.zoom = 1.0f;
    	cam.update();
    	handleInput();
    	if (fadeOut > 0.0f) {
    		fadeOut -= dt/fadeTime;
    		if (fadeOut < 0.0f) fadeOut = 0.0f;
    	} else if (fadeIn <= 1.0f) {
    		fadeIn += dt/fadeTime;
    		if (fadeIn > 1.0f) fadeIn = 2.0f;
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
    	sb.setProjectionMatrix(cam.combined);
    	// Render QUESTION
    	if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, fadeOut);
    	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, fadeIn);
    	else sb.setColor(1, 1, 1, 1); 
        sb.begin();
        // Draw Sky
        sb.draw(background, cam.position.x-SCRWIDTH/2, cam.position.y-SCRHEIGHT/2, 0, 0, SCRWIDTH, SCRHEIGHT, 1.0f, 1.0f, 0.0f);
        // Draw the text
    	if (fadeOut >= 0.0f) question.setColor(1, 1, 1, fadeOut);
    	else if (fadeIn < 1.0f) question.setColor(1, 1, 1, fadeIn);
    	else question.setColor(1, 1, 1, 1);
        question.draw(sb, header, (0.5f*SCRWIDTH-qWidth)/2.0f, cam.position.y + (1.5f* recordHeight * numLevelShow)/2 + 1.5f*qHeight);
        // Draw Player Names
    	if (fadeOut >= 0.0f) alpha=fadeOut;
    	else if (fadeIn < 1.0f) alpha=fadeIn;
    	else alpha=1.0f;
        for (int i = numMin; i<numMin+ numLevelShow; i++) {
        	// Make the currently selected option a little brighter
        	if (currentOption == i) levelFont.setColor(1, 1, 1, alpha);
        	else levelFont.setColor(1, 1, 1, alpha/2);
        	// Grab the text to display
        	if (i == 0) dispText = option1;
        	else dispText = LevelsListGame.getLevelName(i);
        	// Render the text
			glyphLayout.setText(levelFont, dispText);
        	optWidth = glyphLayout.width;
        	levelFont.draw(sb, dispText, (0.5f*SCRWIDTH-optWidth)/2.0f, cam.position.y + (1.5f* recordHeight * numLevelShow)/2 - 1.5f*(i-numMin)* recordHeight);
        }
        if (currentOption >= numExtra) {
			levelFont.setColor(1, 1, 1, alpha);
			SteamVars.RecordStringMenu(diamond);
			float lvlHeight, width;
			// (1) draw ranks
			dispText = SteamVars.recordMenuStringRanks;
			glyphLayout.setText(levelFont, dispText);
			lvlHeight = glyphLayout.height;
			width = glyphLayout.width;
			levelFont.draw(sb, dispText, cam.position.x, cam.position.y+lvlHeight/2, 1.1f*width, Align.right, true);
			// (2) draw names
			dispText = SteamVars.recordMenuStringNames;
			glyphLayout.setText(levelFont, dispText);
			lvlHeight = glyphLayout.height;
			levelFont.draw(sb, dispText, cam.position.x+1.5f*width, cam.position.y+lvlHeight/2, 1.1f*glyphLayout.width, Align.left, true);
			width *= 1.5f;
			width += 1.1f*glyphLayout.width;
			// (3) draw times
			dispText = SteamVars.recordMenuStringTimes;
			glyphLayout.setText(levelFont, dispText);
			lvlHeight = glyphLayout.height;
			levelFont.draw(sb, dispText, cam.position.x+width, cam.position.y+lvlHeight/2, 1.1f*glyphLayout.width, Align.right, true);
		}
		sb.end();
	}

	@Override
	public void dispose() {
		if (question != null) question.dispose();
    	if (levelFont != null) levelFont.dispose();
	}

}
