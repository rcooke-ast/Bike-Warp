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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.GameVars;
import com.mygdx.game.handlers.LevelsListGame;
import com.mygdx.game.handlers.ReplayVars;
import com.mygdx.game.utilities.EditorIO;

/**
 *
 * @author rcooke
 */
public class LevelOptions extends GameState {
	private float SCRWIDTH;
	private BitmapFont menuText;
    private Sprite metalpole, metalcorner;
    private Texture texture, metalmesh;
    private float uRight, vTop, sheight;
    private float menuHeight, menuWidth, lvlWidth;
    private float fadeOut, fadeIn, alpha, fadeTime = 0.5f;
    private int levelNumber, currentOption, totalOptions;
    private float checkLevels = 0.0f;
    private String[] allOptions;

    public LevelOptions(GameStateManager gsm, int levNum) {
        super(gsm);
    	levelNumber = levNum;
    	ReplayVars.Reset();
        create();
    }
    
    public void create() {
		SCRWIDTH = ((float) BikeGame.V_HEIGHT*Gdx.graphics.getDesktopDisplayMode().width)/((float) Gdx.graphics.getDesktopDisplayMode().height);
		sheight = 0.7f*BikeGame.V_HEIGHT;
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
        vTop= BikeGame.V_HEIGHT * ratio / metalmesh.getHeight();
        // Load the black metal pole and the corner
        metalpole = new Sprite(BikeGameTextures.LoadTexture("metalpole_black",1));
        metalcorner = new Sprite(BikeGameTextures.LoadTexture("metalpole_blackcorner",1));
        // Set the starting option
        currentOption = 1;
        fadeOut = -1.0f;
        fadeIn = 0.0f;
    }

    private void SetTotalOptions() {
    	allOptions = new String[] {LevelsListGame.gameLevelNames[levelNumber+1], "Play level", "Select another level"};
    	String[] tmp;
    	// Can skip level
    	if ((GameVars.CanSkip()) & (GameVars.GetLevelStatus(levelNumber) == 0)) {
    		tmp = new String[allOptions.length+1];
    		for (int ii=0; ii<allOptions.length; ii++) {
    			tmp[ii] = allOptions[ii];
    		}
    		tmp[allOptions.length] = "Skip Level";
    		allOptions = tmp.clone();
    	}
    	// Watch or Save replay
    	if (ReplayVars.replayTime.size() != 0) {
    		tmp = new String[allOptions.length+2];
    		for (int ii=0; ii<allOptions.length; ii++) {
    			tmp[ii] = allOptions[ii];
    		}
    		tmp[allOptions.length] = "Watch Replay";
    		tmp[allOptions.length+1] = "Save Replay";
    		allOptions = tmp.clone();
    	}
    	totalOptions = allOptions.length;
    }
    
    private void UpdateMenu() {
        float scaleVal = 1.0f;
        menuText.setScale(scaleVal);
        menuWidth = menuText.getBounds(allOptions[0]).width;
        for (int i=1; i<totalOptions; i++) {
        	if (menuText.getBounds(allOptions[i]).width > menuWidth) menuWidth = menuText.getBounds(allOptions[i]).width;
        }
        scaleVal = 0.25f*(SCRWIDTH-0.075f*BikeGame.V_HEIGHT)/menuWidth;
        menuText.setScale(scaleVal);
        menuText.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        menuHeight = menuText.getBounds("My").height;
    }
    
