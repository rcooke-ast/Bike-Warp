package com.mygdx.game.handlers;

import java.io.Serializable;
import java.util.ArrayList;

public class Replay implements Serializable {
    public ArrayList<Float> replayTime;
    public ArrayList<Float> replayBike_X, replayBike_Y, replayBike_A;
    public ArrayList<Float> replayHead_X, replayHead_Y;
    public ArrayList<Float> replayRider_X, replayRider_Y, replayRider_A;
    public ArrayList<Float> replayLW_X, replayLW_Y, replayLW_A, replayLW_V;
    public ArrayList<Float> replayRW_X, replayRW_Y, replayRW_A, replayRW_V;
    public ArrayList<Float> replayChangeDir;
    public ArrayList<ArrayList<Float>> replayDynamicBodies_X = new ArrayList<ArrayList<Float>>();
    public ArrayList<ArrayList<Float>> replayDynamicBodies_Y = new ArrayList<ArrayList<Float>>();
    public ArrayList<ArrayList<Float>> replayDynamicBodies_A = new ArrayList<ArrayList<Float>>();
    public ArrayList<ArrayList<Float>> replayDynamicBodies_V = new ArrayList<ArrayList<Float>>();
    public int levelNumber=-1, replayMode=-1, replayTimer, replayStatus;
    public String levelName="";
    public float[] bikeColour;

    public Replay(String name, int lvlnmbr, int mode, int status) {
        this.replayTime = new ArrayList<Float>();
        this.bikeColour = GameVars.GetPlayerBikeColor().clone();
        this.replayBike_X = new ArrayList<Float>();
        this.replayBike_Y = new ArrayList<Float>();
        this.replayBike_A = new ArrayList<Float>();
        this.replayRider_X = new ArrayList<Float>();
        this.replayRider_Y = new ArrayList<Float>();
        this.replayRider_A = new ArrayList<Float>();
        this.replayHead_X = new ArrayList<Float>();
        this.replayHead_Y = new ArrayList<Float>();
        this.replayLW_X = new ArrayList<Float>();
        this.replayLW_Y = new ArrayList<Float>();
        this.replayLW_A = new ArrayList<Float>();
        this.replayLW_V = new ArrayList<Float>();
        this.replayRW_X = new ArrayList<Float>();
        this.replayRW_Y = new ArrayList<Float>();
        this.replayRW_A = new ArrayList<Float>();
        this.replayRW_V = new ArrayList<Float>();
        this.replayChangeDir = new ArrayList<Float>();
        this.replayDynamicBodies_X = new ArrayList<ArrayList<Float>>();
        this.replayDynamicBodies_Y = new ArrayList<ArrayList<Float>>();
        this.replayDynamicBodies_A = new ArrayList<ArrayList<Float>>();
        this.replayDynamicBodies_V = new ArrayList<ArrayList<Float>>();
        this.levelName = name;
        this.levelNumber = lvlnmbr;
        this.replayMode = mode+2;
        this.replayStatus = status;
        this.replayTimer = 0;
    }
}
