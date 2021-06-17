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
	public static final int DISPLAY_OFF = 0;
	public static final int DISPLAY_ON = 1;
	public static final int DISPLAY_SMARTON = 2;
	// Latest time
	public static int timerTotal = -1;
	// Player arrays
	public static int currentPlayer = -1;
	public static int currentSteamUser = -1;
	public static boolean personalBest = false;
	public static boolean worldRecord = false;
	public static int[] plyrID = new int[0];
	public static ArrayList<Replay[]> plyrReplays = new ArrayList<Replay[]>();
	public static ArrayList<Replay[]> plyrReplaysDmnd = new ArrayList<Replay[]>();
	public static ArrayList<ArrayList<int[]>> plyrTimes = new ArrayList<ArrayList<int[]>>();
	public static ArrayList<ArrayList<int[]>> plyrTimesDmnd = new ArrayList<ArrayList<int[]>>();
	public static ArrayList<int[]> plyrControls = new ArrayList<int[]>();
	public static ArrayList<int[]> plyrDisplay = new ArrayList<int[]>();
	public static ArrayList<Boolean> plyrFullscreen = new ArrayList<Boolean>();
	public static ArrayList<boolean[]> plyrColDmnd = new ArrayList<boolean[]>();
	public static ArrayList<int[]> plyrLevelComplete = new ArrayList<int[]>();
	public static ArrayList<float[]> plyrBikeColor = new ArrayList<float[]>();
	public static ArrayList<int[]> plyrTotalTimes = new ArrayList<int[]>();
	public static ArrayList<int[]> plyrTotalTimesDmnd = new ArrayList<int[]>();
	// World records
//	public static ArrayList<int[]> worldNames = new ArrayList<int[]>();
//	public static ArrayList<int[]> worldTimes = new ArrayList<int[]>();
//	public static ArrayList<int[]> worldNamesDmnd = new ArrayList<int[]>();
//	public static ArrayList<int[]> worldTimesDmnd = new ArrayList<int[]>();
	// World Total Times
	public static int[] worldTotalTimes, worldTotalTimesDmnd;
	public static int[] worldTotalNames, worldTotalNamesDmnd;

	// Get and Set options
	public static void SetCurrentPlayer(int id) {
		for (int ii=0; ii < plyrID.length; ii++) {
			if (id==plyrID[ii]) {
				currentPlayer = ii;
				return;
			}
		}
		// If we get to here and no player ID has been found, then we need to add it here
		AddPlayer(id);
		SavePlayers();
	}

	// Get and Set options
	public static void SetTimerTotal(int i) {timerTotal = i;}
	public static void SetWorldRecord(boolean wr) {worldRecord = wr;}
	public static void SetPersonalBest(boolean pb) {personalBest = pb;}

	public static int GetCurrentPlayer() {return currentPlayer;}

	// Get Player properties
	public static boolean GetPlayerFullscreen() {return plyrFullscreen.get(currentPlayer);}
	public static void SetPlayerFullscreen(boolean value) {
		plyrFullscreen.set(currentPlayer, value);
		SavePlayers();
	}
	public static int GetPlayerTimes(int lvl, int indx) {return plyrTimes.get(currentPlayer).get(lvl)[indx];}
	public static int GetPlayerTimesDmnd(int lvl, int indx) {return plyrTimesDmnd.get(currentPlayer).get(lvl)[indx];}
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
		// Check if every player's best total time beats any of the world total times
		for (int pp=0; pp<plyrID.length; pp++) {
			CheckWorldTotalTimes(plyrID[pp], worldTotalTimes.clone(), 0, plyrTotalTimes.get(pp)[0]);
			CheckWorldTotalTimes(plyrID[pp], worldTotalTimesDmnd.clone(), 1, plyrTotalTimesDmnd.get(pp)[0]);
		}
		SavePlayers();
