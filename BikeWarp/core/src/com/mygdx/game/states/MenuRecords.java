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

public class MenuRecords extends GameState {
	private static final String option1 = "Return to Main Menu";
	private static final String option2 = "Emerald Records";
	private static final String option3 = "Diamond Records";
	private static final String option4 = "Total Time Record";
    private int currentOption;
	private final float poleWidth = 0.03f;
	private Sprite metalpole, metalcorner;
	private Texture metalmesh;
	private float uRight, vTop;
    private BitmapFont question, recordList;
	private static GlyphLayout glyphLayout = new GlyphLayout();
	private float qWidth, qHeight, SCRWIDTH, SCRHEIGHT, sheight, recordWidth, recordHeight, optWidth;
    private float fadeIn, fadeOut, alpha, fadeTime = 0.5f;
    private final String header = "Select the records to display";
	private static final int numOptions = 4;
	private static String dispText = "";

	public MenuRecords(GameStateManager gsm) {
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
        recordList = new BitmapFont(Gdx.files.internal("data/recordsmenu.fnt"), false);
        recordList.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        SetupMenuFont();
        // Set the fading variables
        fadeOut = -1.0f;
        fadeIn = 0.0f;
    }

    private void SetupMenuFont() {
		float scaleVal = 1.0f;
		recordList.getData().setScale(scaleVal);
		glyphLayout.setText(recordList, "My");
		scaleVal = GameVars.textHeight*SCRHEIGHT/glyphLayout.height;
		recordList.getData().setScale(scaleVal);
		glyphLayout.setText(recordList, "My");
		recordWidth = glyphLayout.width;
		recordHeight = glyphLayout.height;
		recordList.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

    @Override
	public void handleInput() {
		if (GameInput.isPressed(GameInput.KEY_UP)) {
			currentOption--;
			if (currentOption < 0) currentOption = numOptions - 1;
			BikeGameSounds.PlayMenuSwitch();
		} else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
			currentOption++;
			if (currentOption >= numOptions) currentOption = 0;
			BikeGameSounds.PlayMenuSwitch();
		} else if ((GameInput.isPressed(GameInput.KEY_ESC)) & (fadeOut == -1.0f)) {
			fadeOut = 1.0f;
			BikeGameSounds.PlayMenuSelect();
		} else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut == -1.0f)) {
			if (currentOption == 0) {
				fadeOut = 1.0f;
				BikeGameSounds.PlayMenuSelect();
			} else if (currentOption == 1) {
				// Display Records for emerald times
				gsm.setState(GameStateManager.MENURECORDSDISPLAY, true, "", -1, 0);
			} else if (currentOption == 2) {
				// Display Records for diamond times
				gsm.setState(GameStateManager.MENURECORDSDISPLAY, true, "", -1, 1);
			} else if (currentOption == 3) {
				// Display Records for Total time
				gsm.setState(GameStateManager.MENURECORDSTOTAL, true, "", -1, 1);
			}
		} else if (fadeOut == 0.0f) {
			// Go to the main menu
			fadeOut = -1.0f;
			gsm.setState(GameStateManager.PEEK, false, "none", -1, 0);
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
        question.draw(sb, header, (0.5f*SCRWIDTH-qWidth)/2.0f, cam.position.y + (1.5f* recordHeight * numOptions)/2 + 1.5f*qHeight);
        // Draw Player Names
    	if (fadeOut >= 0.0f) alpha=fadeOut;
    	else if (fadeIn < 1.0f) alpha=fadeIn;
    	else alpha=1.0f;
        for (int i=0; i<numOptions; i++) {
        	// Make the currently selected option a little brighter
        	if (currentOption == i) recordList.setColor(1, 1, 1, alpha);
        	else recordList.setColor(1, 1, 1, alpha/2);
        	// Grab the text to display
        	if (i == 0) dispText = option1;
        	else if (i == 1) dispText = option2;
			else if (i == 2) dispText = option3;
			else if (i == 3) dispText = option4;
        	// Render the text
			glyphLayout.setText(recordList, dispText);
        	optWidth = glyphLayout.width;
        	recordList.draw(sb, dispText, (0.5f*SCRWIDTH-optWidth)/2.0f, cam.position.y + (1.5f* recordHeight *numOptions)/2 - 1.5f*i* recordHeight);
        }
		sb.end();
	}

	@Override
	public void dispose() {
		if (question != null) question.dispose();
    	if (recordList != null) recordList.dispose();
	}

}
