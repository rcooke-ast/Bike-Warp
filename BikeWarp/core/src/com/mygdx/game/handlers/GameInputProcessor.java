/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;

/**
 *
 * @author rcooke
 */
public class GameInputProcessor extends InputMultiplexer {
    
	public static boolean disable_keys = false;
	
	public static void Disable(boolean dis) {
		disable_keys = dis;
	}

	public boolean keyDown(int k) {
    	// Check the escape key first (we always need escape)
        if (k == Keys.ESCAPE) {
        	GameInput.setKey(GameInput.KEY_ESC, true);
        	if (disable_keys) return true;
        }
        // Check is keys are disabled
    	if (disable_keys) return false;
    	// Game keys
    	if (k == Keys.UP) GameInput.setKey(GameInput.KEY_ACCEL, true);
    	if (k == Keys.DOWN) GameInput.setKey(GameInput.KEY_BRAKE, true);
    	if (k == Keys.LEFT) GameInput.setKey(GameInput.KEY_SPINL, true);
    	if (k == Keys.RIGHT) GameInput.setKey(GameInput.KEY_SPINR, true);
    	if (k == Keys.SPACE) GameInput.setKey(GameInput.KEY_CHDIR, true);
    	if (k == Keys.B) GameInput.setKey(GameInput.KEY_BUNNY, true);
    	if (k == Keys.N) GameInput.setKey(GameInput.KEY_NITROUS, true);
        // Other keys
        if (k == Keys.UP) GameInput.setKey(GameInput.KEY_UP, true);
        if (k == Keys.DOWN) GameInput.setKey(GameInput.KEY_DOWN, true);
        if (k == Keys.LEFT) GameInput.setKey(GameInput.KEY_LEFT, true);
        if (k == Keys.RIGHT) GameInput.setKey(GameInput.KEY_RIGHT, true);
        if (k == Keys.SPACE) GameInput.setKey(GameInput.KEY_SPACE, true);
        if (k == Keys.ENTER) GameInput.setKey(GameInput.KEY_ENTER, true);
        if (k == Keys.B) GameInput.setKey(GameInput.KEY_B, true);
        if (k == Keys.N) GameInput.setKey(GameInput.KEY_N, true);
        if (k == Keys.Z) GameInput.setKey(GameInput.KEY_Z, true);
        if (k == Keys.T) GameInput.setKey(GameInput.KEY_T, true);
        if (k == Keys.C) GameInput.setKey(GameInput.KEY_C, true);
        if (k == Keys.D) GameInput.setKey(GameInput.KEY_D, true);
        if (k == Keys.NUM_0) GameInput.setKey(GameInput.KEY_NUM0, true);
        if (k == Keys.NUM_1) GameInput.setKey(GameInput.KEY_NUM1, true);
        if (k == Keys.NUM_2) GameInput.setKey(GameInput.KEY_NUM2, true);
        if (k == Keys.NUM_3) GameInput.setKey(GameInput.KEY_NUM3, true);
        if (k == Keys.NUM_4) GameInput.setKey(GameInput.KEY_NUM4, true);
        if (k == Keys.NUM_5) GameInput.setKey(GameInput.KEY_NUM5, true);
        if (k == Keys.NUM_6) GameInput.setKey(GameInput.KEY_NUM6, true);
        if (k == Keys.NUM_7) GameInput.setKey(GameInput.KEY_NUM7, true);
        if (k == Keys.NUM_8) GameInput.setKey(GameInput.KEY_NUM8, true);
        if (k == Keys.NUM_9) GameInput.setKey(GameInput.KEY_NUM9, true);
        return true;
    }

