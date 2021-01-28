/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameSounds;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.GameInput;
import com.mygdx.game.handlers.GameInputProcessor;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.OptionsMainMenu;
import com.mygdx.game.utilities.FileUtils;

/**
 *
 * @author rcooke
 */
public class MainMenu extends GameState {
	private float SCRWIDTH, SCRHEIGHT;
	//private Texture texture;
    private Sprite wheel, shaft, grass, dirt, sky;
    private float wwidth, wheight, mwidth, mheight, mxcen, mycen, swidth, angle;
    private float gscale, gspeed, dscale;
    private int gnwrap, dnwrapx, dnwrapy;
    private float groundTimer, fadeOut, fadeIn, fadeTime = 0.5f;
    private float scrollLevel = 0.0f, scrollGoal = 0.0f, scrollTime = 0.5f, lastStationary = 0.0f;
    private int sGoal = 0, goToLevel;

    public MainMenu(GameStateManager gsm) {
    	/////////////////
    	//  Bike Warp  //
    	/////////////////
        super(gsm);
        create();
    }
    
    public void create() {
		this.game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		SCRWIDTH = BikeGame.viewport.width;
		SCRHEIGHT = BikeGame.viewport.height;
		wheel = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("menu_wheel"),1));
        shaft = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("menu_shaft"),1));
        sky = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("sky_bluesky"),2));
        grass = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("grass_smooth_linrep"),2));
        dirt = new Sprite(BikeGameTextures.LoadTexture(FileUtils.getBaseName("cracked_dirt_linrep"),2));
        groundTimer = 0.0f;
        fadeOut = -1.0f;
        fadeIn = 0.0f;
        // Set the widths and heights of the textures
        wheight = 0.6f*SCRHEIGHT;
        wwidth  = wheight;
        swidth  = wheight*(973.0f/760.0f);
        mheight = wheight*(55.0f/760.0f);
        mwidth  = mheight*8; 
        // Set the centre of the menu option
        mxcen = wwidth*(float)Math.cos(Math.PI/12.0f)/2;
        mycen = wwidth*(float)Math.sin(Math.PI/12.0f)/2;
        // Set the rotation rate
        angle = 0.0f;
        // Set the ground velocity and scale
        gscale = 0.5f;
        dscale = 0.2f;
        gspeed = 5.0f;
        gnwrap = 1 + (int) (SCRWIDTH/(gscale*dscale*wwidth*8.0f));
        dnwrapx = 1 + (int) (SCRWIDTH/(dscale*wwidth*4.0f));
        dnwrapy = 1 + (int) ((cam.position.y-wheight/2.0f-0.75f*(dscale*wheight))/(dscale*wwidth*4.0f));
        // Prepare the font for the menu option
        OptionsMainMenu.loadOptions();
    }

    public void handleInput() {
    	if ((sGoal != 0) & (scrollGoal == 0.0f)) {
    		if (sGoal<0) {
        		lastStationary = OptionsMainMenu.getCurrent();
            	scrollLevel = 0.0f;
            	scrollGoal -= 1.0f;
            	OptionsMainMenu.lower();
				BikeGameSounds.PlayMenuSwitch();
    		} else {
        		lastStationary = OptionsMainMenu.getCurrent();
            	scrollLevel = 0.0f;
            	scrollGoal += 1.0f;        		
            	OptionsMainMenu.raise();
				BikeGameSounds.PlayMenuSwitch();
			}
    	} else if (GameInput.isPressed(GameInput.KEY_DOWN)) {
        	if (scrollGoal == 0.0f) {
        		lastStationary = OptionsMainMenu.getCurrent();
            	scrollLevel = 0.0f;
            	scrollGoal -= 1.0f;
            	OptionsMainMenu.lower();
				BikeGameSounds.PlayMenuSwitch();
        	}
        	sGoal--;
//        	else lastStationary = OptionsMainMenu.menuOptions.length - (Math.abs(lastStationary - scrollLevel)%OptionsMainMenu.menuOptions.length);
//        	scrollGoal -= scrollLevel;
        } else if (GameInput.isPressed(GameInput.KEY_UP)) {
        	if (scrollGoal == 0.0f) {
        		lastStationary = OptionsMainMenu.getCurrent();
            	scrollLevel = 0.0f;
            	scrollGoal += 1.0f;
            	OptionsMainMenu.raise();
				BikeGameSounds.PlayMenuSwitch();
        	}
        	sGoal++;
//        	else lastStationary = (Math.abs(lastStationary + scrollLevel)%OptionsMainMenu.menuOptions.length);
//        	scrollGoal -= scrollLevel;
        } else if ((GameInput.isPressed(GameInput.KEY_E)) & (fadeOut==-1.0f)) {
			fadeOut=1.0f;
			goToLevel = GameStateManager.EDITOR;
		} else if ((GameInput.isPressed(GameInput.KEY_ENTER)) & (sGoal==0) & (fadeOut==-1.0f)) {
        	fadeOut=1.0f;
        	switch (OptionsMainMenu.menuStrings[OptionsMainMenu.getCurrent()]) {
				case OptionsMainMenu.ChangePlayer:
					goToLevel = GameStateManager.MENUPLAYER;
					break;
				case OptionsMainMenu.SinglePlayer:
					goToLevel = GameStateManager.MENULEVELS;
					break;
				case OptionsMainMenu.Expansions:
					goToLevel = GameStateManager.MENUCUSTOM;
					break;
				case OptionsMainMenu.Replays:
					goToLevel = GameStateManager.MENUREPLAY;
					break;
				case OptionsMainMenu.Options:
					goToLevel = GameStateManager.MENUOPTIONS;
					break;
				case OptionsMainMenu.Exit:
					goToLevel = GameStateManager.MENUEXIT;
					break;
			}
			BikeGameSounds.PlayMenuSelect();
        } else if ((GameInput.isPressed(GameInput.KEY_ESC)) & (sGoal==0) & (fadeOut==-1.0f)) {
        	fadeOut=1.0f;
        	goToLevel = GameStateManager.MENUEXIT;
			BikeGameSounds.PlayMenuSelect();
        } else if (fadeOut==0.0f) {
    		fadeOut=-1.0f;
    		gsm.setState(goToLevel, true, "none", -1, 0);
    		fadeIn=0.0f;
        }
    }
    
    public void update(float dt) {
    	// Always make sure the camera is in the correct location and zoom for this screen
		cam.setToOrtho(false, SCRWIDTH, SCRHEIGHT);
//		cam.position.set(SCRWIDTH/2, BikeGame.V_HEIGHT/2, 0);
		cam.zoom = 1.0f;
    	cam.update();
		handleInput();
    	// Update the angle of the bike wheel
    	//angle -= (dangle*dt);
    	angle -= 16.0f*dscale*dt*MathUtils.radiansToDegrees/gspeed;
    	// Update the scroll time
    	groundTimer += dt/gspeed;
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

    }
    
    public void render() {
    	// clear screen
    	//Gdx.gl.glClearColor(0.1f, 0.7f, 0.9f, 1);
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
        // Draw Sky
        sb.draw(sky, cam.position.x-SCRWIDTH/2, cam.position.y-SCRHEIGHT/2, 0, 0, SCRWIDTH, SCRHEIGHT, 1.0f, 1.0f, 0.0f);
        // Draw bike
        sb.draw(wheel, cam.position.x+SCRWIDTH/2-swidth, cam.position.y-wheight/2.0f, wwidth/2.0f, wheight/2.0f, wwidth, wheight, 1.0f, 1.0f, angle);
        sb.draw(shaft, cam.position.x+SCRWIDTH/2-swidth, cam.position.y-wheight/2.0f, wwidth/2.0f, wheight/2.0f, swidth, wheight, 1.0f, 1.0f, 0.0f);
        // Draw dirt
        dirt.setU((2*groundTimer)%1);
        dirt.setU2(((2*groundTimer)%1)+1);
        for (int i=0; i<dnwrapx; i++) {
        	for (int j=0; j<dnwrapy; j++) {
                sb.draw(dirt, i*(dscale*wwidth*4.0f), cam.position.y-wheight/2.0f-0.015625f*(dscale*wwidth)-(j+1)*(dscale*wwidth*4.0f), 0, 0, (dscale*wwidth*4.0f), (dscale*wwidth*4.0f), 1.0f, 1.0f, 0.0f);        	        		
        	}
        }        
        // Draw grass
        grass.setU((groundTimer/gscale)%1);
        grass.setU2((groundTimer/gscale)%1+1);
        for (int i=0; i<gnwrap; i++) {
            sb.draw(grass, i*(gscale*dscale*wwidth*8.0f), cam.position.y-wheight/2.0f-0.75f*(gscale*dscale*wheight), 0, 0, (gscale*dscale*wwidth*8.0f), (gscale*dscale*wheight*2.0f), 1.0f, 1.0f, 0.0f);        	
        }
        sb.end();
        // Draw menu
        float xshift, yshift, mshift, alpha, alphaSign;
        mshift = SCRWIDTH/2-swidth+wwidth/2.0f;
        if (scrollGoal==0.0f) {
        	// Menu is stationary
        	sb.begin();
        	if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, fadeOut);
        	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, fadeIn);
        	else sb.setColor(1, 1, 1, 1); 
            sb.draw(OptionsMainMenu.getDisplaySprites(OptionsMainMenu.currentOption), cam.position.x+mxcen-mwidth/2+mshift, cam.position.y+mycen-mheight/2, mwidth/2, mheight/2, mwidth, mheight, 1.0f, 1.0f, 15.0f);
            sb.end();
            xshift = mheight*(float)Math.cos(105.0*MathUtils.degreesToRadians) + mshift;
            yshift = mheight*(float)Math.sin(105.0*MathUtils.degreesToRadians);
        	sb.begin();
        	if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, fadeOut*0.5f);
        	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, fadeIn*0.5f);
        	else sb.setColor(1, 1, 1, 0.5f); 
            sb.draw(OptionsMainMenu.getDisplaySprites(OptionsMainMenu.currentOption-1), cam.position.x+mxcen-mwidth/2+xshift, cam.position.y+mycen-mheight/2+yshift, mwidth/2, mheight/2, mwidth, mheight, 1.0f, 1.0f, 15.0f);
            sb.end();
            xshift = mheight*(float)Math.cos(285.0*MathUtils.degreesToRadians) + mshift;
            yshift = mheight*(float)Math.sin(285.0*MathUtils.degreesToRadians);
        	sb.begin();
        	if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, fadeOut*0.5f);
        	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, fadeIn*0.5f);
        	else sb.setColor(1, 1, 1, 0.5f); 
            sb.draw(OptionsMainMenu.getDisplaySprites(OptionsMainMenu.currentOption+1), cam.position.x+mxcen-mwidth/2+xshift, cam.position.y+mycen-mheight/2+yshift, mwidth/2, mheight/2, mwidth, mheight, 1.0f, 1.0f, 15.0f);
            sb.end();
        } else {
        	//float aSign = Math.signum(scrollGoal);
            for (int i=0; i<OptionsMainMenu.menuOptions.length; i++) {
            	//if (scrollGoal > 0) alphaSign = -0.5f*(i-(lastStationary + Math.abs(scrollLevel))%OptionsMainMenu.menuOptions.length);
            	//else alphaSign = 0.5f*(i-(lastStationary + Math.abs(scrollLevel))%OptionsMainMenu.menuOptions.length);
            	alphaSign = 0.5f*(i-(lastStationary - scrollLevel)%OptionsMainMenu.menuOptions.length);
            	alpha = 1.0f-Math.abs(alphaSign);
            	if ((alpha > 0.0f) & (alpha < 1.0f)) {
                	if (alphaSign > 0.0) {
                		xshift = 2.0f*(1-alpha)*mheight*(float)Math.cos(285.0*MathUtils.degreesToRadians) + mshift;
                		yshift = 2.0f*(1-alpha)*mheight*(float)Math.sin(285.0*MathUtils.degreesToRadians);
                	} else {
                		xshift = 2.0f*(1-alpha)*mheight*(float)Math.cos(105.0*MathUtils.degreesToRadians) + mshift;
                		yshift = 2.0f*(1-alpha)*mheight*(float)Math.sin(105.0*MathUtils.degreesToRadians);                		
                	}
//                	alpha = 1.0f-Math.abs(alphaSign);
//                	if ((alpha > 0.0f) & (alpha < 1.0f)) {
//                    	if (alphaSign > 0.0) {
//                    		xshift = 2.0f*(1-alpha)*mheight*(float)Math.cos(285.0*MathUtils.degreesToRadians);
//                    		yshift = 2.0f*(1-alpha)*mheight*(float)Math.sin(285.0*MathUtils.degreesToRadians);
//                    	} else {
//                    		xshift = 2.0f*(1-alpha)*mheight*(float)Math.cos(105.0*MathUtils.degreesToRadians);
//                    		yshift = 2.0f*(1-alpha)*mheight*(float)Math.sin(105.0*MathUtils.degreesToRadians);                		
//                    	}
                	sb.begin();
                	if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, fadeOut*alpha);
                	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, fadeIn*alpha);
                	else sb.setColor(1, 1, 1, alpha); 
//                	if (scrollGoal > 0) {
//                		if (alphaSign > 0) idirc = i+1;
//                		else sb.draw(OptionsMainMenu.getDisplaySprites(i-1), mxcen-mwidth/2+xshift, mycen-mheight/2+yshift, mwidth/2, mheight/2, mwidth, mheight, 1.0f, 1.0f, 15.0f);
//                	} else
                	sb.draw(OptionsMainMenu.getDisplaySprites(i), cam.position.x+mxcen-mwidth/2+xshift, cam.position.y+mycen-mheight/2+yshift, mwidth/2, mheight/2, mwidth, mheight, 1.0f, 1.0f, 15.0f);
                	sb.end();
                }
            	if (i<=2) {
                	alphaSign = 0.5f*(i+OptionsMainMenu.menuOptions.length-(lastStationary - scrollLevel)%OptionsMainMenu.menuOptions.length);
                	alpha = 1.0f-Math.abs(alphaSign);
                	if ((alpha > 0.0f) & (alpha < 1.0f)) {
                    	if (alphaSign > 0.0) {
                    		xshift = 2.0f*(1-alpha)*mheight*(float)Math.cos(285.0*MathUtils.degreesToRadians) + mshift;
                    		yshift = 2.0f*(1-alpha)*mheight*(float)Math.sin(285.0*MathUtils.degreesToRadians);
                    	} else {
                    		xshift = 2.0f*(1-alpha)*mheight*(float)Math.cos(105.0*MathUtils.degreesToRadians) + mshift;
                    		yshift = 2.0f*(1-alpha)*mheight*(float)Math.sin(105.0*MathUtils.degreesToRadians);                		
                    	}
                    	sb.begin();
                    	if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, fadeOut*alpha);
                    	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, fadeIn*alpha);
                    	else sb.setColor(1, 1, 1, alpha); 
                    	sb.draw(OptionsMainMenu.getDisplaySprites(i), cam.position.x+mxcen-mwidth/2+xshift, cam.position.y+mycen-mheight/2+yshift, mwidth/2, mheight/2, mwidth, mheight, 1.0f, 1.0f, 15.0f);
                    	sb.end();
                    }
            	}
            	if (i>=OptionsMainMenu.menuOptions.length-2) {
                	alphaSign = 0.5f*(i-OptionsMainMenu.menuOptions.length-(lastStationary - scrollLevel)%OptionsMainMenu.menuOptions.length);
                	alpha = 1.0f-Math.abs(alphaSign);
                	if ((alpha > 0.0f) & (alpha < 1.0f)) {
                    	if (alphaSign > 0.0) {
                    		xshift = 2.0f*(1-alpha)*mheight*(float)Math.cos(285.0*MathUtils.degreesToRadians) + mshift;
                    		yshift = 2.0f*(1-alpha)*mheight*(float)Math.sin(285.0*MathUtils.degreesToRadians);
                    	} else {
                    		xshift = 2.0f*(1-alpha)*mheight*(float)Math.cos(105.0*MathUtils.degreesToRadians) + mshift;
                    		yshift = 2.0f*(1-alpha)*mheight*(float)Math.sin(105.0*MathUtils.degreesToRadians);                		
                    	}
                    	sb.begin();
                    	if (fadeOut >= 0.0f) sb.setColor(1, 1, 1, fadeOut*alpha);
                    	else if (fadeIn < 1.0f) sb.setColor(1, 1, 1, fadeIn*alpha);
                    	else sb.setColor(1, 1, 1, alpha); 
                    	sb.draw(OptionsMainMenu.getDisplaySprites(i), cam.position.x+mxcen-mwidth/2+xshift, cam.position.y+mycen-mheight/2+yshift, mwidth/2, mheight/2, mwidth, mheight, 1.0f, 1.0f, 15.0f);
                    	sb.end();
                    }
            	}
