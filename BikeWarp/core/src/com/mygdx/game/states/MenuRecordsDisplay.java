package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Align;
import com.codedisaster.steamworks.SteamAPI;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameSounds;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.*;

public class MenuRecordsDisplay extends GameState {
	private static final String option1 = "Return to Records Menu";
    private int currentOption, numLevelShow, numMin, numOptions;
	private final float poleWidth = 0.03f;
	private Sprite metalpole, metalcorner, tile;
	private Texture metalmesh;
	private float tile_xpos, tile_ypos_top, tile_xw, tile_yw, tile_sep, tile_yoff, flagWidth, flagHeight;
	private float uRight, vTop;
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

		// Load the background metal grid
		metalmesh = BikeGameTextures.LoadTexture("metal_grid");
		float ratio = 4.0f;
		uRight = SCRWIDTH * ratio / metalmesh.getWidth();
		vTop= SCRHEIGHT * ratio / metalmesh.getHeight();
		// Load the black metal pole and the corner
		metalpole = new Sprite(BikeGameTextures.LoadTexture("metalpole_black"));
		metalcorner = new Sprite(BikeGameTextures.LoadTexture("metalpole_blackcorner"));

		// The tile to be used to display the records
		tile = new Sprite(BikeGameTextures.LoadTexture("records_tile"));
		tile_sep = 0.2f;
		tile_yw = SCRHEIGHT*(1-2*poleWidth)/(11 + 15*tile_sep);
		tile_xw = tile_yw*(1278.0f/106.0f);
		tile_xpos = cam.position.x;
		tile_ypos_top = cam.position.y + SCRHEIGHT/2 - poleWidth*SCRHEIGHT - tile_yw - tile_sep*tile_yw;
		tile_yoff = tile_yw*(12.0f/112.0f);
		flagHeight = 0.5f*tile_yw*(100.0f/112.0f);
		flagWidth = flagHeight * (360.0f/240.0f);

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
		scaleVal = 0.25f*(SCRWIDTH-poleWidth*SCRHEIGHT)/ recordWidth;
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
		// Draw metal mesh, pole, and corners
		sb.draw(metalmesh, cam.position.x-SCRWIDTH/2, cam.position.y-SCRHEIGHT/2, SCRWIDTH, (float) SCRHEIGHT, 0.0f, 0.0f, uRight, vTop);
		sb.draw(metalpole, cam.position.x-SCRWIDTH/2+poleWidth*SCRHEIGHT, cam.position.y+(0.5f-poleWidth)*SCRHEIGHT, 0, 0, SCRWIDTH-2*poleWidth*SCRHEIGHT, poleWidth*SCRHEIGHT, 1.0f, 1.0f, 0.0f);
		sb.draw(metalpole, cam.position.x-SCRWIDTH/2-poleWidth*SCRHEIGHT, cam.position.y-SCRHEIGHT/2, SCRWIDTH/2, 0.5f*poleWidth*SCRHEIGHT, SCRWIDTH-2*poleWidth*SCRHEIGHT, poleWidth*SCRHEIGHT, 1.0f, 1.0f, 180.0f);
		sb.draw(metalpole, cam.position.x-SCRWIDTH/2-SCRHEIGHT/2+0.5f*poleWidth*SCRHEIGHT, cam.position.y-0.5f*poleWidth*SCRHEIGHT+poleWidth*SCRHEIGHT, SCRHEIGHT/2, 0.5f*poleWidth*SCRHEIGHT, SCRHEIGHT-2*poleWidth*SCRHEIGHT, poleWidth*SCRHEIGHT, 1.0f, 1.0f, 90.0f);
		sb.draw(metalpole, cam.position.x+SCRWIDTH/2-SCRHEIGHT/2-0.5f*poleWidth*SCRHEIGHT, cam.position.y-0.5f*poleWidth*SCRHEIGHT-poleWidth*SCRHEIGHT, SCRHEIGHT/2, 0.5f*poleWidth*SCRHEIGHT, SCRHEIGHT-2*poleWidth*SCRHEIGHT, poleWidth*SCRHEIGHT, 1.0f, 1.0f, 270.0f);
		sb.draw(metalcorner, cam.position.x-SCRWIDTH/2, cam.position.y+(0.5f-poleWidth)*SCRHEIGHT, 0.5f*poleWidth*SCRHEIGHT, 0.5f*poleWidth*SCRHEIGHT, poleWidth*SCRHEIGHT, poleWidth*SCRHEIGHT, 1.0f, 1.0f, 180.0f);
		sb.draw(metalcorner, cam.position.x+SCRWIDTH/2-poleWidth*SCRHEIGHT, cam.position.y+(0.5f-poleWidth)*SCRHEIGHT, 0.5f*poleWidth*SCRHEIGHT, 0.5f*poleWidth*SCRHEIGHT, poleWidth*SCRHEIGHT, poleWidth*SCRHEIGHT, 1.0f, 1.0f, 90.0f);
		sb.draw(metalcorner, cam.position.x+SCRWIDTH/2-poleWidth*SCRHEIGHT, cam.position.y-SCRHEIGHT/2, 0.5f*poleWidth*SCRHEIGHT, 0.5f*poleWidth*SCRHEIGHT, poleWidth*SCRHEIGHT, poleWidth*SCRHEIGHT, 1.0f, 1.0f, 0.0f);
		sb.draw(metalcorner, cam.position.x-SCRWIDTH/2, cam.position.y-SCRHEIGHT/2, 0.5f*poleWidth*SCRHEIGHT, 0.5f*poleWidth*SCRHEIGHT, poleWidth*SCRHEIGHT, poleWidth*SCRHEIGHT, 1.0f, 1.0f, 270.0f);
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
			// Draw the tiles
			float tile_ypos = tile_ypos_top;
			int numRender = 12;
			if (SteamVars.recordMenuStringRanks.size()==1) {
				tile_ypos = cam.position.y-0.5f*tile_yw;
				numRender = 1;
			} else if (SteamVars.recordMenuStringRanks.size()==10) {
				// Player is in the top 10
				tile_ypos = tile_ypos_top - 0.5f*(tile_yw + 3*tile_yw*tile_sep);
				numRender = 10;
			} else if (SteamVars.recordMenuStringRanks.size()==11) {
				// Player is ranked 11
				tile_ypos = tile_ypos_top - 0.5f*(tile_yw + 2*tile_yw*tile_sep);
				numRender = 11;
			} else if (SteamVars.recordMenuStringRanks.size()==12) {
				// Player is ranked 12 or higher
				tile_ypos = tile_ypos_top;
				numRender = 12;
			}
			// Find the biggest rank width
			float rankWidth = 0.0f;
			for (int rr=0; rr<SteamVars.recordMenuStringRanks.size(); rr++) {
				glyphLayout.setText(levelFont, SteamVars.recordMenuStringRanks.get(rr));
				lvlHeight = glyphLayout.width;
				if (lvlHeight > rankWidth) rankWidth = lvlHeight;
			}
			float textPos;
			for (int tt=0; tt<numRender; tt++) {
				if ((numRender==12) & (tt==10)) continue;
				sb.draw(tile, tile_xpos, tile_ypos, 0, 0, tile_xw, tile_yw, 1.0f, 1.0f, 0.0f);
				if (SteamVars.recordMenuCountries.get(tt) != -1) {
					// Draw the player name
					dispText = SteamVars.recordMenuStringNames.get(tt);
					glyphLayout.setText(levelFont, dispText);
					lvlHeight = glyphLayout.height;
					textPos = tile_ypos + tile_yoff + tile_yw/2 + lvlHeight/2;
					levelFont.draw(sb, dispText, tile_xpos + rankWidth + 3*tile_sep*tile_yw + flagWidth, textPos, 1.1f*glyphLayout.width, Align.left, true);
					// Draw the rank
					dispText = SteamVars.recordMenuStringRanks.get(tt);
					levelFont.draw(sb, dispText, tile_xpos + tile_sep*tile_yw, textPos, rankWidth, Align.right, true);
					// Draw the flag
					sb.draw(BikeGameTextures.allFlags[SteamVars.recordMenuCountries.get(tt)-1], tile_xpos + rankWidth + 2*tile_sep*tile_yw, tile_ypos + tile_yoff + tile_yw*(100.0f/112.0f)/2 - flagHeight/2, 0, 0, flagWidth, flagHeight, 1.0f, 1.0f, 0.0f);
					// Draw the time
					dispText = SteamVars.recordMenuStringTimes.get(tt);
					levelFont.draw(sb, dispText, tile_xpos + tile_sep*tile_yw, textPos, tile_xw-3*tile_sep*tile_yw, Align.right, true);
				}
				if (tt==9) tile_ypos -= 3*tile_yw*tile_sep;
				tile_ypos -= (1.0+tile_sep)*tile_yw;
			}
		}
		sb.end();
	}

	@Override
	public void dispose() {
		if (question != null) question.dispose();
    	if (levelFont != null) levelFont.dispose();
	}

}
