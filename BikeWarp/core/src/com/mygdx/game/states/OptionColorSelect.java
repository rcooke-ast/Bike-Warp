package com.mygdx.game.states;

import java.awt.Color;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.GameVars;
import com.mygdx.game.utilities.ColorUtils;
import com.mygdx.game.utilities.FileUtils;
import com.mygdx.game.utilities.PolygonOperations;

public class OptionColorSelect extends GameState {
	private float SCRWIDTH;
	private BitmapFont menuText;
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private Sprite arrow, grass, dirt, sky, wheel, fsusp, rsusp, bwite, bolay, black;
    private float rSuspXL, rSuspYL, rSuspAngL, rSuspWidthL, rSuspHeightL, rSuspXR, rSuspYR, rSuspAngR, rSuspWidthR, rSuspHeightR;
    private float fSuspXL, fSuspYL, fSuspAngL, fSuspWidthL, fSuspHeightL, fSuspXR, fSuspYR, fSuspAngR, fSuspWidthR, fSuspHeightR;
    private float wwidth, wheight, owidth, oheight, mWidth, mHeight, mPos, angle;
    private float gscale, gspeed, dscale, bikeScale=1.0f, bikeScaleLev=0.05f, bikeDirc=1.0f;
    private float[] hsb, rgb;
    private float shiftVal = 0.002f;
    private int gnwrap, dnwrapx, dnwrapy;
    private float groundTimer, fadeOut, fadeIn, fadeTime = 0.5f, spdFact;
    private int currentOption;

	public OptionColorSelect(GameStateManager gsm) {
        super(gsm);
        create();
    }

