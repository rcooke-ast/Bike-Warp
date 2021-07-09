package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.GameVars;
import com.mygdx.game.utilities.FileUtils;
import com.mygdx.game.utilities.PolygonOperations;

public class OptionColorSelect extends GameState {
    private static final float[][][] allBikeColors = {
            {{100.0f/255.0f, 30.0f/255.0f, 22.0f/255.0f},
                    {146.0f/255.0f, 43.0f/255.0f, 33/255.0f},
                    {192.0f/255.0f, 57.0f/255.0f, 43/255.0f},
                    {217.0f/255.0f, 136.0f/255.0f, 128/255.0f},
                    {242.0f/255.0f, 215.0f/255.0f, 213/255.0f}},
            {{120.0f/255.0f, 40.0f/255.0f, 31.0f/255.0f},
                    {176.0f/255.0f, 58.0f/255.0f, 46.0f/255.0f},
                    {231.0f/255.0f, 76.0f/255.0f, 60.0f/255.0f},
                    {241.0f/255.0f, 148.0f/255.0f, 138.0f/255.0f},
                    {250.0f/255.0f, 219.0f/255.0f, 216.0f/255.0f}},
            {{110.0f/255.0f, 44.0f/255.0f, 0.0f/255.0f},
                    {160.0f/255.0f, 64.0f/255.0f, 0.0f/255.0f},
                    {211.0f/255.0f, 84.0f/255.0f, 0.0f/255.0f},
                    {229.0f/255.0f, 152.0f/255.0f, 102.0f/255.0f},
                    {246.0f/255.0f, 221.0f/255.0f, 204.0f/255.0f}},
            {{126.0f/255.0f, 81.0f/255.0f, 9.0f/255.0f},
                    {185.0f/255.0f, 119.0f/255.0f, 14.0f/255.0f},
                    {243.0f/255.0f, 156.0f/255.0f, 18.0f/255.0f},
                    {248.0f/255.0f, 196.0f/255.0f, 113.0f/255.0f},
                    {253.0f/255.0f, 235.0f/255.0f, 208.0f/255.0f}},
            {{125.0f/255.0f, 102.0f/255.0f, 8.0f/255.0f},
                    {183.0f/255.0f, 149.0f/255.0f, 11.0f/255.0f},
                    {241.0f/255.0f, 196.0f/255.0f, 15.0f/255.0f},
                    {247.0f/255.0f, 220.0f/255.0f, 111.0f/255.0f},
                    {252.0f/255.0f, 243.0f/255.0f, 207.0f/255.0f}},
            {{20.0f/255.0f, 90.0f/255.0f, 50.0f/255.0f},
                    {30.0f/255.0f, 132.0f/255.0f, 73.0f/255.0f},
                    {39.0f/255.0f, 174.0f/255.0f, 96.0f/255.0f},
                    {125.0f/255.0f, 206.0f/255.0f, 160.0f/255.0f},
                    {212.0f/255.0f, 239.0f/255.0f, 223.0f/255.0f}},
            {{11.0f/255.0f, 83.0f/255.0f, 69.0f/255.0f},
                    {17.0f/255.0f, 122.0f/255.0f, 101.0f/255.0f},
                    {22.0f/255.0f, 160.0f/255.0f, 133.0f/255.0f},
                    {115.0f/255.0f, 198.0f/255.0f, 182.0f/255.0f},
                    {208.0f/255.0f, 236.0f/255.0f, 231.0f/255.0f}},
            {{14.0f/255.0f, 98.0f/255.0f, 81.0f/255.0f},
                    {20.0f/255.0f, 143.0f/255.0f, 119.0f/255.0f},
                    {26.0f/255.0f, 188.0f/255.0f, 156.0f/255.0f},
                    {118.0f/255.0f, 215.0f/255.0f, 196.0f/255.0f},
                    {209.0f/255.0f, 242.0f/255.0f, 235.0f/255.0f}},
            {{27.0f/255.0f, 79.0f/255.0f, 114.0f/255.0f},
                    {40.0f/255.0f, 116.0f/255.0f, 166.0f/255.0f},
                    {52.0f/255.0f, 152.0f/255.0f, 219.0f/255.0f},
                    {133.0f/255.0f, 193.0f/255.0f, 233.0f/255.0f},
                    {214.0f/255.0f, 234.0f/255.0f, 248.0f/255.0f}},
            {{21.0f/255.0f, 67.0f/255.0f, 96.0f/255.0f},
                    {31.0f/255.0f, 97.0f/255.0f, 141.0f/255.0f},
                    {41.0f/255.0f, 128.0f/255.0f, 185.0f/255.0f},
                    {127.0f/255.0f, 179.0f/255.0f, 213.0f/255.0f},
                    {212.0f/255.0f, 230.0f/255.0f, 241.0f/255.0f}},
            {{81.0f/255.0f, 46.0f/255.0f, 95.0f/255.0f},
                    {118.0f/255.0f, 68.0f/255.0f, 138.0f/255.0f},
                    {155.0f/255.0f, 89.0f/255.0f, 182.0f/255.0f},
                    {195.0f/255.0f, 155.0f/255.0f, 211.0f/255.0f},
                    {235.0f/255.0f, 222.0f/255.0f, 240.0f/255.0f}},
            {{27.0f/255.0f, 38.0f/255.0f, 49.0f/255.0f},
                    {40.0f/255.0f, 55.0f/255.0f, 71.0f/255.0f},
                    {52.0f/255.0f, 73.0f/255.0f, 94.0f/255.0f},
                    {133.0f/255.0f, 146.0f/255.0f, 158.0f/255.0f},
                    {214.0f/255.0f, 219.0f/255.0f, 223.0f/255.0f}},
            {{25.0f/255.0f, 25.0f/255.0f, 25.0f/255.0f},
                    {76.0f/255.0f, 76.0f/255.0f, 76.0f/255.0f},
                    {127.0f/255.0f, 127.0f/255.0f, 127.0f/255.0f},
                    {178.0f/255.0f, 178.0f/255.0f, 178.0f/255.0f},
                    {229.0f/255.0f, 229.0f/255.0f, 229.0f/255.0f}},
    };

