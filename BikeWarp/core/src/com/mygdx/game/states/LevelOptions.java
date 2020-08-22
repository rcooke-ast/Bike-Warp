/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.GameVars;
import com.mygdx.game.handlers.LevelsListCustom;
import com.mygdx.game.handlers.LevelsListGame;
import com.mygdx.game.handlers.ReplayVars;
import com.mygdx.game.utilities.EditorIO;

/**
 *
 * @author rcooke
 */
public class LevelOptions extends GameState {
	private float SCRWIDTH, SCRHEIGHT;
	private BitmapFont menuText;
	private static GlyphLayout glyphLayout = new GlyphLayout();
    private Sprite metalpole, metalcorner;
    private Texture texture, metalmesh;
    private float uRight, vTop, sheight;
    private float menuHeight, menuWidth, lvlWidth;
    private float fadeOut, fadeIn, alpha, fadeTime = 0.5f;
    private int levelNumber, modeValue, currentOption, totalOptions;
    private float checkLevels = 0.0f;
    private String[] allOptions;
    private boolean saveReplay, fileExists, firstPlay, goToNext;
    private String replayFilename;

    public LevelOptions(GameStateManager gsm, int levNum, int mode) {
        super(gsm);
    	levelNumber = levNum;
    	modeValue = mode;
    	ReplayVars.Reset("", levelNumber, mode);
        create();
    }
    
    public void create() {
    	firstPlay = true;
    	goToNext = false;
		this.game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		SCRWIDTH = BikeGame.viewport.width;
		SCRHEIGHT = BikeGame.viewport.height;
		sheight = 0.7f*SCRHEIGHT;
		GameVars.SetTimerTotal(-2);
		saveReplay = false;
		replayFilename = "";
		fileExists = false;
        SetTotalOptions();
        // Menu text
        menuText = new BitmapFont(Gdx.files.internal("data/recordsmenu.fnt"), false);
        // Update the menu
        UpdateMenu();
        checkLevels = 0.0f;
        // Load the background metal grid
        metalmesh = BikeGameTextures.LoadTexture("metal_grid",1);
        float ratio = 4.0f;
        uRight = SCRWIDTH * ratio / metalmesh.getWidth();
        vTop= SCRHEIGHT * ratio / metalmesh.getHeight();
        // Load the black metal pole and the corner
        metalpole = new Sprite(BikeGameTextures.LoadTexture("metalpole_black",1));
        metalcorner = new Sprite(BikeGameTextures.LoadTexture("metalpole_blackcorner",1));
        // Set the starting option
        currentOption = 1;
        fadeOut = -1.0f;
        fadeIn = 0.0f;
    }

    public void SetTotalOptions() {
    	if (modeValue == 1) allOptions = new String[] {LevelsListCustom.customLevelNames[levelNumber+1], ""};
    	else if (modeValue == 2) allOptions = new String[] {LevelsListGame.gameLevelNames[levelNumber+1], ""};
    	String[] tmp;
    	// If the level has just been played, use "Replay Level" instead of "Play Level"
    	if (firstPlay) allOptions[1] = "Play Level";
    	else allOptions[1] = "Replay Level";
    	// Check if we should offer "Next Level" as an option
    	// This can only happen if the current level is complete, the current level is skipped, or this is not the final level
    	if ((modeValue == 2) && (levelNumber+1 != LevelsListGame.NUMGAMELEVELS)) {
    		if ((GameVars.GetLevelStatus(levelNumber) == 1) | (GameVars.GetLevelStatus(levelNumber) == 2)) {
	    		tmp = new String[allOptions.length+1];
	    		for (int ii=0; ii<allOptions.length; ii++) tmp[ii] = allOptions[ii];
	    		tmp[allOptions.length] = "Next Level";
	    		allOptions = tmp.clone();
    		}
    	} else if ((modeValue == 1) && (levelNumber+1 != LevelsListCustom.NUMCUSTOMLEVELS)) {
    		tmp = new String[allOptions.length+1];
    		for (int ii=0; ii<allOptions.length; ii++) tmp[ii] = allOptions[ii];
    		tmp[allOptions.length] = "Next Level";
    		allOptions = tmp.clone();
    	}
    	// Go back to level selector
		tmp = new String[allOptions.length+1];
		for (int ii=0; ii<allOptions.length; ii++) tmp[ii] = allOptions[ii];
		tmp[allOptions.length] = "Level Select";
		allOptions = tmp.clone();
    	// Can skip level
    	if ((modeValue == 2) && (GameVars.CanSkip()) && (GameVars.GetLevelStatus(levelNumber) == 0) && (modeValue==2) && (levelNumber != LevelsListGame.NUMGAMELEVELS-1)) {
    		tmp = new String[allOptions.length+1];
    		for (int ii=0; ii<allOptions.length; ii++) tmp[ii] = allOptions[ii];
    		tmp[allOptions.length] = "Skip Level";
    		allOptions = tmp.clone();
    	}
    	// Watch or Save replay
    	if (ReplayVars.replayTime.size() != 0) {
    		tmp = new String[allOptions.length+2];
    		for (int ii=0; ii<allOptions.length; ii++) tmp[ii] = allOptions[ii];
    		tmp[allOptions.length] = "Watch Replay";
    		tmp[allOptions.length+1] = "Save Replay";
    		allOptions = tmp.clone();
    	}
    	totalOptions = allOptions.length;
    }
    
