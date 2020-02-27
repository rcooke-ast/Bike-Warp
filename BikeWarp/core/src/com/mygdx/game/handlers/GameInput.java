/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

/**
 *
 * @author rcooke
 */
public class GameInput {
    
    public static boolean[] keys;
    public static boolean[] pkeys;
    
    public static final int NUM_KEYS = 30;
    // Game keys
    public static final int KEY_ACCEL = 0;
    public static final int KEY_BRAKE = 1;
    public static final int KEY_SPINL = 2;
    public static final int KEY_SPINR = 3;
    public static final int KEY_CHDIR = 4;
    public static final int KEY_BUNNY = 5;
    public static final int KEY_NITROUS = 6;
    // Other keys
    public static final int KEY_T = 7;
    public static final int KEY_C = 8;
    public static final int KEY_D = 9;
    public static final int KEY_Z = 10;
    public static final int KEY_ESC = 11;
    public static final int KEY_ENTER = 12;
    public static final int KEY_NUM0 = 13;
    public static final int KEY_NUM1 = 14;
    public static final int KEY_NUM2 = 15;
    public static final int KEY_NUM3 = 16;
    public static final int KEY_NUM4 = 17;
    public static final int KEY_NUM5 = 18;
    public static final int KEY_NUM6 = 19;
    public static final int KEY_NUM7 = 20;
    public static final int KEY_NUM8 = 21;
    public static final int KEY_NUM9 = 22;
    // Default Game Keys
    public static final int KEY_UP = 23;
    public static final int KEY_DOWN = 24;
    public static final int KEY_LEFT = 25;
    public static final int KEY_RIGHT = 26;
    public static final int KEY_SPACE = 27;
    public static final int KEY_B = 28;
    public static final int KEY_N = 29;

    public static int MBDOWNX = 0;
    public static int MBDOWNY = 0;
    public static int MBUPX = 0;
    public static int MBUPY = 0;
    public static int MBMOVEX = 0;
    public static int MBMOVEY = 0;
    public static int MBDRAGX = 0;
    public static int MBDRAGY = 0;
    public static boolean MBISDOWN = false;
    public static boolean MBRELEASE = false;
    public static boolean MBDRAG = false;
    public static boolean MBJUSTPRESSED = false;
    public static int SCROLL = 0;
    public static String currChar = "";
    
    static {
        keys = new boolean[NUM_KEYS];
        pkeys = new boolean[NUM_KEYS];
    }
    
    public static void update() {
        for (int i=0; i < NUM_KEYS; i++) {
            pkeys[i] = keys[i];
        }
    }
    
    public static void setCharacter(String character) { currChar = character; }
    public static void setKey(int i, boolean b) { keys[i] = b; }
    public static boolean isDown(int i) { return keys[i]; }
    public static boolean isPressed(int i) { return keys[i] && !pkeys[i]; }
    public static boolean isNumberPressed() {
    	return ( (keys[KEY_NUM0] && !pkeys[KEY_NUM0]) ||
    			 (keys[KEY_NUM1] && !pkeys[KEY_NUM1]) ||
    			 (keys[KEY_NUM2] && !pkeys[KEY_NUM2]) ||
    			 (keys[KEY_NUM3] && !pkeys[KEY_NUM3]) ||
    			 (keys[KEY_NUM4] && !pkeys[KEY_NUM4]) ||
    			 (keys[KEY_NUM5] && !pkeys[KEY_NUM5]) ||
    			 (keys[KEY_NUM6] && !pkeys[KEY_NUM6]) ||
    			 (keys[KEY_NUM7] && !pkeys[KEY_NUM7]) ||
    			 (keys[KEY_NUM8] && !pkeys[KEY_NUM8]) ||
    			 (keys[KEY_NUM9] && !pkeys[KEY_NUM9]));
    }
    public static int whatNumberPressed() {
    	if (keys[KEY_NUM0] && !pkeys[KEY_NUM0]) return 0;
    	else if (keys[KEY_NUM1] && !pkeys[KEY_NUM1]) return 1;
    	else if (keys[KEY_NUM2] && !pkeys[KEY_NUM2]) return 2;
    	else if (keys[KEY_NUM3] && !pkeys[KEY_NUM3]) return 3;
    	else if (keys[KEY_NUM4] && !pkeys[KEY_NUM4]) return 4;
    	else if (keys[KEY_NUM5] && !pkeys[KEY_NUM5]) return 5;
    	else if (keys[KEY_NUM6] && !pkeys[KEY_NUM6]) return 6;
    	else if (keys[KEY_NUM7] && !pkeys[KEY_NUM7]) return 7;
    	else if (keys[KEY_NUM8] && !pkeys[KEY_NUM8]) return 8;
    	else if (keys[KEY_NUM9] && !pkeys[KEY_NUM9]) return 9;
    	else return -1;
    }
}
