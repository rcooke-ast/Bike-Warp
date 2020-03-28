package com.mygdx.game.handlers;

import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.badlogic.gdx.utils.Json;

public class GameStateLoader {

	private static final String saveName = "BikeWarp.dat";

	public static class JsonWorld {
		public String[] names;
		public ArrayList<ArrayList<int[]>> times;
		public ArrayList<ArrayList<int[]>> timesDiamond;
		public ArrayList<ArrayList<int[]>> timesTrain;
		public ArrayList<ArrayList<int[]>> timesTrainDiamond;
		public ArrayList<int[]> controls;
		public ArrayList<boolean[]> collectDiamond;
		public ArrayList<boolean[]> collectTrainDiamond;
		public ArrayList<boolean[]> levelComplete;
		public ArrayList<float[]> optBikeColor;
	}

	@SuppressWarnings("unchecked")
	public static void saveGameState() {
		JsonWorld jWorld = new JsonWorld();

		jWorld.names = GameVars.plyrNames.clone();
		jWorld.times = (ArrayList<ArrayList<int[]>>) GameVars.plyrTimes.clone();
		jWorld.timesDiamond = (ArrayList<ArrayList<int[]>>) GameVars.plyrTimesDmnd.clone();
		jWorld.timesTrain = (ArrayList<ArrayList<int[]>>) GameVars.plyrTimesTrain.clone();
		jWorld.timesTrainDiamond = (ArrayList<ArrayList<int[]>>) GameVars.plyrTimesTrainDmnd.clone();
		jWorld.controls = (ArrayList<int[]>) GameVars.plyrControls.clone();
		jWorld.collectDiamond = (ArrayList<boolean[]>) GameVars.plyrColDmnd.clone();
		jWorld.collectTrainDiamond = (ArrayList<boolean[]>) GameVars.plyrColTrainDmnd.clone();
		jWorld.levelComplete = (ArrayList<boolean[]>) GameVars.plyrLevelComplete.clone();
		jWorld.optBikeColor = (ArrayList<float[]>) GameVars.plyrBikeColor.clone();

		Json json = new Json();
		writeFile(saveName, json.toJson(jWorld));
	}

	@SuppressWarnings("unchecked")
	public static void loadGameState() {
		String save = readFile(saveName);
		if (!save.isEmpty()) {
			Json json = new Json();
			JsonWorld jWorld = json.fromJson(JsonWorld.class, save);

			GameVars.plyrNames = jWorld.names.clone();
			GameVars.plyrTimes = (ArrayList<ArrayList<int[]>>) jWorld.times.clone();
			GameVars.plyrTimesDmnd = (ArrayList<ArrayList<int[]>>) jWorld.timesDiamond.clone();
			GameVars.plyrTimesTrain = (ArrayList<ArrayList<int[]>>) jWorld.timesTrain.clone();
			GameVars.plyrTimesTrainDmnd = (ArrayList<ArrayList<int[]>>) jWorld.timesTrainDiamond.clone();
			GameVars.plyrControls = (ArrayList<int[]>) jWorld.controls.clone();
			GameVars.plyrColDmnd = (ArrayList<boolean[]>) jWorld.collectDiamond.clone();
			GameVars.plyrColTrainDmnd = (ArrayList<boolean[]>) jWorld.collectTrainDiamond.clone();
			GameVars.plyrLevelComplete = (ArrayList<int[]>) jWorld.levelComplete.clone();
			GameVars.plyrBikeColor = (ArrayList<float[]>) jWorld.optBikeColor.clone();
		} else {
			// File could not be found, start again with the default settings
		}
	}

//	public static void writeFile(String fileName, String s) {
//		FileHandle file = Gdx.files.local(fileName);
//		file.writeString(com.badlogic.gdx.utils.Base64Coder.encodeString(s), false);
//	}
//	public static String readFile(String fileName) {
//		FileHandle file = Gdx.files.local(fileName);
//		if (file != null && file.exists()) {
//			String s = file.readString();
//			if (!s.isEmpty()) {
//				return com.badlogic.gdx.utils.Base64Coder.decodeString(s);
//			}
//		}
//		return "";
//	}

	public static void writeFile(String fileName, String s) {
		try {
			DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(fileName));
			outputStream.writeUTF(com.badlogic.gdx.utils.Base64Coder.encodeString(s));
			outputStream.close();
		} catch ( IOException e ) {}
	}

	public static String readFile(String fileName) {
		try {
			@SuppressWarnings("resource")
			DataInputStream inputStream = new DataInputStream(new FileInputStream (fileName));
			String s = inputStream.readUTF();
			if (!s.isEmpty()) {
				return com.badlogic.gdx.utils.Base64Coder.decodeString(s);
			}
		} catch ( IOException e ) {}
		return "";
	}

}