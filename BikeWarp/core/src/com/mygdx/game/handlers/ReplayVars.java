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
	public static final String replayExt = ".rpl";
	public static final int statusDNF = 0;
	public static final int statusEmerald = 1;
	public static final int statusDiamond = 2;

	public static Replay currentReplay;
	public static int replayCntr = 0, replayCDCntr = 0;

	// Reset the variables, ready for a new replay
    public static void Reset(String name, int lvlnmbr, int mode) {
		ClearCurrentReplay();
    	currentReplay = new Replay(name, lvlnmbr, mode, statusDNF);
    }

    // Check the player and world record times
	public static String[] GetReplayList() {
		File f = new File(replayDir);
		if (!f.exists()) f.mkdir();
		String[] fils = f.list();
		Array<String> files = new Array<String>();
		for (int ff=0; ff<fils.length; ff++) {
			if (fils[ff].endsWith(replayExt)) {
				files.add(fils[ff].substring(0,fils[ff].length()-4));
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
		for (int i=replayCntr; i<currentReplay.replayTime.size()-1; i++) {
			if ((repTimer >= currentReplay.replayTime.get(i)) & (repTimer < currentReplay.replayTime.get(i+1))) {
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
		if (replayCDCntr >= currentReplay.replayChangeDir.size()) return switchIt;
		if ((currentReplay.replayTime.get(rIndex) <= currentReplay.replayChangeDir.get(replayCDCntr)) & (currentReplay.replayTime.get(rIndex+1) > currentReplay.replayChangeDir.get(replayCDCntr))) {
			switchIt = true;
			replayCDCntr += 1;
		}
		return switchIt;
	}

	public static void SetupDynamicBodies(int nbodies) {
    	for (int dd=0; dd<nbodies; dd++) {
			currentReplay.replayDynamicBodies_X.add(new ArrayList<Float>());
			currentReplay.replayDynamicBodies_Y.add(new ArrayList<Float>());
			currentReplay.replayDynamicBodies_A.add(new ArrayList<Float>());
			currentReplay.replayDynamicBodies_V.add(new ArrayList<Float>());
		}
	}

	public static void UpdateKeyPress() {
		// All key press instances must be deactivated
		GameInput.setKey(GameInput.KEY_BUNNY, false);
		GameInput.setKey(GameInput.KEY_CHDIR, false);
	}

	private static void ClearCurrentReplay() {
    	if (currentReplay != null) {
			currentReplay.replayTime.clear();
			currentReplay.replayBike_X.clear();
			currentReplay.replayBike_Y.clear();
			currentReplay.replayBike_A.clear();
			currentReplay.replayRider_X.clear();
			currentReplay.replayRider_Y.clear();
			currentReplay.replayRider_A.clear();
			currentReplay.replayHead_X.clear();
			currentReplay.replayHead_Y.clear();
			currentReplay.replayLW_X.clear();
			currentReplay.replayLW_Y.clear();
			currentReplay.replayLW_A.clear();
			currentReplay.replayLW_V.clear();
			currentReplay.replayRW_X.clear();
			currentReplay.replayRW_Y.clear();
			currentReplay.replayRW_A.clear();
			currentReplay.replayRW_V.clear();
			currentReplay.replayChangeDir.clear();
			for (int cc=0; cc<currentReplay.replayDynamicBodies_X.size(); cc++) {
				currentReplay.replayDynamicBodies_X.get(cc).clear();
				currentReplay.replayDynamicBodies_Y.get(cc).clear();
				currentReplay.replayDynamicBodies_A.get(cc).clear();
				currentReplay.replayDynamicBodies_V.get(cc).clear();
			}
			currentReplay.replayDynamicBodies_X.clear();
			currentReplay.replayDynamicBodies_Y.clear();
			currentReplay.replayDynamicBodies_A.clear();
			currentReplay.replayDynamicBodies_V.clear();
		}
	}

	public static Replay CopyOfCurrentReplay() {
		Replay currentReplayCopy = new Replay("null", -1, -1, -1);
		currentReplayCopy.replayMode = currentReplay.replayMode;
		currentReplayCopy.levelName = currentReplay.levelName;
		currentReplayCopy.levelNumber = currentReplay.levelNumber;
		currentReplayCopy.replayTimer = currentReplay.replayTimer;
		currentReplayCopy.replayStatus = currentReplay.replayStatus;
		currentReplayCopy.bikeColour = currentReplay.bikeColour.clone();
		currentReplayCopy.replayTime = (ArrayList<Float>) currentReplay.replayTime.clone();
		currentReplayCopy.replayBike_X = (ArrayList<Float>) currentReplay.replayBike_X.clone();
		currentReplayCopy.replayBike_Y = (ArrayList<Float>) currentReplay.replayBike_Y.clone();
		currentReplayCopy.replayBike_A = (ArrayList<Float>) currentReplay.replayBike_A.clone();
		currentReplayCopy.replayRider_X = (ArrayList<Float>) currentReplay.replayRider_X.clone();
		currentReplayCopy.replayRider_Y = (ArrayList<Float>) currentReplay.replayRider_Y.clone();
		currentReplayCopy.replayRider_A = (ArrayList<Float>) currentReplay.replayRider_A.clone();
		currentReplayCopy.replayHead_X = (ArrayList<Float>) currentReplay.replayHead_X.clone();
		currentReplayCopy.replayHead_Y = (ArrayList<Float>) currentReplay.replayHead_Y.clone();
		currentReplayCopy.replayLW_X = (ArrayList<Float>) currentReplay.replayLW_X.clone();
		currentReplayCopy.replayLW_Y = (ArrayList<Float>) currentReplay.replayLW_Y.clone();
		currentReplayCopy.replayLW_A = (ArrayList<Float>) currentReplay.replayLW_A.clone();
		currentReplayCopy.replayLW_V = (ArrayList<Float>) currentReplay.replayLW_V.clone();
		currentReplayCopy.replayRW_X = (ArrayList<Float>) currentReplay.replayRW_X.clone();
		currentReplayCopy.replayRW_Y = (ArrayList<Float>) currentReplay.replayRW_Y.clone();
		currentReplayCopy.replayRW_A = (ArrayList<Float>) currentReplay.replayRW_A.clone();
		currentReplayCopy.replayRW_V = (ArrayList<Float>) currentReplay.replayRW_V.clone();
		currentReplayCopy.replayChangeDir = (ArrayList<Float>) currentReplay.replayChangeDir.clone();
		for (int bb=0; bb < currentReplay.replayDynamicBodies_X.size(); bb++) {
			currentReplayCopy.replayDynamicBodies_X.add((ArrayList<Float>) currentReplay.replayDynamicBodies_X.get(bb).clone());
			currentReplayCopy.replayDynamicBodies_Y.add((ArrayList<Float>) currentReplay.replayDynamicBodies_Y.get(bb).clone());
			currentReplayCopy.replayDynamicBodies_A.add((ArrayList<Float>) currentReplay.replayDynamicBodies_A.get(bb).clone());
			currentReplayCopy.replayDynamicBodies_V.add((ArrayList<Float>) currentReplay.replayDynamicBodies_V.get(bb).clone());
		}
    	return currentReplayCopy;
	}

	@SuppressWarnings("unchecked")
	public static void LoadReplay(String filename) {
		replayCntr = 0;
		replayCDCntr = 0;
		ClearCurrentReplay();
		currentReplay = new Replay("null", -1, -1, -1);
		try {
			FileInputStream fi = new FileInputStream(new File(replayDir+filename+replayExt));
			ObjectInputStream oi = new ObjectInputStream(fi);

			// Read objects
			currentReplay.replayMode = (int) oi.readObject();
			currentReplay.levelName = (String) oi.readObject();
			currentReplay.levelNumber = (int) oi.readObject();
			currentReplay.replayTime = (ArrayList<Float>) oi.readObject();
			currentReplay.replayBike_X = (ArrayList<Float>) oi.readObject();
			currentReplay.replayBike_Y = (ArrayList<Float>) oi.readObject();
			currentReplay.replayBike_A = (ArrayList<Float>) oi.readObject();
			currentReplay.replayRider_X = (ArrayList<Float>) oi.readObject();
			currentReplay.replayRider_Y = (ArrayList<Float>) oi.readObject();
			currentReplay.replayRider_A = (ArrayList<Float>) oi.readObject();
			currentReplay.replayHead_X = (ArrayList<Float>) oi.readObject();
			currentReplay.replayHead_Y = (ArrayList<Float>) oi.readObject();
			currentReplay.replayLW_X = (ArrayList<Float>) oi.readObject();
			currentReplay.replayLW_Y = (ArrayList<Float>) oi.readObject();
			currentReplay.replayLW_A = (ArrayList<Float>) oi.readObject();
			currentReplay.replayLW_V = (ArrayList<Float>) oi.readObject();
			currentReplay.replayRW_X = (ArrayList<Float>) oi.readObject();
			currentReplay.replayRW_Y = (ArrayList<Float>) oi.readObject();
			currentReplay.replayRW_A = (ArrayList<Float>) oi.readObject();
			currentReplay.replayRW_V = (ArrayList<Float>) oi.readObject();
			currentReplay.replayChangeDir = (ArrayList<Float>) oi.readObject();
			currentReplay.replayDynamicBodies_X = (ArrayList<ArrayList<Float>>) oi.readObject();
			currentReplay.replayDynamicBodies_Y = (ArrayList<ArrayList<Float>>) oi.readObject();
			currentReplay.replayDynamicBodies_A = (ArrayList<ArrayList<Float>>) oi.readObject();
			currentReplay.replayDynamicBodies_V = (ArrayList<ArrayList<Float>>) oi.readObject();
			currentReplay.replayTimer = (int) oi.readObject();
			currentReplay.replayStatus = (int) oi.readObject();
			currentReplay.bikeColour = (float[]) oi.readObject();

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
			o.writeObject(currentReplay.replayMode);
			o.writeObject(currentReplay.levelName);
			o.writeObject(currentReplay.levelNumber);
			o.writeObject(currentReplay.replayTime);
			o.writeObject(currentReplay.replayBike_X);
			o.writeObject(currentReplay.replayBike_Y);
			o.writeObject(currentReplay.replayBike_A);
			o.writeObject(currentReplay.replayRider_X);
			o.writeObject(currentReplay.replayRider_Y);
			o.writeObject(currentReplay.replayRider_A);
			o.writeObject(currentReplay.replayHead_X);
			o.writeObject(currentReplay.replayHead_Y);
			o.writeObject(currentReplay.replayLW_X);
			o.writeObject(currentReplay.replayLW_Y);
			o.writeObject(currentReplay.replayLW_A);
			o.writeObject(currentReplay.replayLW_V);
			o.writeObject(currentReplay.replayRW_X);
			o.writeObject(currentReplay.replayRW_Y);
			o.writeObject(currentReplay.replayRW_A);
			o.writeObject(currentReplay.replayRW_V);
			o.writeObject(currentReplay.replayChangeDir);
			o.writeObject(currentReplay.replayDynamicBodies_X);
			o.writeObject(currentReplay.replayDynamicBodies_Y);
			o.writeObject(currentReplay.replayDynamicBodies_A);
			o.writeObject(currentReplay.replayDynamicBodies_V);
			o.writeObject(currentReplay.replayTimer);
			o.writeObject(currentReplay.replayStatus);
			o.writeObject(currentReplay.bikeColour);

			// Close the file
			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("Replay file not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream for replay file");
		}
	}

	public static String ReplayString() {
    	String retstr = String.format("Level Name:\n%s\n\n", LevelsListGame.gameLevelNames[currentReplay.levelNumber+1]);
		retstr += String.format("Duration:\n%s\n\n", GameVars.getTimeString(currentReplay.replayTimer));
		switch (currentReplay.replayStatus) {
			case statusDNF:
				retstr += String.format("Replay status:\nDNF");
				break;
			case statusEmerald:
				retstr += String.format("Replay status:\nEmerald");
				break;
			case statusDiamond:
				retstr += String.format("Replay status:\nDiamond");
				break;
			default:
				break;
		}
		retstr += "\n\nD - Delete\nR - Rename";
    	return retstr;
	}

	public static boolean CheckExists(String filename) {
		File f = new File(replayDir+filename+replayExt);
		if(f.exists() && !f.isDirectory()) return true;
		return false;
	}
}
