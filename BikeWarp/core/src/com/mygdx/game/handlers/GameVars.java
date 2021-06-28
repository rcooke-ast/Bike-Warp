/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import java.io.*;
import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 *
 * @author rcooke
 */
public class GameVars implements Serializable {

	private static final long serialVersionUID = 1L;

	// How many level skips are allowed
	public static final int skipsAllowed = 3; // Allow players to skip 3 levels
	public static final int numStore = 10; // Store the top 10 times of each level
	public static final int MaxTime = 10*60*1000;  // 10 minutes is the maximum time that a player can play for!
	public static final int DISPLAY_OFF = 0;
	public static final int DISPLAY_ON = 1;
	public static final int DISPLAY_SMARTON = 2;
	// Latest time
	public static int timerTotal = -1;
	public static int EmeraldDiamond = 0;
	// Player arrays
	public static int currentPlayer = -1;
	public static int currentSteamUser = -1;
	public static boolean personalBest = false;
	public static boolean worldRecord = false;
	public static int[] plyrID = new int[0];
	public static String[] plyrName = new String[0];
	public static ArrayList<Replay[]> plyrReplays = new ArrayList<Replay[]>();
	public static ArrayList<Replay[]> plyrReplaysDmnd = new ArrayList<Replay[]>();
	public static ArrayList<ArrayList<Integer>> plyrTimes = new ArrayList<ArrayList<Integer>>();
	public static ArrayList<ArrayList<Integer>> plyrTimesDmnd = new ArrayList<ArrayList<Integer>>();
	public static ArrayList<int[]> plyrControls = new ArrayList<int[]>();
	public static ArrayList<int[]> plyrDisplay = new ArrayList<int[]>();
	public static ArrayList<Boolean> plyrFullscreen = new ArrayList<Boolean>();
	public static ArrayList<boolean[]> plyrColDmnd = new ArrayList<boolean[]>();
	public static ArrayList<int[]> plyrLevelComplete = new ArrayList<int[]>();
	public static ArrayList<float[]> plyrBikeColor = new ArrayList<float[]>();
	public static ArrayList<Integer> plyrTotalTimes = new ArrayList<Integer>();
	public static ArrayList<Integer> plyrTotalTimesDmnd = new ArrayList<Integer>();

	// Get and Set options
	public static void SetCurrentPlayerOffline(int id) {
		currentPlayer = id;
	}

	// Get and Set options
	public static void SetCurrentPlayer(int id, String name) {
		for (int ii=0; ii < plyrID.length; ii++) {
			if (id==plyrID[ii]) {
				currentPlayer = ii;
				if (plyrName[currentPlayer].compareTo(name) != 0) {
					// Player has updated their name online - update it here too
					plyrName[currentPlayer] = name;
					SavePlayers();
				}
				return;
			}
		}
		// If we get to here and no player ID has been found, then we need to add it here
		AddPlayer(id, name);
		currentPlayer = plyrID.length-1;
		SavePlayers();
	}

	// Get and Set options
	public static void SetTimerTotal(int i) {
		timerTotal = i;
		if (i<0) SetEmeraldDiamond(0);
	}
	public static void SetEmeraldDiamond(int emdm) {EmeraldDiamond = emdm;}
	public static void SetWorldRecord(boolean wr) {worldRecord = wr;}
	public static void SetPersonalBest(boolean pb) {personalBest = pb;}

	public static int GetCurrentPlayer() {return currentPlayer;}

