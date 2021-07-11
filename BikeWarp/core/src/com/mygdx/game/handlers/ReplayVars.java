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
	public static final String ReplayNotFound = "Replay not found";

	public static Replay currentReplay;
	public static int replayCntr = 0, replayCDCntr = 0;

	// Reset the variables, ready for a new replay
    public static void Reset(String name, int lvlnmbr, int mode) {
		ClearCurrentReplay();
		ResetReplayCounter(); // TODO :: Does this need to be here?
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

	public static void ClearCurrentReplay() {
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
			currentReplay.replayNitrous.clear();
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

	public static void SetCurrentReplay(int levnum) {
		// First clear the replay
		ResetReplayCounter();
		ClearCurrentReplay();
		// Generate a new instance
		currentReplay = new Replay("null", -1, -1, -1);
		// Insert all of the relevant values
		currentReplay.replayMode = GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayMode;
		currentReplay.levelName = GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].levelName;
		currentReplay.levelNumber = GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].levelNumber;
		currentReplay.replayTimer = GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayTimer;
		currentReplay.replayStatus = GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayStatus;
		currentReplay.bikeColour = GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].bikeColour.clone();
		System.out.println("Hello");
		System.out.println(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayTime.size());
		for (int rr=0; rr<GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayTime.size(); rr++) {
			currentReplay.replayTime.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayTime.get(rr));
			currentReplay.replayBike_X.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayBike_X.get(rr));
			currentReplay.replayBike_Y.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayBike_Y.get(rr));
			currentReplay.replayBike_A.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayBike_A.get(rr));
			currentReplay.replayRider_X.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayRider_X.get(rr));
			currentReplay.replayRider_Y.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayRider_Y.get(rr));
			currentReplay.replayRider_A.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayRider_A.get(rr));
			currentReplay.replayHead_X.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayHead_X.get(rr));
			currentReplay.replayHead_Y.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayHead_Y.get(rr));
			currentReplay.replayLW_X.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayLW_X.get(rr));
			currentReplay.replayLW_Y.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayLW_Y.get(rr));
			currentReplay.replayLW_A.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayLW_A.get(rr));
			currentReplay.replayLW_V.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayLW_V.get(rr));
			currentReplay.replayRW_X.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayRW_X.get(rr));
			currentReplay.replayRW_Y.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayRW_Y.get(rr));
			currentReplay.replayRW_A.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayRW_A.get(rr));
			currentReplay.replayRW_V.add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayRW_V.get(rr));
			for (int bb=0; bb < GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayDynamicBodies_X.size(); bb++) {
				currentReplay.replayDynamicBodies_X.get(bb).add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayDynamicBodies_X.get(bb).get(rr));
				currentReplay.replayDynamicBodies_Y.get(bb).add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayDynamicBodies_Y.get(bb).get(rr));
				currentReplay.replayDynamicBodies_A.get(bb).add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayDynamicBodies_A.get(bb).get(rr));
				currentReplay.replayDynamicBodies_V.get(bb).add(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayDynamicBodies_V.get(bb).get(rr));
			}
		}
		currentReplay.replayChangeDir.addAll(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayChangeDir);
		currentReplay.replayNitrous.addAll(GameVars.plyrReplays.get(GameVars.currentPlayer)[levnum].replayNitrous);
	}

	public static Replay CopyOfCurrentReplay() {
		Replay currentReplayCopy = new Replay("null", -1, -1, -1);
		currentReplayCopy.replayMode = currentReplay.replayMode;
		currentReplayCopy.levelName = currentReplay.levelName;
		currentReplayCopy.levelNumber = currentReplay.levelNumber;
		currentReplayCopy.replayTimer = currentReplay.replayTimer;
		currentReplayCopy.replayStatus = currentReplay.replayStatus;
		currentReplayCopy.bikeColour = currentReplay.bikeColour.clone();
		for (int rr=0; rr<currentReplay.replayTime.size(); rr++) {
			currentReplayCopy.replayTime.add(currentReplay.replayTime.get(rr));
			currentReplayCopy.replayBike_X.add(currentReplay.replayBike_X.get(rr));
			currentReplayCopy.replayBike_Y.add(currentReplay.replayBike_Y.get(rr));
			currentReplayCopy.replayBike_A.add(currentReplay.replayBike_A.get(rr));
			currentReplayCopy.replayRider_X.add(currentReplay.replayRider_X.get(rr));
			currentReplayCopy.replayRider_Y.add(currentReplay.replayRider_Y.get(rr));
			currentReplayCopy.replayRider_A.add(currentReplay.replayRider_A.get(rr));
			currentReplayCopy.replayHead_X.add(currentReplay.replayHead_X.get(rr));
			currentReplayCopy.replayHead_Y.add(currentReplay.replayHead_Y.get(rr));
			currentReplayCopy.replayLW_X.add(currentReplay.replayLW_X.get(rr));
			currentReplayCopy.replayLW_Y.add(currentReplay.replayLW_Y.get(rr));
			currentReplayCopy.replayLW_A.add(currentReplay.replayLW_A.get(rr));
			currentReplayCopy.replayLW_V.add(currentReplay.replayLW_V.get(rr));
			currentReplayCopy.replayRW_X.add(currentReplay.replayRW_X.get(rr));
			currentReplayCopy.replayRW_Y.add(currentReplay.replayRW_Y.get(rr));
			currentReplayCopy.replayRW_A.add(currentReplay.replayRW_A.get(rr));
			currentReplayCopy.replayRW_V.add(currentReplay.replayRW_V.get(rr));
			for (int bb=0; bb < currentReplay.replayDynamicBodies_X.size(); bb++) {
				currentReplayCopy.replayDynamicBodies_X.get(bb).add(currentReplay.replayDynamicBodies_X.get(bb).get(rr));
				currentReplayCopy.replayDynamicBodies_Y.get(bb).add(currentReplay.replayDynamicBodies_Y.get(bb).get(rr));
				currentReplayCopy.replayDynamicBodies_A.get(bb).add(currentReplay.replayDynamicBodies_A.get(bb).get(rr));
				currentReplayCopy.replayDynamicBodies_V.get(bb).add(currentReplay.replayDynamicBodies_V.get(bb).get(rr));
			}
		}
		currentReplayCopy.replayChangeDir.addAll(currentReplay.replayChangeDir);
		currentReplayCopy.replayNitrous.addAll(currentReplay.replayNitrous);
    	return currentReplayCopy;
	}

	@SuppressWarnings("unchecked")
	public static void LoadReplay(String filename) {
		ResetReplayCounter();
		ClearCurrentReplay();
		try {
			FileInputStream fi = new FileInputStream(new File(replayDir+filename+replayExt));
			ObjectInputStream oi = new ObjectInputStream(fi);
			// Read objects
			currentReplay = (Replay) oi.readObject();
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
			o.writeObject(currentReplay);
			// Close the file
			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("Replay file not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream for replay file");
		}
	}

	public static String ReplayString(boolean addDR) {
		if (currentReplay == null) return ReplayNotFound;
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
		if (addDR) retstr += "\n\nD - Delete\nR - Rename";
    	return retstr;
	}

	public static boolean CheckExists(String filename) {
		File f = new File(replayDir+filename+replayExt);
		if(f.exists() && !f.isDirectory()) return true;
		return false;
	}
}
