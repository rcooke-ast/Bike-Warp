/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
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
public class GameVars implements Serializable {

	private static final long serialVersionUID = 1L;

	// How many level skips are allowed
	public static final int skipsAllowed = 3; // Allow players to skip 3 levels
	public static final int numStore = 10; // Store the top 10 times of each level
	// Latest time
	public static int timerTotal = -1;
	// Player arrays
	public static int currentPlayer = -1;
	public static String[] plyrNames = new String[0];
	public static ArrayList<ArrayList<int[]>> plyrTimes = new ArrayList<ArrayList<int[]>>();
	public static ArrayList<ArrayList<int[]>> plyrTimesDmnd = new ArrayList<ArrayList<int[]>>();
	public static ArrayList<ArrayList<int[]>> plyrTimesTrain = new ArrayList<ArrayList<int[]>>();
	public static ArrayList<ArrayList<int[]>> plyrTimesTrainDmnd = new ArrayList<ArrayList<int[]>>();
	public static ArrayList<int[]> plyrControls = new ArrayList<int[]>();
	public static ArrayList<boolean[]> plyrColDmnd = new ArrayList<boolean[]>();
	public static ArrayList<boolean[]> plyrColTrainDmnd = new ArrayList<boolean[]>();
	public static ArrayList<int[]> plyrLevelComplete = new ArrayList<int[]>();
	public static ArrayList<float[]> plyrBikeColor = new ArrayList<float[]>();
	public static ArrayList<int[]> plyrTotalTimes = new ArrayList<int[]>();
	public static ArrayList<int[]> plyrTotalTimesTrain = new ArrayList<int[]>();
	public static ArrayList<int[]> plyrTotalTimesDmnd = new ArrayList<int[]>();
	public static ArrayList<int[]> plyrTotalTimesTrainDmnd = new ArrayList<int[]>();
	// World records
	public static ArrayList<String[]> worldNames = new ArrayList<String[]>();
	public static ArrayList<int[]> worldTimes = new ArrayList<int[]>();
	public static ArrayList<String[]> worldNamesDmnd = new ArrayList<String[]>();
	public static ArrayList<int[]> worldTimesDmnd = new ArrayList<int[]>();
	public static ArrayList<String[]> worldNamesTrain = new ArrayList<String[]>();
	public static ArrayList<int[]> worldTimesTrain = new ArrayList<int[]>();
	public static ArrayList<String[]> worldNamesTrainDmnd = new ArrayList<String[]>();
	public static ArrayList<int[]> worldTimesTrainDmnd = new ArrayList<int[]>();
	// World Total Times
	public static int[] worldTotalTimes, worldTotalTimesTrain, worldTotalTimesDmnd, worldTotalTimesTrainDmnd;
	public static String[] worldTotalNames, worldTotalNamesTrain, worldTotalNamesDmnd, worldTotalNamesTrainDmnd;

	// Get and Set options
	public static void SetCurrentPlayer(int i) {
		currentPlayer = i;
		// Set the keys of this user
	}

	// Get and Set options
	public static void SetTimerTotal(int i) {
		timerTotal = i;
	}

	public static int GetCurrentPlayer() {return currentPlayer;}