//		SaveWorldRecords();
	}

	public static void UpdateTotalTimesAllPlayers(boolean world) {
		// Check if every player's best total time beats any of the world total times
		for (int pp=0; pp<plyrID.length; pp++) {
			// Do the Player total times first
			plyrTotalTimes.set(pp, GetTotalTimesPlayer(pp, false));
			plyrTotalTimesDmnd.set(pp, GetTotalTimesPlayer(pp, true));
			if (world) {
				CheckWorldTotalTimes(plyrID[pp], worldTotalTimes.clone(), 0, plyrTotalTimes.get(pp)[0]);
				CheckWorldTotalTimes(plyrID[pp], worldTotalTimesDmnd.clone(), 1, plyrTotalTimesDmnd.get(pp)[0]);
			}
		}
		SavePlayers();
//		if (world) SaveWorldRecords();
	}

	public static int[] GetTotalTimes(boolean diamond) {
		int[] totalTimes = new int[numStore];
		int timeVal;
		int numLevels = LevelsListGame.NUMGAMELEVELS;
		for (int nn=0; nn<numStore; nn++) {
			for (int ll=0; ll<numLevels; ll++) {
				// First get the relevant time
				timeVal = -1;
				if (diamond) timeVal = GetPlayerTimesDmnd(ll, nn);
				else timeVal = GetPlayerTimes(ll, nn);
				// Check the time is valid
				if (timeVal != -1) totalTimes[nn] += timeVal;
				else {
					totalTimes[nn] = -1;
					break;
				}
			}
		}
		return totalTimes.clone();
	}

	public static int[] GetTotalTimesPlayer(int plyr, boolean diamond) {
		int[] totalTimes = new int[numStore];
		int timeVal;
		int numLevels = LevelsListGame.NUMGAMELEVELS;
		for (int nn=0; nn<numStore; nn++) {
			for (int ll=0; ll<numLevels; ll++) {
				// First get the relevant time
				timeVal = -1;
				if (diamond) timeVal = plyrTimesDmnd.get(plyr).get(ll)[nn];
				else timeVal = plyrTimes.get(plyr).get(ll)[nn];
				// Check the time is valid
				if (timeVal != -1) totalTimes[nn] += timeVal;
				else {
					totalTimes[nn] = -1;
					break;
				}
			}
		}
		return totalTimes.clone();
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
//	public static void CheckTimes(int[] times, int indx, int lvl, int timerTotal, boolean world) {
//		// times = plyrTimes.get(currentPlayer).get(lvl)
//		// indx = 0, 1, 2, 3 = plyrTimes, plyrTimesDmnd, plyrTimesTrain, plyrTimesTrainDmnd
//		// Need to fetch names for the world records
//		String[] names = new String[numStore];
//		int plyrName = GetPlayerName();
//		if (world) {
//			if (indx == 0) names = worldNames.get(lvl).clone();
//			else if (indx == 1) names = worldNamesDmnd.get(lvl).clone();
//		}
//		boolean saveTimes = false;
//		int[] tempTime = times.clone();
//		for (int i=0; i<numStore; i++) {
//			if (saveTimes) {
//				// Shifting down times, as long as a player doesn't already have a world record
//				tempTime[i] = times[i-1];
//				if (world) {
//					if (indx == 0) {
//						names[i] = worldNames.get(lvl).clone()[i-1];
//						if (plyrName.compareTo(worldNames.get(lvl)[i])==0) break;
//					}
//					else if (indx == 1) {
//						names[i] = worldNamesDmnd.get(lvl).clone()[i-1];
//						if (plyrName.compareTo(worldNamesDmnd.get(lvl)[i])==0) break;
//					}
//				}
//			} else if ((timerTotal < times[i]) | (times[i] == -1)) {
//				if (i==0) SetPersonalBest(true);
//				tempTime[i] = timerTotal;
//				saveTimes = true;
//				if (world) {
//					names[i] = plyrName;
//					if (i==0) SetWorldRecord(true);
//				}
//			}
//			// If the current Player has a previous record that's faster, don't add their name to the list
//			if (world) {
//				if ((indx == 0) && (plyrName.compareTo(worldNames.get(lvl)[i])==0)) break;
//				else if ((indx == 1) && (plyrName.compareTo(worldNamesDmnd.get(lvl)[i])==0)) break;
//			}
//
//		}
//		if (saveTimes) {
//			if (world) {
//				// First update the arrays
//				if (indx == 0) {
//					worldNames.set(lvl, names);
//					worldTimes.set(lvl, tempTime);
//				} else if (indx == 1) {
//					worldNamesDmnd.set(lvl, names);
//					worldTimesDmnd.set(lvl, tempTime);
//				}
//				SaveWorldRecords();
//			} else {
//				// First update the arrays
//				if (indx == 0) {
//					ArrayList<int[]> copyTimes = plyrTimes.get(currentPlayer);
//					copyTimes.set(lvl, tempTime);
//					plyrTimes.set(currentPlayer, copyTimes);
//				} else if (indx == 1) {
//					ArrayList<int[]> copyTimes = plyrTimesDmnd.get(currentPlayer);
//					copyTimes.set(lvl, tempTime);
//					plyrTimesDmnd.set(currentPlayer, copyTimes);
//				}
//				SavePlayers();
//			}
//		}
//	}

	// Check the player and world record times
	public static void CheckWorldTotalTimes(int plyrName, int[] times, int indx, int timerTotal) {
		// times = plyrTotalTimes.get(currentPlayer)
		// indx = 0, 1, 2, 3 = plyrTimes, plyrTimesDmnd, plyrTimesTrain, plyrTimesTrainDmnd
		if (timerTotal==-1) return;
		// Need to fetch names for the world records
		int[] names = new int[numStore];
		if (indx == 0) names = worldTotalNames.clone();
		else if (indx == 1) names = worldTotalNamesDmnd.clone();
		boolean saveTimes = false;
		int[] tempTime = times.clone();
		for (int i=0; i<numStore; i++) {
			if (saveTimes) {
				// Shifting down times, as long as a player doesn't already have a world record
				tempTime[i] = times[i-1];
				if (indx == 0) {
					names[i] = worldTotalNames.clone()[i-1];
					if (plyrName == worldTotalNames[i]) break;
				}
				else if (indx == 1) {
					names[i] = worldTotalNamesDmnd.clone()[i-1];
					if (plyrName == worldTotalNamesDmnd[i]) break;
				}
			} else if ((timerTotal < times[i]) | (times[i] == -1)) {
				tempTime[i] = timerTotal;
				saveTimes = true;
				names[i] = plyrName;
			}
			// If the current Player has a previous record that's faster, don't add their name to the list
			if ((indx == 0) && (plyrName == worldTotalNames[i])) break;
			else if ((indx == 1) && (plyrName == worldTotalNamesDmnd[i])) break;
		}
		if (saveTimes) {
			// First update the arrays
			if (indx == 0) {
				worldTotalNames = names.clone();
				worldTotalTimes = tempTime.clone();
			} else if (indx == 1) {
				worldTotalNamesDmnd = names.clone();
				worldTotalTimesDmnd = tempTime.clone();
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
	public static void AddPlayer(int steamID) {
		// Add the player name
		int[] oldNames = plyrID.clone();
		plyrID = new int[1+oldNames.length];
		for (int i=0;i<oldNames.length;i++) plyrID[i] = oldNames[i];
		plyrID[oldNames.length] = steamID;
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
		plyrTotalTimes.add(ValueInt(numStore, -1));
		plyrTotalTimesDmnd.add(ValueInt(numStore, -1));
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
		int[] controls = new int[8];
		controls[0] = Keys.UP;
		controls[1] = Keys.DOWN;
		controls[2] = Keys.RIGHT;
		controls[3] = Keys.LEFT;
		controls[4] = Keys.SPACE;
		controls[5] = Keys.B;
		controls[6] = Keys.N;
		controls[7] = Keys.R;
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
			plyrFullscreen = (ArrayList<Boolean>) oi.readObject();
			plyrTimes = (ArrayList<ArrayList<int[]>>) oi.readObject();
			plyrTimesDmnd = (ArrayList<ArrayList<int[]>>) oi.readObject();
			plyrTotalTimes = (ArrayList<int[]>) oi.readObject();
			plyrTotalTimesDmnd = (ArrayList<int[]>) oi.readObject();
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

//	@SuppressWarnings("unchecked")
//	public static void LoadWorldRecords() {
//		boolean failed = true;
//		try {
//			FileInputStream fi = new FileInputStream(new File("WorldRecords.dat"));
//			ObjectInputStream oi = new ObjectInputStream(fi);
//
//			// Read objects
//			worldNames = (ArrayList<int[]>) oi.readObject();
//			worldTimes = (ArrayList<int[]>) oi.readObject();
//			worldNamesDmnd = (ArrayList<String[]>) oi.readObject();
//			worldTimesDmnd = (ArrayList<int[]>) oi.readObject();
//			// Write the total times
//			worldTotalNames = (String[]) oi.readObject();
//			worldTotalTimes = (int[]) oi.readObject();
//			worldTotalNamesDmnd = (String[]) oi.readObject();
//			worldTotalTimesDmnd = (int[]) oi.readObject();
//
//			// Close files
//			oi.close();
//			fi.close();
//			failed = false;
//		} catch (FileNotFoundException e) {
//			System.out.println("World records file not found");
//		} catch (IOException e) {
//			System.out.println("Error initializing stream for world records");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		if (failed) {
////			GenerateFakeTimes();
////			return;
//			worldNames = GetEmptyNames(LevelsListGame.NUMGAMELEVELS);
//			worldTimes = GetEmptyTimes(LevelsListGame.NUMGAMELEVELS);
//			worldNamesDmnd = GetEmptyNames(LevelsListGame.NUMGAMELEVELS);
//			worldTimesDmnd = GetEmptyTimes(LevelsListGame.NUMGAMELEVELS);
////			 Now the total times
//			worldTotalNames = GetEmptyStringArray(numStore);
//			worldTotalTimes = GetEmptyIntArray(numStore);
//			worldTotalNamesDmnd = GetEmptyStringArray(numStore);
//			worldTotalTimesDmnd = GetEmptyIntArray(numStore);
//		}
//	}

//	public static void SaveWorldRecords() {
//		FileOutputStream f;
//		try {
//			f = new FileOutputStream(new File("WorldRecords.dat"));
//			ObjectOutputStream o = new ObjectOutputStream(f);
//
//			// Write objects to file
//			o.writeObject(worldNames);
//			o.writeObject(worldTimes);
//			o.writeObject(worldNamesDmnd);
//			o.writeObject(worldTimesDmnd);
//			// Write the total times
//			o.writeObject(worldTotalNames);
//			o.writeObject(worldTotalTimes);
//			o.writeObject(worldTotalNamesDmnd);
//			o.writeObject(worldTotalTimesDmnd);
//
//			// Close the file
//			o.close();
//			f.close();
//		} catch (FileNotFoundException e) {
//			System.out.println("File not found");
//		} catch (IOException e) {
//			System.out.println("Error initializing stream");
//		}
//	}

	@SuppressWarnings("unchecked")
	public static ArrayList<int[]> GetEmptyTimes(int numLevels) {
		ArrayList<int[]> times = new ArrayList<int[]>();
		int[] emptyTimes = new int[numStore]; // Store the top 10 times in each level
		for (int i=0; i<numStore; i++) emptyTimes[i] = -1; // -1 means that a time has not been recorded for this level
		// Add a copy of this array for all levels
		for (int i=0; i<numLevels; i++) times.add(emptyTimes.clone());
		return (ArrayList<int[]>) times.clone();
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

	public static int[] ValueInt(int num, int val) {
		int[] empty = new int[num];
		for (int i=0; i<num; i++) empty[i]=val;
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
