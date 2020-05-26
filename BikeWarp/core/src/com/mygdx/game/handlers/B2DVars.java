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
public class B2DVars {
    // Pixels per meter
    public static final float PPM = 0.1f;
    public static final float EPPM = 0.01f; // PPM in the Editor
    
    public static final float SCRWIDTH = 12.0f;       // How many meters (in width) should the screen display
    public static final float GRAVITY_EARTH = 8.807f; // magnitude of the gravity vector in m/s/s
    public static final float GRAVITY_MARS  = 3.711f; // magnitude of the gravity vector in m/s/s
    public static final float GRAVITY_MOON  = 1.622f; // magnitude of the gravity vector in m/s/s
    public static final float GRAVITY_ZERO  = 0.0f;   // magnitude of the gravity vector in m/s/s

    // category bits
    public static final short BIT_GROUND = 2;
    public static final short BIT_WHEEL = 4;
    public static final short BIT_HEAD = 8;
    public static final short BIT_BODY = 16;
    public static final short BIT_SPIKE = 32;
    public static final short BIT_JEWEL = 64;
    public static final short BIT_CHAIN = 128;
    public static final short BIT_NOTHING = 256; // Do not set any fixture to have category bits = 256
    public static final short BIT_KEY = 512;
    public static final short BIT_TRANSPORT = 1024;
    public static final short BIT_FINISH = 2048;
    public static final short BIT_DOOR = 4096;
    public static final short BIT_SWITCH = 8192;
    public static final short BIT_GRAVITY = 16384;
    
}
