/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

/**
 *
 * @author rcooke
 */
public class MenuRecords extends GameState {
	private float SCRWIDTH;
	//private Texture texture;
	private BitmapFont times, textcarve, textcarveglow, menuText;
    private Sprite grass, dirt, menu, sky, stone;
    private float wwidth, wheight, sheight;//, mwidth, mheight, mxcen, mycen, swidth;
    private float dscale, gscale, numScale;
    private float nmbrWidth, timesWidth, timesHeight, carveHeight, menuHeight, menuWidth, lvlWidth;
    private int gnwrap, dnwrapx, dnwrapy;
    private float fadeOut, fadeIn, alpha, fadeTime = 0.5f, levnumTime = 1.0f, levnum;
    private int currentOption, levelNumber, tLevelNumber, totalLevels;

    public MenuRecords(GameStateManager gsm) {
        super(gsm);
        create();
    }
    
    public void create() {
		SCRWIDTH = ((float) BikeGame.V_HEIGHT*Gdx.graphics.getDesktopDisplayMode().width)/((float) Gdx.graphics.getDesktopDisplayMode().height);
        sheight = 0.7f*BikeGame.V_HEIGHT;
        // Text for Record Times
        times = new BitmapFont(Gdx.files.internal("data/digital-dream-bold-48.fnt"), false);
        float scaleVal = 1.0f;
        times.setScale(scaleVal);
        timesHeight = times.getBounds("10").height;
        scaleVal = sheight/(15.0f*timesHeight);
        times.setScale(0.5f*scaleVal);
        timesWidth = times.getBounds("00:00:000").width;
        timesHeight = times.getBounds("00:00:000").height;
        times.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        // Menu text
        menuText = new BitmapFont(Gdx.files.internal("data/recordsmenu.fnt"), false);
        scaleVal = 1.0f;
        menuText.setScale(scaleVal);
        menuWidth = menuText.getBounds("National Records").width;
        scaleVal = 0.75f*(0.5f*sheight)/menuWidth;
        menuText.setScale(scaleVal);
        menuText.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        menuHeight = menuText.getBounds("Main Menu").height;
        lvlWidth = menuText.getBounds("Go to Level").width;
        // Carved text
        textcarve = new BitmapFont(Gdx.files.internal("data/records_carve.fnt"), false);
        numScale = 1.0f;
        textcarve.setScale(numScale);
        carveHeight = textcarve.getBounds("10").height;
        numScale = sheight/(15.0f*carveHeight);
        textcarve.setScale(numScale);
        textcarve.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        carveHeight = textcarve.getBounds("10").height;
        nmbrWidth = textcarve.getBounds("10").width;
        // Carved text with a white glow
        textcarveglow = new BitmapFont(Gdx.files.internal("data/records_carveglow.fnt"), false);
        textcarveglow.setScale(numScale);
        textcarveglow.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        // Load the grass
        grass = new Sprite(BikeGameTextures.LoadTexture("grass_smooth_linrep",2));
        dirt = new Sprite(BikeGameTextures.LoadTexture("cracked_dirt_linrep",2));
        sky = new Sprite(BikeGameTextures.LoadTexture("sky_bluesky",2));
        stone = new Sprite(BikeGameTextures.LoadTexture("records_stone",2));
        menu = new Sprite(BikeGameTextures.LoadTexture("records_stone_menu",2));
        fadeOut = -1.0f;
        fadeIn = 0.0f;
        // Set the widths and heights of the textures
        wheight = 0.6f*BikeGame.V_HEIGHT;
        wwidth  = wheight;
        //swidth  = wheight*(973.0f/760.0f);
        //mheight = wheight*(55.0f/760.0f);
        //mwidth  = mheight*8; 
        // Set the centre of the menu option
        //mxcen = wwidth*(float)Math.cos(Math.PI/12.0f)/2;
        //mycen = wwidth*(float)Math.sin(Math.PI/12.0f)/2;
        // Set the ground velocity and scale
        gscale = 0.5f;
        dscale = 0.2f;
        gnwrap = 1 + (int) (Gdx.graphics.getWidth()/(gscale*dscale*wwidth*8.0f));
        dnwrapx = 1 + (int) (Gdx.graphics.getWidth()/(dscale*wwidth*4.0f));
        dnwrapy = 1 + (int) ((cam.position.y-wheight/2.0f-0.75f*(dscale*wheight))/(dscale*wwidth*4.0f));
        // Set the starting option
        currentOption = 0;
        levelNumber = 0; // level number = 0 is for the total times
        tLevelNumber = -1; // Only positive when a new level is being entered
        levnum = -1.0f; // Once levnum reaches levnumTime, LevelNumber will be set to the tLevelNumber
        System.out.println("ERROR :: get total number of levels");
        totalLevels = 10;
    }

