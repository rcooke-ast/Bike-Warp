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
import com.codedisaster.steamworks.SteamAPI;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameSounds;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.*;
import com.mygdx.game.utilities.EditorIO;

/**
 *
 * @author rcooke
 */
public class LevelSelectGame extends GameState {
	private float SCRWIDTH, SCRHEIGHT;
	private BitmapFont menuText;
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private final float poleWidth = 0.03f;
    private Sprite metalpole, metalcorner;
    private Texture metalmesh;
    private float uRight, vTop, sheight;
    private float menuHeight, menuWidth, lvlWidth;
    private float fadeOut, fadeIn, alpha, fadeTime = 0.5f;
    private int numMin, numLevShow, totalLevels;
    private static int currentLevel;
    private String dispText = "";

    public LevelSelectGame(GameStateManager gsm) {
        super(gsm);
        create();
    }
    
    public void create() {
        this.game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        SCRWIDTH = BikeGame.viewport.width;
        SCRHEIGHT = BikeGame.viewport.height;
		sheight = 0.7f*SCRHEIGHT;
        // Menu text
        menuText = new BitmapFont(Gdx.files.internal("data/recordsmenu.fnt"), false);
        // Load the background metal grid
        metalmesh = BikeGameTextures.LoadTexture("metal_grid");
        float ratio = 4.0f;
        uRight = SCRWIDTH * ratio / metalmesh.getWidth();
        vTop= SCRHEIGHT * ratio / metalmesh.getHeight();
        // Load the black metal pole and the corner
        metalpole = new Sprite(BikeGameTextures.LoadTexture("metalpole_black"));
        metalcorner = new Sprite(BikeGameTextures.LoadTexture("metalpole_blackcorner"));
        // Set the starting option
        currentLevel = 0;
        numMin = 0;
        // Update the menu
        UpdateMenu();
        fadeOut = -1.0f;
        fadeIn = 0.0f;
    }

    public static void SetCurrentLevel(int lev) {currentLevel=lev;}

    private void UpdateMenu() {
    	totalLevels = GameVars.GetNumLevels();
        float scaleVal = 1.0f;
        menuText.getData().setScale(scaleVal);
        glyphLayout.setText(menuText, "XXXXXXXXXXXXXXX");
        menuWidth = glyphLayout.width;
        float tstMenuWidth;
        for (int i=0; i<totalLevels; i++) {
            glyphLayout.setText(menuText, LevelsListGame.gameLevelNames[i]);
            tstMenuWidth = glyphLayout.width;
        	if (tstMenuWidth > menuWidth) menuWidth = tstMenuWidth;
        }
        scaleVal = 0.25f*(SCRWIDTH-poleWidth*SCRHEIGHT)/menuWidth;
        menuText.getData().setScale(scaleVal);
        menuText.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        glyphLayout.setText(menuText, "My");
        menuHeight = glyphLayout.height;
        SetNumLevShow();
        if (currentLevel != 0) {
            SteamVars.LoadPBWR(currentLevel);
            SteamVars.RecordString();
        }
    }
    
    private void SetNumLevShow() {
        numLevShow = (int) Math.floor(sheight/(1.5f*menuHeight));
        if (numLevShow > totalLevels) numLevShow = totalLevels;
    }

