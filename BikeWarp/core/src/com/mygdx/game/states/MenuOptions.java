/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.states;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameSounds;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.GameVars;
import com.mygdx.game.handlers.LevelsListGame;

/**
 *
 * @author rcooke
 */
public class MenuOptions extends GameState {
    private static final String[] options = {"Main Menu", "Change Bike Colour", "Change Controls", "Change HUD", "Change Display"};
	private float SCRWIDTH, SCRHEIGHT;
	private BitmapFont menuText;
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private final float poleWidth = 0.03f;
    private Sprite metalpole, metalcorner;
    private Texture metalmesh;
    private float uRight, vTop;
    private float menuHeight, menuWidth, lvlWidth;
    private float fadeOut, fadeIn, alpha, fadeTime = 0.5f;
    private int currentOption, totalOptions;
    private String displayText = "", fsText="";

    public MenuOptions(GameStateManager gsm) {
        super(gsm);
        create();
    }
    
    public void create() {
        this.game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        SCRWIDTH = BikeGame.viewport.width;
        SCRHEIGHT = BikeGame.viewport.height;
        // Menu text
        menuText = new BitmapFont(Gdx.files.internal("data/recordsmenu.fnt"), false);
        // Set text heights
        float scaleVal = 1.0f;
        menuText.getData().setScale(scaleVal);
        glyphLayout.setText(menuText, "XXXXXXXXXXXXXXX");
        menuWidth = glyphLayout.width;
        float tstMenuWidth;
        for (int i = 0; i< totalOptions; i++) {
            glyphLayout.setText(menuText, options[i]);
            tstMenuWidth = glyphLayout.width;
            if (tstMenuWidth > menuWidth) menuWidth = tstMenuWidth;
        }
        scaleVal = 0.25f*(SCRWIDTH-poleWidth*SCRHEIGHT)/menuWidth;
        menuText.getData().setScale(scaleVal);
        menuText.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        glyphLayout.setText(menuText, "My");
        menuHeight = glyphLayout.height;
        // Load the background metal grid
        metalmesh = BikeGameTextures.LoadTexture("metal_grid");
        float ratio = 4.0f;
        uRight = SCRWIDTH * ratio / metalmesh.getWidth();
        vTop= SCRHEIGHT * ratio / metalmesh.getHeight();
        // Load the black metal pole and the corner
        metalpole = new Sprite(BikeGameTextures.LoadTexture("metalpole_black"));
        metalcorner = new Sprite(BikeGameTextures.LoadTexture("metalpole_blackcorner"));
        // Set the starting option
        currentOption = 0;
        fadeOut = -1.0f;
        fadeIn = 0.0f;
        totalOptions = options.length;
    }

    public void handleInput() {
    	if (GameInput.isPressed(GameInput.KEY_UP)) {
    		currentOption--;
    		if (currentOption < 0) currentOption = totalOptions -1;
            BikeGameSounds.PlayMenuSwitch();
        } else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
    		currentOption++;
    		if (currentOption >= totalOptions) currentOption = 0;
            BikeGameSounds.PlayMenuSwitch();
        } else if (GameInput.isPressed(GameInput.KEY_ESC)) {
        	fadeOut=1.0f; // Return to Main Menu
            BikeGameSounds.PlayMenuSelect();
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut==-1.0f)) {
        	if (currentOption==0) {
        	    fadeOut=1.0f; // Return to Main Menu
                BikeGameSounds.PlayMenuSelect();
            } else if (currentOption==1) {
                gsm.setState(GameStateManager.MENUOPTIONSCOLOR, true, "", currentOption-1, 2);
                BikeGameSounds.PlayMenuSelect();
            } else if (currentOption==2) {
                gsm.setState(GameStateManager.MENUOPTIONSCONTROLS, true, "", currentOption-1, 2);
                BikeGameSounds.PlayMenuSelect();
            } else if (currentOption==3) {
                gsm.setState(GameStateManager.MENUOPTIONSHUDDISP, true, "", currentOption - 1, 2);
                BikeGameSounds.PlayMenuSelect();
            } else if (currentOption==4) {
                GameVars.SetPlayerFullscreen(!GameVars.GetPlayerFullscreen());
                BikeGame.UpdateDisplay();
                sb.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        } else if (fadeOut==0.0f) {
    		fadeOut=-1.0f;
    		gsm.setState(GameStateManager.PEEK, false, "none", currentOption-1, 2);
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
        for (int i=0; i<totalOptions; i++) {
        	if (currentOption == i) menuText.setColor(1, 1, 1, alpha);
        	else menuText.setColor(1, 1, 1, alpha/2);
        	glyphLayout.setText(menuText, options[i]);
        	lvlWidth = glyphLayout.width;
        	menuText.draw(sb, options[i], cam.position.x-0.25f*(SCRWIDTH-poleWidth*SCRHEIGHT)-lvlWidth/2, cam.position.y + (1.5f*menuHeight*totalOptions)/2 - 1.5f*i*menuHeight);
        }
        // Draw level description
        menuText.setColor(1, 1, 1, alpha/2);
        if (GameVars.GetPlayerFullscreen()) fsText = "Fullscreen";
        else fsText = "Windowed";
        if (currentOption == 0) displayText = "Press enter to return to the main menu";
        else if (currentOption == 1) displayText = "Press enter to set the bike colour";
        else if (currentOption == 2) displayText = "Press enter to set the controls";
        else if (currentOption == 3) displayText = "Press enter to change the HUD display";
        else if (currentOption == 4) displayText = "Press enter to toggle fullscreen/windowed:\nCurrent mode: "+fsText;
        else displayText = "Press enter to change the " + options[currentOption].replace("Change ", "").toLowerCase() + " key.\n\nThe current key is:\n" + Input.Keys.toString(GameVars.plyrControls.get(GameVars.currentPlayer)[currentOption-2]);
        glyphLayout.setText(menuText, displayText);
        lvlWidth = glyphLayout.height;
        menuText.draw(sb, displayText, cam.position.x, cam.position.y+lvlWidth/2, 0.45f*(SCRWIDTH-poleWidth*SCRHEIGHT), Align.center, true);
        sb.end();
    }
    
    public void dispose() {
    	if (metalmesh != null) metalmesh = null;
    	if (menuText != null) menuText.dispose();
    }

	public void pause() {}

	public void resume() {}
}