	private float SCRWIDTH, SCRHEIGHT;
	private BitmapFont menuText;
	private String menuString;
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private Sprite chart, grass, dirt, sky, wheel, fsusp, rsusp, bwite, bolay, black;
    private float rSuspXL, rSuspYL, rSuspAngL, rSuspWidthL, rSuspHeightL, rSuspXR, rSuspYR, rSuspAngR, rSuspWidthR, rSuspHeightR;
    private float fSuspXL, fSuspYL, fSuspAngL, fSuspWidthL, fSuspHeightL, fSuspXR, fSuspYR, fSuspAngR, fSuspWidthR, fSuspHeightR;
    private float wwidth, wheight, owidth, oheight, mWidth, mHeight, angle;
    private float gscale, gspeed, dscale, bikeScale=1.0f, bikeScaleLev=0.05f, bikeDirc=1.0f;
    private float[] hsb, rgb;
    private float shiftVal = 0.002f;
    private int gnwrap, dnwrapx, dnwrapy;
    private float groundTimer, fadeOut, fadeIn, fadeTime = 0.5f, spdFact;
    private int xOption, yOption;
    // Setup params for the color selection
    private float x_bot, sqrsz, y_bot;

	public OptionColorSelect(GameStateManager gsm) {
        super(gsm);
        create();
    }