    public void handleInput() {
    	if (GameInput.isPressed(GameInput.KEY_UP)) {
    		currentOption--;
    		if (currentOption < 1) currentOption = totalOptions-1;
        } else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
    		currentOption++;
    		if (currentOption >= totalOptions) currentOption = 1;
        } else if (GameInput.isPressed(GameInput.KEY_ESC)) {
        	fadeOut=1.0f; // Return to level selector
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut==-1.0f)) {
        	if (currentOption == 1) {
        		// Load the level
        		gsm.setState(GameStateManager.PLAY, true, EditorIO.loadLevelPlay(Gdx.files.internal(LevelsListGame.gameLevelFiles[levelNumber+1])), levelNumber, 2);
        	} else if (currentOption==2) fadeOut=1.0f; // Return to level selector
        	else if (allOptions[currentOption].equalsIgnoreCase("Skip Level")) {
            	GameVars.SetSkipLevel(levelNumber); // Skip this level
            	fadeOut=1.0f; // Return to level selector
        	} else if (allOptions[currentOption] == "Watch Replay"){
        		// Load the replay
        		gsm.setState(GameStateManager.PLAY, true, EditorIO.loadLevelPlay(Gdx.files.internal(LevelsListGame.gameLevelFiles[levelNumber+1])), levelNumber, 4);
        	} else if (allOptions[currentOption] == "Save Replay"){
 	   			ReplayVars.SaveReplay("replay.rpl");
        	}
        } else if (fadeOut==0.0f) {
    		fadeOut=-1.0f;
    		gsm.setState(GameStateManager.PEEK, false, "none", levelNumber, 2);
    		checkLevels=0.0f;
        }
    }
    
    public void update(float dt) {
    	// Always make sure the camera is in the correct location and zoom for this screen
		cam.setToOrtho(false, SCRWIDTH, BikeGame.V_HEIGHT);
		cam.zoom = 1.0f;
    	cam.update();
		handleInput();
		// Update how many levels should be shown
		if (checkLevels > 1.0f) {
			SetTotalOptions();
	    	UpdateMenu();
	    	checkLevels = 0.0f;
		} else checkLevels += dt;
		// Set the fading
    	if (fadeOut > 0.0f) {
    		fadeOut -= dt/fadeTime;
    		if (fadeOut < 0.0f) fadeOut = 0.0f;
    	} else if (fadeIn <= 1.0f) {
    		fadeIn += dt/fadeTime;
    		if (fadeIn > 1.0f) {
    			fadeIn = 2.0f;
    		}
    	}
    }
    
    public void render() {
    	// clear screen
    	Gdx.gl.glClearColor(0, 0, 0, 1);
    	Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    	Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	sb.setProjectionMatrix(cam.combined);
    	if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, fadeOut);
    	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, fadeIn);
    	else sb.setColor(1, 1, 1, 1); 
        sb.begin();
        // Draw metal mesh, pole, and corners
        sb.draw(metalmesh, cam.position.x-SCRWIDTH/2, cam.position.y-BikeGame.V_HEIGHT/2, SCRWIDTH, (float) BikeGame.V_HEIGHT, 0.0f, 0.0f, uRight, vTop);
        sb.draw(metalpole, cam.position.x-SCRWIDTH/2+0.075f*BikeGame.V_HEIGHT, cam.position.y+0.425f*BikeGame.V_HEIGHT, 0, 0, SCRWIDTH-0.15f*BikeGame.V_HEIGHT, 0.075f*BikeGame.V_HEIGHT, 1.0f, 1.0f, 0.0f);        	
        sb.draw(metalpole, cam.position.x-SCRWIDTH/2-0.075f*BikeGame.V_HEIGHT, cam.position.y-BikeGame.V_HEIGHT/2, SCRWIDTH/2, 0.0375f*BikeGame.V_HEIGHT, SCRWIDTH-0.15f*BikeGame.V_HEIGHT, 0.075f*BikeGame.V_HEIGHT, 1.0f, 1.0f, 180.0f);        	
        sb.draw(metalpole, cam.position.x-SCRWIDTH/2-BikeGame.V_HEIGHT/2+0.0375f*BikeGame.V_HEIGHT, cam.position.y-0.0375f*BikeGame.V_HEIGHT+0.075f*BikeGame.V_HEIGHT, BikeGame.V_HEIGHT/2, 0.0375f*BikeGame.V_HEIGHT, BikeGame.V_HEIGHT-0.15f*BikeGame.V_HEIGHT, 0.075f*BikeGame.V_HEIGHT, 1.0f, 1.0f, 90.0f);        	
        sb.draw(metalpole, cam.position.x+SCRWIDTH/2-BikeGame.V_HEIGHT/2-0.0375f*BikeGame.V_HEIGHT, cam.position.y-0.0375f*BikeGame.V_HEIGHT-0.075f*BikeGame.V_HEIGHT, BikeGame.V_HEIGHT/2, 0.0375f*BikeGame.V_HEIGHT, BikeGame.V_HEIGHT-0.15f*BikeGame.V_HEIGHT, 0.075f*BikeGame.V_HEIGHT, 1.0f, 1.0f, 270.0f);        	
        sb.draw(metalcorner, cam.position.x-SCRWIDTH/2, cam.position.y+0.425f*BikeGame.V_HEIGHT, 0.0375f*BikeGame.V_HEIGHT, 0.0375f*BikeGame.V_HEIGHT, 0.075f*BikeGame.V_HEIGHT, 0.075f*BikeGame.V_HEIGHT, 1.0f, 1.0f, 180.0f);
        sb.draw(metalcorner, cam.position.x+SCRWIDTH/2-0.075f*BikeGame.V_HEIGHT, cam.position.y+0.425f*BikeGame.V_HEIGHT, 0.0375f*BikeGame.V_HEIGHT, 0.0375f*BikeGame.V_HEIGHT, 0.075f*BikeGame.V_HEIGHT, 0.075f*BikeGame.V_HEIGHT, 1.0f, 1.0f, 90.0f);
        sb.draw(metalcorner, cam.position.x+SCRWIDTH/2-0.075f*BikeGame.V_HEIGHT, cam.position.y-BikeGame.V_HEIGHT/2, 0.0375f*BikeGame.V_HEIGHT, 0.0375f*BikeGame.V_HEIGHT, 0.075f*BikeGame.V_HEIGHT, 0.075f*BikeGame.V_HEIGHT, 1.0f, 1.0f, 0.0f);
        sb.draw(metalcorner, cam.position.x-SCRWIDTH/2, cam.position.y-BikeGame.V_HEIGHT/2, 0.0375f*BikeGame.V_HEIGHT, 0.0375f*BikeGame.V_HEIGHT, 0.075f*BikeGame.V_HEIGHT, 0.075f*BikeGame.V_HEIGHT, 1.0f, 1.0f, 270.0f);
        // Draw level names
    	if (fadeOut >= 0.0f) alpha=fadeOut;
    	else if (fadeIn < 1.0f) alpha=fadeIn;
    	else alpha=1.0f;
        for (int i=0; i<totalOptions; i++) {
        	if (currentOption == i) menuText.setColor(1, 1, 1, alpha);
        	else menuText.setColor(1, 1, 1, alpha/2);
        	lvlWidth = menuText.getBounds(allOptions[i]).width;
        	if (i==0) menuText.draw(sb, allOptions[i], cam.position.x-0.25f*(SCRWIDTH-0.075f*BikeGame.V_HEIGHT)-lvlWidth/2, cam.position.y + (1.5f*menuHeight*(totalOptions+1))/2 - 1.5f*(i+0.5f)*menuHeight);
        	else menuText.draw(sb, allOptions[i], cam.position.x-0.25f*(SCRWIDTH-0.075f*BikeGame.V_HEIGHT)-lvlWidth/2, cam.position.y + (1.5f*menuHeight*(totalOptions+1))/2 - 1.5f*(i+1)*menuHeight);
        }
        // Draw level description
        menuText.setColor(1, 1, 1, alpha/2);
        lvlWidth = menuText.getWrappedBounds(LevelsListGame.gameLevelDescr[levelNumber+1], 0.45f*(SCRWIDTH-0.075f*BikeGame.V_HEIGHT)).height;
        menuText.drawWrapped(sb, LevelsListGame.gameLevelDescr[levelNumber+1], cam.position.x, cam.position.y + lvlWidth/2, 0.45f*(SCRWIDTH-0.075f*BikeGame.V_HEIGHT));
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