	// Get Player properties
	public static String GetPlayerName() {return plyrNames[currentPlayer];}
	public static int GetPlayerTimes(int lvl, int indx) {return plyrTimes.get(currentPlayer).get(lvl)[indx];}
	public static int GetPlayerTimesDmnd(int lvl, int indx) {return plyrTimesDmnd.get(currentPlayer).get(lvl)[indx];}
	public static int GetPlayerTimesTrain(int lvl, int indx) {return plyrTimesTrain.get(currentPlayer).get(lvl)[indx];}
	public static int GetPlayerTimesTrainDmnd(int lvl, int indx) {return plyrTimesTrainDmnd.get(currentPlayer).get(lvl)[indx];}
	public static int[] GetPlayerControls() {return plyrControls.get(currentPlayer);}
	public static boolean[] GetPlayerDiamonds() {return plyrColDmnd.get(currentPlayer);}
	public static boolean[] GetPlayerDiamondsTrain() {return plyrColTrainDmnd.get(currentPlayer);}
	public static int[] GetPlayerSkipLevel() {return plyrLevelComplete.get(currentPlayer);}
	public static float[] GetPlayerBikeColor() {return plyrBikeColor.get(currentPlayer);}
	public static void SetPlayerBikeColor(float[] rgb) {plyrBikeColor.set(currentPlayer, rgb.clone());}
	// Get the world record times and aliases
	public static int GetWorldTimes(int lvl, int indx) {return worldTimes.get(lvl)[indx];}
	public static int GetWorldTimesDmnd(int lvl, int indx) {return worldTimesDmnd.get(lvl)[indx];}
	public static int GetWorldTimesTrain(int lvl, int indx) {return worldTimesTrain.get(lvl)[indx];}
	public static int GetWorldTimesTrainDmnd(int lvl, int indx) {return worldTimesTrainDmnd.get(lvl)[indx];}
	public static String GetWorldNames(int lvl, int indx) {return worldNames.get(lvl)[indx];}
	public static String GetWorldNamesDmnd(int lvl, int indx) {return worldNamesDmnd.get(lvl)[indx];}
	public static String GetWorldNamesTrain(int lvl, int indx) {return worldNamesTrain.get(lvl)[indx];}
	public static String GetWorldNamesTrainDmnd(int lvl, int indx) {return worldNamesTrainDmnd.get(lvl)[indx];}

	public static void UpdateTotalTimes() {
		// Do the Player total times first
		plyrTotalTimes.set(currentPlayer, GetTotalTimes(false, false, false));
		plyrTotalTimesTrain.set(currentPlayer, GetTotalTimes(true, false, false));
		plyrTotalTimesDmnd.set(currentPlayer, GetTotalTimes(false, false, true));
		plyrTotalTimesTrainDmnd.set(currentPlayer, GetTotalTimes(true, false, true));
		// TODO :: update world total times
//		worldTotalNames = (String[]) oi.readObject();
//		worldTotalTimes = (int[]) oi.readObject();
//		worldTotalNamesDmnd = (String[]) oi.readObject();
//		worldTotalTimesDmnd = (int[]) oi.readObject();
//		worldTotalNamesTrain = (String[]) oi.readObject();
//		worldTotalTimesTrain = (int[]) oi.readObject();
//		worldTotalNamesTrainDmnd = (String[]) oi.readObject();
//		worldTotalTimesTrainDmnd = (int[]) oi.readObject();
		SavePlayers();
	}
	