	// Get Player properties
	public static String GetPlayerName() {return plyrName[currentPlayer];}
	public static boolean GetPlayerFullscreen() {
		if (currentPlayer == -1) return true;
		return plyrFullscreen.get(currentPlayer);
	}
	public static void SetPlayerFullscreen(boolean value) {
		plyrFullscreen.set(currentPlayer, value);
		SavePlayers();
	}
	public static int GetPlayerTimes(int lvl) {return plyrTimes.get(currentPlayer).get(lvl);}
	public static int GetPlayerTimesDmnd(int lvl) {return plyrTimesDmnd.get(currentPlayer).get(lvl);}
	public static int[] GetPlayerControls() {return plyrControls.get(currentPlayer);}
	public static int GetPlayerDisplay(int index) {return plyrDisplay.get(currentPlayer)[index];}
	public static boolean[] GetPlayerDiamonds() {return plyrColDmnd.get(currentPlayer);}
	public static boolean GetPlayerDiamond(int level) {return plyrColDmnd.get(currentPlayer)[level];}
	public static int[] GetPlayerSkipLevel() {return plyrLevelComplete.get(currentPlayer);}
	public static float[] GetPlayerBikeColor() {return plyrBikeColor.get(currentPlayer);}
	public static void SetPlayerBikeColor(float[] rgb) {plyrBikeColor.set(currentPlayer, rgb.clone());}
	public static boolean SetPlayerControls(int index, int value) {
		int[] newControls = plyrControls.get(currentPlayer).clone();
		// Check if the key is not already being used
		for (int ii=0; ii<newControls.length; ii++) {
			if (newControls[ii] == value) return false;
		}
		// Not allowed to use ESC
		if (value == Keys.ESCAPE) return false;
		// Assign the new value
		newControls[index] = value;
		plyrControls.set(currentPlayer, newControls.clone());
		// Finally save the updates
		SavePlayers();
		return true;
	}

	public static boolean SetPlayerDisplay(int index, int value) {
		int[] newDisplay = plyrDisplay.get(currentPlayer).clone();
		newDisplay[index] = value;
		plyrDisplay.set(currentPlayer, newDisplay.clone());
		// Finally save the updates
		SavePlayers();
		return true;
	}

	// Get the world record times and aliases
//	public static int GetWorldTimes(int lvl, int indx) {return worldTimes.get(lvl)[indx];}
//	public static int GetWorldTimesDmnd(int lvl, int indx) {return worldTimesDmnd.get(lvl)[indx];}
//	public static int GetWorldNames(int lvl, int indx) {return worldNames.get(lvl)[indx];}
//	public static int GetWorldNamesDmnd(int lvl, int indx) {return worldNamesDmnd.get(lvl)[indx];}

	public static void UpdateTotalTimes() {
		// Do the Player total times first
		plyrTotalTimes.set(currentPlayer, GetTotalTimes(false));
		plyrTotalTimesDmnd.set(currentPlayer, GetTotalTimes(true));
		SavePlayers();
	}

	public static void UpdateTotalTimesAllPlayers(boolean world) {
		// Check if every player's best total time beats any of the world total times
		for (int pp=0; pp<plyrID.length; pp++) {
			// Do the Player total times first
			plyrTotalTimes.set(pp, GetTotalTimesPlayer(pp, false));
			plyrTotalTimesDmnd.set(pp, GetTotalTimesPlayer(pp, true));
		}
		SavePlayers();
	}

	public static int GetTotalTimes(boolean diamond) {return GetTotalTimesPlayer(currentPlayer, diamond);}

	public static int GetTotalTimesPlayer(int plyr, boolean diamond) {
		int totalTime = 0;
		int timeVal;
		int numLevels = LevelsListGame.NUMGAMELEVELS;
		for (int ll=0; ll<numLevels; ll++) {
			// First get the relevant time
			if (diamond) timeVal = plyrTimesDmnd.get(plyr).get(ll);
			else timeVal = plyrTimes.get(plyr).get(ll);
			// Check the time is valid
			if (timeVal != -1) totalTime += timeVal;
			else totalTime += MaxTime;
		}
		return totalTime;
	}

	public static int[] GetEmeraldComplete() {
		int nlev = 0;
		for (int i=0; i<LevelsListGame.NUMGAMELEVELS; i++) {
			if (plyrLevelComplete.get(currentPlayer)[i]==1) nlev++;
		}
		int[] emeraldList = new int[nlev];
		int cntr = 0;
		for (int i=0; i<LevelsListGame.NUMGAMELEVELS; i++) {
			if (plyrLevelComplete.get(currentPlayer)[i]==1) {
				emeraldList[cntr] = i;
				cntr++;
			};
		}
		return emeraldList;
	}

	public static int[] GetDiamondComplete() {
		int nlev = 0;
		for (int i=0; i<LevelsListGame.NUMGAMELEVELS; i++) {
			if (IsDiamondCollected(i)) nlev++;
		}
		int[] diamondList = new int[nlev];
		int cntr = 0;
		for (int i=0; i<LevelsListGame.NUMGAMELEVELS; i++) {
			if (IsDiamondCollected(i)) {
				diamondList[cntr] = i;
				cntr++;
			};
		}
		return diamondList;
	}

