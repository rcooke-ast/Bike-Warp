package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameSounds;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameStateManager;

public class MenuExit extends GameState {
    private int currentOption, action;
    private Sprite background;
    private BitmapFont question, ansYes, ansNo;
	private static GlyphLayout glyphLayout = new GlyphLayout();
    private float qWidth, qHeight, SCRWIDTH, SCRHEIGHT, yWidth, nWidth;
    private float fadeIn, fadeOut, fadeTime = 0.5f;
    //private float finAngle=0.0f, finishRad;
   
	public MenuExit(GameStateManager gsm) {
		super(gsm);
        create();
	}

    public void create() {
		this.game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		SCRWIDTH = BikeGame.viewport.width;
		SCRHEIGHT = BikeGame.viewport.height;
        background = new Sprite(BikeGameTextures.LoadTexture("sky_evening",2));
        fadeOut = -1.0f;
        fadeIn = 0.0f;
        // Load the finish ball textures
        //texture = new Texture(Gdx.files.internal("data/images/finish_whirl.png"));
        //texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        //finishFG = new Sprite(texture);
        // Grab the bitmap fonts
        question = new BitmapFont(Gdx.files.internal("data/font-48.fnt"), false);
        question.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        question.setColor(1, 1, 1, 1);
        float scaleVal = 1.0f;
        question.getData().setScale(scaleVal);
		glyphLayout.setText(question, "Are you sure you want to exit?");
        qWidth = glyphLayout.width;
        if ((qWidth) > 0.5f*SCRWIDTH) scaleVal = 0.5f*SCRWIDTH/qWidth;
        question.getData().setScale(scaleVal);
        glyphLayout.setText(question, "Are you sure you want to exit?");
        qWidth = glyphLayout.width;
        qHeight = glyphLayout.height;
        ansYes = new BitmapFont(Gdx.files.internal("data/font-48.fnt"), false);
        ansYes.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        ansYes.getData().setScale(scaleVal);
		glyphLayout.setText(question, "Yes");
        yWidth = glyphLayout.width;
        ansNo = new BitmapFont(Gdx.files.internal("data/font-48.fnt"), false);
        ansNo.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        ansNo.getData().setScale(scaleVal);
		glyphLayout.setText(question, "No");
        nWidth = glyphLayout.width;
        // Set the starting option
        currentOption = 1;
        //finishRad = scaleVal*SCRWIDTH/4;
    }

    @Override
	public void handleInput() {
    	if ((GameInput.isPressed(GameInput.KEY_UP)) & (fadeOut==-1.0f)) {
    		currentOption--;
    		if (currentOption < 0) currentOption = 1;
    	} else if ((GameInput.isPressed(GameInput.KEY_DOWN)) & (fadeOut==-1.0f)) {
    		currentOption++;
    		if (currentOption > 1) currentOption = 0;
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut==-1.0f)) {
        	action = currentOption;
        	fadeOut=1.0f;
        } else if (fadeOut==0.0f) {
    		if (action==0) {
    			BikeGameSounds.dispose();
    			BikeGameTextures.dispose();
    			Gdx.app.exit(); // Exit game
    		}
    		else gsm.setState(GameStateManager.PEEK, false, "none", -1, 0); // Return to menu
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
        // Draw Sky
        sb.draw(background, cam.position.x-SCRWIDTH/2, cam.position.y-SCRHEIGHT/2, 0, 0, SCRWIDTH, SCRHEIGHT, 1.0f, 1.0f, 0.0f);
        // Draw Exit Ball
        //sb.draw(finishFG, cam.position.x-finishRad, cam.position.y-finishRad, finishRad, finishRad, 2.0f*finishRad, 2.0f*finishRad, 1.0f, 1.0f, finAngle);
 	   	//finAngle += 5.0f;
 	   	//if (finAngle >= 360.0f) finAngle -= 360.0f;
        // Draw the text
    	if (fadeOut >= 0.0f) question.setColor(1, 1, 1, fadeOut);
    	else if (fadeIn < 1.0f) question.setColor(1, 1, 1, fadeIn);
    	else question.setColor(1, 1, 1, 1);
        question.draw(sb, "Are you sure you want to exit?", (SCRWIDTH-qWidth)/2.0f,(SCRHEIGHT+4.0f*qHeight)/2.0f);
        // Render YES
        if (currentOption == 1) {
        	if (fadeOut >= 0.0f) ansYes.setColor(1, 1, 1, fadeOut*0.5f);
        	else if (fadeIn < 1.0f) ansYes.setColor(1, 1, 1, fadeIn*0.5f);
        	else ansYes.setColor(1, 1, 1, 0.5f);
        } else {
        	if (fadeOut >= 0.0f) ansYes.setColor(1, 1, 1, fadeOut);
        	else if (fadeIn < 1.0f) ansYes.setColor(1, 1, 1, fadeIn);
        	else ansYes.setColor(1, 1, 1, 1);
        }
        ansYes.draw(sb, "Yes", (SCRWIDTH-yWidth)/2.0f,(SCRHEIGHT+qHeight)/2.0f);
        // Render NO
        if (currentOption == 0) {
        	if (fadeOut >= 0.0f) ansNo.setColor(1, 1, 1, fadeOut*0.5f);
        	else if (fadeIn < 1.0f) ansNo.setColor(1, 1, 1, fadeIn*0.5f);
        	else ansNo.setColor(1, 1, 1, 0.5f);
        } else {
        	if (fadeOut >= 0.0f) ansNo.setColor(1, 1, 1, fadeOut);
        	else if (fadeIn < 1.0f) ansNo.setColor(1, 1, 1, fadeIn);
        	else ansNo.setColor(1, 1, 1, 1);
        }
        ansNo.draw(sb, "No", (SCRWIDTH-nWidth)/2.0f,(SCRHEIGHT-qHeight)/2.0f);
        sb.end();
	}

	@Override
	public void dispose() {
		if (question != null) question.dispose();
		if (ansYes != null) ansYes.dispose();
		if (ansNo != null) ansNo.dispose();
	}

}