    public boolean keyUp(int k) {
    	// Check the escape key first (we always need escape)
        if (k == Keys.ESCAPE) {
        	GameInput.setKey(GameInput.KEY_ESC, false);
        	if (disable_keys) return true;
        }
    	if (disable_keys) return false;
    	// Game keys
    	if (k == Keys.UP) GameInput.setKey(GameInput.KEY_ACCEL, false);
    	if (k == Keys.DOWN) GameInput.setKey(GameInput.KEY_BRAKE, false);
    	if (k == Keys.LEFT) GameInput.setKey(GameInput.KEY_SPINL, false);
    	if (k == Keys.RIGHT) GameInput.setKey(GameInput.KEY_SPINR, false);
    	if (k == Keys.SPACE) GameInput.setKey(GameInput.KEY_CHDIR, false);
    	if (k == Keys.B) GameInput.setKey(GameInput.KEY_BUNNY, false);
    	if (k == Keys.N) GameInput.setKey(GameInput.KEY_NITROUS, false);
        // Other keys
        if (k == Keys.UP) GameInput.setKey(GameInput.KEY_UP, false);
        if (k == Keys.DOWN) GameInput.setKey(GameInput.KEY_DOWN, false);
        if (k == Keys.LEFT) GameInput.setKey(GameInput.KEY_LEFT, false);
        if (k == Keys.RIGHT) GameInput.setKey(GameInput.KEY_RIGHT, false);
        if (k == Keys.SPACE) GameInput.setKey(GameInput.KEY_SPACE, false);
        if (k == Keys.ENTER) GameInput.setKey(GameInput.KEY_ENTER, false);
        if (k == Keys.B) GameInput.setKey(GameInput.KEY_B, false);
        if (k == Keys.N) GameInput.setKey(GameInput.KEY_N, false);
        if (k == Keys.Z) GameInput.setKey(GameInput.KEY_Z, false);
        if (k == Keys.T) GameInput.setKey(GameInput.KEY_T, false);
        if (k == Keys.C) GameInput.setKey(GameInput.KEY_C, false);
        if (k == Keys.D) GameInput.setKey(GameInput.KEY_D, false);
        if (k == Keys.NUM_0) GameInput.setKey(GameInput.KEY_NUM0, false);
        if (k == Keys.NUM_1) GameInput.setKey(GameInput.KEY_NUM1, false);
        if (k == Keys.NUM_2) GameInput.setKey(GameInput.KEY_NUM2, false);
        if (k == Keys.NUM_3) GameInput.setKey(GameInput.KEY_NUM3, false);
        if (k == Keys.NUM_4) GameInput.setKey(GameInput.KEY_NUM4, false);
        if (k == Keys.NUM_5) GameInput.setKey(GameInput.KEY_NUM5, false);
        if (k == Keys.NUM_6) GameInput.setKey(GameInput.KEY_NUM6, false);
        if (k == Keys.NUM_7) GameInput.setKey(GameInput.KEY_NUM7, false);
        if (k == Keys.NUM_8) GameInput.setKey(GameInput.KEY_NUM8, false);
        if (k == Keys.NUM_9) GameInput.setKey(GameInput.KEY_NUM9, false);
        return true;
    }

    public boolean keyTyped(char ch) {
    	// Catch a backspace
    	if (ch == '\b') GameInput.setCharacter("\b");
    	else GameInput.setCharacter(Character.toString(ch).trim());
		return false;
    }

    public boolean scrolled(int k) {
        GameInput.SCROLL = k;
        return true;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    	GameInput.MBDOWNX = screenX;
    	GameInput.MBDOWNY = screenY;
    	GameInput.MBISDOWN = true;
    	GameInput.MBRELEASE = false;
    	return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	GameInput.MBUPX = screenX;
    	GameInput.MBUPY = screenY;
    	GameInput.MBISDOWN = false;
    	GameInput.MBRELEASE = true;
    	if (GameInput.MBDRAG) GameInput.MBJUSTDRAGGED = true;
    	else GameInput.MBJUSTPRESSED = true;
    	GameInput.MBDRAG = false;
    	return true;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
    	GameInput.MBDRAGX = screenX;
    	GameInput.MBDRAGY = screenY;
    	GameInput.MBDRAG = true;
    	return true;
    }

    public boolean mouseMoved(int screenX, int screenY) {
    	GameInput.MBMOVEX = screenX;
    	GameInput.MBMOVEY = screenY;
    	return true;
    }

}