//                if (i<=2) {
//                	alphaSign = 0.5f*(i+OptionsMainMenu.menuOptions.length-(lastStationary + Math.abs(scrollLevel))%OptionsMainMenu.menuOptions.length);
//                    alpha = 1.0f-Math.abs(alphaSign);
//                    if ((alpha > 0.0f) & (alpha < 1.0f)) {
//                    	if (alphaSign > 0.0) {
//                    		xshift = 2.0f*(1-alpha)*mheight*(float)Math.cos(285.0*MathUtils.degreesToRadians);
//                    		yshift = 2.0f*(1-alpha)*mheight*(float)Math.sin(285.0*MathUtils.degreesToRadians);
//                    	} else {
//                    		xshift = 2.0f*(1-alpha)*mheight*(float)Math.cos(105.0*MathUtils.degreesToRadians);
//                    		yshift = 2.0f*(1-alpha)*mheight*(float)Math.sin(105.0*MathUtils.degreesToRadians);                		
//                    	}
//                    	sb.begin();
//                    	sb.setColor(1, 1, 1, alpha);
//                    	sb.draw(OptionsMainMenu.getDisplaySprites(i), mxcen-mwidth/2+xshift, mycen-mheight/2+yshift, mwidth/2, mheight/2, mwidth, mheight, 1.0f, 1.0f, 15.0f);        	        		
//                    	sb.end();
//                    }                	
//                }
            }
        	if (scrollGoal < 0.0f) {
        		scrollLevel += scrollGoal*Gdx.graphics.getDeltaTime()/scrollTime;
        		if (scrollLevel < scrollGoal) {
        			scrollGoal = 0.0f;
        			scrollLevel = 0.0f;
        			sGoal++;
        		}
        	} else {
        		scrollLevel += scrollGoal*Gdx.graphics.getDeltaTime()/scrollTime;
        		if (scrollLevel > scrollGoal) {
        			scrollGoal = 0.0f;
        			scrollLevel = 0.0f;
        			sGoal--;
        		}
        	}

        }
    }
    
    public void dispose() {
    	//if (texture != null) texture.dispose();
    }

	public void pause() {}

	public void resume() {}
}