    public static String getTimeString(int time) {
    	String retval = "--:--:---";
    	if (time > 0) {
        	// time is in milliseconds
            int MSecs = time%1000;
        	int Secs  = ((time-MSecs)%60000)/1000;
        	int Mins  = (time-MSecs-1000*Secs)/60000;
        	retval = String.format("%02d", Mins) + ":" + String.format("%02d", Secs) + ":" + String.format("%03d", MSecs);
    	}
		return retval;
    }

	// Check the player and world record times
	public static void CheckTimes(int lvl, boolean diamond, int timerTotal) {
		int timePrev;
		if (diamond) {
			timePrev = plyrTimesDmnd.get(currentPlayer).get(lvl);
			if ((timerTotal < timePrev) | (timePrev==-1)) {
				ArrayList<Integer> copyTimes = (ArrayList<Integer>) plyrTimesDmnd.get(currentPlayer).clone();
				copyTimes.set(lvl, timerTotal);
				plyrTimesDmnd.set(currentPlayer, copyTimes);
				GameVars.StoreReplay(lvl, diamond);
				GameVars.SetPersonalBest(true);
				SavePlayers();
			}
		} else {
			timePrev = plyrTimes.get(currentPlayer).get(lvl);
			if ((timerTotal < timePrev) | (timePrev==-1)) {
				ArrayList<Integer> copyTimes = (ArrayList<Integer>) plyrTimes.get(currentPlayer).clone();
				copyTimes.set(lvl, timerTotal);
				plyrTimes.set(currentPlayer, copyTimes);
				GameVars.StoreReplay(lvl, diamond);
				GameVars.SetPersonalBest(true);
				SavePlayers();
			}
		}
	}

	// Is options
	public static boolean IsDiamondCollected(int lvl) {return plyrColDmnd.get(currentPlayer)[lvl];}
	public static boolean IsSkipLevel(int lvl) {
		if (plyrLevelComplete.get(currentPlayer)[lvl] == 2) return true;
		else return false;
	}
	
	// Set options
	public static void SetDiamond(int lvl) {
		boolean[] copyDmnd = plyrColDmnd.get(currentPlayer).clone();
		copyDmnd[lvl] = true;
		plyrColDmnd.set(currentPlayer, copyDmnd);
		SavePlayers();
	}

	public static void SetSkipLevel(int lvl) {
		if (CanSkip() && (lvl < LevelsListGame.NUMGAMELEVELS-1)) {
			int[] copyLevComp = plyrLevelComplete.get(currentPlayer).clone();
			copyLevComp[lvl] = 2;
			plyrLevelComplete.set(currentPlayer, copyLevComp);
			SavePlayers();
		}
	}

	public static void SetLevelComplete(int lvl) {
		int[] copyLevComp = plyrLevelComplete.get(currentPlayer);
		copyLevComp[lvl] = 1;
		plyrLevelComplete.set(currentPlayer, copyLevComp);
		SavePlayers();
	}