    public void handleInput() {
    	if (GameInput.isPressed(GameInput.KEY_UP)) {
    		currentOption--;
    		if (currentOption < 0) currentOption = 4;
        } else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
    		currentOption++;
    		if (currentOption > 4) currentOption = 0;
        } else if (GameInput.isPressed(GameInput.KEY_RIGHT)) {
    		levelNumber++;
    		if (levelNumber > totalLevels) levelNumber = 0;
        } else if (GameInput.isPressed(GameInput.KEY_LEFT)) {
    		levelNumber--;
    		if (levelNumber < 0) levelNumber = totalLevels;
        } else if ((GameInput.isNumberPressed()) & (currentOption==1)) {
        	int numPress = GameInput.whatNumberPressed();
        	if (tLevelNumber == -1) {
        		tLevelNumber = numPress;
        		levnum = 0.0f;
        	}
        	else {
        		levelNumber = tLevelNumber*10 + numPress;
        		tLevelNumber = -1;
        		levnum = 0.0f;
        	}
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut==-1.0f)) {
        	if (currentOption==0) fadeOut=1.0f; // Return to Main Menu
        } else if (fadeOut==0.0f) {
    		fadeOut=-1.0f;
    		gsm.setState(GameStateManager.PEEK, false, "none");
        }
    	//if (currentOption == 1) currentLevelTxt = "";

    }
    
    public void update(float dt) {
    	// Always make sure the camera is in the correct location and zoom for this screen
		cam.setToOrtho(false, SCRWIDTH, BikeGame.V_HEIGHT);
//		cam.position.set(SCRWIDTH/2, BikeGame.V_HEIGHT/2, 0);
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
    	if (tLevelNumber != -1) {
    		levnum += dt;
    		if (levnum > levnumTime) {
    			levelNumber = tLevelNumber;
    			tLevelNumber = -1;
    			levnum = -1.0f;
    		}
    	} else if (levnum >= 0.0f) {
    		levnum += dt;
    		if (levnum > levnumTime) levnum = -1.0f;
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
        // Draw Sky
        sb.draw(sky, cam.position.x-SCRWIDTH/2, cam.position.y-BikeGame.V_HEIGHT/2, 0, 0, SCRWIDTH, BikeGame.V_HEIGHT, 1.0f, 1.0f, 0.0f);        	
        // Draw menu
        sb.draw(menu, cam.position.x+SCRWIDTH/20+sheight/4, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth), 0, 0, 0.5f*sheight, sheight, 1.0f, 1.0f, 0.0f);
    	if (fadeOut >= 0.0f) alpha=fadeOut;
    	else if (fadeIn < 1.0f) alpha=fadeIn;
    	else alpha=1.0f;
    	if (currentOption == 0) menuText.setColor(1, 1, 1, alpha);
    	else menuText.setColor(1, 1, 1, alpha/2);
    	menuText.draw(sb, "Main Menu", cam.position.x+SCRWIDTH/20+sheight/4 + sheight/20.0f, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + 0.64f*sheight - 1.5f*menuHeight);
    	if (currentOption == 1) menuText.setColor(1, 1, 1, alpha);
    	else menuText.setColor(1, 1, 1, alpha/2);
    	menuText.draw(sb, "Go to Level", cam.position.x+SCRWIDTH/20+sheight/4 + sheight/20.0f, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + 0.64f*sheight - 3.0f*menuHeight);
    	if (currentOption == 2) menuText.setColor(1, 1, 1, alpha);
    	else menuText.setColor(1, 1, 1, alpha/2);
    	menuText.draw(sb, "Personal Times", cam.position.x+SCRWIDTH/20+sheight/4 + sheight/20.0f, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + 0.64f*sheight - 4.5f*menuHeight);
    	if (currentOption == 3) menuText.setColor(1, 1, 1, alpha);
    	else menuText.setColor(1, 1, 1, alpha/2);
    	menuText.draw(sb, "National Records", cam.position.x+SCRWIDTH/20+sheight/4 + sheight/20.0f, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + 0.64f*sheight - 6.0f*menuHeight);
    	if (currentOption == 4) menuText.setColor(1, 1, 1, alpha);
    	else menuText.setColor(1, 1, 1, alpha/2);
    	menuText.draw(sb, "World Records", cam.position.x+SCRWIDTH/20+sheight/4 + sheight/20.0f, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + 0.64f*sheight - 7.5f*menuHeight);
    	// Draw the level number on the menu
    	String numtxt = "--";
    	if (tLevelNumber != -1) {
    		numtxt = "0"+tLevelNumber;
    	} else if (levnum >= 0.0f) {
    		if (levelNumber < 10) numtxt = "0"+levelNumber;
    		else numtxt = ""+levelNumber;
    	}
    	times.draw(sb, numtxt, cam.position.x+SCRWIDTH/20+sheight/4 + sheight/20.0f + (lvlWidth + 0.05f*sheight), cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + 0.64f*sheight - 3.0f*menuHeight - (menuHeight-1.3f*timesHeight));
//        for (int i=0; i<10; i++) {
//        	numbers.setColor(1, 1, 1, (0.5f+i/20.0f)*alpha);
//        	//else numbers.setColor(0.3f, 0.3f, 0.3f, alpha);
//        	numbers.draw(sb, "Main Menu", cam.position.x+SCRWIDTH/20+sheight/4 + sheight/20.0f, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + (i-2.8f)*nmbrHeight);
//        }
        // Draw Records board
        sb.draw(stone, cam.position.x-SCRWIDTH*2/5, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth), 0, 0, sheight, sheight, 1.0f, 1.0f, 0.0f);
        // Draw Records
        String nmbrString, aliasString;
    	if (fadeOut >= 0.0f) alpha=fadeOut;
    	else if (fadeIn < 1.0f) alpha=fadeIn;
    	else alpha=1.0f;
        for (int i=0; i<10; i++) {
        	if (i==0) nmbrString = String.format("%d", 10-i);
        	else nmbrString = String.format(" %d", 10-i);
        	if ((10-i) == 1) textcarveglow.setColor(0.85098f, 0.643137f, 0.254901f, alpha);
        	else if ((10-i) == 2) textcarveglow.setColor(0.658823f, 0.658823f, 0.658823f, alpha);
        	else if ((10-i) == 3) textcarveglow.setColor(0.588235f, 0.352941f, 0.219607f, alpha);
        	else textcarve.setColor(1, 1, 1, alpha);
        	if ((10-i) <= 3) textcarveglow.draw(sb, nmbrString, cam.position.x-SCRWIDTH*2/5 + sheight/15.0f, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + (i+2.5f)*carveHeight);
        	else textcarve.draw(sb, nmbrString, cam.position.x-SCRWIDTH*2/5 + sheight/15.0f, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + (i+2.5f)*carveHeight);
        	aliasString = "";
        	if ((10-i) <= 15) aliasString = "Your alias hereO";
        	if ((10-i) <= 3) textcarveglow.draw(sb, aliasString, cam.position.x-SCRWIDTH*2/5 + sheight/15.0f + 1.25f*nmbrWidth, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + (i+2.5f)*carveHeight);
        	else textcarve.draw(sb, aliasString, cam.position.x-SCRWIDTH*2/5 + sheight/15.0f + 1.25f*nmbrWidth, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + (i+2.5f)*carveHeight);
        	times.setColor(0, 0, 0, alpha);
        	times.draw(sb, "00:00:000", cam.position.x-SCRWIDTH*2/5 + 14.0f*sheight/15.0f - timesWidth, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + (i+1.5f)*carveHeight + 1.3f*timesHeight);
        }
        // Draw Level Number and Name
        if (levelNumber == 0) aliasString = "Total Times";
        else aliasString = "99. Insert Level Number Here";
        textcarve.setScale(numScale);
        float numWid = textcarve.getBounds(aliasString).width;
        float numOff = 0.0f; // Offset the level string so that it appears in the centre of the board 
        numWid = 0.79795f*sheight/numWid;
        if (numWid < 1.0f) textcarve.setScale(numScale*numWid);
        numWid = textcarve.getBounds(aliasString).width;
        if ((0.79795f*sheight-numWid)/2>0.0f) numOff = (0.79795f*sheight-numWid)/2;
        textcarve.draw(sb, aliasString, cam.position.x-SCRWIDTH*2/5 + 0.10955f*sheight + numOff, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + 0.85166f*sheight);
        textcarve.setScale(numScale);        
        // Draw records type
        aliasString = "";
        if (currentOption == 2) aliasString = "Your Best Times"; 
        if (currentOption == 3) aliasString = "National Records"; 
        if (currentOption == 4) aliasString = "World Records";
        numWid = textcarve.getBounds(aliasString).width;
        numWid = (0.4f*sheight)/numWid;
        if (numWid < 1.0f) textcarve.setScale(numScale*numWid);
        textcarve.draw(sb, aliasString, cam.position.x-SCRWIDTH*2/5 + 0.48637f*sheight, cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth) + 0.95086f*sheight);
        textcarve.setScale(numScale);
        // Draw dirt
        for (int i=0; i<dnwrapx; i++) {
        	for (int j=0; j<dnwrapy; j++) {
                sb.draw(dirt, i*(dscale*wwidth*4.0f), cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth)-(j+1)*(dscale*wwidth*4.0f), 0, 0, (dscale*wwidth*4.0f), (dscale*wwidth*4.0f), 1.0f, 1.0f, 0.0f);        	        		
        	}
        }        
        // Draw grass
        for (int i=0; i<gnwrap; i++) {
            sb.draw(grass, i*(gscale*dscale*wwidth*8.0f), cam.position.y-wheight/2.0f-0.75f*(gscale*dscale*wheight), 0, 0, (gscale*dscale*wwidth*8.0f), (gscale*dscale*wheight*2.0f), 1.0f, 1.0f, 0.0f);        	
        }
        sb.end();
    }
    
    public void dispose() {
    	if (times != null) times.dispose();
    	if (textcarve != null) textcarve.dispose();
    	if (textcarveglow != null) textcarveglow.dispose();
    	if (menuText != null) menuText.dispose();
    }

	public void pause() {}

	public void resume() {}
}
