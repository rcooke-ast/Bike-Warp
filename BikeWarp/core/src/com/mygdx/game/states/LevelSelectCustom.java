/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.states;

import java.util.ArrayList;

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
import com.mygdx.game.handlers.LevelsListCustom;
import com.mygdx.game.handlers.LevelsListTraining;
import com.mygdx.game.utilities.EditorIO;

/**
 *
 * @author rcooke
 */
public class LevelSelectCustom extends GameState {
	private float SCRWIDTH;
	private BitmapFont menuText;
    private Sprite metalpole, metalcorner;
    private Texture texture, metalmesh;
    private float uRight, vTop, sheight;
    private float menuHeight, menuWidth, lvlWidth;
    private float fadeOut, fadeIn, alpha, fadeTime = 0.5f;
    private int currentOption, numMin, numLevShow, totalItems;

    public LevelSelectCustom(GameStateManager gsm) {
        super(gsm);
        create();
    }
    
    public void create() {
    	// Initialise the custom levels
    	LevelsListCustom.initialise();
		SCRWIDTH = ((float) BikeGame.V_HEIGHT*Gdx.graphics.getDesktopDisplayMode().width)/((float) Gdx.graphics.getDesktopDisplayMode().height);
		sheight = 0.7f*BikeGame.V_HEIGHT;
		// Initialise custom names
        totalItems = LevelsListCustom.NUMCUSTOMLEVELS + 1;
        // Menu text
        menuText = new BitmapFont(Gdx.files.internal("data/recordsmenu.fnt"), false);
        float scaleVal = 1.0f;
        menuText.setScale(scaleVal);
        menuWidth = menuText.getBounds(LevelsListCustom.customLevelNames[0]).width;
        for (int i=1; i<totalItems; i++) {
        	if (menuText.getBounds(LevelsListCustom.customLevelNames[i]).width > menuWidth) menuWidth = menuText.getBounds(LevelsListCustom.customLevelNames[i]).width;
        }
        scaleVal = 0.25f*(SCRWIDTH-0.075f*BikeGame.V_HEIGHT)/menuWidth;
        menuText.setScale(scaleVal);
        menuText.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        menuHeight = menuText.getBounds("My").height;
        numLevShow = (int) Math.floor(sheight/(1.5f*menuHeight));
        if (numLevShow > totalItems) numLevShow = totalItems;
        // Load the background metal grid
        metalmesh = BikeGameTextures.LoadTexture("metal_grid",1);
        float ratio = 4.0f;
        uRight = SCRWIDTH * ratio / metalmesh.getWidth();
        vTop= BikeGame.V_HEIGHT * ratio / metalmesh.getHeight();
        // Load the black metal pole and the corner
        metalpole = new Sprite(BikeGameTextures.LoadTexture("metalpole_black",1));
        metalcorner = new Sprite(BikeGameTextures.LoadTexture("metalpole_blackcorner",1));
        // Set the starting option
        currentOption = 0;
        numMin = 0;
        fadeOut = -1.0f;
        fadeIn = 0.0f;
    }

    public void handleInput() {
    	if (GameInput.isPressed(GameInput.KEY_UP)) {
    		currentOption--;
    		if (currentOption < 0) currentOption = totalItems-1;
        } else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
    		currentOption++;
    		if (currentOption >= totalItems) currentOption = 0;
        } else if (GameInput.isPressed(GameInput.KEY_ESC)) {
        	fadeOut=1.0f; // Return to Main Menu
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut==-1.0f)) {
        	if (currentOption==0) fadeOut=1.0f; // Return to Main Menu
        	else {
        		// Load the level
        		gsm.setState(GameStateManager.LEVELOPTIONS, true, "", currentOption-1, 1);
        		//gsm.setState(GameStateManager.PLAY, true, EditorIO.loadLevelPlay(Gdx.files.internal(LevelsListTraining.trainingLevelFiles[currentOption])), currentOption-1, 1);
        	}
        } else if (fadeOut==0.0f) {
    		fadeOut=-1.0f;
    		gsm.setState(GameStateManager.PEEK, false, "none", currentOption-1, 1);
        }
    	//if (currentOption == 1) currentLevelTxt = "";
    	if ((currentOption>numLevShow/2) & (currentOption<totalItems-numLevShow/2)) numMin = currentOption-numLevShow/2;
    	else if (currentOption<=numLevShow/2) numMin = 0;
    	else if (currentOption>=totalItems-numLevShow/2) numMin = totalItems-numLevShow;
    }
    
    public void update(float dt) {
    	// Always make sure the camera is in the correct location and zoom for this screen
		cam.setToOrtho(false, SCRWIDTH, BikeGame.V_HEIGHT);
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
        for (int i=numMin; i<numMin+numLevShow; i++) {
        	if (currentOption == i) menuText.setColor(1, 1, 1, alpha);
        	else menuText.setColor(1, 1, 1, alpha/2);
        	lvlWidth = menuText.getBounds(LevelsListCustom.customLevelNames[i]).width;
        	menuText.draw(sb, LevelsListCustom.customLevelNames[i], cam.position.x-0.25f*(SCRWIDTH-0.075f*BikeGame.V_HEIGHT)-lvlWidth/2, cam.position.y + (1.5f*menuHeight*numLevShow)/2 - 1.5f*(i-numMin)*menuHeight);
        }
        // Draw level description
//        menuText.setColor(1, 1, 1, alpha/2);
//        lvlWidth = menuText.getWrappedBounds(LevelsListTraining.trainingLevelDescr[currentOption], 0.45f*(SCRWIDTH-0.075f*BikeGame.V_HEIGHT)).height;
//        menuText.drawWrapped(sb, LevelsListTraining.trainingLevelDescr[currentOption], cam.position.x, cam.position.y + lvlWidth/2, 0.45f*(SCRWIDTH-0.075f*BikeGame.V_HEIGHT));
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