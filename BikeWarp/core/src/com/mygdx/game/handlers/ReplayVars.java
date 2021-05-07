/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.io.Serializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/**
 *
 * @author rcooke
 */
public class ReplayVars implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String replayDir = "replays/";
	private static final String replayExt = ".rpl";

    public static ArrayList<Float> replayTime;
    public static ArrayList<Float> replayBike_X, replayBike_Y, replayBike_A;
    public static ArrayList<Float> replayHead_X, replayHead_Y;
    public static ArrayList<Float> replayRider_X, replayRider_Y, replayRider_A;
    public static ArrayList<Float> replayLW_X, replayLW_Y, replayLW_A, replayLW_V;
    public static ArrayList<Float> replayRW_X, replayRW_Y, replayRW_A, replayRW_V;
    public static ArrayList<Float> replayChangeDir;
	public static ArrayList<ArrayList<Float>> replayDynamicBodies_X = new ArrayList<ArrayList<Float>>();
	public static ArrayList<ArrayList<Float>> replayDynamicBodies_Y = new ArrayList<ArrayList<Float>>();
	public static ArrayList<ArrayList<Float>> replayDynamicBodies_A = new ArrayList<ArrayList<Float>>();
	public static ArrayList<ArrayList<Float>> replayDynamicBodies_V = new ArrayList<ArrayList<Float>>();
    public static int replayCntr = 0, replayCDCntr = 0, levelNumber=-1, replayMode=-1, replayTimer;
    public static String levelName="";

    // Reset the variables, ready for a new replay
    public static void Reset(String name, int lvlnmbr, int mode) {
    	replayTime = new ArrayList<Float>();
    	replayBike_X = new ArrayList<Float>();
    	replayBike_Y = new ArrayList<Float>();
    	replayBike_A = new ArrayList<Float>();
    	replayRider_X = new ArrayList<Float>();
    	replayRider_Y = new ArrayList<Float>();
    	replayRider_A = new ArrayList<Float>();
    	replayHead_X = new ArrayList<Float>();
    	replayHead_Y = new ArrayList<Float>();
    	replayLW_X = new ArrayList<Float>();
    	replayLW_Y = new ArrayList<Float>();
    	replayLW_A = new ArrayList<Float>();
    	replayLW_V = new ArrayList<Float>();
    	replayRW_X = new ArrayList<Float>();
    	replayRW_Y = new ArrayList<Float>();
    	replayRW_A = new ArrayList<Float>();
    	replayRW_V = new ArrayList<Float>();
    	replayChangeDir = new ArrayList<Float>();
		replayDynamicBodies_X = new ArrayList<ArrayList<Float>>();
		replayDynamicBodies_Y = new ArrayList<ArrayList<Float>>();
		replayDynamicBodies_A = new ArrayList<ArrayList<Float>>();
		replayDynamicBodies_V = new ArrayList<ArrayList<Float>>();
    	replayCntr = 0;
    	replayCDCntr = 0;
    	levelName = name;
    	levelNumber = lvlnmbr;
    	replayMode = mode+2;
    }

    // Check the player and world record times
	public static String[] GetReplayList() {
		File f = new File(replayDir);
		if (!f.exists()) f.mkdir();
		String[] fils = f.list();
		Array<String> files = new Array<String>();
		for (int ff=0; ff<fils.length; ff++) {
			if (fils[ff].endsWith(replayExt)) {
				files.add(fils[ff]);
			}
		}
		// Put the files in a string list
		String[] repFiles = new String[files.size];
		for (int ff=0; ff<files.size; ff++) {
			repFiles[ff] = files.get(ff);
		}
		// Sort the files
		Arrays.sort(repFiles);
		return repFiles;
	}

    // Check the player and world record times
	public static int GetIndex(float repTimer) {
		boolean updated = false;
		for (int i=replayCntr; i<replayTime.size()-1; i++) {
			if ((repTimer >= replayTime.get(i)) & (repTimer < replayTime.get(i+1))) {
				replayCntr = i;
				break;
			}
		}
		return replayCntr;
	}

	public static void ResetReplayCounter() {
    	// Only use this routine if the replay is being reset while a replay is being shown
		replayCntr = 0;
		replayCDCntr = 0;
	}

	public static boolean CheckSwitchDirection(int rIndex) {
		boolean switchIt = false;
		if (replayCDCntr >= replayChangeDir.size()) return switchIt;
		if ((replayTime.get(rIndex) <= replayChangeDir.get(replayCDCntr)) & (replayTime.get(rIndex+1) > replayChangeDir.get(replayCDCntr))) {
			switchIt = true;
			replayCDCntr += 1;
		}
		return switchIt;
	}

	public static void SetupDynamicBodies(int nbodies) {
    	for (int dd=0; dd<nbodies; dd++) {
			replayDynamicBodies_X.add(new ArrayList<Float>());
			replayDynamicBodies_Y.add(new ArrayList<Float>());
			replayDynamicBodies_A.add(new ArrayList<Float>());
			replayDynamicBodies_V.add(new ArrayList<Float>());
		}
	}

	public static void UpdateKeyPress() {
		// All key press instances must be deactivated
		GameInput.setKey(GameInput.KEY_BUNNY, false);
		GameInput.setKey(GameInput.KEY_CHDIR, false);
	}
	
	@SuppressWarnings("unchecked")
	public static void LoadReplay(String filename) {
		replayCntr = 0;
		replayCDCntr = 0;
		try {
			FileInputStream fi = new FileInputStream(new File(replayDir+filename));
			ObjectInputStream oi = new ObjectInputStream(fi);

			// Read objects
			replayMode = (int) oi.readObject();
			levelName = (String) oi.readObject();
			levelNumber = (int) oi.readObject();
	    	replayTime = (ArrayList<Float>) oi.readObject();
	    	replayBike_X = (ArrayList<Float>) oi.readObject();
	    	replayBike_Y = (ArrayList<Float>) oi.readObject();
	    	replayBike_A = (ArrayList<Float>) oi.readObject();
	    	replayRider_X = (ArrayList<Float>) oi.readObject();
	    	replayRider_Y = (ArrayList<Float>) oi.readObject();
	    	replayRider_A = (ArrayList<Float>) oi.readObject();
	    	replayHead_X = (ArrayList<Float>) oi.readObject();
	    	replayHead_Y = (ArrayList<Float>) oi.readObject();
	    	replayLW_X = (ArrayList<Float>) oi.readObject();
	    	replayLW_Y = (ArrayList<Float>) oi.readObject();
	    	replayLW_A = (ArrayList<Float>) oi.readObject();
	    	replayLW_V = (ArrayList<Float>) oi.readObject();
	    	replayRW_X = (ArrayList<Float>) oi.readObject();
	    	replayRW_Y = (ArrayList<Float>) oi.readObject();
	    	replayRW_A = (ArrayList<Float>) oi.readObject();
	    	replayRW_V = (ArrayList<Float>) oi.readObject();
			replayChangeDir = (ArrayList<Float>) oi.readObject();
			replayDynamicBodies_X = (ArrayList<ArrayList<Float>>) oi.readObject();
			replayDynamicBodies_Y = (ArrayList<ArrayList<Float>>) oi.readObject();
			replayDynamicBodies_A = (ArrayList<ArrayList<Float>>) oi.readObject();
			replayDynamicBodies_V = (ArrayList<ArrayList<Float>>) oi.readObject();
	    	replayTimer = (int) oi.readObject();

			// Close files
			oi.close();
			fi.close();
		} catch (FileNotFoundException e) {
			System.out.println("Replay file not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream for replay file");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void SaveReplay(String filename) {
		// First check if the directory structure exists
		File directory = new File(replayDir);
	    if (!directory.exists()) directory.mkdir();
		// Now write out the file
		FileOutputStream f;
		try {
			f = new FileOutputStream(new File(replayDir+filename+replayExt));
			ObjectOutputStream o = new ObjectOutputStream(f);
			// Write objects to file
			o.writeObject(replayMode);
			o.writeObject(levelName);
			o.writeObject(levelNumber);
			o.writeObject(replayTime);
			o.writeObject(replayBike_X);
			o.writeObject(replayBike_Y);
			o.writeObject(replayBike_A);
			o.writeObject(replayRider_X);
			o.writeObject(replayRider_Y);
			o.writeObject(replayRider_A);
			o.writeObject(replayHead_X);
			o.writeObject(replayHead_Y);
			o.writeObject(replayLW_X);
			o.writeObject(replayLW_Y);
			o.writeObject(replayLW_A);
			o.writeObject(replayLW_V);
			o.writeObject(replayRW_X);
			o.writeObject(replayRW_Y);
			o.writeObject(replayRW_A);
			o.writeObject(replayRW_V);
			o.writeObject(replayChangeDir);
			o.writeObject(replayDynamicBodies_X);
			o.writeObject(replayDynamicBodies_Y);
			o.writeObject(replayDynamicBodies_A);
			o.writeObject(replayDynamicBodies_V);
			o.writeObject(replayTimer);

			// Close the file
			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("Replay file not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream for replay file");
		}
	}

	public static boolean CheckExists(String filename) {
		File f = new File(replayDir+filename+replayExt);
		if(f.exists() && !f.isDirectory()) return true;
		return false;
	}
}