	public static Object deepClone(Object object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Replay GetReplay(int lvl, boolean diamond) {
		ReplayVars.ResetReplayCounter();
		if (diamond) {
			return (Replay) deepClone(plyrReplaysDmnd.get(currentPlayer)[lvl]);
		} else {
			return (Replay) deepClone(plyrReplays.get(currentPlayer)[lvl]);
		}
	}

	public static void StoreReplay(int lvl, boolean diamond) {
		Replay[] allReplays;
		if (diamond) {
			allReplays = plyrReplaysDmnd.get(currentPlayer).clone();
			allReplays[lvl] = ReplayVars.CopyOfCurrentReplay();
			plyrReplaysDmnd.set(currentPlayer, allReplays);
		} else {
			allReplays = plyrReplays.get(currentPlayer).clone();
			allReplays[lvl] = ReplayVars.CopyOfCurrentReplay();
			plyrReplays.set(currentPlayer, allReplays);
		}
		SavePlayers();
	}

	// Add Player
	public static void AddPlayer(int steamID, String name) {
		// Add the player Account ID
		int[] oldIDs = plyrID.clone();
		plyrID = new int[1+oldIDs.length];
		for (int i=0;i<oldIDs.length;i++) plyrID[i] = oldIDs[i];
		// make sure the steamID doesn't already exist
		for (int ss=0; ss< plyrID.length-1; ss++) {
			if (plyrID[ss] <= -2) steamID -= 1;
		}
		plyrID[oldIDs.length] = steamID;
		// Add the player name
		String[] oldNames = plyrName.clone();
		plyrName = new String[1+oldNames.length];
		for (int i=0;i<oldNames.length;i++) plyrName[i] = oldNames[i];
		plyrName[oldNames.length] = name;
		// Add the player times
		plyrTimes.add(GetEmptyTimes(LevelsListGame.NUMGAMELEVELS));
		plyrTimesDmnd.add(GetEmptyTimes(LevelsListGame.NUMGAMELEVELS));
		// Add the player controls
		plyrDisplay.add(GetDefaultDisplay());
		// Add the player controls
		plyrControls.add(GetDefaultControls());
		// Add an empty diamonds array
		plyrColDmnd.add(FalseBoolean(LevelsListGame.NUMGAMELEVELS));
		// Add an empty level skip array
		plyrLevelComplete.add(ZeroInt(LevelsListGame.NUMGAMELEVELS));
		// Add a default Bike color
		plyrBikeColor.add(GetDefaultBikeColor());
		// Add the total times
		plyrTotalTimes.add(-1);
		plyrTotalTimesDmnd.add(-1);
		// Add the Replays
		plyrReplays.add(new Replay[LevelsListGame.NUMGAMELEVELS]);
		plyrReplaysDmnd.add(new Replay[LevelsListGame.NUMGAMELEVELS]);
		plyrFullscreen.add(true);
	}

	// Methods
	public static boolean CanSkip() {
		int numSkips=0;
		for (int i=0; i<plyrLevelComplete.get(currentPlayer).length; i++) {
			if (plyrLevelComplete.get(currentPlayer)[i]==2) numSkips += 1;
		}
		if (numSkips<skipsAllowed) return true;
		else return false;
	}

	public static int SkipsLeft() {
		int numSkips=0;
		for (int i=0; i<plyrLevelComplete.get(currentPlayer).length; i++) {
			if (plyrLevelComplete.get(currentPlayer)[i]==2) numSkips += 1;
		}
		return (skipsAllowed-numSkips);
	}

	public static int GetNumLevels() {
		int numLevels=2;
		for (int i=0; i<plyrLevelComplete.get(currentPlayer).length; i++) {
			if (plyrLevelComplete.get(currentPlayer)[i]>0) numLevels += 1;
		}
		// Reduce by one if all levels are complete
		if (numLevels==2+LevelsListGame.NUMGAMELEVELS) numLevels--; 
		return numLevels;
	}

	public static int GetNumLevelsDiamond() {
		int numLevels=2;
		for (int i=0; i<plyrLevelComplete.get(currentPlayer).length; i++) {
			if (plyrLevelComplete.get(currentPlayer)[i]>0) numLevels += 1;
		}
		// Reduce by one if all levels are complete
		if (numLevels==2+LevelsListGame.NUMGAMELEVELS) numLevels--;
		return numLevels;
	}

	public static int GetLevelStatus(int lev) {
		if (lev == -1) return -1;
		return plyrLevelComplete.get(currentPlayer)[lev];
	}
	
	public static float[] GetDefaultBikeColor() {
	    //mBatch.setColor(1, 0, 1, 1); // Magenta
	    //mBatch.setColor(0.1f,0.5f,1.0f,1.0f); // Blue
		//float[] color = new float[]{0.1f,0.5f,1.0f,1.0f};  // Blue
		//float[] color = new float[]{0.1568627450980392f, 0.23529411764705882f, 0.5882352941176471f, 1.0f};  // Midnight blue
		float[] color = new float[]{0.9f,0.9f,0.9f};  // Off white
		return color.clone();
	}

	public static int[] GetDefaultControls() {
		int[] controls = new int[9];
		controls[0] = Keys.UP;
		controls[1] = Keys.DOWN;
		controls[2] = Keys.RIGHT;
		controls[3] = Keys.LEFT;
		controls[4] = Keys.SPACE;
		controls[5] = Keys.B;
		controls[6] = Keys.N;
		controls[7] = Keys.R;
		controls[8] = Keys.Q;
		return controls.clone();
	}

	public static int[] GetDefaultDisplay() {
		int[] controls = new int[6];
		controls[0] = DISPLAY_ON; // Clock
		controls[1] = DISPLAY_ON; // Personal Record
		controls[2] = DISPLAY_ON; // World Record
		controls[3] = DISPLAY_SMARTON; // Emerald Count
		controls[4] = DISPLAY_SMARTON; // Key Count
		controls[5] = DISPLAY_SMARTON; // Nitrous Count
		return controls.clone();
	}

	@SuppressWarnings("unchecked")
	public static void LoadPlayers() {
		try {
			FileInputStream fi = new FileInputStream(new File("ABstate.dat"));
			ObjectInputStream oi = new ObjectInputStream(fi);

			// Read objects
			plyrID = (int[]) oi.readObject();
			plyrName = (String[]) oi.readObject();
			plyrFullscreen = (ArrayList<Boolean>) oi.readObject();
			plyrTimes = (ArrayList<ArrayList<Integer>>) oi.readObject();
			plyrTimesDmnd = (ArrayList<ArrayList<Integer>>) oi.readObject();
			plyrTotalTimes = (ArrayList<Integer>) oi.readObject();
			plyrTotalTimesDmnd = (ArrayList<Integer>) oi.readObject();
			plyrControls = (ArrayList<int[]>) oi.readObject();
			plyrDisplay = (ArrayList<int[]>) oi.readObject();
			plyrColDmnd = (ArrayList<boolean[]>) oi.readObject();
			plyrLevelComplete = (ArrayList<int[]>) oi.readObject();
			plyrBikeColor = (ArrayList<float[]>) oi.readObject();
			plyrReplays = (ArrayList<Replay[]>) oi.readObject();
			plyrReplaysDmnd = (ArrayList<Replay[]>) oi.readObject();
			// Close files
			oi.close();
			fi.close();
		} catch (FileNotFoundException e) {
			System.out.println("Players file not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream for players file");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void SavePlayers() {
		FileOutputStream f;
		try {
			f = new FileOutputStream(new File("ABstate.dat"));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(plyrID);
			o.writeObject(plyrName);
			o.writeObject(plyrFullscreen);
			o.writeObject(plyrTimes);
			o.writeObject(plyrTimesDmnd);
			o.writeObject(plyrTotalTimes);
			o.writeObject(plyrTotalTimesDmnd);
			o.writeObject(plyrControls);
			o.writeObject(plyrDisplay);
			o.writeObject(plyrColDmnd);
			o.writeObject(plyrLevelComplete);
			o.writeObject(plyrBikeColor);
			o.writeObject(plyrReplays);
			o.writeObject(plyrReplaysDmnd);

			// Close the file
			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("Players file not found!");
		} catch (IOException e) {
			System.out.println("IO error");
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Integer> GetEmptyTimes(int numLevels) {
		ArrayList<Integer> times = new ArrayList<Integer>();
		for (int i=0; i<numLevels; i++) times.add(-1);
		return (ArrayList<Integer>) times.clone();
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String[]> GetEmptyNames(int numLevels) {
		ArrayList<String[]> names = new ArrayList<String[]>();
		String[] emptyNames = new String[numStore]; // Store the top 10 times in each level
		for (int i=0; i<numStore; i++) emptyNames[i] = ""; // "" means that a time has not been recorded for this level
		// Add a copy of this array for all levels
		for (int i=0; i<numLevels; i++) names.add(emptyNames.clone());
		return (ArrayList<String[]>) names.clone();
	}

	public static String[] GetEmptyStringArray(int num) {
		String[] emptySA = new String[num]; // Store the top 10 times in each level
		for (int i=0; i<num; i++) emptySA[i] = ""; // "" means that a time has not been recorded
		return emptySA.clone();
	}

	public static int[] GetEmptyIntArray(int num) {
		int[] emptyIA = new int[num]; // Store the top 10 times in each level
		for (int i=0; i<num; i++) emptyIA[i] = -1; // -1 means that a time has not been recorded
		return emptyIA.clone();
	}

	public static boolean[] FalseBoolean(int num) {
		boolean[] empty = new boolean[num];
		for (int i=0; i<num; i++) empty[i]=false;
		return empty.clone();
	}

	public static int[] ZeroInt(int num) {
		int[] empty = new int[num];
		for (int i=0; i<num; i++) empty[i]=0;
		return empty.clone();
	}

//	private static void GenerateFakeTimes() {
//		AddFakePlayer("Ryan");
//		AddFakePlayer("Steve");
//		AddFakePlayer("Who");
//		AddFakePlayer("Else");
//		AddFakePlayer("Wants");
//		AddFakePlayer("To");
//		AddFakePlayer("Play");
//		// Initialise the world records
//		worldNames = RandomNames(LevelsListGame.NUMGAMELEVELS);
//		worldTimes = RandomTimes(LevelsListGame.NUMGAMELEVELS);
//		worldNamesDmnd = RandomNames(LevelsListGame.NUMGAMELEVELS);
//		worldTimesDmnd = RandomTimes(LevelsListGame.NUMGAMELEVELS);
//		//GetTotalTimes(boolean train, boolean world, boolean diamond)
//		// Now generate all of the total times
//		UpdateTotalTimesAllPlayers(false);
//		worldTotalNames = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
//		worldTotalTimes = GetTotalTimes(true, false);
//		worldTotalNamesDmnd = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
//		worldTotalTimesDmnd = GetTotalTimes(true, true);
////
////		CheckTimes(int[] times, int indx, int lvl, int timerTotal, boolean world) {
////		times = plyrTimes.get(currentPlayer).get(lvl)
////		indx = 0, 1, 2, 3 = plyrTimes, plyrTimesDmnd, plyrTimesTrain, plyrTimesTrainDmnd
////
//		// Now apply all world records
//		int timerTotal;
//		for (int p=0; p<plyrID.length; p++) {
//			SetCurrentPlayer(p);
//			UpdateTotalTimes();
//			// For all game levels
//			for (int l=0; l<LevelsListGame.NUMGAMELEVELS; l++) {
//				timerTotal = plyrTimes.get(currentPlayer).get(l)[0];
//				CheckTimes(worldTimes.get(l), 0, l, timerTotal, true);
//			}
//		}
//
//	}

//	private static void AddFakePlayer(int name) {
//		int[] oldNames = plyrID.clone();
//		plyrID = new int[1+oldNames.length];
//		for (int i=0;i<oldNames.length;i++) plyrID[i] = oldNames[i];
//		plyrID[oldNames.length] = name;
//		// Add the player times
//		plyrTimes.add(RandomTimes(LevelsListGame.NUMGAMELEVELS));
//		plyrTimesDmnd.add(RandomTimes(LevelsListGame.NUMGAMELEVELS));
//		// Add the player display preferences
//		plyrDisplay.add(GetDefaultDisplay());
//		// Add the player controls
//		plyrControls.add(GetDefaultControls());
//		// Add an empty diamonds array
//		plyrColDmnd.add(FalseBoolean(LevelsListGame.NUMGAMELEVELS));
//		// Add a completed levels array
//		plyrLevelComplete.add(ValueInt(LevelsListGame.NUMGAMELEVELS, 1));
//		// Add a default Bike color
//		plyrBikeColor.add(GetDefaultBikeColor());
//		// Add the total times
//		plyrTotalTimes.add(ValueInt(numStore, -1));
//		plyrTotalTimesDmnd.add(ValueInt(numStore, -1));
//	}
//
//	@SuppressWarnings("unchecked")
//	private static ArrayList<int[]> RandomTimes(int numLevels) {
//		int min = 1160000, max = 1170000;
//		ArrayList<int[]> times = new ArrayList<int[]>();
//		int[] emptyTimes = new int[numStore]; // Store the top 10 times in each level
//		for (int l=0; l<numLevels; l++) {
//			for (int i=0; i<numStore; i++) emptyTimes[i] = getRandomNumberInRange(min, max);
//			Arrays.sort(emptyTimes);
//			times.add(emptyTimes.clone());
//		}
//		return (ArrayList<int[]>) times.clone();
//	}
//
//	@SuppressWarnings("unchecked")
//	private static ArrayList<String[]> RandomNames(int numLevels) {
//		ArrayList<String[]> names = new ArrayList<String[]>();
//		String[] emptyNames = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"}; // Generate 10 names
//		for (int l=0; l<numLevels; l++) {
//			names.add(emptyNames.clone());
//		}
//		return (ArrayList<String[]>) names.clone();
//	}
//
//	private static int getRandomNumberInRange(int min, int max) {
//
//		if (min >= max) {
//			throw new IllegalArgumentException("max must be greater than min");
//		}
//
//		Random r = new Random();
//		return r.nextInt((max - min) + 1) + min;
//	}
}
