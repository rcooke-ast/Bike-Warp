/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.BikeGame;
import com.mygdx.game.handlers.GameInputProcessor;
import com.mygdx.game.handlers.GameStateManager;

/**
 *
 * @author rcooke
 */
public abstract class GameState {
    protected GameStateManager gsm;
    protected BikeGame game;
    
    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;
    
    protected GameState(GameStateManager gsm) {
        this.gsm= gsm;
        game = gsm.game();
        sb = game.getSpriteBatch();
        cam = game.getCamera();
        hudCam = game.getHUDCamera();
        Gdx.input.setInputProcessor(new GameInputProcessor());
    }
    
    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}
