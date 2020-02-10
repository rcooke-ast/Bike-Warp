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
public class LevelVars {

	public static String[] props;
    
    public static final int NUM_PROPS = 5;
    public static final int PROP_NUMJEWELS = 0;
    public static final int PROP_GROUND_TEXTURE = 1;
    public static final int PROP_SKY_TEXTURE = 2;
    public static final int PROP_START_DIRECTION = 3;
    public static final int PROP_GRAVITY = 4;
    
    static {
        props = new String[NUM_PROPS];
    }

    public static void set(int i, String s) { props[i] = s; }
    public static String get(int i) { return props[i]; }
    public static String[] getProps() { return props.clone(); }

    public static void setDefaults() {
    	// Set the defaults
        props[PROP_NUMJEWELS] = "0";
        props[PROP_GROUND_TEXTURE] = "Cracked Mud";
        props[PROP_SKY_TEXTURE] = "Blue Sky";
        props[PROP_START_DIRECTION] = "Right";
        props[PROP_GRAVITY] = "Earth";
    }
    
}
