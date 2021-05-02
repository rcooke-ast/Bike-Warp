package com.mygdx.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rcooke
 */
public class OptionsMainMenu {
	public static int currentOption;
	public static final int ChangePlayer = 0;
    public static final int SinglePlayer = 1;
    public static final int Expansions = 2;
    public static final int Replays = 3;
    public static final int Options = 4;
    public static final int Exit = 5;
	// The following string array *must* match the order of loaded graphics in the loadoptions routine below
    public static final int[] menuStrings = {ChangePlayer,SinglePlayer,Expansions,Replays,Options,Exit};
    public static Sprite[] menuOptions;
    static {
        currentOption = 1;
    }

    public static void raise() {
    	currentOption -= 1;
    	if (currentOption < 0) currentOption = menuOptions.length-1;
    }

    public static void lower() {
    	currentOption += 1;
    	if (currentOption >= menuOptions.length) currentOption = 0;
    }
    
    public static Sprite getDisplaySprites(int i) {
    	if (i < 0) return menuOptions[menuOptions.length+i];
    	else if (i >= menuOptions.length) return menuOptions[i-menuOptions.length];
    	else return menuOptions[i];
    }

    public static void SetAlpha(int i, float alpha) {
    	Sprite sp = null;
    	if (i < 0) sp = menuOptions[menuOptions.length+i];
    	else if (i >= menuOptions.length) sp = menuOptions[i-menuOptions.length];
    	else sp = menuOptions[i];
		sp.setColor(sp.getColor().r, sp.getColor().g, sp.getColor().b, alpha);
    	if (i < 0) menuOptions[menuOptions.length+i] = sp;
    	else if (i >= menuOptions.length) menuOptions[i-menuOptions.length] = sp;
    	else menuOptions[i] = sp;
    }

    public static int getCurrent() { return currentOption; }
    
    public static Sprite getCurrentName() { return menuOptions[currentOption]; }

    public static void loadOptions() {
    	menuOptions = new Sprite[6];
        Texture texture = new Texture(Gdx.files.internal("data/images/menu_ChangePlayer.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        menuOptions[0] = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("data/images/menu_SinglePlayer.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        menuOptions[1] = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("data/images/menu_Expansions.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        menuOptions[2] = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("data/images/menu_Replays.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        menuOptions[3] = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("data/images/menu_Options.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        menuOptions[4] = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("data/images/menu_Exit.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        menuOptions[5] = new Sprite(texture);
        // Once the texture is no longer needed, make it null
        texture = null;
    }

}