	public static int[] GetTotalTimes(boolean train, boolean world, boolean diamond) {
		int[] totalTimes = new int[numStore];
		int timeVal;
		int numLevels = LevelsListGame.NUMGAMELEVELS;
		if (train) numLevels = LevelsListTraining.NUMTRAINLEVELS;
		for (int nn=0; nn<numStore; nn++) {
			for (int ll=0; ll<numLevels; ll++) {
				// First get the relevant time
				timeVal = -1;
				if (world && train && diamond) timeVal = GetWorldTimesTrainDmnd(ll, nn);
				else if (world && train && !diamond) timeVal = GetWorldTimesTrain(ll, nn);
				else if (world && !train && diamond) timeVal = GetWorldTimesDmnd(ll, nn);
				else if (world && !train && !diamond) timeVal = GetWorldTimes(ll, nn);
				else if (!world && train && diamond) timeVal = GetPlayerTimesTrainDmnd(ll, nn);
				else if (!world && train && !diamond) timeVal = GetPlayerTimesTrain(ll, nn);
				else if (!world && !train && diamond) timeVal = GetPlayerTimesDmnd(ll, nn);
				else if (!world && !train && !diamond) timeVal = GetPlayerTimes(ll, nn);
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
	public static void CheckTimes(int[] times, int indx, int lvl, int timerTotal, boolean world) {
		// times = plyrTimes.get(currentPlayer).get(lvl)
		// indx = 0, 1, 2, 3 = plyrTimes, plyrTimesDmnd, plyrTimesTrain, plyrTimesTrainDmnd
		// Need to fetch names for the world records
		String[] names = new String[numStore];
		String plyrName = GetPlayerName();
		if (world) {
			if (indx == 0) names = worldNames.get(lvl).clone();
			else if (indx == 1) names = worldNamesDmnd.get(lvl).clone();
			else if (indx == 2) names = worldNamesTrain.get(lvl).clone();
			else if (indx == 3) names = worldNamesTrainDmnd.get(lvl).clone();
		}
		boolean saveTimes = false;
		int[] tempTime = times.clone();
		for (int i=0; i<numStore; i++) {
			if (saveTimes) {
				// Shifting down times, as long as a player doesn't already have a world record
				tempTime[i] = times[i-1];
				if (world) {
					if (indx == 0) {
						names[i] = worldNames.get(lvl).clone()[i-1];
						if (plyrName.compareTo(worldNames.get(lvl)[i])==0) break;
					}
					else if (indx == 1) {
						names[i] = worldNamesDmnd.get(lvl).clone()[i-1];
						if (plyrName.compareTo(worldNamesDmnd.get(lvl)[i])==0) break;
					}
					else if (indx == 2) {
						names[i] = worldNamesTrain.get(lvl).clone()[i-1];
						if (plyrName.compareTo(worldNamesTrain.get(lvl)[i])==0) break;						
					}
					else if (indx == 3) {
						names[i] = worldNamesTrainDmnd.get(lvl).clone()[i-1];
						if (plyrName.compareTo(worldNamesTrainDmnd.get(lvl)[i])==0) break;
					}
				}
			} else if ((timerTotal < times[i]) | (times[i] == -1)) {
				tempTime[i] = timerTotal;
				saveTimes = true;
				if (world) names[i] = plyrName;
			}
			// If the current Player has a previous record that's faster, don't add their name to the list
			if (world) {
				if ((indx == 0) && (plyrName.compareTo(worldNames.get(lvl)[i])==0)) break;
				else if ((indx == 1) && (plyrName.compareTo(worldNamesDmnd.get(lvl)[i])==0)) break;
				else if ((indx == 2) && (plyrName.compareTo(worldNamesTrain.get(lvl)[i])==0)) break;
				else if ((indx == 3) && (plyrName.compareTo(worldNamesTrainDmnd.get(lvl)[i])==0)) break;
			}

		}
		if (saveTimes) {
			if (world) {
				// First update the arrays
				if (indx == 0) {
					worldNames.set(lvl, names);
					worldTimes.set(lvl, tempTime);
				} else if (indx == 1) {
					worldNamesDmnd.set(lvl, names);
					worldTimesDmnd.set(lvl, tempTime);
				} else if (indx == 2) {
					worldNamesTrain.set(lvl, names);
					worldTimesTrain.set(lvl, tempTime);
				} else if (indx == 3) {
					worldNamesTrainDmnd.set(lvl, names);
					worldTimesTrainDmnd.set(lvl, tempTime);
				}
				SaveWorldRecords();
			} else {
				// First update the arrays
				if (indx == 0) {
					ArrayList<int[]> copyTimes = plyrTimes.get(currentPlayer);
					copyTimes.set(lvl, tempTime);
					plyrTimes.set(currentPlayer, copyTimes);
				} else if (indx == 1) {
					ArrayList<int[]> copyTimes = plyrTimesDmnd.get(currentPlayer);
					copyTimes.set(lvl, tempTime);
					plyrTimesDmnd.set(currentPlayer, copyTimes);				
				} else if (indx == 2) {
					ArrayList<int[]> copyTimes = plyrTimesTrain.get(currentPlayer);
					copyTimes.set(lvl, tempTime);
					plyrTimesTrain.set(currentPlayer, copyTimes);
				} else if (indx == 3) {
					ArrayList<int[]> copyTimes = plyrTimesTrainDmnd.get(currentPlayer);
					copyTimes.set(lvl, tempTime);
					plyrTimesTrainDmnd.set(currentPlayer, copyTimes);
				}
				SavePlayers();
			}
		}
	}

	// Is options
	public static boolean IsDiamondCollected(int lvl) {return plyrColDmnd.get(currentPlayer)[lvl];}
	public static boolean IsDiamondCollectedTrain(int lvl) {return plyrColTrainDmnd.get(currentPlayer)[lvl];}
	public static boolean IsSkipLevel(int lvl) {
		if (plyrLevelComplete.get(currentPlayer)[lvl] == 2) return true;
		else return false;
	}
	
	// Set options
	public static void SetDiamond(int lvl) {
		boolean[] copyDmnd = plyrColDmnd.get(currentPlayer);
		copyDmnd[lvl] = true;
		plyrColDmnd.set(currentPlayer, copyDmnd);
		SavePlayers();
	}

	public static void SetDiamondTrain(int lvl) {
		boolean[] copyDmnd = plyrColTrainDmnd.get(currentPlayer);
		copyDmnd[lvl] = true;
		plyrColTrainDmnd.set(currentPlayer, copyDmnd);
		SavePlayers();
	}
	
	public static void SetSkipLevel(int lvl) {
		if (CanSkip()) {
			int[] copyLevComp = plyrLevelComplete.get(currentPlayer);
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

	// Add Player
	public static void AddPlayer(String name) {
		// Add the player name
		String[] oldNames = plyrNames.clone();
		plyrNames = new String[1+oldNames.length];
		for (int i=0;i<oldNames.length;i++) plyrNames[i] = oldNames[i];
		plyrNames[oldNames.length] = name;
		// Add the player times
		plyrTimes.add(GetEmptyTimes(LevelsListGame.NUMGAMELEVELS));
		plyrTimesDmnd.add(GetEmptyTimes(LevelsListGame.NUMGAMELEVELS));
		plyrTimesTrain.add(GetEmptyTimes(LevelsListTraining.NUMTRAINLEVELS));
		plyrTimesTrainDmnd.add(GetEmptyTimes(LevelsListTraining.NUMTRAINLEVELS));
		// Add the player controls
		plyrControls.add(GetDefaultControls());
		// Add an empty diamonds array
		plyrColDmnd.add(FalseBoolean(LevelsListGame.NUMGAMELEVELS));
		// Add an empty diamonds training array
		plyrColTrainDmnd.add(FalseBoolean(LevelsListTraining.NUMTRAINLEVELS));
		// Add an empty level skip array
		plyrLevelComplete.add(ZeroInt(LevelsListGame.NUMGAMELEVELS));
		// Add a default Bike color
		plyrBikeColor.add(GetDefaultBikeColor());
		// Add the total times
		plyrTotalTimes.add(ValueInt(numStore, -1));
		plyrTotalTimesTrain.add(ValueInt(numStore, -1));
		plyrTotalTimesDmnd.add(ValueInt(numStore, -1));
		plyrTotalTimesTrainDmnd.add(ValueInt(numStore, -1));
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
		return numLevels;
	}

	public static int GetLevelStatus(int lev) {
		if (lev == -1) return -1;
		return plyrLevelComplete.get(currentPlayer)[lev];
	}
	
	public static float[] GetDefaultBikeColor() {
	    //mBatch.setColor(1, 0, 1, 1); // Magenta
	    //mBatch.setColor(0.1f,0.5f,1.0f,1.0f); // Blue
		float[] color = new float[]{0.1f,0.5f,1.0f,1.0f};
		return color.clone();
	}

	public static int[] GetDefaultControls() {
		int[] controls = new int[6];
		controls[0] = Keys.UP;
		controls[1] = Keys.DOWN;
		controls[2] = Keys.LEFT;
		controls[3] = Keys.RIGHT;
		controls[4] = Keys.SPACE;
		controls[5] = Keys.A;
		return controls.clone();
	}

	@SuppressWarnings("unchecked")
	public static void LoadPlayers() {
		try {
			FileInputStream fi = new FileInputStream(new File("BGstate.dat"));
			ObjectInputStream oi = new ObjectInputStream(fi);

			// Read objects
			plyrNames = (String[]) oi.readObject();
			plyrTimes = (ArrayList<ArrayList<int[]>>) oi.readObject();
			plyrTimesDmnd = (ArrayList<ArrayList<int[]>>) oi.readObject();
			plyrTimesTrain = (ArrayList<ArrayList<int[]>>) oi.readObject();
			plyrTimesTrainDmnd = (ArrayList<ArrayList<int[]>>) oi.readObject();
			plyrTotalTimes = (ArrayList<int[]>) oi.readObject();
			plyrTotalTimesTrain = (ArrayList<int[]>) oi.readObject();
			plyrTotalTimesDmnd = (ArrayList<int[]>) oi.readObject();
			plyrTotalTimesTrainDmnd = (ArrayList<int[]>) oi.readObject();
			plyrControls = (ArrayList<int[]>) oi.readObject();
			plyrColDmnd = (ArrayList<boolean[]>) oi.readObject();
			plyrColTrainDmnd = (ArrayList<boolean[]>) oi.readObject();
			plyrLevelComplete = (ArrayList<int[]>) oi.readObject();
			plyrBikeColor = (ArrayList<float[]>) oi.readObject();

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
			f = new FileOutputStream(new File("BGstate.dat"));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(plyrNames);
			o.writeObject(plyrTimes);
			o.writeObject(plyrTimesDmnd);
			o.writeObject(plyrTimesTrain);
			o.writeObject(plyrTimesTrainDmnd);
			o.writeObject(plyrTotalTimes);
			o.writeObject(plyrTotalTimesTrain);
			o.writeObject(plyrTotalTimesDmnd);
			o.writeObject(plyrTotalTimesTrainDmnd);
			o.writeObject(plyrControls);
			o.writeObject(plyrColDmnd);
			o.writeObject(plyrColTrainDmnd);
			o.writeObject(plyrLevelComplete);
			o.writeObject(plyrBikeColor);

			// Close the file
			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("World records file not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream for world records");
		}
	}

	@SuppressWarnings("unchecked")
	public static void LoadWorldRecords() {
		boolean failed = true;
		try {
			FileInputStream fi = new FileInputStream(new File("WorldRecords.dat"));
			ObjectInputStream oi = new ObjectInputStream(fi);

			// Read objects
			worldNames = (ArrayList<String[]>) oi.readObject();
			worldTimes = (ArrayList<int[]>) oi.readObject();
			worldNamesDmnd = (ArrayList<String[]>) oi.readObject();
			worldTimesDmnd = (ArrayList<int[]>) oi.readObject();
			worldNamesTrain = (ArrayList<String[]>) oi.readObject();
			worldTimesTrain = (ArrayList<int[]>) oi.readObject();
			worldNamesTrainDmnd = (ArrayList<String[]>) oi.readObject();
			worldTimesTrainDmnd = (ArrayList<int[]>) oi.readObject();
			// Write the total times
			worldTotalNames = (String[]) oi.readObject();
			worldTotalTimes = (int[]) oi.readObject();
			worldTotalNamesDmnd = (String[]) oi.readObject();
			worldTotalTimesDmnd = (int[]) oi.readObject();
			worldTotalNamesTrain = (String[]) oi.readObject();
			worldTotalTimesTrain = (int[]) oi.readObject();
			worldTotalNamesTrainDmnd = (String[]) oi.readObject();
			worldTotalTimesTrainDmnd = (int[]) oi.readObject();

			// Close files
			oi.close();
			fi.close();
			failed = false;
		} catch (FileNotFoundException e) {
			System.out.println("World records file not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream for world records");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (failed) {
			GenerateFakeTimes();
			return;
//			worldNames = GetEmptyNames(LevelsListGame.NUMGAMELEVELS);
//			worldTimes = GetEmptyTimes(LevelsListGame.NUMGAMELEVELS);
//			worldNamesDmnd = GetEmptyNames(LevelsListGame.NUMGAMELEVELS);
//			worldTimesDmnd = GetEmptyTimes(LevelsListGame.NUMGAMELEVELS);
//			worldNamesTrain = GetEmptyNames(LevelsListTraining.NUMTRAINLEVELS);
//			worldTimesTrain = GetEmptyTimes(LevelsListTraining.NUMTRAINLEVELS);
//			worldNamesTrainDmnd = GetEmptyNames(LevelsListTraining.NUMTRAINLEVELS);
//			worldTimesTrainDmnd = GetEmptyTimes(LevelsListTraining.NUMTRAINLEVELS);
			// Now the total times
//			worldTotalNames = GetEmptyStringArray(numStore);
//			worldTotalTimes = GetEmptyIntArray(numStore);
//			worldTotalNamesDmnd = GetEmptyStringArray(numStore);
//			worldTotalTimesDmnd = GetEmptyIntArray(numStore);
//			worldTotalNamesTrain = GetEmptyStringArray(numStore);
//			worldTotalTimesTrain = GetEmptyIntArray(numStore);
//			worldTotalNamesTrainDmnd = GetEmptyStringArray(numStore);
//			worldTotalTimesTrainDmnd = GetEmptyIntArray(numStore);
		}
	}

	public static void SaveWorldRecords() {
		FileOutputStream f;
		try {
			f = new FileOutputStream(new File("WorldRecords.dat"));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(worldNames);
			o.writeObject(worldTimes);
			o.writeObject(worldNamesDmnd);
			o.writeObject(worldTimesDmnd);
			o.writeObject(worldNamesTrain);
			o.writeObject(worldTimesTrain);
			o.writeObject(worldNamesTrainDmnd);
			o.writeObject(worldTimesTrainDmnd);
			// Write the total times
			o.writeObject(worldTotalNames);
			o.writeObject(worldTotalTimes);
			o.writeObject(worldTotalNamesDmnd);
			o.writeObject(worldTotalTimesDmnd);
			o.writeObject(worldTotalNamesTrain);
			o.writeObject(worldTotalTimesTrain);
			o.writeObject(worldTotalNamesTrainDmnd);
			o.writeObject(worldTotalTimesTrainDmnd);

			// Close the file
			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		}
	}

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

	private static void GenerateFakeTimes() {
		AddFakePlayer("Ryan");
		AddFakePlayer("Steve");
		AddFakePlayer("Who");
		AddFakePlayer("Else");
		AddFakePlayer("Wants");
		AddFakePlayer("To");
		AddFakePlayer("Play");
		// Initialise the world records
		worldNames = GetEmptyNames(LevelsListGame.NUMGAMELEVELS);
		worldTimes = GetEmptyTimes(LevelsListGame.NUMGAMELEVELS);
		worldNamesDmnd = GetEmptyNames(LevelsListGame.NUMGAMELEVELS);
		worldTimesDmnd = GetEmptyTimes(LevelsListGame.NUMGAMELEVELS);
		worldNamesTrain = GetEmptyNames(LevelsListTraining.NUMTRAINLEVELS);
		worldTimesTrain = GetEmptyTimes(LevelsListTraining.NUMTRAINLEVELS);
		worldNamesTrainDmnd = GetEmptyNames(LevelsListTraining.NUMTRAINLEVELS);
		worldTimesTrainDmnd = GetEmptyTimes(LevelsListTraining.NUMTRAINLEVELS);
		// Now the total times
		worldTotalNames = GetEmptyStringArray(numStore);
		worldTotalTimes = GetEmptyIntArray(numStore);
		worldTotalNamesDmnd = GetEmptyStringArray(numStore);
		worldTotalTimesDmnd = GetEmptyIntArray(numStore);
		worldTotalNamesTrain = GetEmptyStringArray(numStore);
		worldTotalTimesTrain = GetEmptyIntArray(numStore);
		worldTotalNamesTrainDmnd = GetEmptyStringArray(numStore);
		worldTotalTimesTrainDmnd = GetEmptyIntArray(numStore);
//
//		CheckTimes(int[] times, int indx, int lvl, int timerTotal, boolean world) {
//		times = plyrTimes.get(currentPlayer).get(lvl)
//		indx = 0, 1, 2, 3 = plyrTimes, plyrTimesDmnd, plyrTimesTrain, plyrTimesTrainDmnd
//
		// Now apply all world records
		int timerTotal;
		for (int p=0; p<plyrNames.length; p++) {
			SetCurrentPlayer(p);
			UpdateTotalTimes();
			// For all game levels
			for (int l=0; l<LevelsListGame.NUMGAMELEVELS; l++) {
				timerTotal = plyrTimes.get(currentPlayer).get(l)[0];
				CheckTimes(worldTimes.get(l), 0, l, timerTotal, true);
			}
		}

	}

	private static void AddFakePlayer(String name) {
		String[] oldNames = plyrNames.clone();
		plyrNames = new String[1+oldNames.length];
		for (int i=0;i<oldNames.length;i++) plyrNames[i] = oldNames[i];
		plyrNames[oldNames.length] = name;
		// Add the player times
		plyrTimes.add(RandomTimes(LevelsListGame.NUMGAMELEVELS));
		plyrTimesDmnd.add(RandomTimes(LevelsListGame.NUMGAMELEVELS));
		plyrTimesTrain.add(RandomTimes(LevelsListTraining.NUMTRAINLEVELS));
		plyrTimesTrainDmnd.add(RandomTimes(LevelsListTraining.NUMTRAINLEVELS));
		// Add the player controls
		plyrControls.add(GetDefaultControls());
		// Add an empty diamonds array
		plyrColDmnd.add(FalseBoolean(LevelsListGame.NUMGAMELEVELS));
		// Add an empty diamonds training array
		plyrColTrainDmnd.add(FalseBoolean(LevelsListTraining.NUMTRAINLEVELS));
		// Add a completed levels array
		plyrLevelComplete.add(ValueInt(LevelsListGame.NUMGAMELEVELS, 1));
		// Add a default Bike color
		plyrBikeColor.add(GetDefaultBikeColor());
		// Add the total times
		plyrTotalTimes.add(ValueInt(numStore, -1));
		plyrTotalTimesTrain.add(ValueInt(numStore, -1));
		plyrTotalTimesDmnd.add(ValueInt(numStore, -1));
		plyrTotalTimesTrainDmnd.add(ValueInt(numStore, -1));
	}
	
	@SuppressWarnings("unchecked")
	private static ArrayList<int[]> RandomTimes(int numLevels) {
		int min = 60000, max = 70000;
		ArrayList<int[]> times = new ArrayList<int[]>();
		int[] emptyTimes = new int[numStore]; // Store the top 10 times in each level
		for (int l=0; l<numLevels; l++) {
			for (int i=0; i<numStore; i++) emptyTimes[i] = getRandomNumberInRange(min, max);
			Arrays.sort(emptyTimes);
			times.add(emptyTimes.clone());
		}
		return (ArrayList<int[]>) times.clone();
	}
	
	private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
