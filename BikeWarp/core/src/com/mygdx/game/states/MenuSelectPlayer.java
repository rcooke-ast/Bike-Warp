package com.mygdx.game.states;

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

public class MenuSelectPlayer extends GameState {
    private int currentOption, numPlyrShow, numMin, numOptions;
    private Sprite background;
    private BitmapFont question, playerList;
    private float qWidth, qHeight, SCRWIDTH, sheight, plyrWidth, plyrHeight, optWidth;
    private float fadeIn, fadeOut, alpha, fadeTime = 0.5f;
    private String header = "Select your player name, or create a new one";
    private String newName = "";
    private boolean createPlayer = false;
   
	public MenuSelectPlayer(GameStateManager gsm) {
		super(gsm);
		// If we have no information on the players, load it
		if (GameVars.currentPlayer == -1) {
			GameVars.LoadPlayers();
		} else {
	        // Set the starting option
	        currentOption = GameVars.GetCurrentPlayer();
		}
		// Create the canvas
        create();
	}

    public void create() {
        float SCTOSCRW = ((float) Gdx.graphics.getHeight()*Gdx.graphics.getDesktopDisplayMode().width)/((float) Gdx.graphics.getDesktopDisplayMode().height);
        SCRWIDTH = SCTOSCRW/BikeGame.SCALE;
		sheight = 0.7f*BikeGame.V_HEIGHT;
        background = new Sprite(BikeGameTextures.LoadTexture("sky_bluesky",2));
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
        playerList = new BitmapFont(Gdx.files.internal("data/recordsmenu.fnt"), false);
        playerList.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        scaleVal = 1.0f;
        playerList.setScale(scaleVal);
        plyrWidth = playerList.getBounds("Create new player").width;
        numOptions = 1 + GameVars.plyrNames.length;
        for (int i=0; i<GameVars.plyrNames.length; i++) {
        	if (playerList.getBounds(GameVars.plyrNames[i]).width > plyrWidth) plyrWidth = playerList.getBounds(GameVars.plyrNames[i]).width;
        }
        scaleVal = 0.25f*(SCRWIDTH-0.075f*BikeGame.V_HEIGHT)/plyrWidth;
        playerList.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        playerList.setScale(scaleVal);
        SetNumPlyrShow();
        // Set the fading variables
        fadeOut = -1.0f;
        fadeIn = 0.0f;
    }

    public void SetNumPlyrShow() {
        plyrHeight = playerList.getBounds("My").height;
        numPlyrShow = (int) Math.floor(sheight/(1.5f*plyrHeight));
        if (numPlyrShow > numOptions) numPlyrShow = numOptions;    	
    }
    
    @Override
	public void handleInput() {
    	boolean exists;
    	if (GameInput.isPressed(GameInput.KEY_UP)) {
    		currentOption--;
    		if (currentOption < 0) currentOption = numOptions - 1;
        } else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
    		currentOption++;
    		if (currentOption >= numOptions) currentOption = 0;
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut==-1.0f)) {
        	if ((currentOption == numOptions-1) & (createPlayer == false)) {
    			createPlayer = true;
        	} else if (createPlayer) {
        		// A player name has been generated
        		if (newName == "") {
        			createPlayer = false;
        		} else {
        			// Check if name already exists
        			exists = false;
        	        for (int i=0; i<GameVars.plyrNames.length; i++) {
        	        	if (GameVars.plyrNames[i] == newName) {
        	        		exists = true;
        	        		GameVars.SetCurrentPlayer(i);
        	        	}
        	        }
        	        if (exists == false) {
        	        	// Player created successfully
        	        	GameVars.AddPlayer(newName);
        	        	SetNumPlyrShow();
        	        	numOptions = 1 + GameVars.plyrNames.length;
        	        	newName = "";
        	        	createPlayer = false;
        	        }
        		}
        		
        	} else {
        		// Set the current player
        		GameVars.SetCurrentPlayer(currentOption);
        		fadeOut=1.0f; // Create a new player
        	}
        } else if (fadeOut==0.0f) {
        	// Go to the main menu
    		fadeOut=-1.0f;
    		gsm.setState(GameStateManager.MAINMENU, false, "none");
        }
    	if ((currentOption>numPlyrShow/2) & (currentOption<numOptions-numPlyrShow/2)) numMin = currentOption-numPlyrShow/2;
    	else if (currentOption<=numPlyrShow/2) numMin = 0;
    	else if (currentOption>=numOptions-numPlyrShow/2) numMin = numOptions-numPlyrShow;
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
    	if (createPlayer) {
    		sb.setColor(1, 1, 1, 1);
	        sb.begin();
	        // Draw Sky
	        sb.draw(background, cam.position.x-SCRWIDTH/2, cam.position.y-BikeGame.V_HEIGHT/2, 0, 0, SCRWIDTH, BikeGame.V_HEIGHT, 1.0f, 1.0f, 0.0f);
	        // Draw the text
	    	if (fadeOut >= 0.0f) question.setColor(1, 1, 1, fadeOut);
	    	else if (fadeIn < 1.0f) question.setColor(1, 1, 1, fadeIn);
	    	else question.setColor(1, 1, 1, 1);
	    	optWidth = playerList.getBounds("Enter your alias:").width;
	        question.draw(sb, "Enter your alias:", (SCRWIDTH-optWidth)/2.0f, cam.position.y + (1.5f*plyrHeight*numPlyrShow)/2 + 1.5f*qHeight);
	        // Check if a new character is available
        	if (GameInput.currChar != "") {
        		if ((newName.length() > 0) & (GameInput.currChar == "\b")) newName = newName.substring(0, newName.length() - 1);
        		else newName += GameInput.currChar;
        		GameInput.currChar = "";
        	}
	        // Draw Player Name
	    	if (fadeOut >= 0.0f) alpha=fadeOut;
	    	else if (fadeIn < 1.0f) alpha=fadeIn;
	    	else alpha=1.0f;
	    	playerList.setColor(1, 1, 1, alpha);
	    	optWidth = playerList.getBounds(newName).width;
	    	playerList.draw(sb, newName, (SCRWIDTH-optWidth)/2.0f, cam.position.y + (1.5f*plyrHeight*numPlyrShow)/2);
	    	sb.end();

		} else {
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
	        question.draw(sb, header, (SCRWIDTH-qWidth)/2.0f, cam.position.y + (1.5f*plyrHeight*numPlyrShow)/2 + 1.5f*qHeight);
	        // Draw Player Names
	    	if (fadeOut >= 0.0f) alpha=fadeOut;
	    	else if (fadeIn < 1.0f) alpha=fadeIn;
	    	else alpha=1.0f;
	        for (int i=numMin; i<numMin+numPlyrShow; i++) {
	        	// Make the currently selected option a little brighter
	        	if (currentOption == i) playerList.setColor(1, 1, 1, alpha);
	        	else playerList.setColor(1, 1, 1, alpha/2);
	        	// Grab the text to display
	        	if (i == numOptions-1) dispText = "Create new player";
	        	else dispText = GameVars.plyrNames[i];
	        	// Render the text
	        	optWidth = playerList.getBounds(dispText).width;
	        	playerList.draw(sb, dispText, (SCRWIDTH-optWidth)/2.0f, cam.position.y + (1.5f*plyrHeight*numPlyrShow)/2 - 1.5f*(i-numMin)*plyrHeight);
	        }
	        sb.end();
		}
	}

	@Override
	public void dispose() {
		if (question != null) question.dispose();
    	if (playerList != null) playerList.dispose();
	}

}