    public void create() {
        this.game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        SCRWIDTH = BikeGame.viewport.width;
        SCRHEIGHT = BikeGame.viewport.height;
		// Prepare the Sprites to be used on this screen
		black = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("menu_black")));
		wheel = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("bikewheel")));
        fsusp = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("front_suspension")));
        rsusp = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("rear_suspension")));
        bwite = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("bike_white")));
        bolay = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("bike_overlay")));
        sky = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("bg_StarsSparse")));
        grass = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("grass_smooth_linrep")));
        dirt = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("cracked_dirt_linrep")));
        groundTimer = 0.0f;
        fadeOut = -1.0f;
        fadeIn = 0.0f;
        // Set the widths and heights of the textures
        oheight = SCRHEIGHT*(1.0f - 0.5f*(446.0f/1090.0f))*(1.0f/3.0f);
        owidth  = oheight*(1413.0f/1090.0f);
        wheight = oheight*(446.0f/1090.0f);
        wwidth  = wheight;
        // Set the rotation rate
        angle = 0.0f;
        // Set the ground velocity and scale
        gscale = 0.5f;
        dscale = 0.3f;
        gspeed = 5.0f;
        gnwrap = 1 + (int) (Gdx.graphics.getWidth()/(gscale*dscale*wwidth*8.0f));
        dnwrapx = 1 + (int) (Gdx.graphics.getWidth()/(dscale*wwidth*4.0f));
        dnwrapy = 1 + (int) ((cam.position.y-wheight/2.0f-0.75f*(dscale*wheight))/(dscale*wwidth*4.0f));
        SetStartingColor();
        // Calculate the coordinates for the Rear Suspension
        float bcx, bcy, wcx, wcy, mtopix = wwidth*(968.0f/446.0f);
        // Prepare the rear suspension (Left wheel)
        wcx = cam.position.x + 0.5f*mtopix;
        wcy = cam.position.y - SCRHEIGHT*(5.0f/12.0f) + 0.5f*wheight;
        bcx = cam.position.x;
        bcy = cam.position.y - SCRHEIGHT*(5.0f/12.0f) + 0.5f*wheight*(1.0f + (0.3f/0.22f));
        float[] cCoord = new float[]{-0.143796f*-1.0f*mtopix,-0.218244f*mtopix};
        bcx += cCoord[0];
        bcy += cCoord[1];
        rSuspHeightL = 1.0955f*0.054051f * mtopix;
        rSuspWidthL = 1.0955f*(float) Math.sqrt((bcx-wcx)*(bcx-wcx) + (bcy-wcy)*(bcy-wcy));
        rSuspAngL = PolygonOperations.GetAngle(wcx, wcy, bcx, bcy);
        cCoord = PolygonOperations.RotateCoordinate(-0.3f*rSuspHeightL,-0.5f*rSuspHeightL,MathUtils.radiansToDegrees*rSuspAngL,0.0f,0.0f).clone();
        rSuspXL = wcx+cCoord[0];
        rSuspYL = wcy+cCoord[1];
        // Prepare the rear suspension (Right wheel)
        wcx = cam.position.x-0.5f*mtopix;
        bcx = cam.position.x;
        bcy = cam.position.y - SCRHEIGHT*(5.0f/12.0f) + 0.5f*wheight*(1.0f + (0.3f/0.22f));
        cCoord = new float[]{-0.143796f*1.0f*mtopix,-0.218244f*mtopix};
        bcx += cCoord[0];
        bcy += cCoord[1];
        rSuspHeightR = 1.0955f*0.054051f * mtopix;
        rSuspWidthR = 1.0955f*(float) Math.sqrt((bcx-wcx)*(bcx-wcx) + (bcy-wcy)*(bcy-wcy));
        rSuspAngR = PolygonOperations.GetAngle(wcx, wcy, bcx, bcy);
        cCoord = PolygonOperations.RotateCoordinate(-0.3f*rSuspHeightR,-0.5f*rSuspHeightR,MathUtils.radiansToDegrees*rSuspAngR,0.0f,0.0f).clone();
        rSuspXR = wcx+cCoord[0];
        rSuspYR = wcy+cCoord[1];
        // Prepare the front suspension (Right Wheel)
       	wcx = cam.position.x+0.5f*mtopix;
        cCoord = new float[]{0.2192f*mtopix,0.2614f*mtopix};
        bcx = cam.position.x + cCoord[0];
        bcy = cam.position.y - SCRHEIGHT*(5.0f/12.0f) + 0.5f*wheight*(1.0f + (0.3f/0.22f)) + cCoord[1];
        fSuspHeightR = 0.06714876f*mtopix;
        fSuspWidthR = (512.0f/497.0f) * (float) Math.sqrt((bcx-wcx)*(bcx-wcx) + (bcy-wcy)*(bcy-wcy));
        fSuspAngR = PolygonOperations.GetAngle(wcx, wcy, bcx, bcy);
        fSuspXR = wcx-fSuspWidthR*15.0f/512.0f;
        fSuspYR = wcy-fSuspHeightR*15.0f/64.0f;
        // Prepare the front suspension (Left Wheel)
       	wcx = cam.position.x - 0.5f*mtopix;
        cCoord = new float[]{-0.2192f*mtopix,0.2614f*mtopix};
        bcx = cam.position.x + cCoord[0];
        bcy = cam.position.y - SCRHEIGHT*(5.0f/12.0f) + 0.5f*wheight*(1.0f + (0.3f/0.22f)) + cCoord[1];
        fSuspHeightL = 0.06714876f*mtopix;
        fSuspWidthL = (512.0f/497.0f) * (float) Math.sqrt((bcx-wcx)*(bcx-wcx) + (bcy-wcy)*(bcy-wcy));
        fSuspAngL = PolygonOperations.GetAngle(wcx, wcy, bcx, bcy);
        fSuspXL = wcx-fSuspWidthL*15.0f/512.0f;
        fSuspYL = wcy+fSuspHeightL*15.0f/64.0f;
		// Prepare the bitmap fonts
        menuText = new BitmapFont(Gdx.files.internal("data/recordsmenu.fnt"), false);
