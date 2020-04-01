/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import java.util.ArrayList;
import java.io.Serializable;
import com.badlogic.gdx.Input.Keys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author rcooke
 */
public class ReplayVars implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String replayDir = "replays/";
	private static final String replayExt = ".rpl";

    public static ArrayList<Float> replayTime;
    public static ArrayList<Float> replayBike_X, replayBike_Y, replayBike_A;
    public static ArrayList<Float> replayLW_X, replayLW_Y, replayLW_A;
    public static ArrayList<Float> replayRW_X, replayRW_Y, replayRW_A;
    public static ArrayList<Float> replayChangeDir;
    public static int replayCntr = 0, replayCDCntr = 0;

    // Reset the variables, ready for a new replay
    public static void Reset() {
    	replayTime = new ArrayList<Float>();
    	replayBike_X = new ArrayList<Float>();
    	replayBike_Y = new ArrayList<Float>();
    	replayBike_A = new ArrayList<Float>();
    	replayLW_X = new ArrayList<Float>();
    	replayLW_Y = new ArrayList<Float>();
    	replayLW_A = new ArrayList<Float>();
    	replayRW_X = new ArrayList<Float>();
    	replayRW_Y = new ArrayList<Float>();
    	replayRW_A = new ArrayList<Float>();
    	replayChangeDir = new ArrayList<Float>();
    	replayCntr = 0;
    	replayCDCntr = 0;
    }

    // Check the player and world record times
	public static int GetIndex(float repTimer) {
		for (int i=replayCntr; i<replayTime.size()-1; i++) {
			if ((repTimer >= replayTime.get(i)) & (repTimer < replayTime.get(i+1))) {
				replayCntr = i;
				break;
			}
		}
		return replayCntr;
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
	    	replayTime = (ArrayList<Float>) oi.readObject();
	    	replayBike_X = (ArrayList<Float>) oi.readObject();
	    	replayBike_Y = (ArrayList<Float>) oi.readObject();
	    	replayBike_A = (ArrayList<Float>) oi.readObject();
	    	replayLW_X = (ArrayList<Float>) oi.readObject();
	    	replayLW_Y = (ArrayList<Float>) oi.readObject();
	    	replayLW_A = (ArrayList<Float>) oi.readObject();
	    	replayRW_X = (ArrayList<Float>) oi.readObject();
	    	replayRW_Y = (ArrayList<Float>) oi.readObject();
	    	replayRW_A = (ArrayList<Float>) oi.readObject();
	    	replayChangeDir = (ArrayList<Float>) oi.readObject();

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
			o.writeObject(replayTime);
			o.writeObject(replayBike_X);
			o.writeObject(replayBike_Y);
			o.writeObject(replayBike_A);
			o.writeObject(replayLW_X);
			o.writeObject(replayLW_Y);
			o.writeObject(replayLW_A);
			o.writeObject(replayRW_X);
			o.writeObject(replayRW_Y);
			o.writeObject(replayRW_A);
			o.writeObject(replayChangeDir);

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