    public void create() {
		SCRWIDTH = ((float) BikeGame.V_HEIGHT*Gdx.graphics.getDisplayMode().width)/((float) Gdx.graphics.getDisplayMode().height);
		// Prepare the Sprites to be used on this screen
		arrow = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("menu_arrow"),1));
		black = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("menu_black"),1));
		wheel = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("bikewheel"),0));
        fsusp = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("front_suspension"),0));
        rsusp = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("rear_suspension"),0));
        bwite = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("bike_white"),0));
        bolay = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("bike_overlay"),0));
        sky = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("sky_bluesky"),2));
        grass = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("grass_smooth_linrep"),2));
        dirt = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("cracked_dirt_linrep"),2));
        groundTimer = 0.0f;
        fadeOut = -1.0f;
        fadeIn = 0.0f;
        // Set the widths and heights of the textures
        oheight = BikeGame.V_HEIGHT*(1.0f - 0.5f*(446.0f/1090.0f))*(2.0f/3.0f);
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
        currentOption=0;
        // Get current user Bike color
        hsb = new float[3];
        rgb = GameVars.GetPlayerBikeColor().clone();
        Color.RGBtoHSB((int)(rgb[0]*255.0f), (int)(rgb[1]*255.0f), (int)(rgb[2]*255.0f), hsb);
        // Calculate the coordinates for the Rear Suspension
        float bcx, bcy, wcx, wcy, mtopix = wwidth*(968.0f/446.0f);
        // Prepare the rear suspension (Left wheel)
        wcx = cam.position.x + 0.5f*mtopix;
        wcy = cam.position.y - BikeGame.V_HEIGHT*(5.0f/12.0f) + 0.5f*wheight;
        bcx = cam.position.x;
        bcy = cam.position.y - BikeGame.V_HEIGHT*(5.0f/12.0f) + 0.5f*wheight*(1.0f + (0.3f/0.22f));
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
        bcy = cam.position.y - BikeGame.V_HEIGHT*(5.0f/12.0f) + 0.5f*wheight*(1.0f + (0.3f/0.22f));
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
        bcy = cam.position.y - BikeGame.V_HEIGHT*(5.0f/12.0f) + 0.5f*wheight*(1.0f + (0.3f/0.22f)) + cCoord[1];
        fSuspHeightR = 0.06714876f*mtopix;
        fSuspWidthR = (512.0f/497.0f) * (float) Math.sqrt((bcx-wcx)*(bcx-wcx) + (bcy-wcy)*(bcy-wcy));
        fSuspAngR = PolygonOperations.GetAngle(wcx, wcy, bcx, bcy);
        fSuspXR = wcx-fSuspWidthR*15.0f/512.0f;
        fSuspYR = wcy-fSuspHeightR*15.0f/64.0f;
        // Prepare the front suspension (Left Wheel)
       	wcx = cam.position.x - 0.5f*mtopix;
        cCoord = new float[]{-0.2192f*mtopix,0.2614f*mtopix};
        bcx = cam.position.x + cCoord[0];
        bcy = cam.position.y - BikeGame.V_HEIGHT*(5.0f/12.0f) + 0.5f*wheight*(1.0f + (0.3f/0.22f)) + cCoord[1];
        fSuspHeightL = 0.06714876f*mtopix;
        fSuspWidthL = (512.0f/497.0f) * (float) Math.sqrt((bcx-wcx)*(bcx-wcx) + (bcy-wcy)*(bcy-wcy));
        fSuspAngL = PolygonOperations.GetAngle(wcx, wcy, bcx, bcy);
        fSuspXL = wcx-fSuspWidthL*15.0f/512.0f;
        fSuspYL = wcy+fSuspHeightL*15.0f/64.0f;
		// Prepare the bitmap fonts
        menuText = new BitmapFont(Gdx.files.internal("data/font-48.fnt"), false);
        menuText.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        menuText.setColor(1, 1, 1, 1);
        float scaleVal = 0.5f;
        menuText.getData().setScale(scaleVal);
        glyphLayout.setText(menuText, "Accelerate and switch direction to");
        mWidth = glyphLayout.width;
        if ((mWidth/0.5f) > SCRWIDTH) scaleVal = 0.25f*SCRWIDTH/mWidth;
        menuText.getData().setScale(scaleVal);
        glyphLayout.setText(menuText, "Accelerate and switch direction to");
        mWidth = glyphLayout.width;
        mHeight = glyphLayout.height;
        float topgap = BikeGame.V_HEIGHT*(11.0f/12.0f) - (0.5f*wheight + oheight);
        if (topgap < 3.0f*mHeight) scaleVal *= (topgap/(3.0f*mHeight)); 
        menuText.getData().setScale(scaleVal);
        glyphLayout.setText(menuText, "Accelerate and switch direction to");
        mHeight = glyphLayout.height;
        mPos  = cam.position.y - BikeGame.V_HEIGHT*(5.0f/12.0f) + 0.5f*wheight + oheight; // This position marks the top of the player
        mPos += 0.75f*mHeight; // add half the menu height
    }

	@Override
	public void handleInput() {
    	if (GameInput.isPressed(GameInput.KEY_LEFT)) {
        	currentOption -= 1;
        	if (currentOption < 0) currentOption=3;
    	} else if (GameInput.isPressed(GameInput.KEY_RIGHT)) {
        	currentOption += 1;
        	if (currentOption > 3) currentOption=0;
    	} else if (GameInput.isPressed(GameInput.KEY_CHDIR)) {
    		shiftVal *= -1.0f;
    		bikeDirc *= -1.0f;
    		bikeScaleLev *= -1.0f;
    		bikeScale += bikeScaleLev;
        } else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (currentOption==0) & (fadeOut==-1.0f)) {
        	GameVars.SetPlayerBikeColor(ColorUtils.hsbToRgb(hsb[0], hsb[1], hsb[2]));
        	GameVars.SavePlayers();
        	fadeOut=1.0f;
        } else if (fadeOut==0.0f) {
    		fadeOut=-1.0f;
    		gsm.setState(GameStateManager.PEEK, false, "none", -1, 0);
    		fadeIn=0.0f;
        }
    	if (GameInput.isDown(GameInput.KEY_ACCEL)) {
    		if (currentOption!=0) {
    			hsb[currentOption-1] += shiftVal;
    			if (hsb[currentOption-1] > 1.0f) hsb[currentOption-1] = 0.0f;
    			else if (hsb[currentOption-1] < 0.0f) hsb[currentOption-1] = 1.0f;
    			spdFact = bikeDirc*3.0f;
    		}
    	} else spdFact = bikeDirc*1.0f;
	}

	@Override
	public void update(float dt) {
    	// Always make sure the camera is in the correct location and zoom for this screen
		cam.setToOrtho(false, SCRWIDTH, BikeGame.V_HEIGHT);
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
    	Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	sb.setProjectionMatrix(cam.combined);
    	sb.setColor(1, 1, 1, 1); 
        sb.begin();
        // Draw Sky
        sb.draw(sky, cam.position.x-SCRWIDTH/2, cam.position.y-BikeGame.V_HEIGHT/2, 0, 0, SCRWIDTH, BikeGame.V_HEIGHT, 1.0f, 1.0f, 0.0f);        	
        // Draw Bike Wheels
        sb.draw(wheel, cam.position.x-0.5f*wwidth*(1.0f+(968.0f/446.0f)), cam.position.y - BikeGame.V_HEIGHT*(5.0f/12.0f), wwidth/2.0f, wheight/2.0f, wwidth, wheight, 1.0f, 1.0f, angle);
        sb.draw(wheel, cam.position.x-0.5f*wwidth*(1.0f-(968.0f/446.0f)), cam.position.y - BikeGame.V_HEIGHT*(5.0f/12.0f), wwidth/2.0f, wheight/2.0f, wwidth, wheight, 1.0f, 1.0f, angle);
        sb.end();
        // Draw colored part of the bike
        Color rgbcol = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    	sb.setColor(rgbcol.getRed()/255.0f, rgbcol.getGreen()/255.0f, rgbcol.getBlue()/255.0f, 1);
    	float bscale = (float)Math.sin(bikeScale*Math.PI/2);
        sb.begin();
        sb.draw(bwite, cam.position.x-0.5f*wwidth*bscale*(1.0f+(968.0f/446.0f)), cam.position.y - BikeGame.V_HEIGHT*(5.0f/12.0f) + 0.5f*wheight, bscale*owidth/2.0f, oheight/2.0f, bscale*owidth, oheight, 1.0f, 1.0f, 0);
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
        sb.draw(bolay, cam.position.x-0.5f*wwidth*bscale*(1.0f+(968.0f/446.0f)), cam.position.y - BikeGame.V_HEIGHT*(5.0f/12.0f) + 0.5f*wheight, bscale*owidth/2.0f, oheight/2.0f, bscale*owidth, oheight, 1.0f, 1.0f, 0);
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
                sb.draw(dirt, i*(dscale*wwidth*4.0f), cam.position.y - BikeGame.V_HEIGHT*(5.0f/12.0f) - 0.015625f*(dscale*wwidth)-(j+1)*(dscale*wwidth*4.0f), 0, 0, (dscale*wwidth*4.0f), (dscale*wwidth*4.0f), 1.0f, 1.0f, 0.0f);        	        		
        	}
        }        
        // Draw grass
        grass.setU((groundTimer/gscale)%1);
        grass.setU2((groundTimer/gscale)%1+1);
        for (int i=0; i<gnwrap; i++) {
            sb.draw(grass, i*(gscale*dscale*wwidth*8.0f), cam.position.y - BikeGame.V_HEIGHT*(5.0f/12.0f) - 0.75f*(gscale*dscale*wheight), 0, 0, (gscale*dscale*wwidth*8.0f), (gscale*dscale*wheight*2.0f), 1.0f, 1.0f, 0.0f);        	
        }
        sb.end();
        // Draw menu text
        sb.setColor(1, 1, 1, 1);
        sb.begin();
        String text = "";
        if (currentOption==0) text = "Press enter to";
        else text = "Accelerate and switch direction to";
        glyphLayout.setText(menuText, text);
        mWidth = glyphLayout.width;
        menuText.draw(sb, text, (SCRWIDTH-mWidth)/2.0f,mPos+1.75f*mHeight+menuText.getXHeight());
        if (currentOption==0) text = "return to the options menu";
        else if (currentOption==1) text = "change bike color";
        else if (currentOption==2) text = "saturate bike color";
        else if (currentOption==3) text = "change bike shade";
        glyphLayout.setText(menuText, text);
        mWidth = glyphLayout.width;
        menuText.draw(sb, text, (SCRWIDTH-mWidth)/2.0f,mPos+0.5f*mHeight+menuText.getXHeight());
        // Draw menu arrows
        sb.draw(arrow, (SCRWIDTH-mWidth)/2.0f - mHeight, mPos, 0.5f*mHeight/1.55f, 0.5f*mHeight, mHeight/1.55f, mHeight, 1.0f, 1.0f, 0.0f);
        sb.draw(arrow, (SCRWIDTH+mWidth)/2.0f + mHeight, mPos, -0.5f*mHeight/1.55f, 0.5f*mHeight, -mHeight/1.55f, mHeight, 1.0f, 1.0f, 0.0f);
        sb.end();
        // Draw fade in/out
        if ((fadeOut >= 0.0f) | (fadeIn < 1.0f)) {
            if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, 1-fadeOut);
        	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, 1-fadeIn);
        	sb.begin();
        	sb.draw(black, cam.position.x-SCRWIDTH/2, cam.position.y-BikeGame.V_HEIGHT/2, 0, 0, SCRWIDTH, BikeGame.V_HEIGHT, 1.0f, 1.0f, 0.0f);
        	sb.end();
        }
	}

	@Override
	public void dispose() {}
}