//        menuText = new BitmapFont(Gdx.files.internal("data/font-48.fnt"), false);
        menuText.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        menuText.setColor(0.9f, 0.9f, 0.9f, 1);
        float scaleVal = 1.0f;
        menuText.getData().setScale(scaleVal);
        menuString = String.format("Use the arrow keys to change the bike color\n" +
                "Press ENTER to set bike color,\nPress '%s' to cancel", GameVars.GetPlayerESCString());
        glyphLayout.setText(menuText, menuString);
        mWidth = glyphLayout.width;
        scaleVal = 0.35f*SCRWIDTH/mWidth;
        menuText.getData().setScale(scaleVal);

        // Prepare the color chart
        float fullwidth = 0.4f;
        sqrsz = SCRWIDTH*fullwidth/allBikeColors.length;
        x_bot = cam.position.x - 0.5f*SCRWIDTH*fullwidth;
        y_bot = cam.position.y + SCRHEIGHT/2 - sqrsz*allBikeColors[0].length - 0.2f*SCRHEIGHT;
        chart = new Sprite(BikeGameTextures.LoadTexture("bike_colortile"));
    }

    private void SetStartingColor() {
	    float xDist, yDist, bst, tmp;
	    float[] rgbCurr = GameVars.GetPlayerBikeColor();
	    bst = 4*255;
        for (int xx=0; xx<allBikeColors.length; xx++) {
            for (int yy=0; yy<allBikeColors[xx].length; yy++) {
                tmp = 0.0f;
                for (int ii=0; ii<3; ii++) tmp += Math.abs(rgbCurr[ii]-allBikeColors[xx][yy][ii]);
                if (tmp < bst) {
                    xOption = xx;
                    yOption = yy;
                    bst = tmp;
                }
            }
        }
    }

	@Override
	public void handleInput() {
        if ((GameInput.isPressed(GameInput.KEY_ESC)) & (fadeOut==-1.0f)) {
            fadeOut=1.0f;
        } else if (GameInput.isPressed(GameInput.KEY_LEFT)) {
            xOption -= 1;
            if (xOption < 0) xOption = allBikeColors.length-1;
        } else if (GameInput.isPressed(GameInput.KEY_RIGHT)) {
            xOption += 1;
            if (xOption > allBikeColors.length-1) xOption=0;
        } else if (GameInput.isPressed(GameInput.KEY_UP)) {
            yOption += 1;
            if (yOption > allBikeColors[0].length-1) yOption=0;
        } else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
            yOption -= 1;
            if (yOption < 0) yOption= allBikeColors[0].length-1;
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (fadeOut==-1.0f)) {
            GameVars.SetPlayerBikeColor(allBikeColors[xOption][yOption].clone());
            GameVars.SavePlayers();
            fadeOut=1.0f;
        } else if (fadeOut==0.0f) {
    		fadeOut=-1.0f;
    		gsm.setState(GameStateManager.PEEK, false, "none", -1, 0);
    		fadeIn=0.0f;
        }
	}

	@Override
	public void update(float dt) {
    	// Always make sure the camera is in the correct location and zoom for this screen
		cam.setToOrtho(false, SCRWIDTH, SCRHEIGHT);
//		cam.position.set(SCRWIDTH/2, BikeGame.V_HEIGHT/2, 0);
		cam.zoom = 1.0f;
    	cam.update();
		handleInput();
    	// Update the angle of the bike wheel
    	//angle -= (dangle*dt);
    	angle -= 16.0f*dscale*dt*MathUtils.radiansToDegrees*spdFact/gspeed;
    	// Update the scroll time
    	groundTimer += dt*spdFact/gspeed;
//    	if (elapsedTimer >= scrollTimer) elapsedTimer = -1.0f;
//    	else if (elapsedTimer != -1.0f) elapsedTimer += dt;
    	if (groundTimer > 1.0f) groundTimer -= 1.0f;
    	if (fadeOut > 0.0f) {
    		fadeOut -= dt/fadeTime;
    		if (fadeOut < 0.0f) fadeOut = 0.0f;
    	} else if (fadeIn <= 1.0f) {
    		fadeIn += dt/fadeTime;
    		if (fadeIn > 1.0f) fadeIn = 2.0f;
    	}
    	// Set the Bike Scale
		if ((bikeScale > -1.0f) & (bikeScale < 1.0f)) {
			bikeScale += bikeScaleLev;
			if (bikeScale < -1.0f) {
				bikeScale = -1.0f;
			} else if (bikeScale > 1.0f) {
				bikeScale = 1.0f;
			}
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
    	sb.setColor(1, 1, 1, 1); 
        sb.begin();
        // Draw Sky
        sb.draw(sky, cam.position.x-SCRWIDTH/2, cam.position.y-SCRHEIGHT/2, 0, 0, SCRWIDTH, SCRHEIGHT, 1.0f, 1.0f, 0.0f);
        // Draw Bike Wheels
        sb.draw(wheel, cam.position.x-0.5f*wwidth*(1.0f+(968.0f/446.0f)), cam.position.y - SCRHEIGHT*(5.0f/12.0f), wwidth/2.0f, wheight/2.0f, wwidth, wheight, 1.0f, 1.0f, angle);
        sb.draw(wheel, cam.position.x-0.5f*wwidth*(1.0f-(968.0f/446.0f)), cam.position.y - SCRHEIGHT*(5.0f/12.0f), wwidth/2.0f, wheight/2.0f, wwidth, wheight, 1.0f, 1.0f, angle);
        sb.end();
        // Draw colored part of the bike
    	sb.setColor(allBikeColors[xOption][yOption][0], allBikeColors[xOption][yOption][1], allBikeColors[xOption][yOption][2], 1);
    	float bscale = (float)Math.sin(bikeScale*Math.PI/2);
        sb.begin();
        sb.draw(bwite, cam.position.x-0.5f*wwidth*bscale*(1.0f+(968.0f/446.0f)), cam.position.y - SCRHEIGHT*(5.0f/12.0f) + 0.5f*wheight, bscale*owidth/2.0f, oheight/2.0f, bscale*owidth, oheight, 1.0f, 1.0f, 0);
        sb.end();
        // Draw rear suspension
        if (bikeDirc == 1.0f) {
        	if (bscale > 0.0f) sb.setColor(1, 1, 1, bscale);
        	else sb.setColor(1, 1, 1, 0);
        } else {
        	if (bscale < 0.0f) sb.setColor(1, 1, 1, -bscale);
        	else sb.setColor(1, 1, 1, 0);
        }
        sb.begin();
        if (bikeDirc==1.0f) sb.draw(rsusp, rSuspXR, rSuspYR, 0.0f, 0.0f, rSuspWidthR, rSuspHeightR, 1.0f, 1.0f, MathUtils.radiansToDegrees*rSuspAngR);
        else sb.draw(rsusp, rSuspXL, rSuspYL, 0.0f, 0.0f, rSuspWidthL, rSuspHeightL, 1.0f, 1.0f, MathUtils.radiansToDegrees*rSuspAngL);
        sb.end();
        // Draw the bike overlay
    	sb.setColor(1, 1, 1, 1);        
    	sb.begin();
        sb.draw(bolay, cam.position.x-0.5f*wwidth*bscale*(1.0f+(968.0f/446.0f)), cam.position.y - SCRHEIGHT*(5.0f/12.0f) + 0.5f*wheight, bscale*owidth/2.0f, oheight/2.0f, bscale*owidth, oheight, 1.0f, 1.0f, 0);
        sb.end();
        // Draw front suspension
        if (bikeDirc == 1.0f) {
        	if (bscale > 0.0f) sb.setColor(1, 1, 1, bscale*bscale);
        	else sb.setColor(1, 1, 1, 0);
        } else {
        	if (bscale < 0.0f) sb.setColor(1, 1, 1, bscale*bscale);
        	else sb.setColor(1, 1, 1, 0);
        }
        sb.begin();
        if (bikeDirc==1.0f) sb.draw(fsusp, fSuspXR, fSuspYR, fSuspWidthR*15.0f/512.0f, fSuspHeightR*15.0f/64.0f, fSuspWidthR, fSuspHeightR, 1.0f, 1.0f, MathUtils.radiansToDegrees*fSuspAngR);
        else sb.draw(fsusp, fSuspXL, fSuspYL, fSuspWidthL*15.0f/512.0f, -fSuspHeightL*15.0f/64.0f, fSuspWidthL, -fSuspHeightL, 1.0f, 1.0f, MathUtils.radiansToDegrees*fSuspAngL);
        sb.end();
        // Draw dirt
        sb.setColor(1, 1, 1, 1);
        sb.begin();
        dirt.setU((2*groundTimer)%1);
        dirt.setU2(((2*groundTimer)%1)+1);
        for (int i=0; i<dnwrapx; i++) {
        	for (int j=0; j<dnwrapy; j++) {
                sb.draw(dirt, i*(dscale*wwidth*4.0f), cam.position.y - SCRHEIGHT*(5.0f/12.0f) - 0.015625f*(dscale*wwidth)-(j+1)*(dscale*wwidth*4.0f), 0, 0, (dscale*wwidth*4.0f), (dscale*wwidth*4.0f), 1.0f, 1.0f, 0.0f);
        	}
        }        
        // Draw grass
        grass.setU((groundTimer/gscale)%1);
        grass.setU2((groundTimer/gscale)%1+1);
        for (int i=0; i<gnwrap; i++) {
            sb.draw(grass, i*(gscale*dscale*wwidth*8.0f), cam.position.y - SCRHEIGHT*(5.0f/12.0f) - 0.75f*(gscale*dscale*wheight), 0, 0, (gscale*dscale*wwidth*8.0f), (gscale*dscale*wheight*2.0f), 1.0f, 1.0f, 0.0f);
        }
        sb.end();
        // Draw menu text
        sb.setColor(1, 1, 1, 1);
        sb.begin();
        menuText.draw(sb, menuString, (SCRWIDTH-1.05f*mWidth)/2.0f,y_bot+(allBikeColors[0].length+2.5f)*sqrsz, 1.05f*mWidth, Align.center, true);
        // Draw the colour chart
        for (int xx=0; xx<allBikeColors.length; xx++) {
            for (int yy=0; yy<allBikeColors[xx].length; yy++) {
                sb.setColor(allBikeColors[xx][yy][0], allBikeColors[xx][yy][1], allBikeColors[xx][yy][2], 1);
                sb.draw(chart, x_bot+xx*sqrsz, y_bot+yy*sqrsz, sqrsz/2, sqrsz/2,sqrsz,sqrsz,1,1,0);
            }
        }
        sb.setColor(allBikeColors[xOption][yOption][0], allBikeColors[xOption][yOption][1], allBikeColors[xOption][yOption][2], 1);
        sb.draw(chart, x_bot+xOption*sqrsz, y_bot+yOption*sqrsz, sqrsz/2, sqrsz/2,sqrsz,sqrsz,1.5f,1.5f,0);
        sb.end();
        // Draw fade in/out
        if ((fadeOut >= 0.0f) | (fadeIn < 1.0f)) {
            if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, 1-fadeOut);
        	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, 1-fadeIn);
        	sb.begin();
        	sb.draw(black, cam.position.x-SCRWIDTH/2, cam.position.y-SCRHEIGHT/2, 0, 0, SCRWIDTH, SCRHEIGHT, 1.0f, 1.0f, 0.0f);
        	sb.end();
        }
	}

	@Override
	public void dispose() {}
}