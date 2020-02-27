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
public class GameVars implements Serializable {

	private static final long serialVersionUID = 1L;

	// How many level skips are allowed
	public static final int skipsAllowed = 3;
	// Player arrays
	public static int currentPlayer = -1;
	public static String[] plyrNames = new String[0];
	public static ArrayList<ArrayList<int[]>> plyrTimes = new ArrayList<ArrayList<int[]>>();
	public static ArrayList<ArrayList<int[]>> plyrTimesDmnd = new ArrayList<ArrayList<int[]>>();
	public static ArrayList<ArrayList<int[]>> plyrTimesTrain = new ArrayList<ArrayList<int[]>>();
	public static ArrayList<ArrayList<int[]>> plyrTimesTrainDmnd = new ArrayList<ArrayList<int[]>>();
	public static ArrayList<int[]> plyrControls = new ArrayList<int[]>();
	public static ArrayList<boolean[]> plyrColDmnd = new ArrayList<boolean[]>();
	public static ArrayList<boolean[]> plyrColDmndTrain = new ArrayList<boolean[]>();
	public static ArrayList<boolean[]> plyrSkipLevel = new ArrayList<boolean[]>();
	public static ArrayList<float[]> plyrBikeColor = new ArrayList<float[]>();

	// Get and Set options
	public static void SetCurrentPlayer(int i) {currentPlayer = i;}
	public static int GetCurrentPlayer() {return currentPlayer;}

	// Get options
	public static String GetPlayerName() {return plyrNames[currentPlayer];}
	public static int[] GetPlayerTimes(int lvl) {return plyrTimes.get(currentPlayer).get(lvl);}
	public static int[] GetPlayerTimesDmnd(int lvl) {return plyrTimesDmnd.get(currentPlayer).get(lvl);}
	public static int[] GetPlayerTimesTrain(int lvl) {return plyrTimesTrain.get(currentPlayer).get(lvl);}
	public static int[] GetPlayerTimesTrainDmnd(int lvl) {return plyrTimesTrainDmnd.get(currentPlayer).get(lvl);}
	public static int[] GetPlayerControls() {return plyrControls.get(currentPlayer);}
	public static boolean[] GetPlayerDiamonds() {return plyrColDmnd.get(currentPlayer);}
	public static boolean[] GetPlayerDiamondsTrain() {return plyrColDmndTrain.get(currentPlayer);}
	public static boolean[] GetPlayerSkipLevel() {return plyrSkipLevel.get(currentPlayer);}
	public static float[] GetPlayerBikeColor() {return plyrBikeColor.get(currentPlayer);}

	// Is options
	public static boolean IsDiamondCollected(int lvl) {return plyrColDmnd.get(currentPlayer)[lvl];}
	public static boolean IsDiamondCollectedTrain(int lvl) {return plyrColDmndTrain.get(currentPlayer)[lvl];}
	public static boolean IsSkipLevel(int lvl) {return plyrSkipLevel.get(currentPlayer)[lvl];}
	
	// Set options
	public static void SetDiamond(int lvl) {plyrColDmnd.get(currentPlayer)[lvl]=true;}
	public static void SetDiamondTrain(int lvl) {plyrColDmndTrain.get(currentPlayer)[lvl]=true;}
	public static void SetSkipLevel(int lvl) {
		if (CanSkip()) {
			plyrSkipLevel.get(currentPlayer)[lvl]=true;}
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
		plyrColDmndTrain.add(FalseBoolean(LevelsListTraining.NUMTRAINLEVELS));
		// Add an empty level skip array
		plyrSkipLevel.add(FalseBoolean(LevelsListGame.NUMGAMELEVELS));
		// Add a default Bike color
		plyrBikeColor.add(GetDefaultBikeColor());
	}

	// Methods
	public static boolean CanSkip() {
		int numSkips=0;
		for (int i=0; i<plyrSkipLevel.get(currentPlayer).length; i++) {
			if (plyrSkipLevel.get(currentPlayer)[i]) numSkips += 1;
		}
		if (numSkips<skipsAllowed) return true;
		else return false;
	}

	public static int SkipsLeft() {
		int numSkips=0;
		for (int i=0; i<plyrSkipLevel.get(currentPlayer).length; i++) {
			if (plyrSkipLevel.get(currentPlayer)[i]) numSkips += 1;
		}
		return (skipsAllowed-numSkips);
	}

	public static float[] GetDefaultBikeColor() {
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
			plyrControls = (ArrayList<int[]>) oi.readObject();
			plyrColDmnd = (ArrayList<boolean[]>) oi.readObject();
			plyrColDmndTrain = (ArrayList<boolean[]>) oi.readObject();
			plyrSkipLevel = (ArrayList<boolean[]>) oi.readObject();
			plyrBikeColor = (ArrayList<float[]>) oi.readObject();

			// Close files
			oi.close();
			fi.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
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
			o.writeObject(plyrControls);
			o.writeObject(plyrColDmnd);
			o.writeObject(plyrColDmndTrain);
			o.writeObject(plyrSkipLevel);
			o.writeObject(plyrBikeColor);

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
		int[] emptyTimes = new int[10]; // Store the top 10 times in each level
		for (int i=0; i<10; i++) emptyTimes[i] = -1; // -1 means that a time has not been recorded for this level
		// Add a copy of this array for all levels
		for (int i=0; i<numLevels; i++) times.add(emptyTimes.clone());
		return (ArrayList<int[]>) times.clone();
	}

	public static boolean[] FalseBoolean(int num) {
		boolean[] empty = new boolean[num];
		for (int i=0; i<num; i++) empty[i]=false;
		return empty.clone();
	}
}
