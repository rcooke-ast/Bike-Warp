package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameSounds;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.GameVars;
import com.mygdx.game.utilities.ColorUtils;
import com.mygdx.game.utilities.FileUtils;
import com.mygdx.game.utilities.PolygonOperations;

import java.awt.*;

public class OptionChangeControls extends GameState {
    private static final String[] options = {"Options Menu", "Accelerator", "Brake", "Pull Right", "Pull Left", "Switch Direction", "Bunny Hop", "Nitrous", "Restart"};
    private float SCRWIDTH, SCRHEIGHT;
	private BitmapFont menuText;
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private Sprite metalpole, metalcorner;
    private Texture texture, metalmesh;
    private float uRight, vTop;
    private float menuHeight, menuWidth, lvlWidth;
    private float fadeOut, fadeIn, alpha, fadeTime = 0.5f;
    private int currentOption, numMin, totalOptions;
    private float checkLevels = 0.0f, keyNotAllowedTimer = 0.0f;
    private boolean changeKey;
    private int keyNotAllowed;
    private String displayText = "";

	public OptionChangeControls(GameStateManager gsm) {
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
        scaleVal = 0.25f*(SCRWIDTH-0.075f*SCRHEIGHT)/menuWidth;
        menuText.getData().setScale(scaleVal);
        menuText.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        glyphLayout.setText(menuText, "My");
        menuHeight = glyphLayout.height;
        checkLevels = 0.0f;
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
        changeKey = false;
        keyNotAllowed = 0;
        keyNotAllowedTimer = 0.0f;
    }

    public void handleInput() {
        if (changeKey) {
            // Check if a new character is available
            int currKey = GameInput.GetKeyPress();
            if (GameInput.isPressed(GameInput.KEY_ESC)) {
                fadeOut=1.0f; // Return to Main Menu
                BikeGameSounds.PlayMenuSelect();
            } else if (currKey != -1) {
                if ((currKey >= 0) && (currKey <= 255)) {
                    boolean allowed = GameVars.SetPlayerControls(currentOption-1, currKey);
                    if (!allowed) keyNotAllowed = 2;
                } else {
                    keyNotAllowed = 1;
                }
                changeKey = false;
            }
            // No need to check the rest if we're entering a new character.
            return;
        }
        if (GameInput.isPressed(GameInput.KEY_UP)) {
            currentOption--;
            if (currentOption < 0) currentOption = totalOptions-1;
            BikeGameSounds.PlayMenuSwitch();
        } else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
            currentOption++;
            if (currentOption >= totalOptions) currentOption = 0;
            BikeGameSounds.PlayMenuSwitch();
        } else if (GameInput.isPressed(GameInput.KEY_ESC)) {
            fadeOut=1.0f; // Return to Options Menu
            BikeGameSounds.PlayMenuSelect();
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut==-1.0f)) {
            if (currentOption==0) {
                fadeOut=1.0f; // Return to Options Menu
                BikeGameSounds.PlayMenuSelect();
            } else {
                // Changing the key
                changeKey = true;
            }
        } else if (fadeOut==0.0f) {
            fadeOut=-1.0f;
            gsm.setState(GameStateManager.PEEK, false, "none", currentOption-1, 2);
            checkLevels=0.0f;
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
        // Show key not allowed message
        if (keyNotAllowed != 0) {
            keyNotAllowedTimer += dt;
            if (keyNotAllowedTimer > 1.0f) {
                keyNotAllowedTimer = 0.0f;
                keyNotAllowed = 0;
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
        for (int i=0; i<totalOptions; i++) {
            if (currentOption == i) menuText.setColor(1, 1, 1, alpha);
            else menuText.setColor(1, 1, 1, alpha/2);
            glyphLayout.setText(menuText, options[i]);
            lvlWidth = glyphLayout.width;
            menuText.draw(sb, options[i], cam.position.x-0.25f*(SCRWIDTH-0.075f*SCRHEIGHT)-lvlWidth/2, cam.position.y + (1.5f*menuHeight*totalOptions)/2 - 1.5f*i*menuHeight);
        }
        // Draw level description
        menuText.setColor(1, 1, 1, alpha/2);
        if (keyNotAllowed == 1) {
            displayText = "That key is not allowed";
        } else if (keyNotAllowed == 2) {
            displayText = "That key is already in use";
        } else if (changeKey) {
            displayText = "Press any key to set the " + options[currentOption].toLowerCase() + " key";
        } else {
            if (currentOption == 0) displayText = "Press enter to return to the options menu";
            else displayText = "Press enter to change the " + options[currentOption].toLowerCase() + " key.\n\nThe current key is:\n" + Input.Keys.toString(GameVars.plyrControls.get(GameVars.currentPlayer)[currentOption-1]);
        }
        glyphLayout.setText(menuText, displayText);
        lvlWidth = glyphLayout.height;
        menuText.draw(sb, displayText, cam.position.x, cam.position.y+lvlWidth/2, 0.45f*(SCRWIDTH-0.075f*SCRHEIGHT), Align.center, true);
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