    public void UpdateMenu() {
        float scaleVal = 1.0f;
        menuText.getData().setScale(scaleVal);
		glyphLayout.setText(menuText, "XXXXXXXXXXXXXXX");
		menuWidth = glyphLayout.width;
		float tmpMenuWidth;
        for (int i=0; i<totalOptions; i++) {
			tmpMenuWidth = glyphLayout.width;
        	if (tmpMenuWidth > menuWidth) menuWidth = tmpMenuWidth;
        }
        scaleVal = 0.25f*(SCRWIDTH-0.075f*SCRHEIGHT)/menuWidth;
        menuText.getData().setScale(scaleVal);
        menuText.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        glyphLayout.setText(menuText, "My");
        menuHeight = glyphLayout.height;
    }
    
    public void handleInput() {
    	if ((GameInput.isPressed(GameInput.KEY_UP)) & (!saveReplay)) {
    		currentOption--;
    		if (currentOption < 1) currentOption = totalOptions-1;
        } else if ((GameInput.isPressed(GameInput.KEY_DOWN)) & (!saveReplay)) {
    		currentOption++;
    		if (currentOption >= totalOptions) currentOption = 1;
        } else if (GameInput.isPressed(GameInput.KEY_ESC)) {
        	if (saveReplay) saveReplay = false;
        	else fadeOut=1.0f; // Return to level selector
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut==-1.0f)) {
        	if (saveReplay) {
        		// Check that a valid name has been supplied, then write to file
        		if (replayFilename.equals("")) {
        			saveReplay = false;
        		} else if (!fileExists) {
        			ReplayVars.SaveReplay(replayFilename);
        			saveReplay = false;
        		}
        	} else if ((allOptions[currentOption].equalsIgnoreCase("Play Level")) || (allOptions[currentOption].equalsIgnoreCase("Replay Level"))) {
        		firstPlay = false;
        		// Load the level
        		String levelName;
        		if (modeValue==1) levelName = EditorIO.loadLevelPlay(Gdx.files.internal(LevelsListCustom.customLevelFiles[levelNumber+1]));
        		else levelName = EditorIO.loadLevelPlay(Gdx.files.internal(LevelsListGame.getLevelFile(levelNumber+1)));
        		ReplayVars.Reset(levelName, levelNumber, modeValue);
        		gsm.setState(GameStateManager.PLAY, true, levelName, levelNumber, modeValue);
        	} else if (allOptions[currentOption].equalsIgnoreCase("Level Select")) fadeOut=1.0f; // Return to level selector
        	else if (allOptions[currentOption].equalsIgnoreCase("Skip Level")) {
            	GameVars.SetSkipLevel(levelNumber); // Skip this level
            	goToNext=true;
            	fadeOut=1.0f; // Return to level selector
            	LevelsListGame.updateRecords();
        	} else if (allOptions[currentOption].equalsIgnoreCase("Watch Replay")){
        		// Load the replay
        		String levelName = EditorIO.loadLevelPlay(Gdx.files.internal(LevelsListGame.getLevelFile(levelNumber+1)));
        		if (modeValue==1) levelName = EditorIO.loadLevelPlay(Gdx.files.internal(LevelsListCustom.customLevelFiles[levelNumber+1]));
        		ReplayVars.replayCntr = 0;
        		ReplayVars.replayCDCntr = 0;
        		gsm.setState(GameStateManager.PLAY, true, levelName, levelNumber, modeValue+2);
        	} else if (allOptions[currentOption].equalsIgnoreCase("Save Replay")){
        		saveReplay = true;
	    	} else if (allOptions[currentOption].equalsIgnoreCase("Next Level")) {
            	fadeOut=1.0f; // Select next level
            	goToNext=true;
	    	}
        } else if (fadeOut==0.0f) {
    		fadeOut=-1.0f;
    		gsm.setState(GameStateManager.PEEK, false, "none", levelNumber, modeValue);
    		checkLevels=0.0f;
    		if (goToNext) gsm.setState(GameStateManager.LEVELOPTIONS, true, "", levelNumber+1, modeValue);
        }
    }
    
    public void update(float dt) {
    	// Always make sure the camera is in the correct location and zoom for this screen
		cam.setToOrtho(false, SCRWIDTH, SCRHEIGHT);
		cam.zoom = 1.0f;
    	cam.update();
		handleInput();
		// Set the fading
    	if (fadeOut > 0.0f) {
    		fadeOut -= dt/fadeTime;
    		if (fadeOut < 0.0f) fadeOut = 0.0f;
    	} else if (fadeIn <= 1.0f) {
    		fadeIn += dt/fadeTime;
    		if (fadeIn > 1.0f) {
    			fadeIn = 2.0f;
    		}
    	} else {
    		// Update how many levels should be shown
			SetTotalOptions();
	    	UpdateMenu();
    	}
    }
    
    public void render() {
    	// clear screen
    	Gdx.gl.glClearColor(0, 0, 0, 1);
    	Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glViewport((int) BikeGame.viewport.x, (int) BikeGame.viewport.y, (int) BikeGame.viewport.width, (int) BikeGame.viewport.height);
		//Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	sb.setProjectionMatrix(cam.combined);
    	if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, fadeOut);
    	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, fadeIn);
    	else sb.setColor(1, 1, 1, 1); 
        sb.begin();
        // Draw metal mesh, pole, and corners
        sb.draw(metalmesh, cam.position.x-SCRWIDTH/2, cam.position.y-SCRHEIGHT/2, SCRWIDTH, (float) SCRHEIGHT, 0.0f, 0.0f, uRight, vTop);
        sb.draw(metalpole, cam.position.x-SCRWIDTH/2+0.075f*SCRHEIGHT, cam.position.y+0.425f*SCRHEIGHT, 0, 0, SCRWIDTH-0.15f*SCRHEIGHT, 0.075f*SCRHEIGHT, 1.0f, 1.0f, 0.0f);
        sb.draw(metalpole, cam.position.x-SCRWIDTH/2-0.075f*SCRHEIGHT, cam.position.y-SCRHEIGHT/2, SCRWIDTH/2, 0.0375f*SCRHEIGHT, SCRWIDTH-0.15f*SCRHEIGHT, 0.075f*SCRHEIGHT, 1.0f, 1.0f, 180.0f);
        sb.draw(metalpole, cam.position.x-SCRWIDTH/2-SCRHEIGHT/2+0.0375f*SCRHEIGHT, cam.position.y-0.0375f*SCRHEIGHT+0.075f*SCRHEIGHT, SCRHEIGHT/2, 0.0375f*SCRHEIGHT, SCRHEIGHT-0.15f*SCRHEIGHT, 0.075f*SCRHEIGHT, 1.0f, 1.0f, 90.0f);
        sb.draw(metalpole, cam.position.x+SCRWIDTH/2-SCRHEIGHT/2-0.0375f*SCRHEIGHT, cam.position.y-0.0375f*SCRHEIGHT-0.075f*SCRHEIGHT, SCRHEIGHT/2, 0.0375f*SCRHEIGHT, SCRHEIGHT-0.15f*SCRHEIGHT, 0.075f*SCRHEIGHT, 1.0f, 1.0f, 270.0f);
        sb.draw(metalcorner, cam.position.x-SCRWIDTH/2, cam.position.y+0.425f*SCRHEIGHT, 0.0375f*SCRHEIGHT, 0.0375f*SCRHEIGHT, 0.075f*SCRHEIGHT, 0.075f*SCRHEIGHT, 1.0f, 1.0f, 180.0f);
        sb.draw(metalcorner, cam.position.x+SCRWIDTH/2-0.075f*SCRHEIGHT, cam.position.y+0.425f*SCRHEIGHT, 0.0375f*SCRHEIGHT, 0.0375f*SCRHEIGHT, 0.075f*SCRHEIGHT, 0.075f*SCRHEIGHT, 1.0f, 1.0f, 90.0f);
        sb.draw(metalcorner, cam.position.x+SCRWIDTH/2-0.075f*SCRHEIGHT, cam.position.y-SCRHEIGHT/2, 0.0375f*SCRHEIGHT, 0.0375f*SCRHEIGHT, 0.075f*SCRHEIGHT, 0.075f*SCRHEIGHT, 1.0f, 1.0f, 0.0f);
        sb.draw(metalcorner, cam.position.x-SCRWIDTH/2, cam.position.y-SCRHEIGHT/2, 0.0375f*SCRHEIGHT, 0.0375f*SCRHEIGHT, 0.075f*SCRHEIGHT, 0.075f*SCRHEIGHT, 1.0f, 1.0f, 270.0f);
        // Draw level names
    	if (fadeOut >= 0.0f) alpha=fadeOut;
    	else if (fadeIn < 1.0f) alpha=fadeIn;
    	else alpha=1.0f;
    	float shift = 0.0f;
        for (int i=0; i<totalOptions; i++) {
        	if (currentOption == i) menuText.setColor(1, 1, 1, alpha);
        	else menuText.setColor(1, 1, 1, alpha/2);

        	glyphLayout.setText(menuText, allOptions[i]);
        	lvlWidth = glyphLayout.width;
        	if (allOptions[i].equalsIgnoreCase("Watch Replay")) shift = 0.6f;
        	if (i==0) menuText.draw(sb, allOptions[i], cam.position.x-0.25f*(SCRWIDTH-0.075f*SCRHEIGHT)-lvlWidth/2, cam.position.y + (1.5f*menuHeight*(totalOptions+1))/2 - 1.5f*(i+0.5f)*menuHeight);
        	else menuText.draw(sb, allOptions[i], cam.position.x-0.25f*(SCRWIDTH-0.075f*SCRHEIGHT)-lvlWidth/2, cam.position.y + (1.5f*menuHeight*(totalOptions+1))/2 - 1.5f*(i+1+shift)*menuHeight);
        }
        String dispText = "";
        if (saveReplay) {
	        // Check if a new character is available
        	if (GameInput.currChar != "") {
        		if ((replayFilename.length() > 0) & (GameInput.currChar == "\b")) replayFilename = replayFilename.substring(0, replayFilename.length() - 1);
        		else if (replayFilename.length() <= 20) replayFilename += GameInput.currChar;
        		GameInput.setCharacter("");
        		fileExists = ReplayVars.CheckExists(replayFilename);
        	}
        	dispText = "Press ESC to return to options\n\nEnter a filename:\n";
        	dispText += replayFilename;
        	if (fileExists) dispText += "\n\nFile exists!";
        } else {
	        // Draw level description
	        menuText.setColor(1, 1, 1, alpha/2);
	        if (GameVars.timerTotal == -1) dispText = "Did not finish\n\n";
	        else if (GameVars.timerTotal>0) {
	        	if (GameVars.worldRecord) dispText = "New World Record!\n";
	        	else if (GameVars.personalBest) dispText = "New Personal Best time!\n";
	        	else dispText = "Your time:\n";
	        	// Apend the time
	        	dispText += GameVars.getTimeString(GameVars.timerTotal) + "\n\n";
	        }
	        if (modeValue == 1) dispText += LevelsListCustom.customLevelTimes[levelNumber+1];
	        else if (modeValue == 2) dispText += LevelsListGame.gameLevelDescr[levelNumber+1];
        }
		//  lvlWidth = menuText.getWrappedBounds(dispText, 0.45f*(SCRWIDTH-0.075f*BikeGame.V_HEIGHT)).height;
		glyphLayout.setText(menuText, dispText);
		lvlWidth = glyphLayout.height;
		menuText.draw(sb, dispText, cam.position.x, cam.position.y+lvlWidth/2, 0.45f*(SCRWIDTH-0.075f*SCRHEIGHT), Align.center, true);
        sb.end();
    }
    
    public void dispose() {
    	if (metalmesh != null) metalmesh = null;
    	if (texture != null) texture.dispose();
    	if (menuText != null) menuText.dispose();
    }

	public void pause() {}

	public void resume() {}
}
