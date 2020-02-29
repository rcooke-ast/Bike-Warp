/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.BikeGame;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.states.GameState;
import com.mygdx.game.utilities.EditorIO;

/**
 *
 * @author rcooke
 */
public class LevelStateManager {
	public static String[] levelState;
    
    public static final int NUM_PROPS = 7;
    public static final int PROP_STATE = 0;
    public static final int PROP_SKIPPED = 1;
    public static final int PROP_TOTAL_SKIP = 2;
    public static final int PROP_DIAMOND = 3;
    public static final int PROP_PB = 4;
    public static final int PROP_WORLD_RCRD = 5;
    
    static {
    	levelState = new String[NUM_PROPS];
    }
    
    public static void set(int i, String s) { levelState[i] = s; }
    public static String get(int i) { return levelState[i]; }
    public static String[] getProps() { return levelState.clone(); }

    public static void setDefaults() {
    	// Load the Level Properties
        levelState[PROP_STATE] = "";
        levelState[PROP_SKIPPED] = "";
        levelState[PROP_TOTAL_SKIP] = "";
        levelState[PROP_DIAMOND] = "";
        levelState[PROP_PB] = "";
        levelState[PROP_WORLD_RCRD] = "";
    }
}
