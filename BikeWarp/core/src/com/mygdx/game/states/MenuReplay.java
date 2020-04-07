package com.mygdx.game.states;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.GameVars;
import com.mygdx.game.handlers.LevelsListGame;
import com.mygdx.game.handlers.LevelsListTraining;
import com.mygdx.game.handlers.ReplayVars;
import com.mygdx.game.utilities.EditorIO;

public class MenuReplay extends GameState {
    private int currentOption, numReplayShow, numMin, numOptions;
    private Sprite background;
    private BitmapFont question, replayList;
    private float qWidth, qHeight, SCRWIDTH, sheight, replayWidth, replayHeight, optWidth;
    private float fadeIn, fadeOut, alpha, fadeTime = 0.5f;
    private final String header = "Select a replay to watch";
    private String newName = "";
    private boolean createPlayer = false;
    private ArrayList<String> replayFiles;

	public MenuReplay(GameStateManager gsm) {
		super(gsm);
		// Create the canvas
        create();
	}

    public void create() {
    	// First load the list of replays
    	replayFiles = ReplayVars.GetReplayList();
    	// Setup the canvas
        float SCTOSCRW = ((float) Gdx.graphics.getHeight()*Gdx.graphics.getDesktopDisplayMode().width)/((float) Gdx.graphics.getDesktopDisplayMode().height);
        SCRWIDTH = SCTOSCRW/BikeGame.SCALE;
		sheight = 0.7f*BikeGame.V_HEIGHT;
        background = new Sprite(BikeGameTextures.LoadTexture("sky_mars",2));
        // Grab the bitmap fonts
        question = new BitmapFont(Gdx.files.internal("data/font-48.fnt"), false);
        question.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        question.setColor(1, 1, 1, 1);
        float scaleVal = 0.5f;
        question.setScale(scaleVal);
        qWidth = question.getBounds(header).width;
        if ((qWidth/0.5f) > SCRWIDTH) scaleVal = 0.25f*SCRWIDTH/qWidth;
        question.setScale(scaleVal);
        qWidth = question.getBounds(header).width;
        qHeight = question.getBounds(header).height;
        // Now grab the fonts for the player names
        replayList = new BitmapFont(Gdx.files.internal("data/recordsmenu.fnt"), false);
        replayList.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        scaleVal = 1.0f;
        replayList.setScale(scaleVal);
        replayWidth = replayList.getBounds("Return to Main Menu").width;
        numOptions = 1 + replayFiles.size();
        for (int i=0; i<replayFiles.size(); i++) {
        	if (replayList.getBounds(replayFiles.get(i)).width > replayWidth) replayWidth = replayList.getBounds(replayFiles.get(i)).width;
        }
        scaleVal = 0.25f*(SCRWIDTH-0.075f*BikeGame.V_HEIGHT)/replayWidth;
        replayList.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        replayList.setScale(scaleVal);
        SetNumReplayShow();
        // Set the fading variables
        fadeOut = -1.0f;
        fadeIn = 0.0f;
    }

    public void SetNumReplayShow() {
        replayHeight = replayList.getBounds("My").height;
        numReplayShow = (int) Math.floor(sheight/(1.5f*replayHeight));
        if (numReplayShow > numOptions) numReplayShow = numOptions;    	
    }

    @Override
	public void handleInput() {
    	if (GameInput.isPressed(GameInput.KEY_UP)) {
    		currentOption--;
    		if (currentOption < 0) currentOption = numOptions - 1;
        } else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
    		currentOption++;
    		if (currentOption >= numOptions) currentOption = 0;
        } else if (GameInput.isPressed(GameInput.KEY_ESC)) {
    		fadeOut=1.0f;
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut==-1.0f)) {
        	if (currentOption == 0) {
        		fadeOut=1.0f;
        	} else {
        		// Load the replay
        		ReplayVars.LoadReplay(replayFiles.get(currentOption-1));
        		int mode = ReplayVars.replayMode; // Determine if it's a training or game level
        		int levelNumber = ReplayVars.levelNumber;
        		String levelName = EditorIO.loadLevelPlay(Gdx.files.internal(LevelsListGame.gameLevelFiles[levelNumber+1]));
        		if (mode==3) levelName = EditorIO.loadLevelPlay(Gdx.files.internal(LevelsListTraining.trainingLevelFiles[levelNumber+1]));
        		// Now execute the replay
        		gsm.setState(GameStateManager.PLAY, true, levelName, levelNumber, mode);
        	}
        } else if (fadeOut==0.0f) {
        	// Go to the main menu
    		fadeOut=-1.0f;
    		gsm.setState(GameStateManager.PEEK, false, "none", -1, 0);
        }
    	if ((currentOption>numReplayShow/2) & (currentOption<numOptions-numReplayShow/2)) numMin = currentOption-numReplayShow/2;
    	else if (currentOption<=numReplayShow/2) numMin = 0;
    	else if (currentOption>=numOptions-numReplayShow/2) numMin = numOptions-numReplayShow;
	}

	@Override
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

	@Override
	public void render() {
		String dispText;
    	// clear screen
    	Gdx.gl.glClearColor(0, 0, 0, 1);
    	Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    	Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	sb.setProjectionMatrix(cam.combined);
    	// Create player or display current list of players

    	// Render QUESTION
    	if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, fadeOut);
    	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, fadeIn);
    	else sb.setColor(1, 1, 1, 1); 
        sb.begin();
        // Draw Sky
        sb.draw(background, cam.position.x-SCRWIDTH/2, cam.position.y-BikeGame.V_HEIGHT/2, 0, 0, SCRWIDTH, BikeGame.V_HEIGHT, 1.0f, 1.0f, 0.0f);
        // Draw the text
    	if (fadeOut >= 0.0f) question.setColor(1, 1, 1, fadeOut);
    	else if (fadeIn < 1.0f) question.setColor(1, 1, 1, fadeIn);
    	else question.setColor(1, 1, 1, 1);
        question.draw(sb, header, (SCRWIDTH-qWidth)/2.0f, cam.position.y + (1.5f*replayHeight*numReplayShow)/2 + 1.5f*qHeight);
        // Draw Player Names
    	if (fadeOut >= 0.0f) alpha=fadeOut;
    	else if (fadeIn < 1.0f) alpha=fadeIn;
    	else alpha=1.0f;
        for (int i=numMin; i<numMin+numReplayShow; i++) {
        	// Make the currently selected option a little brighter
        	if (currentOption == i) replayList.setColor(1, 1, 1, alpha);
        	else replayList.setColor(1, 1, 1, alpha/2);
        	// Grab the text to display
        	if (i == 0) dispText = "Return to Main Menu";
        	else dispText = replayFiles.get(i-1);
        	// Render the text
        	optWidth = replayList.getBounds(dispText).width;
        	replayList.draw(sb, dispText, (SCRWIDTH-optWidth)/2.0f, cam.position.y + (1.5f*replayHeight*numReplayShow)/2 - 1.5f*(i-numMin)*replayHeight);
        }
        sb.end();
	}

	@Override
	public void dispose() {
		if (question != null) question.dispose();
    	if (replayList != null) replayList.dispose();
	}

}
