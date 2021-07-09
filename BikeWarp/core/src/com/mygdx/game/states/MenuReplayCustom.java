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
import com.mygdx.game.BikeGameSounds;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.GameVars;
import com.mygdx.game.handlers.ReplayVars;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MenuReplayCustom extends GameState {
	private static final String option1 = "Return to Replay Menu";
    private int currentOption, numReplayShow, numMin, numOptions;
	private final float poleWidth = 0.03f;
	private Sprite metalpole, metalcorner;
	private Texture metalmesh;
	private float uRight, vTop;
    private BitmapFont question, replayList;
	private static GlyphLayout glyphLayout = new GlyphLayout();
	private float qWidth, qHeight, SCRWIDTH, SCRHEIGHT, sheight, replayWidth, replayHeight, optWidth;
    private float fadeIn, fadeOut, alpha, fadeTime = 0.5f;
    private final String header = "Select a replay to watch";
    private String[] replayFiles;
    private boolean renaming, fileExists;
	private String renameFilename;
	private static final int numExtra = 1;
	private static String dispText = "";

	public MenuReplayCustom(GameStateManager gsm) {
		super(gsm);
		// Create the canvas
        create();
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
        replayList = new BitmapFont(Gdx.files.internal("data/recordsmenu.fnt"), false);
        replayList.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        SetupMenuFont();
		renaming = false;
		fileExists = true;
        // Set the fading variables
        fadeOut = -1.0f;
        fadeIn = 0.0f;
    }

    private void SetNumReplayShow() {
		glyphLayout.setText(replayList, "My");
        replayHeight = glyphLayout.height;
        numReplayShow = (int) Math.floor(sheight/(1.5f*replayHeight));
        if (numReplayShow > numOptions) numReplayShow = numOptions;    	
    }

    private void SetupMenuFont() {
		float scaleVal = 1.0f;
		// First load the list of replays
		replayFiles = ReplayVars.GetReplayList();
		replayList.getData().setScale(scaleVal);
		glyphLayout.setText(replayList, "My");
		scaleVal = GameVars.textHeight*SCRHEIGHT/glyphLayout.height;
		replayList.getData().setScale(scaleVal);
		replayWidth = glyphLayout.width;
		// Do option 1
		glyphLayout.setText(replayList, option1);
		if (glyphLayout.width > replayWidth) replayWidth = glyphLayout.width;
		float tstReplayWidth;
		// Remaining options
		numOptions = numExtra + replayFiles.length;
		for (int i=0; i<replayFiles.length; i++) {
			glyphLayout.setText(replayList, replayFiles[i]);
			tstReplayWidth = glyphLayout.width;
			if (tstReplayWidth > replayWidth) replayWidth = tstReplayWidth;
		}
		float scaling = 0.25f*(SCRWIDTH-poleWidth*SCRHEIGHT)/replayWidth;
		if (scaling < 1) scaleVal *= scaling;
		replayList.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		replayList.getData().setScale(scaleVal);
		SetNumReplayShow();
	}

    @Override
	public void handleInput() {
		if (renaming) {
			if (GameInput.isPressed(GameInput.KEY_ESC)) {
				renaming = false;
				renameFilename = "";
				fileExists = true;
			}
			else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (!fileExists)) {
				// Rename the file
				Path src = Paths.get(ReplayVars.replayDir+replayFiles[currentOption-numExtra]+ReplayVars.replayExt);
				Path dst = Paths.get(ReplayVars.replayDir+renameFilename+ReplayVars.replayExt);
				try {
					Files.move(src, dst, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
				SetupMenuFont();
				renaming = false;
			}
		} else {
			if (GameInput.isPressed(GameInput.KEY_UP)) {
				currentOption--;
				if (currentOption < 0) currentOption = numOptions - 1;
				if (currentOption >= numExtra) ReplayVars.LoadReplay(replayFiles[currentOption - numExtra]);
				BikeGameSounds.PlayMenuSwitch();
			} else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
				currentOption++;
				if (currentOption >= numOptions) currentOption = 0;
				if (currentOption >= numExtra) ReplayVars.LoadReplay(replayFiles[currentOption - numExtra]);
				BikeGameSounds.PlayMenuSwitch();
			} else if ((GameInput.isPressed(GameInput.KEY_ESC)) & (fadeOut == -1.0f)) {
				fadeOut = 1.0f;
				BikeGameSounds.PlayMenuSelect();
			} else if ((GameInput.isPressed(GameInput.KEY_D)) & (fadeOut == -1.0f) & (currentOption >= numExtra)) {
				// Get the name of the file to be deleted
				String delfile = replayFiles[currentOption - numExtra];
				// Check if we need to change the current option
				if (currentOption == numOptions - 1) currentOption--;
				// Delete the file
				File file = new File(Gdx.files.internal("replays/" + delfile + ReplayVars.replayExt).toString());
				file.delete();
				// Finally, check the fonts
				SetupMenuFont();
			} else if ((GameInput.isPressed(GameInput.KEY_R)) & (fadeOut == -1.0f)  & (currentOption >= numExtra) & (!renaming)) {
				// Renaming a file
				GameInput.setCharacter("");
				renaming = true;
				renameFilename = replayFiles[currentOption - numExtra];
			} else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut == -1.0f)) {
				if (currentOption == 0) {
					fadeOut = 1.0f;
					BikeGameSounds.PlayMenuSelect();
				} else if (dispText.compareTo(ReplayVars.ReplayNotFound)!=0) {
					// Load the replay
					ReplayVars.ResetReplayCounter();
					int mode = ReplayVars.currentReplay.replayMode; // Determine if it's a training or game level
					// Now execute the replay
					gsm.setState(GameStateManager.PLAY, true, ReplayVars.currentReplay.levelName, ReplayVars.currentReplay.levelNumber, mode);
				}
			} else if (fadeOut == 0.0f) {
				// Go to the main menu
				fadeOut = -1.0f;
				gsm.setState(GameStateManager.PEEK, false, "none", -1, 0);
			}
			if ((currentOption > numReplayShow / 2) & (currentOption < numOptions - numReplayShow / 2))
				numMin = currentOption - numReplayShow / 2;
			else if (currentOption <= numReplayShow / 2) numMin = 0;
			else if (currentOption >= numOptions - numReplayShow / 2) numMin = numOptions - numReplayShow;
		}
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
		//Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        question.draw(sb, header, (0.5f*SCRWIDTH-qWidth)/2.0f, cam.position.y + (1.5f*replayHeight*numReplayShow)/2 + 1.5f*qHeight);
        // Draw Player Names
    	if (fadeOut >= 0.0f) alpha=fadeOut;
    	else if (fadeIn < 1.0f) alpha=fadeIn;
    	else alpha=1.0f;
        for (int i=numMin; i<numMin+numReplayShow; i++) {
        	// Make the currently selected option a little brighter
        	if (currentOption == i) replayList.setColor(1, 1, 1, alpha);
        	else replayList.setColor(1, 1, 1, alpha/2);
        	// Grab the text to display
        	if (i == 0) dispText = option1;
        	else dispText = replayFiles[i-numExtra];
        	if ((renaming) & (currentOption == i)) {
				if (GameInput.currChar != "") {
					if ((renameFilename.length() > 0) & (GameInput.currChar == "\b")) renameFilename = renameFilename.substring(0, renameFilename.length() - 1);
					else if (renameFilename.length() <= 20) renameFilename += GameInput.currChar;
					GameInput.setCharacter("");
					fileExists = ReplayVars.CheckExists(renameFilename);
				}
				dispText = renameFilename;
			}
        	// Render the text
			glyphLayout.setText(replayList, dispText);
        	optWidth = glyphLayout.width;
        	replayList.draw(sb, dispText, (0.5f*SCRWIDTH-optWidth)/2.0f, cam.position.y + (1.5f*replayHeight*numReplayShow)/2 - 1.5f*(i-numMin)*replayHeight);
        }
        if (currentOption >= numExtra) {
			replayList.setColor(1, 1, 1, alpha);
			if (renaming) {
				dispText = String.format("Renaming Replay\n\nPress %s to cancel\nPress Enter to confirm", GameVars.GetPlayerESCString());
				if (fileExists) dispText += "\n\nFile exists!";
				else dispText += "\n\n";
			} else dispText = ReplayVars.ReplayString(true);
			glyphLayout.setText(replayList, dispText);
			float lvlWidth = glyphLayout.height;
			replayList.draw(sb, dispText, cam.position.x, cam.position.y+lvlWidth/2, 0.45f*(SCRWIDTH-poleWidth*SCRHEIGHT), Align.center, true);
			replayList.draw(sb, dispText, cam.position.x, cam.position.y+lvlWidth/2, 0.45f*(SCRWIDTH-poleWidth*SCRHEIGHT), Align.center, true);
		}
		sb.end();
	}

	@Override
	public void dispose() {
		if (question != null) question.dispose();
    	if (replayList != null) replayList.dispose();
	}

}