    public void handleInput() {
    	if (GameInput.isPressed(GameInput.KEY_UP)) {
    		currentLevel--;
    		if (currentLevel < 0) currentLevel = totalLevels-1;
    		if (currentLevel>=1) SteamVars.LoadPBWR(currentLevel);
            BikeGameSounds.PlayMenuSwitch();
        } else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
    		currentLevel++;
    		if (currentLevel >= totalLevels) currentLevel = 0;
            if (currentLevel >= 1) SteamVars.LoadPBWR(currentLevel);
            BikeGameSounds.PlayMenuSwitch();
        } else if (GameInput.isPressed(GameInput.KEY_ESC)) {
        	fadeOut=1.0f; // Return to Main Menu
            BikeGameSounds.PlayMenuSelect();
        } else if ((GameInput.isPressed(GameInput.KEY_S)) & (GameVars.GetLevelStatus(currentLevel-1)==0)) {
            BikeGameSounds.PlayMenuSelect();
        	GameVars.SetSkipLevel(currentLevel-1); // Skip this level
        	totalLevels = GameVars.GetNumLevels();
        	LevelsListGame.updateRecords();
        	UpdateMenu();
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut==-1.0f)) {
        	if (currentLevel==0) {
        	    fadeOut=1.0f; // Return to Main Menu
                BikeGameSounds.PlayMenuSelect();
            }
        	else {
                BikeGameSounds.PlayMenuSelect();
        		// Load the level
        		gsm.setState(GameStateManager.LEVELOPTIONS, true, "", currentLevel-1, 2);
        		//gsm.setState(GameStateManager.PLAY, true, EditorIO.loadLevelPlay(Gdx.files.internal(LevelsListGame.gameLevelFiles[currentOption])), currentOption-1, 2);
        	}
        } else if (fadeOut==0.0f) {
    		fadeOut=-1.0f;
    		gsm.setState(GameStateManager.PEEK, false, "none", currentLevel-1, 2);
        }
    	//if (currentOption == 1) currentLevelTxt = "";
    	if ((currentLevel>numLevShow/2) & (currentLevel<totalLevels-numLevShow/2)) numMin = currentLevel-numLevShow/2;
    	else if (currentLevel<=numLevShow/2) numMin = 0;
    	else if (currentLevel>=totalLevels-numLevShow/2) numMin = totalLevels-numLevShow;
    }
    
    public void update(float dt) {
    	// Always make sure the camera is in the correct location and zoom for this screen
		cam.setToOrtho(false, SCRWIDTH, SCRHEIGHT);
		cam.zoom = 1.0f;
    	cam.update();
		handleInput();
		// Update how many levels should be shown
	    UpdateMenu();
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
        Gdx.gl.glViewport((int) BikeGame.viewport.x, (int) BikeGame.viewport.y, (int) BikeGame.viewport.width, (int) BikeGame.viewport.height);
        //Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	sb.setProjectionMatrix(cam.combined);
    	if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, fadeOut);
    	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, fadeIn);
    	else sb.setColor(1, 1, 1, 1); 
        sb.begin();
        // Draw metal mesh, pole, and corners
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
        // Draw level names
    	if (fadeOut >= 0.0f) alpha=fadeOut;
    	else if (fadeIn < 1.0f) alpha=fadeIn;
    	else alpha=1.0f;
        for (int i=numMin; i<numMin+numLevShow; i++) {
        	if (currentLevel == i) menuText.setColor(1, 1, 1, alpha);
        	else menuText.setColor(1, 1, 1, alpha/2);
        	glyphLayout.setText(menuText, LevelsListGame.gameLevelNames[i]);
        	lvlWidth = glyphLayout.width;
        	menuText.draw(sb, LevelsListGame.gameLevelNames[i], cam.position.x-0.25f*(SCRWIDTH-poleWidth*SCRHEIGHT)-lvlWidth/2, cam.position.y + (1.5f*menuHeight*numLevShow)/2 - 1.5f*(i-numMin)*menuHeight);
        }
        // Draw level description
        menuText.setColor(1, 1, 1, alpha/2);
        //lvlWidth = menuText.getWrappedBounds(LevelsListGame.gameLevelDescr[currentOption], 0.45f*(SCRWIDTH-poleWidth*BikeGame.V_HEIGHT)).height;
        if (currentLevel == 0) dispText = "Return to the Main Menu (or press Esc).";
        else dispText = SteamVars.currentDisplayString;
        glyphLayout.setText(menuText, dispText);
        lvlWidth = glyphLayout.height;
        menuText.draw(sb, dispText, cam.position.x, cam.position.y+lvlWidth/2, 0.45f*(SCRWIDTH-poleWidth*SCRHEIGHT), Align.center, true);
        sb.end();
    }
    
    public void dispose() {
    	if (metalmesh != null) metalmesh = null;
    	if (menuText != null) menuText.dispose();
    }

	public void pause() {}

	public void resume() {}
}
