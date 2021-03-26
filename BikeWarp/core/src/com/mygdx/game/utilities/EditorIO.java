package com.mygdx.game.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.BikeGameTextures;
import com.mygdx.game.handlers.B2DVars;
import com.mygdx.game.handlers.DecorVars;
import com.mygdx.game.handlers.LevelVars;
import com.mygdx.game.handlers.ObjectVars;
import com.mygdx.game.utilities.EditorImageIO;
import com.mygdx.game.utilities.EditorJointIO;
import com.mygdx.game.utilities.EditorObjectIO;
import com.mygdx.game.utilities.json.JSONException;
import com.mygdx.game.utilities.json.JSONStringer;

public class EditorIO {

	private File projectFile;
	private static ArrayList<float[]> convexPolygons;
	private static ArrayList<ArrayList<Vector2>> convexVectorPolygons;
	private static ArrayList<Vector2> concaveVertices;
	private static ArrayList<NewJoint> jointList;
	public static final String levelDir = "levels/";

	public static String levelFilename = "";

	private static int cntKinematic;
	private static int cntFalling;
	private static int cntTrigger;
	private static int cntBallChain;
	private static int cntBoulder;
	private static int cntBridge;
	private static int cntCrate;
	private static int cntDoor;
	private static int cntGateSwitch;
	private static int cntGravity;
	private static int cntJewel;
	private static int cntKey;
	private static int cntLog;
	private static int cntNitrous;
	private static int cntPendulum;
	private static int cntPlanet;
	private static int cntSpike;
	private static int cntSpikeZone;
	private static int cntTransport;
	private static int cntTransportInvisible;
	private static int finishObjNumber = 3;

	private static ArrayList<float[][][]> ConvertTextureToArray(ArrayList<Texture> allLevelTextures) {
		ArrayList<float[][][]> textureArray = new ArrayList<float[][][]>();
		Pixmap pixmap = null;
		Color color;
		int width, height;
		float[][][] textArr;
		for (int i=0; i<allLevelTextures.size(); i++) {
			if (!allLevelTextures.get(i).getTextureData().isPrepared()) {
				allLevelTextures.get(i).getTextureData().prepare();
			}
			pixmap = allLevelTextures.get(i).getTextureData().consumePixmap();
			width = pixmap.getWidth();
			height = pixmap.getHeight();
			textArr = new float[width][height][4];
			for (int ww=0; ww<width; ww++) {
				for (int hh=0; hh<height; hh++) {
					color = new Color(pixmap.getPixel(ww, hh));
					textArr[ww][hh][0] = color.r;
					textArr[ww][hh][1] = color.g;
					textArr[ww][hh][2] = color.b;
					textArr[ww][hh][3] = color.a;
				}
			}
			// Add the texture to the array
			textureArray.add(FileUtils.deepCopyFloatArray(textArr));
		}
		if (pixmap != null) pixmap.dispose();
		return textureArray;
	}

	private static ArrayList<Texture> ConvertArrayToTexture(ArrayList<float[][][]> textureArray) {
		ArrayList<Texture> allTextures = new ArrayList<Texture>();
		Pixmap pixmap = null;
		int width, height;
		float rr, gg, bb, aa;
		for (int i=0; i<textureArray.size(); i++) {
			width = textureArray.get(i).length;
			height = textureArray.get(i)[0].length;
			pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
			for (int ww=0; ww<width; ww++) {
				for (int hh=0; hh<height; hh++) {
					rr = textureArray.get(i)[ww][hh][0];
					gg = textureArray.get(i)[ww][hh][1];
					bb = textureArray.get(i)[ww][hh][2];
					aa = textureArray.get(i)[ww][hh][3];
					pixmap.setColor(rr, gg, bb, aa);
					pixmap.drawPixel(ww, hh);
				}
			}
			// Add the texture to the array
			allTextures.add(new Texture(pixmap));
		}
		if (pixmap != null) pixmap.dispose();
		return allTextures;
	}

	public static String saveLevel(ArrayList<float[]> allPolygons,
								   ArrayList<Integer> allPolygonTypes,
								   ArrayList<float[]> allPolygonPaths,
								   ArrayList<String> allPolygonTextures,
								   ArrayList<float[]> allObjects,
								   ArrayList<float[]> allObjectArrows,
								   ArrayList<float[]> allObjectCoords,
								   ArrayList<Integer> allObjectTypes,
								   ArrayList<float[]> allDecors,
								   ArrayList<Integer> allDecorTypes,
								   ArrayList<Integer> allDecorPolys,
								   ArrayList<Object> allDecorImages,
//								   ArrayList<Texture> allLevelTextures,
//								   ArrayList<String> allLevelTextureNames,
			String aOutputFileName) throws FileNotFoundException, JSONException {
		// First need to convert the Textures into something that can be saved and loaded
//		ArrayList<float[][][]> allTextures = ConvertTextureToArray(allLevelTextures);
		// Now save the level
		try{
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream (levelDir+aOutputFileName));
			outputStream.writeObject(allPolygons);
			outputStream.writeObject(allPolygonTypes);
			outputStream.writeObject(allPolygonPaths);
			outputStream.writeObject(allPolygonTextures);
			outputStream.writeObject(allObjects);
			outputStream.writeObject(allObjectArrows);
			outputStream.writeObject(allObjectCoords);
			outputStream.writeObject(allObjectTypes);
			outputStream.writeObject(allDecors);
			outputStream.writeObject(allDecorTypes);
			outputStream.writeObject(allDecorPolys);
			outputStream.writeObject(allDecorImages);
			outputStream.writeObject(LevelVars.getProps());
//			outputStream.writeObject(allTextures);
//			outputStream.writeObject(allLevelTextureNames);
			outputStream.close();
		}
		catch ( IOException e ){
			return "Error writing to file " + aOutputFileName;
		}
//		String jsonString = JSONserialize(allPolygons, allPolygonTypes, allPolygonPaths, allObjects, allObjectArrows, allObjectCoords, allObjectTypes, allDecors, allDecorTypes, allDecorPolys);
        // Write the file
//		try {
//			System.out.println(); // Remove "desktop/" and add "core/data/levelname.json" ---> Eventually this will need to be changed to the location of the compiled.jar file /levels or something like that.
//			String[] strspl = Gdx.files.getLocalStoragePath().split("/");
//			String outpath = "";
//			for (int i = 0; i<strspl.length-1; i++){
//				outpath += strspl[i] + "/";
//			}
//			outpath += "core/assets/data/levelname.json";
//			System.out.println("ERROR:: EditorIO before return statement -- This needs to be changed -- see comment above this line");
//			FileWriter file = new FileWriter(outpath);
//			file.write(jsonString);
//			file.flush();
//			file.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Object> loadLevel(String aInputFileName) {
		ArrayList<float[]> allPolygons = null;
		ArrayList<Integer> allPolygonTypes = null;
		ArrayList<float[]> allPolygonPaths = null;
		ArrayList<String> allPolygonTextures = null;
		ArrayList<float[]> allObjects = null;
		ArrayList<float[]> allObjectArrows = null;
		ArrayList<float[]> allObjectCoords = null;
		ArrayList<Integer> allObjectTypes = null;
		ArrayList<float[]> allDecors = null;
		ArrayList<Integer> allDecorTypes = null;
		ArrayList<Integer> allDecorPolys = null;
		ArrayList<Object> allDecorImages = new ArrayList<Object>();
		String[] levelVarProps = null;
//		ArrayList<float[][][]> allTextures = null;
//		ArrayList<Texture> allLevelTextures = null;
//		ArrayList<String> allLevelTextureNames = null;
		ArrayList<Object> retarr = new ArrayList<Object>();
		try {
			// TODO :: If you want to reset level file format - comment out the relevant array, then update (see below)
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream (levelDir+aInputFileName));
			allPolygons = (ArrayList<float[]>)inputStream.readObject();
			allPolygonTypes = (ArrayList<Integer>)inputStream.readObject();
			allPolygonPaths = (ArrayList<float[]>)inputStream.readObject();
			allPolygonTextures = (ArrayList<String>)inputStream.readObject();
			allObjects = (ArrayList<float[]>)inputStream.readObject();
			allObjectArrows = (ArrayList<float[]>)inputStream.readObject();
			allObjectCoords = (ArrayList<float[]>)inputStream.readObject();
			allObjectTypes = (ArrayList<Integer>)inputStream.readObject();
			allDecors = (ArrayList<float[]>)inputStream.readObject();
			allDecorTypes = (ArrayList<Integer>)inputStream.readObject();
			allDecorPolys = (ArrayList<Integer>)inputStream.readObject();
			allDecorImages = (ArrayList<Object>)inputStream.readObject();
			levelVarProps = (String[])inputStream.readObject();
//			allTextures = (ArrayList<float[][][]>)inputStream.readObject();
//			allLevelTextureNames = (ArrayList<String>)inputStream.readObject();
			inputStream.close();
			// Convert textures
//			allLevelTextures = ConvertArrayToTexture(allTextures);
			// Temporary fix to Coords
//			allObjectCoords = new ArrayList<float[]>();
//			float[] newCoord = new float[2];
//			int otype;
//			for (int i=0; i<allObjects.size(); i++) {
//				otype = allObjectTypes.get(i); 
//				if ((otype == ObjectVars.BallChain) | (otype == ObjectVars.Pendulum)) {
//					newCoord[0] = allObjects.get(i)[0] - ObjectVars.objectBallChain[0];
//					newCoord[1] = allObjects.get(i)[1] - ObjectVars.objectBallChain[1];
//				} else if (otype == ObjectVars.Boulder) {
//					newCoord[0] = allObjects.get(i)[0] - ObjectVars.objectBoulder[0];
//					newCoord[1] = allObjects.get(i)[1] - ObjectVars.objectBoulder[1];
//				} else if (otype == ObjectVars.Crate) {
//					newCoord[0] = 0.5f*(allObjects.get(i)[0] + allObjects.get(i)[4]);
//					newCoord[1] = 0.5f*(allObjects.get(i)[1] + allObjects.get(i)[5]);
//				} else if ((otype == ObjectVars.DoorBlue) | (otype == ObjectVars.DoorGreen) | (otype == ObjectVars.DoorRed)) {
//					newCoord[0] = 0.5f*(allObjects.get(i)[0] + allObjects.get(i)[4]);
//					newCoord[1] = 0.5f*(allObjects.get(i)[1] + allObjects.get(i)[5]);
//				} else if ((otype == ObjectVars.KeyBlue) | (otype == ObjectVars.KeyGreen) | (otype == ObjectVars.KeyRed)) {
//					newCoord[0] = 0.5f*(allObjects.get(i)[0] + allObjects.get(i)[4]);
//					newCoord[1] = 0.5f*(allObjects.get(i)[1] + allObjects.get(i)[5]);
//				} else if (otype == ObjectVars.GateSwitch) {
//					newCoord[0] = 0.5f*(allObjects.get(i)[0] + allObjects.get(i)[4]);
//					newCoord[1] = 0.5f*(allObjects.get(i)[1] + allObjects.get(i)[5]);
//				} else if (otype == ObjectVars.Gravity) {
//					newCoord[0] = allObjects.get(i)[0] - ObjectVars.objectGravity[0];
//					newCoord[1] = allObjects.get(i)[1] - ObjectVars.objectGravity[1];
//				} else if (otype == ObjectVars.Jewel) {
//					newCoord[0] = (0.5f*13.0f*(allObjects.get(i)[0]+allObjects.get(i)[4]) + 2.3f*allObjects.get(i)[2])/(13.0f+2.3f);
//					newCoord[1] = (0.5f*13.0f*(allObjects.get(i)[1]+allObjects.get(i)[5]) + 2.3f*allObjects.get(i)[3])/(13.0f+2.3f);
//				} else if (otype == ObjectVars.Spike) {
//					newCoord[0] = allObjects.get(i)[0] - ObjectVars.objectSpike[0];
//					newCoord[1] = allObjects.get(i)[1] - ObjectVars.objectSpike[1];
//				} else if (otype == ObjectVars.Transport) {
//					newCoord[0] = 0.5f*(allObjects.get(i)[0] + allObjects.get(i)[4]);
//					newCoord[1] = 0.5f*(allObjects.get(i)[1] + allObjects.get(i)[5]);
//				} else if (otype == ObjectVars.Start) {
//					newCoord[0] = allObjects.get(i)[0] - ObjectVars.objectStart[0];
//					newCoord[1] = allObjects.get(i)[1] - ObjectVars.objectStart[1];
//				} else if (otype == ObjectVars.Finish) {
//					newCoord[0] = allObjects.get(i)[0] - ObjectVars.objectFinish[0];
//					newCoord[1] = allObjects.get(i)[1] - ObjectVars.objectFinish[1];
//				}
//				allObjectCoords.add(newCoord.clone());
//			}
			//allDecorPolys = new ArrayList<Integer>();
			//for (int i=0; i<allDecorTypes.size(); i++) allDecorPolys.add(-1);
			//for (int i=0; i<levelVarProps.length; i++) LevelVars.set(i, levelVarProps[i]);
			//saveLevel(allPolygons, allPolygonTypes, allPolygonPaths, allObjects, allObjectArrows, allObjectCoords, allObjectTypes, allDecors, allDecorTypes, allDecorPolys, aInputFileName);

			// Temporary fix to falling platforms
//			for (int j=0; j<allPolygonTypes.size(); j++) {
//				if (allPolygonTypes.get(j).equals(4)) {
//					int imax = 0;
//					float maxv = -10000.0f;
//					for (int i=0; i<allPolygons.get(j).length/2; i++) {
//						if (allPolygons.get(j)[2*i+1] > maxv) {
//							imax = i;
//							maxv = allPolygons.get(j)[2*i+1]; 
//						}
//					}
//					float xcenp = allPolygons.get(j)[2*imax];
//					float ycenp = allPolygons.get(j)[2*imax+1];
//					float[] newArr = {5.0f, 0.2f, xcenp, ycenp};
//					allPolygonPaths.set(j, newArr.clone());
//				}
//			}
//			for (int i=0; i<levelVarProps.length; i++) LevelVars.set(i, levelVarProps[i]);
//			saveLevel(allPolygons, allPolygonTypes, allPolygonPaths, allObjects, allObjectArrows, allObjectCoords, allObjectTypes, allDecors, allDecorTypes, allDecorPolys, aInputFileName);

			// Temporary fix for platform textures
//			allPolygonTextures = new ArrayList<String>();
//			for (int i=0; i<allPolygons.size(); i++) {
//				allPolygonTextures.add("");
//			}
//			for (int i=0; i<levelVarProps.length; i++) LevelVars.set(i, levelVarProps[i]);
//			saveLevel(allPolygons, allPolygonTypes, allPolygonPaths, allPolygonTextures, allObjects, allObjectArrows, allObjectCoords, allObjectTypes, allDecors, allDecorTypes, allDecorPolys, aInputFileName);

			// TODO :: If you want to reset level file format - update the relevant array
			// Temporary fix for putting collisionless platforms into static textures, and adding DecorImages as a possibile ArrayList
//			System.out.println("Resetting file: "+levelDir+aInputFileName);
//			int cntr_rm = 0;
//			for (int i=0; i<allDecors.size(); i++) {
//				if ((allDecorTypes.get(i) == 32) || (allDecorTypes.get(i) == 33)) {
//					// Move decoration to polygons arraylist
//					allPolygons.add(allDecors.get(i).clone());
//					allPolygonTextures.add(DecorVars.GetPlatformTextureFromIndex(allDecorPolys.get(i)));
//					allPolygonPaths.add(null);
//					if (allDecorTypes.get(i) == 32) {
//						allPolygonTypes.add(8);
//					} else if (allDecorTypes.get(i) == 33) {
//						allPolygonTypes.add(10);
//					}
//					// Now remove the decoration
//					allDecors.remove(i-cntr_rm);
//					allDecorTypes.remove(i-cntr_rm);
//					allDecorPolys.remove(i-cntr_rm);
//					cntr_rm += 1;
//				}
//			}
//			for (int i=0; i<allDecors.size(); i++) allDecorImages.add(null);
//			for (int i=0; i<levelVarProps.length; i++) LevelVars.set(i, levelVarProps[i]);
//			try {
//				saveLevel(allPolygons, allPolygonTypes, allPolygonPaths, allPolygonTextures, allObjects, allObjectArrows, allObjectCoords, allObjectTypes, allDecors, allDecorTypes, allDecorPolys, allDecorImages, aInputFileName);
//			} catch (Exception e) {
//				System.out.println(e);
//			}

			// Temporary fix for Waterfall/Rain and animated backgrounds
//			System.out.println("Resetting file: "+levelDir+aInputFileName);
//			int cntr_rm = 0;
//			float[] updatearr = new float[DecorVars.decorWaterfall.length];
//			for (int i=0; i<allDecors.size(); i++) {
//				if ((allDecorTypes.get(i) == DecorVars.Waterfall) || (allDecorTypes.get(i) == DecorVars.Rain)) {
//					// Move decoration to polygons arraylist
//					for (int jj=0; jj<10; jj++) {
//						updatearr[jj] = allDecors.get(i)[jj];
//					}
//					if (allDecorTypes.get(i) == DecorVars.Waterfall) {
//						updatearr[10] = DecorVars.imageWaterfall;
//					} else {
//						// Change it to a hard edge
//						allDecorTypes.set(i, DecorVars.Waterfall);
//						updatearr[10] = DecorVars.imageRain;
//					}
//					allDecors.set(i, updatearr.clone());
//				}
//			}
//			for (int ii=0; ii<levelVarProps.length; ii++) LevelVars.set(ii, levelVarProps[ii]);
//			try {
//				saveLevel(allPolygons, allPolygonTypes, allPolygonPaths, allPolygonTextures, allObjects, allObjectArrows, allObjectCoords, allObjectTypes, allDecors, allDecorTypes, allDecorPolys, allDecorImages, aInputFileName);
//			} catch (Exception e) {
//				System.out.println(e);
//			}

			// Temporary fix for adding start/stop times to moving platforms
//			System.out.println("Resetting file: "+levelDir+aInputFileName);
//			for (int i=0; i<allPolygons.size(); i++) {
//				if ((allPolygonTypes.get(i) == 2) || (allPolygonTypes.get(i) == 3)) {
//					float[] updatearr = new float[allPolygonPaths.get(i).length+2];
//					// Add in a start/stop time
//					for (int jj=0; jj<6; jj++) {
//						updatearr[jj] = allPolygonPaths.get(i)[jj];
//					}
//					updatearr[6] = 0.0f;
//					updatearr[7] = 0.0f;
//					for (int jj=6; jj<allPolygonPaths.get(i).length; jj++) {
//						updatearr[jj+2] = allPolygonPaths.get(i)[jj];
//					}
//					allPolygonPaths.set(i, updatearr.clone());
//				}
//			}

//			for (int ii=0; ii<levelVarProps.length; ii++) LevelVars.set(ii, levelVarProps[ii]);
//			LevelVars.set(LevelVars.PROP_BIKE_SHADE, "1.0f");
//			try {
//				saveLevel(allPolygons, allPolygonTypes, allPolygonPaths, allPolygonTextures, allObjects, allObjectArrows, allObjectCoords, allObjectTypes, allDecors, allDecorTypes, allDecorPolys, allDecorImages, aInputFileName);
//			} catch (Exception e) {
//				System.out.println(e);
//			}

			// Carry on as normal
			retarr.add(allPolygons);
			retarr.add(allPolygonTypes);
			retarr.add(allPolygonPaths);
			retarr.add(allPolygonTextures);
			retarr.add(allObjects);
			retarr.add(allObjectArrows);
			retarr.add(allObjectCoords);
			retarr.add(allObjectTypes);
			retarr.add(allDecors);
			retarr.add(allDecorTypes);
			retarr.add(allDecorPolys);
			retarr.add(allDecorImages);
			retarr.add(levelVarProps);
//			retarr.add(allLevelTextures);
//			retarr.add(allLevelTextureNames);
		} catch (IOException e) {
			System.out.println(e);
			System.out.println("Problem reading the file " + aInputFileName);
			for (int i=0; i<13; i++) retarr.add(null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return retarr;
	}

	@SuppressWarnings("unchecked")
	public static String loadLevelPlay(FileHandle aInputFileName) {
		ArrayList<float[]> allPolygons = null;
		ArrayList<Integer> allPolygonTypes = null;
		ArrayList<float[]> allPolygonPaths = null;
		ArrayList<String> allPolygonTextures = null;
		ArrayList<float[]> allObjects = null;
		ArrayList<float[]> allObjectArrows = null;
		ArrayList<float[]> allObjectCoords = null;
		ArrayList<Integer> allObjectTypes = null;
		ArrayList<float[]> allDecors = null;
		ArrayList<Integer> allDecorTypes = null;
		ArrayList<Integer> allDecorPolys = null;
		ArrayList<Object> allDecorImages = null;
//		ArrayList<float[][][]> allTextures = null;
//		ArrayList<Texture> allLevelTextures = null;
//		ArrayList<String> allLevelTextureNames = null;
		String[] levelVarProps = null;
		String jsonLevelString = "";
		try {
			ObjectInputStream inputStream = new ObjectInputStream(aInputFileName.read());
			allPolygons = (ArrayList<float[]>)inputStream.readObject();
			allPolygonTypes = (ArrayList<Integer>)inputStream.readObject();
			allPolygonPaths = (ArrayList<float[]>)inputStream.readObject();
			allPolygonTextures = (ArrayList<String>)inputStream.readObject();
			allObjects = (ArrayList<float[]>)inputStream.readObject();
			allObjectArrows = (ArrayList<float[]>)inputStream.readObject();
			allObjectCoords = (ArrayList<float[]>)inputStream.readObject();
			allObjectTypes = (ArrayList<Integer>)inputStream.readObject();
			allDecors = (ArrayList<float[]>)inputStream.readObject();
			allDecorTypes = (ArrayList<Integer>)inputStream.readObject();
			allDecorPolys = (ArrayList<Integer>)inputStream.readObject();
			allDecorImages = (ArrayList<Object>)inputStream.readObject();
			levelVarProps = (String[])inputStream.readObject();
//			allTextures = (ArrayList<float[][][]>)inputStream.readObject();
//			allLevelTextureNames = (ArrayList<String>)inputStream.readObject();
			inputStream.close();
			// Carry on as normal
			for (int i=0; i<levelVarProps.length; i++) LevelVars.set(i, levelVarProps[i]);
			jsonLevelString = EditorIO.JSONserialize(allPolygons,allPolygonTypes,allPolygonPaths,allPolygonTextures,allObjects,allObjectArrows,allObjectCoords,allObjectTypes,allDecors,allDecorTypes,allDecorPolys,allDecorImages);
			// Load the level textures
//			if (!BikeGameTextures.IsLevelLoaded(aInputFileName.nameWithoutExtension())) {
//				System.out.println("Generating textures!");
//				// Convert the textures
//				allLevelTextures = ConvertArrayToTexture(allTextures);
//				for (int i=0;i<allLevelTextures.size(); i++) {
//					BikeGameTextures.AddLevelTexture(allLevelTextures.get(i), aInputFileName.nameWithoutExtension(), allLevelTextureNames.get(i));
//				}
//			}
		} catch (Exception e) {
			System.out.println("Problem reading the file " + aInputFileName);
			e.printStackTrace();
		}
		return jsonLevelString;
	}

	public static String GetLevelFilename() { return levelFilename; };
	public static void SetLevelFilename(String name) { levelFilename = name; };

	public static String[] LoadLevelNames(String[] defaultStr) {
		File folder = new File(levelDir);
		//File folder = new File("data/levels");
		String temp = "";
		int numFiles = 0;
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile()) {
				temp = fileEntry.getName();
				if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("lvl")) numFiles +=1;
			}
		}
		// Repeat but insert the files into the array
		String[] allLevels = new String[defaultStr.length+numFiles];
		for (int i=0; i<defaultStr.length; i++) allLevels[i] = defaultStr[i];
		numFiles = defaultStr.length;
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile()) {
				temp = fileEntry.getName();
				if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("lvl")) {
					allLevels[numFiles] =  FileUtils.getBaseName(fileEntry.getName());
					// Reset all levels
					// TODO :: If you want to reset level file format - save them upon loading
//					try {
//						loadLevel(FileUtils.getBaseName(fileEntry.getName())+".lvl");
//					} catch (Exception e) {
//						System.out.println(e);
//					}
					numFiles +=1;
				}
			}
		}
		return allLevels;
	}

	public static String[] LoadTraceImages(String[] defaultStr) {
		File folder = new File("").getAbsoluteFile();
		String temp = "", tst = "";
		int numFiles = 0;
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile()) {
				temp = fileEntry.getName();
				tst = temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase();
				if (tst.equals("png")) numFiles +=1;
				else if (tst.equals("jpg")) numFiles +=1;
			}
		}
		// Repeat but insert the files into the array
		String[] allFiles = new String[defaultStr.length+numFiles];
		for (int i=0; i<defaultStr.length; i++) allFiles[i] = defaultStr[i];
		numFiles = defaultStr.length;
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile()) {
				temp = fileEntry.getName();
				tst = temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase();
				if ((tst.equals("png")) | (tst.equals("jpg"))) {
					allFiles[numFiles] = FileUtils.getBaseName(fileEntry.getName()) + "." + tst;
					numFiles +=1;
				}
			}
		}
		return allFiles;
	}

	public static String GetTexture(String textName, String defval) {
		if (textName.equals("Default")) return defval;
		else if (textName.startsWith("COLOR_")) return textName;
		else if (textName.equals("Asphalt")) return "images/ground_asphalt.png";
		else if (textName.equals("Bark")) return "images/ground_treebark.png";
		else if (textName.equals("Bark (Moss)")) return "images/ground_treebark_moss.png";
		else if (textName.equals("Bark (Dark)")) return "images/ground_treebark_dark.png";
		else if (textName.equals("Bricks")) return "images/ground_bricks.png";
		else if (textName.equals("Bubbles")) return "images/ground_bubbles.png";
		else if (textName.equals("Cracked Mud")) return "images/ground_cracked.png";
		else if (textName.equals("Dirt")) return "images/ground_dirt.png";
		else if (textName.equals("Fog")) return "images/ground_fog.png";
		else if (textName.equals("Fog Stain")) return "images/ground_fog_stain.png";
		else if (textName.equals("Grass")) return "images/grass_full.png";
		else if (textName.equals("Grass (Daisy)")) return "images/ground_daisy.png";
		else if (textName.equals("Grass (Short)")) return "images/grass_short.png";
		else if (textName.equals("Grass (Meadow)")) return "images/grass_meadow.png";
		else if (textName.equals("Gravel")) return "images/ground_gravel.png";
		else if (textName.equals("Ice")) return "images/ground_ice.png";
		else if (textName.equals("Lava")) return "images/ground_lava.png";
		else if (textName.equals("Leaves")) return "images/ground_leaves.png";
		else if (textName.equals("Mars")) return "images/ground_mars.png";
		else if (textName.equals("Metal (Black)")) return "images/ground_metalblack_small.png";
		else if (textName.equals("Metal (Plate)")) return "images/ground_metalplate.png";
		else if (textName.equals("Metal (Rust/Yellow)")) return "images/ground_metalrust_yellow.png";
		else if (textName.equals("Moon")) return "images/ground_moon.png";
		else if (textName.equals("Reptile")) return "images/ground_reptile.png";
		else if (textName.equals("Roof tile (green)")) return "images/roof_green.png";
		else if (textName.equals("Roof tile (red)")) return "images/roof_red.png";
		else if (textName.equals("Sand")) return "images/ground_sand.png";
		else if (textName.equals("Shade")) return "images/ground_shade.png";
		else if (textName.equals("Snow")) return "images/ground_snow.png";
		else if (textName.equals("Snow (light)")) return "images/ground_snowlight.png";
		else if (textName.equals("Steel")) return "images/ground_steel.png";
		else if (textName.equals("Water")) return "images/ground_water.png";
		else if (textName.equals("Wood")) return "images/ground_wood.png";
		else if (textName.equals("Wood Plancks (H)")) return "images/ground_woodplanksH.png";
		else if (textName.equals("Wood Plancks (D)")) return "images/ground_woodplanksD.png";
		else if (textName.equals("Wood Plancks (V)")) return "images/ground_woodplanksV.png";
		else return defval;
	}
	
	public static String GetFGTexture(String textName, String defval) {
		if (textName.equals("Default")) return defval;
		else if (textName.startsWith("COLOR_")) return textName;
		else if (textName.equals("Asphalt")) return "images/ground_asphalt.png";
		else if (textName.equals("Bark")) return "images/ground_treebark.png";
		else if (textName.equals("Bark (Moss)")) return "images/ground_treebark_moss.png";
		else if (textName.equals("Bark (Dark)")) return "images/ground_treebark_dark.png";
		else if (textName.equals("Bricks")) return "images/ground_bricks.png";
		else if (textName.equals("Bubbles")) return "images/ground_bubbles.png";
		else if (textName.equals("Cracked Mud")) return "images/ground_cracked.png";
		else if (textName.equals("Dirt")) return "images/ground_dirt.png";
		else if (textName.equals("Fog")) return "images/ground_fog.png";
		else if (textName.equals("Fog Stain")) return "images/ground_fog_stain.png";
		else if (textName.equals("Grass")) return "images/grass_full.png";
		else if (textName.equals("Grass (Daisy)")) return "images/ground_daisy.png";
		else if (textName.equals("Grass (Short)")) return "images/grass_short.png";
		else if (textName.equals("Grass (Meadow)")) return "images/grass_meadow.png";
		else if (textName.equals("Gravel")) return "images/ground_gravel.png";
		else if (textName.equals("Ice")) return "images/ground_ice.png";
		else if (textName.equals("Lava")) return "images/ground_lava.png";
		else if (textName.equals("Leaves")) return "images/ground_leaves.png";
		else if (textName.equals("Mars")) return "images/ground_mars.png";
		else if (textName.equals("Metal (Black)")) return "images/ground_metalblack_small.png";
		else if (textName.equals("Metal (Plate)")) return "images/ground_metalplate.png";
		else if (textName.equals("Metal (Rust/Yellow)")) return "images/ground_metalrust_yellow.png";
		else if (textName.equals("Moon")) return "images/ground_moon.png";
		else if (textName.equals("Reptile")) return "images/ground_reptile.png";
		else if (textName.equals("Roof tile (green)")) return "images/roof_green.png";
		else if (textName.equals("Roof tile (red)")) return "images/roof_red.png";
		else if (textName.equals("Sand")) return "images/ground_sand.png";
		else if (textName.equals("Shade")) return "images/ground_shade.png";
		else if (textName.equals("Snow")) return "images/ground_snow.png";
		else if (textName.equals("Snow (light)")) return "images/ground_snowlight.png";
		else if (textName.equals("Steel")) return "images/ground_steel.png";
		else if (textName.equals("Water")) return "images/ground_water.png";
		else if (textName.equals("Wood")) return "images/ground_wood.png";
		else if (textName.equals("Wood Plancks (V)")) return "images/ground_woodplanksV.png";
		else if (textName.equals("Wood Plancks (D)")) return "images/ground_woodplanksD.png";
		else if (textName.equals("Wood Plancks (H)")) return "images/ground_woodplanksH.png";
		else return defval;
	}

	public static String GetAnimatedBGTexture(String textName) {
		if (textName.equals("None")) return "None";
		else if (textName.equals("Asteroids")) return "images/asteroids.png";
		else if (textName.equals("Snow")) return "images/snow.png";
		else return "None";
	}

	public static String GetClimateFromIndex(int idx) {
		switch (idx) {
			case DecorVars.imageRain: return "images/rain.png";
			case DecorVars.imageSnow: return "images/snow.png";
			case DecorVars.imageWaterfall: return "images/waterfall.png";
			default: return "images/waterfall.png";
		}
	}

	public static String GetBGTexture(String textName) {
		String defval = "background_waterfall";
		if (textName.equals("Aurora (Blue)")) return "background_aurora_blue";
		else if (textName.equals("Aurora (Green)")) return "background_aurora_green";
		else if (textName.equals("Milky Way")) return "background_milkyway";
		else if (textName.equals("Shooting Star")) return "background_shootingstar";
		else if (textName.equals("Mountains")) return "background_mountains";
		else if ((textName.equals("Stars")) | textName.equals("Space")) return "background_stars";
		else if (textName.equals("Waterfall")) return "background_waterfall";
		else if (textName.equals("None")) return "none";
		else if (textName.equals("Sunset")) return "bg_sunset";
		else if (textName.equals("Astronaut")) return "bg_Astronaut";
		else if (textName.equals("Blue Bubble")) return "bg_BubbleBlue";
		else if (textName.equals("Earth At Night")) return "bg_EarthAtNight";
		else if (textName.equals("Galaxy (Andromeda)")) return "bg_GalaxyAndromeda";
		else if (textName.equals("Galaxy (Dusty)")) return "bg_GalaxyDusty";
		else if (textName.equals("Galaxy (Spiral)")) return "bg_GalaxySpiral";
		else if (textName.equals("Galaxy (White)")) return "bg_GalaxyWhite";
		else if (textName.equals("Milky Way (Mountains)")) return "bg_MilkyWayMountains";
		else if (textName.equals("Milky Way (Rocks)")) return "bg_MilkyWayRocks";
		else if (textName.equals("Milky Way (Tall Rocks)")) return "bg_MilkyWayTallRocks";
		else if (textName.equals("Milky Way (Blue Torch)")) return "bg_MilkyWay_BlueTorch";
		else if (textName.equals("Milky Way (Shooting Star)")) return "bg_MilkyWay_ShootingStar2";
		else if (textName.equals("Moon (Full)")) return "bg_MoonFull";
		else if (textName.equals("Moon (Gibbous)")) return "bg_MoonGibbous";
		else if (textName.equals("Moon (Rising)")) return "bg_MoonRising";
		else if (textName.equals("Mountain (Stars Blue)")) return "bg_MountainStarsBlue";
		else if (textName.equals("Mountain (Stars Yellow)")) return "bg_MountainStarsYellow";
		else if (textName.equals("Nebula (Blue)")) return "bg_NebulaBlue";
		else if (textName.equals("Nebula (Blue/Orange)")) return "bg_NebulaBlueOrange";
		else if (textName.equals("Nebula (Orange)")) return "bg_NebulaOrange";
		else if (textName.equals("Nebula (Red/Green)")) return "bg_NebulaRedGreen";
		else if (textName.equals("Shuttle Launch")) return "bg_ShuttleLaunch";
		else if (textName.equals("Star Circles")) return "bg_StarCircles";
		else if (textName.equals("Stargazer")) return "bg_Stargazer";
		else if (textName.equals("Stars (Blue)")) return "bg_StarsBlue";
		else if (textName.equals("Stars (Blue/Dust)")) return "bg_StarsBlueDust";
		else if (textName.equals("Stars (Blue/Green)")) return "bg_StarsBlueGreen";
		else if (textName.equals("Stars (Blue/Purple)")) return "bg_StarsBluePurple";
		else if (textName.equals("Stars+Clouds (Blue/Orange)")) return "bg_StarsCloudsBlueOrange";
		else if (textName.equals("Stars (Dusty)")) return "bg_StarsDusty";
		else if (textName.equals("Stars (Orange)")) return "bg_StarsOrange";
		else if (textName.equals("Stars (Purple)")) return "bg_StarsPurple";
		else if (textName.equals("Stars (Purple/Dust)")) return "bg_StarsPurpleDust";
		else if (textName.equals("Stars (Purple/Orange)")) return "bg_StarsPurpleOrange";
		else if (textName.equals("Stars (Red)")) return "bg_StarsRed";
		else if (textName.equals("Stars+Rocks (Blue/Pink)")) return "bg_StarsRocksBluePink";
		else if (textName.equals("Stars+Trees (Green)")) return "bg_TreesStarsGreen";
		else if (textName.equals("Stars Sparse")) return "bg_StarsSparse";
		else if (textName.equals("Planets (1)")) return "bg_planets_1";
		else if (textName.equals("Sunset Mud")) return "bg_sunset_mud";
		//		else if (textName.contains("LevText")) return textName;
		else return null;
	}

	public static String GetFGTexture(String textName) {
		String defval = "foreground_plants";
		if (textName.equals("Bushes")) return "foreground_bushes";
		else if (textName.equals("Plants")) return "foreground_plants";
		else if (textName.equals("Trees")) return "foreground_trees";
		else if (textName.equals("None")) return "none";
		else return defval;
	}

	public static String JSONserialize(ArrayList<float[]> allPolygons,
									   ArrayList<Integer> allPolygonTypes,
									   ArrayList<float[]> allPolygonPaths,
									   ArrayList<String> allPolygonTextures,
									   ArrayList<float[]> allObjects,
									   ArrayList<float[]> allObjectArrows,
									   ArrayList<float[]> allObjectCoords,
									   ArrayList<Integer> allObjectTypes,
									   ArrayList<float[]> allDecors,
									   ArrayList<Integer> allDecorTypes,
									   ArrayList<Integer> allDecorPolys,
									   ArrayList<Object> allDecorImages) throws JSONException, IndexOutOfBoundsException {
        String retval;
		float friction = 0.9f;
		float restitution = 0.2f;
        // Reset the polygons and joints
        convexPolygons = null;
    	convexVectorPolygons = null;
    	concaveVertices = null;
    	jointList = new ArrayList<NewJoint>();
    	// Reset the counters
    	cntKinematic = 0;
    	cntFalling = 0;
    	cntTrigger = 0;
    	cntBallChain = 0;
    	cntBoulder = 0;
    	cntBridge = 0;
    	cntCrate = 0;
    	cntDoor = 0;
    	cntGateSwitch = 0;
    	cntGravity = 0;
    	cntJewel = 0;
    	cntKey = 0;
    	cntLog = 0;
    	cntNitrous = 0;
		cntPendulum = 0;
		cntPlanet = 0;
    	cntSpike = 0;
    	cntSpikeZone = 0;
    	cntTransport = 0;
    	cntTransportInvisible = 0;
    	// Determine what texture to be used for the ground
    	String textString = GetTexture(LevelVars.get(LevelVars.PROP_GROUND_TEXTURE), "Default");
    	String textPlatform;
        // Determine the grass texture;
        //String textGrass = "images/grass_seamless.png";
        String textGrass = "images/grass_full.png";
        String textSurface = "";
        // Get the foreground/background textures
        String textFG = GetFGTexture(LevelVars.get(LevelVars.PROP_FG_TEXTURE));
        String textBG = GetBGTexture(LevelVars.get(LevelVars.PROP_BG_TEXTURE));
        // Reset the json object
    	JSONStringer json = new JSONStringer();
        json.object();
        json.key("body");
        json.array();
        //
        /* Create the static body that represents the ground */
        //
        json.object();
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
        json.key("customProperties");
        json.array();
        json.object();
        json.key("name").value("GameInfo");
        json.key("string").value("SURFACE");
        json.endObject();
        json.endArray();
        // Add the fixtures
        json.key("fixture");
        json.array();
        // Add the boundary ...
        EditorObjectIO.AddBoundary(json,textString);
        // ... and then add the remaining surfaces
        for (int i = 0; i<allPolygons.size(); i++){
        	// Decompose each polygon into a series of convex polygons
        	textPlatform = GetTexture(allPolygonTextures.get(i), textString);
            if (allPolygonTypes.get(i) == 0) {
    			concaveVertices = PolygonOperations.MakeVertices(allPolygons.get(i));
    			try {
    				convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
    			} catch (IndexOutOfBoundsException e) {
    				return "BD "+i+" P";
    			}
    			convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
    			for (int k = 0; k<convexPolygons.size(); k++){
    				if (PolygonOperations.CheckUnique(convexPolygons.get(k).clone())) return "CU "+i+" P"; // A problem with the length^2 of a polygon
        			if (PolygonOperations.CheckAreas(convexPolygons.get(k).clone())) return "CA "+i+" P"; // One of the areas was too small
    				//else if (PolygonOperations.CheckConvexHull(convexPolygons.get(k).clone())) return "CH "+i+" P"; // polygon is not convex
                	json.object();
		            // Specify other properties of this fixture
		        	json.key("density").value(1);
		            json.key("friction").value(friction);
		            json.key("restitution").value(restitution);
		            json.key("name").value("fixture8");
		            if (textPlatform.equalsIgnoreCase("images/ground_lava.png")) {
			            json.key("filter-categoryBits").value(B2DVars.BIT_SPIKE);
			            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);		            			            	
		            } else {
			            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
			            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);		            	
		            }
		            // Set the (background) ground texture
		            json.key("customProperties");
		            json.array();
		            json.object();
		            json.key("name").value("TextureMask");
		            json.key("string").value(textPlatform);
		            json.endObject();
		            json.endArray();
	    			json.key("polygon");
	                json.object(); // Begin polygon object
	                json.key("vertices");
	                json.object(); // Begin vertices object
	                json.key("x");
	                json.array();
	                for (int j = 0; j<convexPolygons.get(k).length/2; j++){
	                	json.value(B2DVars.EPPM*convexPolygons.get(k)[2*j]);
	                }
	                json.endArray();
	                json.key("y");
	                json.array();
	                for (int j = 0; j<convexPolygons.get(k).length/2; j++){
	                	json.value(B2DVars.EPPM*convexPolygons.get(k)[2*j+1]);
	                }
	                json.endArray();
	                json.endObject(); // End the vertices object
	                json.endObject(); // End polygon object
	                json.endObject(); // End this fixture
    			}
            } else if (allPolygonTypes.get(i) == 1) {
            	json.object();
                // Specify other properties of this fixture
            	json.key("density").value(1);
                json.key("friction").value(friction);
                json.key("restitution").value(restitution);
                json.key("name").value("fixture8");
	            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
	            json.key("filter-maskBits").value(B2DVars.BIT_GROUND | B2DVars.BIT_HEAD | B2DVars.BIT_WHEEL | B2DVars.BIT_CHAIN | B2DVars.BIT_SPIKE);
                // Set the (background) ground texture
                json.key("customProperties");
                json.array();
                json.object();
                json.key("name").value("TextureMask");
                json.key("string").value(textPlatform);
                json.endObject();
                json.endArray();
                json.key("circle");
                // Begin circle object
                json.object();
                // Specify the center of the circle
                json.key("center");
                json.object();
                json.key("x").value(B2DVars.EPPM*allPolygons.get(i)[0]);
                json.key("y").value(B2DVars.EPPM*allPolygons.get(i)[1]);
                json.endObject();
                // Specify the radius of the circle
                json.key("radius").value(B2DVars.EPPM*allPolygons.get(i)[2]);
                json.endObject(); // End circle object
                json.endObject(); // End this fixture
            }
        }
        // Add grass to the static polygons if needed
        for (int i = 0; i<allDecors.size(); i++) {
        	// Decompose each polygon into a series of convex polygons
            if ((allDecorTypes.get(i) == DecorVars.Grass) | (allDecorTypes.get(i) >= 100)) {
            	try {
            		int tst = allPolygonTypes.get(allDecorPolys.get(i));
            	} catch (IndexOutOfBoundsException e) {
            		return "GRASS_ERROR";
            	}
            	textSurface = GetTexture(DecorVars.GetPlatformTextureFromIndex(allDecorTypes.get(i)), textGrass);
            	if (allPolygonTypes.get(allDecorPolys.get(i))==0) {
	    			concaveVertices = PolygonOperations.MakeVertices(allDecors.get(i));
	    			try {
	    				convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
	    			} catch (IndexOutOfBoundsException e) {
	    				return "BD "+i+" D";
	    			}
	    			convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
	    			for (int k = 0; k<convexPolygons.size(); k++){
	    				if (PolygonOperations.CheckUnique(convexPolygons.get(k).clone())) return "CU "+i+" G"; // A problem with the length^2 of a polygon
	        			if (PolygonOperations.CheckAreas(convexPolygons.get(k).clone())) return "CA "+i+" D"; // One of the areas was too small
	    				//else if (PolygonOperations.CheckConvexHull(convexPolygons.get(k).clone())) return "CH "+i+" G"; // polygon is not convex
	                	json.object();
			            // Specify other properties of this fixture
			        	json.key("density").value(1);
			            json.key("friction").value(0);
			            json.key("restitution").value(0);
			            json.key("name").value("fixture8");
			            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
			            json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
			            // Set the (background) ground texture
			            json.key("customProperties");
			            json.array();
			            json.object();
			            json.key("name").value("TextureMask");
			            json.key("string").value(textSurface);
			            json.endObject();
			            json.endArray();
		    			json.key("polygon");
		                json.object(); // Begin polygon object
		                json.key("vertices");
		                json.object(); // Begin vertices object
		                json.key("x");
		                json.array();
		                for (int j = 0; j<convexPolygons.get(k).length/2; j++){
		                	json.value(B2DVars.EPPM*convexPolygons.get(k)[2*j]);
		                }
		                json.endArray();
		                json.key("y");
		                json.array();
		                for (int j = 0; j<convexPolygons.get(k).length/2; j++){
		                	json.value(B2DVars.EPPM*convexPolygons.get(k)[2*j+1]);
		                }
		                json.endArray();
		                json.endObject(); // End the vertices object
		                json.endObject(); // End polygon object
		                json.endObject(); // End this fixture
	    			}
            	}
            }
        }
        // Clear the polygons
        if (concaveVertices != null) concaveVertices.clear();
        if (convexVectorPolygons != null) convexVectorPolygons.clear();
        if (convexPolygons != null) convexPolygons.clear();
        json.endArray(); // End of the fixtures for the ground
        // Add some final properties for the ground body
		json.key("linearVelocity").value(0);
		json.key("name").value("Ground");
		json.key("position");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("type").value(0);
        json.endObject(); // End of Ground Body
        
        // Add kinematic bodies
        for (int i = 0; i<allPolygons.size(); i++){
        	textPlatform = GetTexture(allPolygonTextures.get(i), textString);
            if ((allPolygonTypes.get(i) == 2) | (allPolygonTypes.get(i) == 3)) {
            	retval = EditorObjectIO.AddKinematicPolygon(json,allPolygons.get(i),allPolygonPaths.get(i),allPolygonTypes.get(i),allDecors,allDecorTypes,allDecorPolys,textPlatform,textGrass,friction,restitution,i);
            	if (!retval.equals("")) return retval+i+" P";
            	cntKinematic += 1;
            } else if ((allPolygonTypes.get(i) == 4) | (allPolygonTypes.get(i) == 5)) {
            	retval = EditorObjectIO.AddFallingPolygon(json,allPolygons.get(i),allPolygonPaths.get(i),allPolygonTypes.get(i),allDecors,allDecorTypes,allDecorPolys,textPlatform,textGrass,friction,restitution,cntFalling,i);
            	if (!retval.equals("")) return retval+i+" P";
            	EditorJointIO.JointFalling(jointList, allPolygons.get(i), cntFalling+cntKinematic+cntTrigger+1);
            	cntFalling += 1;
            } else if ((allPolygonTypes.get(i) == 6) | (allPolygonTypes.get(i) == 7)) {
            	retval = EditorObjectIO.AddTriggerPolygon(json,allPolygons.get(i),allPolygonPaths.get(i),allPolygonTypes.get(i),allDecors,allDecorTypes,allDecorPolys,textPlatform,textGrass,friction,restitution,cntTrigger,i);
            	if (!retval.equals("")) return retval+i+" P";
            	EditorJointIO.JointTrigger(jointList, allPolygons.get(i), cntFalling+cntKinematic+cntTrigger+1);
            	cntTrigger += 1;
            }
        }

        // Add Objects
        int bodyIdx = cntKinematic+cntFalling+cntTrigger+1;
        int addBodies;
        // Add Diamond Jewel
        EditorObjectIO.AddJewelDiamond(json, allObjects.get(2), 0);
        bodyIdx += 1;
        for (int i = finishObjNumber; i<allObjects.size(); i++){
        	if (allObjectTypes.get(i) == ObjectVars.BallChain) {
        		addBodies = EditorObjectIO.AddBallChain(json, allObjects.get(i), friction, cntBallChain);
        		EditorJointIO.JointBallChain(jointList, allObjects.get(i), bodyIdx);
        		cntBallChain += 1;
        		bodyIdx += addBodies;
        	} else if (allObjectTypes.get(i) == ObjectVars.Boulder) {
        		addBodies = EditorObjectIO.AddBoulder(json, allObjects.get(i), cntBoulder);
        		cntBoulder += 1;
        		bodyIdx += addBodies;
        	} else if (allObjectTypes.get(i) == ObjectVars.Bridge) {
        		addBodies = EditorObjectIO.AddBridge(json, allObjects.get(i), friction, cntBridge);
        		EditorJointIO.JointBridge(jointList, allObjects.get(i), bodyIdx);
        		cntBridge += 1;
        		bodyIdx += addBodies;
        	} else if (allObjectTypes.get(i) == ObjectVars.Crate) {
        		addBodies = EditorObjectIO.AddCrate(json, allObjects.get(i), cntCrate);
        		cntCrate += 1;
        		bodyIdx += addBodies;
        	} else if ((allObjectTypes.get(i) == ObjectVars.DoorRed)|(allObjectTypes.get(i) == ObjectVars.DoorGreen)|(allObjectTypes.get(i) == ObjectVars.DoorBlue)) {
        		addBodies = EditorObjectIO.AddDoor(json, allObjects.get(i), cntDoor, allObjectTypes.get(i));
        		cntDoor += 1;
        		bodyIdx += addBodies;
        	} else if (allObjectTypes.get(i) == ObjectVars.Finish) {
        		addBodies = EditorObjectIO.AddFinish(json, allObjects.get(i));
        		bodyIdx += addBodies;
        	} else if (allObjectTypes.get(i) == ObjectVars.GateSwitch) {
        		addBodies = EditorObjectIO.AddGateSwitch(json, allObjects.get(i), cntGateSwitch);
        		bodyIdx += addBodies;
        		cntGateSwitch += 1;
        	} else if (ObjectVars.IsGravity(allObjectTypes.get(i))) {
        		Vector2 gravityVec = new Vector2(allObjectArrows.get(i)[2]-allObjectArrows.get(i)[0],allObjectArrows.get(i)[3]-allObjectArrows.get(i)[1]);
                gravityVec.nor();
        		addBodies = EditorObjectIO.AddGravity(json, allObjects.get(i), allObjectTypes.get(i), cntGravity, gravityVec);
        		bodyIdx += addBodies;
        		cntGravity += 1;
        	} else if (ObjectVars.IsEmerald(allObjectTypes.get(i))) {
        		addBodies = EditorObjectIO.AddJewel(json, allObjects.get(i), cntJewel);
        		cntJewel += 1;
        		bodyIdx += addBodies;
        	} else if ((allObjectTypes.get(i) == ObjectVars.KeyRed)|(allObjectTypes.get(i) == ObjectVars.KeyGreen)|(allObjectTypes.get(i) == ObjectVars.KeyBlue)) {
        		addBodies = EditorObjectIO.AddKey(json, allObjects.get(i), cntKey, allObjectTypes.get(i));
        		cntKey += 1;
        		bodyIdx += addBodies;
        	} else if (allObjectTypes.get(i) == ObjectVars.Log) {
        		addBodies = EditorObjectIO.AddLog(json, allObjects.get(i), cntLog);
        		cntLog += 1;
        		bodyIdx += addBodies;
        	} else if (allObjectTypes.get(i) == ObjectVars.Nitrous) {
        		addBodies = EditorObjectIO.AddNitrous(json, allObjects.get(i), cntNitrous);
        		cntNitrous += 1;
        		bodyIdx += addBodies;
        	} else if (allObjectTypes.get(i) == ObjectVars.Pendulum) {
        		addBodies = EditorObjectIO.AddPendulum(json, allObjects.get(i), cntPendulum);
        		EditorJointIO.JointPendulum(jointList, allObjects.get(i), bodyIdx);
        		cntPendulum += 1;
        		bodyIdx += addBodies;
			} else if (ObjectVars.IsPlanet(allObjectTypes.get(i))) {
        		String failure = "";
        		if (allObjectTypes.get(i) == ObjectVars.PlanetSaturn) failure = EditorObjectIO.AddPlanet(json, allObjects.get(i), cntPlanet, true);
				else failure = EditorObjectIO.AddPlanet(json, allObjects.get(i), cntPlanet, false);
				if (!failure.equalsIgnoreCase("")) return failure;
				addBodies = 1;
				cntPlanet += 1;
				bodyIdx += addBodies;
        	} else if (allObjectTypes.get(i) == ObjectVars.Spike) {
        		addBodies = EditorObjectIO.AddSpike(json, allObjects.get(i), cntSpike);
        		cntSpike += 1;
        		bodyIdx += addBodies;
        	} else if (allObjectTypes.get(i) == ObjectVars.SpikeZone) {
        		addBodies = EditorObjectIO.AddSpikeZone(json, allObjects.get(i), cntSpikeZone);
        		cntSpikeZone += 1;
        		bodyIdx += addBodies;
        	} else if (allObjectTypes.get(i) == ObjectVars.Transport) {
        		addBodies = EditorObjectIO.AddTransport(json, allObjects.get(i), allObjectTypes.get(i), cntTransport, null);
        		cntTransport += 1;
        		bodyIdx += addBodies;
        	} else if (ObjectVars.IsTransportInvisible(allObjectTypes.get(i))) {
        		Vector2 gravityVec = new Vector2(allObjectArrows.get(i)[2]-allObjectArrows.get(i)[0],allObjectArrows.get(i)[3]-allObjectArrows.get(i)[1]);
                gravityVec.nor();
        		addBodies = EditorObjectIO.AddTransport(json, allObjects.get(i), allObjectTypes.get(i), cntTransportInvisible, gravityVec.cpy());
        		cntTransportInvisible += 1;
        		bodyIdx += addBodies;
        	}
        }
        
        // Add some GameInfo to a body at the origin
        json.object();
        json.key("active").value(false);
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("customProperties");
        json.array();
        // Insert the starting position
        json.object();
        json.key("name").value("startPosition");
        json.key("vec2");
        json.object();
        json.key("x").value(B2DVars.EPPM*(allObjects.get(1)[0]-ObjectVars.objectStart[0]));
        json.key("y").value(B2DVars.EPPM*(allObjects.get(1)[1]-ObjectVars.objectStart[1]));
        json.endObject();
        json.endObject();
        json.object();
        json.key("name").value("finishPosition");
        json.key("vec2");
        json.object();
        json.key("x").value(B2DVars.EPPM*(allObjects.get(finishObjNumber)[0]-ObjectVars.objectFinish[0]));
        json.key("y").value(B2DVars.EPPM*(allObjects.get(finishObjNumber)[1]-ObjectVars.objectFinish[1]));
        json.endObject();
        json.endObject();
        // Insert the starting direction 1=right, -1=left
        json.object();
        json.key("name").value("startDirection");
        if (LevelVars.props[LevelVars.PROP_START_DIRECTION].equals("Left")) json.key("float").value(-1.0f);
        else json.key("float").value(1.0f);
        json.endObject();
        // Insert the starting angle
        json.object();
        float angle = PolygonOperations.GetAngle(allObjectArrows.get(1)[0], allObjectArrows.get(1)[1], allObjectArrows.get(1)[2], allObjectArrows.get(1)[3]);
        json.key("name").value("startAngle");
        json.key("float").value(angle);
        json.endObject();
        // How many jewels need to be collected
        json.object();
        json.key("name").value("numJewel");
        json.key("int").value(Integer.parseInt(LevelVars.get(LevelVars.PROP_NUMJEWELS)));
        json.endObject();
        // Set the sky texture
        json.object();
        json.key("name").value("skyTexture");
        String textSky = LevelVars.get(LevelVars.PROP_SKY_TEXTURE); 
        if (textSky.equals("Blue Sky")) json.key("string").value("data/images/sky_bluesky.png");
		else if (textSky.equals("Dusk")) json.key("string").value("data/images/sky_dusk.png");
        else if (textSky.equals("Evening")) json.key("string").value("data/images/sky_evening.png");
        else if (textSky.equals("Islands")) json.key("string").value("data/images/sky_islands.png");
        else if (textSky.equals("Mars")) json.key("string").value("data/images/sky_mars.png");
        else if (textSky.equals("Moon")) json.key("string").value("data/images/sky_moon.png");
        else if (textSky.equals("Sunrise")) json.key("string").value("data/images/sky_sunrise.png");
        else json.key("string").value("data/images/sky_bluesky.png");
        json.endObject();
        // Set the background image bounds
        json.object();
        json.key("name").value("bounds");
        json.key("vec2");
        json.object();
        json.key("x").value(B2DVars.EPPM*Float.parseFloat(LevelVars.get(LevelVars.PROP_BG_BOUNDSX1)));
        json.key("y").value(B2DVars.EPPM*Float.parseFloat(LevelVars.get(LevelVars.PROP_BG_BOUNDSX2)));
        json.endObject();
        json.endObject();
        // Set the background image
        if (textBG != null) {
	        json.object();
	        json.key("name").value("bgTexture");
	        json.key("string").value(textBG);
	        json.endObject();
        }
		// Set the foreground image
		json.object();
		json.key("name").value("fgTexture");
		json.key("string").value(textFG);
		json.endObject();
		// Set the timer color (red)
		json.object();
		json.key("name").value("timerColorRed");
		json.key("int").value(Integer.parseInt(LevelVars.get(LevelVars.PROP_TIMER_RED)));
		json.endObject();
		// Set the timer color (green)
		json.object();
		json.key("name").value("timerColorGreen");
		json.key("int").value(Integer.parseInt(LevelVars.get(LevelVars.PROP_TIMER_GREEN)));
		json.endObject();
		// Set the timer color (blue)
		json.object();
		json.key("name").value("timerColorBlue");
		json.key("int").value(Integer.parseInt(LevelVars.get(LevelVars.PROP_TIMER_BLUE)));
		json.endObject();
		// Set the bike shade
		json.object();
		json.key("name").value("bikeShadeScale");
		json.key("float").value(Float.parseFloat(LevelVars.get(LevelVars.PROP_BIKE_SHADE)));
		json.endObject();
        //
        json.endArray();
        json.key("linearVelocity").value(0);
        json.key("name").value("GameInfo");
        json.key("position");
        json.object();
        json.key("x").value(0.0f);
        json.key("y").value(0.0f);
        json.endObject();
        json.key("type").value(0);
        json.endObject();
        // Do something else...

		// Add Animated BG
		String textAnimBG = GetAnimatedBGTexture(LevelVars.get(LevelVars.PROP_ANIMATED_BG));
		if (!"None".equalsIgnoreCase(textAnimBG)) {
			float[] animBGarray = {-100.0f, -100.0f, 1100.0f, -100.0f, 1100.0f, 1100.0f, -100.0f, 1100.0f};
			json.object();
			json.key("angle").value(0);
			json.key("angularVelocity").value(0);
			json.key("awake").value(true);
			// Add the animated BG fixtures
			json.key("fixture");
			json.array();
			// Decompose each polygon into a series of convex polygons
			concaveVertices = PolygonOperations.MakeVertices(animBGarray);
			convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
			convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
			for (int k = 0; k<convexPolygons.size(); k++){
				json.object();
				// Specify other properties of this fixture
				json.key("density").value(1);
				json.key("friction").value(0);
				json.key("restitution").value(0);
				json.key("name").value("AnimatedBG");
				json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
				json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
				// Set the (background) ground texture
				json.key("customProperties");
				json.array();
				json.object();
				json.key("name").value("TextureMask");
				json.key("string").value(textAnimBG);
				json.endObject();
				json.object();
				json.key("name").value("Type");
				json.key("string").value("AnimatedBG");
				json.endObject();
				json.endArray();
				json.key("polygon");
				json.object(); // Begin polygon object
				json.key("vertices");
				json.object(); // Begin vertices object
				json.key("x");
				json.array();
				for (int j = 0; j<convexPolygons.get(k).length/2; j++){
					json.value(convexPolygons.get(k)[2*j]);
				}
				json.endArray();
				json.key("y");
				json.array();
				for (int j = 0; j<convexPolygons.get(k).length/2; j++){
					json.value(convexPolygons.get(k)[2*j+1]);
				}
				json.endArray();
				json.endObject(); // End the vertices object
				json.endObject(); // End polygon object
				json.endObject(); // End this fixture
			}
			// Clear the polygons
			if (concaveVertices != null) concaveVertices.clear();
			if (convexVectorPolygons != null) convexVectorPolygons.clear();
			if (convexPolygons != null) convexPolygons.clear();
			json.endArray(); // End of the fixtures for the animated background
			// Add some final properties for the animated background body
			json.key("linearVelocity").value(0);
			json.key("name").value("AnimatedBG");
			json.key("position");
			json.object();
			json.key("x").value(0);
			json.key("y").value(0);
			json.endObject();
			json.key("type").value(1);
			json.endObject(); // End of Animated BG Body
			bodyIdx += 1; // Add one for the animated BG body
		}

		// Add waterfall / Climate (Hard Edge)
        json.object();
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
//        json.key("customProperties");
//        json.array();
//        json.object();
//        json.key("name").value("GameInfo");
//        json.key("string").value("SURFACE");
//        json.endObject();
//        json.endArray();
        // Add the waterfall fixtures
        json.key("fixture");
        json.array();
        int wfcntr = 0; 
        for (int i = 0; i<allDecors.size(); i++) {
        	// Decompose each polygon into a series of convex polygons
            if (allDecorTypes.get(i) == DecorVars.Waterfall) {
    			concaveVertices = PolygonOperations.MakeVertices(Arrays.copyOfRange(allDecors.get(i), 0, 8));
    			convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
    			convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
    			for (int k = 0; k<convexPolygons.size(); k++){
    				if (PolygonOperations.CheckUnique(convexPolygons.get(k).clone())) return "CU "+i+" W"; // A problem with the length^2 of a polygon
        			if (PolygonOperations.CheckAreas(convexPolygons.get(k).clone())) return "CA "+i+" D"; // One of the areas was too small
    				//else if (PolygonOperations.CheckConvexHull(convexPolygons.get(k).clone())) return "CH "+i+" G"; // polygon is not convex
                	json.object();
		            // Specify other properties of this fixture
		        	json.key("density").value(1);
		            json.key("friction").value(0);
		            json.key("restitution").value(0);
		            json.key("name").value("Waterfall"+wfcntr);
		            json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
		            json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
		            // Set the (background) ground texture
		            json.key("customProperties");
		            json.array();
		            json.object();
		            json.key("name").value("TextureMask");
		            json.key("string").value(GetClimateFromIndex((int) allDecors.get(i)[10]));
		            json.endObject();
					json.object();
					json.key("name").value("Type");
					if (allDecors.get(i)[8]<0.5f) json.key("string").value("CollisionlessBG");
					else json.key("string").value("CollisionlessFG");
					json.endObject();
					json.object();
					json.key("name").value("Sound");
					json.key("int").value((int) allDecors.get(i)[9]);
					json.endObject();
					json.object();
					json.key("name").value("Climate");
					json.key("int").value(0); // Hard edge
					json.endObject();
		            json.endArray();
	    			json.key("polygon");
	                json.object(); // Begin polygon object
	                json.key("vertices");
	                json.object(); // Begin vertices object
	                json.key("x");
	                json.array();
	                for (int j = 0; j<convexPolygons.get(k).length/2; j++){
	                	json.value(B2DVars.EPPM*convexPolygons.get(k)[2*j]);
	                }
	                json.endArray();
	                json.key("y");
	                json.array();
	                for (int j = 0; j<convexPolygons.get(k).length/2; j++){
	                	json.value(B2DVars.EPPM*convexPolygons.get(k)[2*j+1]);
	                }
	                json.endArray();
	                json.endObject(); // End the vertices object
	                json.endObject(); // End polygon object
	                json.endObject(); // End this fixture
	                wfcntr += 1;
    			}
            }
        }
        // Clear the polygons
        if (concaveVertices != null) concaveVertices.clear();
        if (convexVectorPolygons != null) convexVectorPolygons.clear();
        if (convexPolygons != null) convexPolygons.clear();
        json.endArray(); // End of the fixtures for the waterfall
        // Add some final properties for the waterfall body
		json.key("linearVelocity").value(0);
		json.key("name").value("Waterfall");
		json.key("position");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("type").value(1);
        json.endObject(); // End of Waterfall Body
        if (wfcntr != 0) bodyIdx += 1; // Add one for the waterfall body

        // Add Rain
        json.object();
        json.key("angle").value(0);
        json.key("angularVelocity").value(0);
        json.key("awake").value(true);
//        json.key("customProperties");
//        json.array();
//        json.object();
//        json.key("name").value("GameInfo");
//        json.key("string").value("SURFACE");
//        json.endObject();
//        json.endArray();
        // Add the rain fixtures
        json.key("fixture");
        json.array();
        int rncntr = 0;
        for (int i = 0; i<allDecors.size(); i++) {
        	// Decompose each polygon into a series of convex polygons
            if (allDecorTypes.get(i) == DecorVars.Rain) {
//    			concaveVertices = PolygonOperations.MakeVertices(Arrays.copyOfRange(allDecors.get(i), 0, 8));
//    			convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
//    			convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
//    			for (int k = 0; k<convexPolygons.size(); k++){
				if (PolygonOperations.CheckUnique(allDecors.get(i).clone())) return "CU "+i+" W"; // A problem with the length^2 of a polygon
				if (PolygonOperations.CheckAreas(allDecors.get(i).clone())) return "CA "+i+" D"; // One of the areas was too small
				//else if (PolygonOperations.CheckConvexHull(convexPolygons.get(k).clone())) return "CH "+i+" G"; // polygon is not convex
				json.object();
				// Specify other properties of this fixture
				json.key("density").value(1);
				json.key("friction").value(0);
				json.key("restitution").value(0);
				json.key("name").value("Rain"+rncntr);
				json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
				json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
				// Set the (background) ground texture
				json.key("customProperties");
				json.array();
				json.object();
				json.key("name").value("TextureMask");
				json.key("string").value(GetClimateFromIndex((int) allDecors.get(i)[10]));
				json.endObject();
				json.object();
				json.key("name").value("Type");
				if (allDecors.get(i)[8]==0) json.key("string").value("CollisionlessBG");
				else json.key("string").value("CollisionlessFG");
				json.endObject();
				json.object();
				json.key("name").value("Sound");
				json.key("int").value((int) allDecors.get(i)[9]);
				json.endObject();
				json.object();
				json.key("name").value("Climate");
				json.key("int").value(1); // Soft edge
				json.endObject();
				json.endArray();
				json.key("polygon");
				json.object(); // Begin polygon object
				json.key("vertices");
				json.object(); // Begin vertices object
				json.key("x");
				json.array();
				for (int j = 0; j<4; j++){
					json.value(B2DVars.EPPM*allDecors.get(i)[2*j]);
				}
				json.endArray();
				json.key("y");
				json.array();
				for (int j = 0; j<4; j++){
					json.value(B2DVars.EPPM*allDecors.get(i)[2*j+1]);
				}
				json.endArray();
				json.endObject(); // End the vertices object
				json.endObject(); // End polygon object
				json.endObject(); // End this fixture
				rncntr += 1;
            }
        }
        // Clear the polygons
//        if (concaveVertices != null) concaveVertices.clear();
//        if (convexVectorPolygons != null) convexVectorPolygons.clear();
//        if (convexPolygons != null) convexPolygons.clear();
        json.endArray(); // End of the fixtures for the rain
        // Add some final properties for the rain body
		json.key("linearVelocity").value(0);
		json.key("name").value("Rain");
		json.key("position");
		json.object();
		json.key("x").value(0);
		json.key("y").value(0);
		json.endObject();
		json.key("type").value(1);
        json.endObject(); // End of Rain Body
        if (rncntr != 0) bodyIdx += 1; // Add one for the rain body

        // Add a collisionless background if the background is to be one of the platform textures
        // First, check if the BG texture is not already set
        if (textBG == null) {
        	textBG = GetFGTexture(LevelVars.get(LevelVars.PROP_BG_TEXTURE), null);
            // See if there's a valid texture to replace it
            if (textBG != null) {
            	float[] bgarr = {0.0f, 0.0f, 1000.0f, 0.0f, 1000.0f, 1000.0f, 0.0f, 1000.0f};
            	// Generate a collisionless background
    	        json.object();
    	        json.key("angle").value(0);
    	        json.key("angularVelocity").value(0);
    	        json.key("awake").value(true);
    	        // Add the fixture
    	        json.key("fixture");
    	        json.array();
            	// Decompose each polygon into a series of convex polygons
            	json.object();
                // Specify other properties of this fixture
            	json.key("density").value(1);
                json.key("friction").value(0);
                json.key("restitution").value(0);
                json.key("name").value("Collisionless"+0+"_BG_"+0);
                json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
                json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
                // Set the (background) ground texture
                json.key("customProperties");
                json.array();
                json.object();
                json.key("name").value("TextureMask");
                json.key("string").value(textBG);
                json.endObject();
                json.object();
                json.key("name").value("Type");
                json.key("string").value("CollisionlessBG");
                json.endObject();
                json.endArray();
    			json.key("polygon");
                json.object(); // Begin polygon object
                json.key("vertices");
                json.object(); // Begin vertices object
                json.key("x");
                json.array();
                for (int j = 0; j<bgarr.length/2; j++) json.value(bgarr[2*j]);
                json.endArray();
                json.key("y");
                json.array();
                for (int j = 0; j<bgarr.length/2; j++) json.value(bgarr[2*j+1]);
                json.endArray();
                json.endObject(); // End the vertices object
                json.endObject(); // End polygon object
                json.endObject(); // End this fixture
    	        json.endArray(); // End of the fixtures for the collisionless body
    	        // Add some final properties for the collisionless body
    			json.key("linearVelocity").value(0);
    			json.key("name").value("Collisionless"+0);
    			json.key("position");
    			json.object();
    			json.key("x").value(0);
    			json.key("y").value(0);
    			json.endObject();
    			json.key("type").value(0);
    	        json.endObject(); // End of Collisionless Bodies
    	        bodyIdx += 1; // Add one for the collisionless FG bodies, and add one for the collisionless BG bodies (if either exist)
            }
        }

        // Add the Foreground and Background Collisionless objects
        int[] collVars = {8, 10};
        String textMask;
		boolean bgFound = false;
		boolean fgFound = false;
        for (int cc=0; cc < collVars.length; cc++) {
	        json.object();
	        json.key("angle").value(0);
	        json.key("angularVelocity").value(0);
	        json.key("awake").value(true);
	//        json.key("customProperties");
	//        json.array();
	//        json.object();
	//        json.key("name").value("GameInfo");
	//        json.key("string").value("SURFACE");
	//        json.endObject();
	//        json.endArray();
	        // Add the waterfall fixtures
	        json.key("fixture");
	        json.array();
	        int ccntr = 0; 
	        for (int i = 0; i<allPolygons.size(); i++) {
	        	// Decompose each polygon into a series of convex polygons
	            if ((allPolygonTypes.get(i) == collVars[cc]) || (allPolygonTypes.get(i) == collVars[cc]+1)) {
					if (cc == 0) bgFound = true;
					else fgFound = true;
	            	// Grab the name of the texture
	            	textMask = GetTexture(allPolygonTextures.get(i), textString);
					if (allPolygonTypes.get(i)%2 == 0) {
						concaveVertices = PolygonOperations.MakeVertices(allPolygons.get(i));
						try {
							convexVectorPolygons = BayazitDecomposer.convexPartition(concaveVertices);
						} catch (IndexOutOfBoundsException e) {
							return "BD "+i+" P";
						}
						convexPolygons = PolygonOperations.MakeConvexPolygon(convexVectorPolygons);
						for (int k = 0; k<convexPolygons.size(); k++){
							if (PolygonOperations.CheckUnique(convexPolygons.get(k).clone())) return "CU "+i+" P"; // A problem with the length^2 of a polygon
							if (PolygonOperations.CheckAreas(convexPolygons.get(k).clone())) return "CA "+i+" P"; // One of the areas was too small
							//else if (PolygonOperations.CheckConvexHull(convexPolygons.get(k).clone())) return "CH "+i+" P"; // polygon is not convex
							json.object();
							// Specify other properties of this fixture
							json.key("density").value(1);
							json.key("friction").value(friction);
							json.key("restitution").value(restitution);
							json.key("name").value("Collisionless"+cc+"_"+ccntr);
							json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
							json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
							// Set the (background) ground texture
							json.key("customProperties");
							json.array();
							json.object();
							json.key("name").value("TextureMask");
							json.key("string").value(textMask);
							json.endObject();
							json.object();
							json.key("name").value("Type");
							if (cc==0) json.key("string").value("CollisionlessBG");
							else json.key("string").value("CollisionlessFG");
							json.endObject();
							json.endArray();
							json.key("polygon");
							json.object(); // Begin polygon object
							json.key("vertices");
							json.object(); // Begin vertices object
							json.key("x");
							json.array();
							for (int j = 0; j<convexPolygons.get(k).length/2; j++){
								json.value(B2DVars.EPPM*convexPolygons.get(k)[2*j]);
							}
							json.endArray();
							json.key("y");
							json.array();
							for (int j = 0; j<convexPolygons.get(k).length/2; j++){
								json.value(B2DVars.EPPM*convexPolygons.get(k)[2*j+1]);
							}
							json.endArray();
							json.endObject(); // End the vertices object
							json.endObject(); // End polygon object
							json.endObject(); // End this fixture
						}
					} else if (allPolygonTypes.get(i)%2 == 1) {
						json.object();
						// Specify other properties of this fixture
						json.key("density").value(1);
						json.key("friction").value(friction);
						json.key("restitution").value(restitution);
						json.key("name").value("Collisionless"+cc+"_"+ccntr);
						json.key("filter-categoryBits").value(B2DVars.BIT_GROUND);
						json.key("filter-maskBits").value(B2DVars.BIT_NOTHING);
						// Set the (background) ground texture
						json.key("customProperties");
						json.array();
						json.object();
						json.key("name").value("TextureMask");
						json.key("string").value(textMask);
						json.endObject();
						json.object();
						json.key("name").value("Type");
						if (cc==0) json.key("string").value("CollisionlessBG");
						else json.key("string").value("CollisionlessFG");
						json.endObject();
						json.endArray();
						json.key("circle");
						// Begin circle object
						json.object();
						// Specify the center of the circle
						json.key("center");
						json.object();
						json.key("x").value(B2DVars.EPPM*allPolygons.get(i)[0]);
						json.key("y").value(B2DVars.EPPM*allPolygons.get(i)[1]);
						json.endObject();
						// Specify the radius of the circle
						json.key("radius").value(B2DVars.EPPM*allPolygons.get(i)[2]);
						json.endObject(); // End circle object
						json.endObject(); // End this fixture
					}
	            }
	        }
	        // Clear the polygons
	        if (concaveVertices != null) concaveVertices.clear();
	        if (convexVectorPolygons != null) convexVectorPolygons.clear();
	        if (convexPolygons != null) convexPolygons.clear();
	        json.endArray(); // End of the fixtures for the collisionless body
	        // Add some final properties for the collisionless body
			json.key("linearVelocity").value(0);
			json.key("name").value("Collisionless"+cc);
			json.key("position");
			json.object();
			json.key("x").value(0);
			json.key("y").value(0);
			json.endObject();
			json.key("type").value(0);
	        json.endObject(); // End of Collisionless Bodies
        }
		// Add one for the collisionless FG bodies, and add one for the collisionless BG bodies (if either exist)
		if (bgFound) bodyIdx += 1;
		if (fgFound) bodyIdx += 1;

        // End of describing all bodies
        json.endArray(); // End of body array
        // Add images
        json.key("image");
        json.array();
        bodyIdx = 1;
        cntBallChain = 0;
        cntBoulder = 0;
        cntBridge = 0;
        cntCrate = 0;
        cntDoor = 0;
        cntGateSwitch = 0;
        cntGravity = 0;
        cntJewel = 0;
        cntKey = 0;
        cntLog = 0;
        cntNitrous = 0;
        cntPendulum = 0;
		cntPlanet = 0;
        cntSpike = 0;
        cntSpikeZone = 0;
        cntTransport = 0;
        cntTransportInvisible = 0;
        // Apply images to falling bodies
        for (int i = 0; i<allPolygons.size(); i++){
            if ((allPolygonTypes.get(i) == 2) | (allPolygonTypes.get(i) == 3)) {
            	bodyIdx += 1;
            } else if ((allPolygonTypes.get(i) == 4) | (allPolygonTypes.get(i) == 5)) {
            	//addBodies = EditorImageIO.ImageFallingSign(json, allPolygons.get(i), allPolygonPaths.get(i), bodyIdx, 1);
            	bodyIdx += 1;
            } else if ((allPolygonTypes.get(i) == 6) | (allPolygonTypes.get(i) == 7)) {
            	bodyIdx += 1;
            }
        }
        // Add Diamond Jewel
        EditorImageIO.ImageJewelDiamond(json, allObjects.get(2), allObjectTypes.get(2), bodyIdx, 0);
        bodyIdx += 1;
        for (int i = finishObjNumber; i<allObjects.size(); i++){
        	if (allObjectTypes.get(i) == ObjectVars.BallChain) {
        		addBodies = EditorImageIO.ImageBallChain(json, allObjects.get(i), bodyIdx, cntBallChain);
        		bodyIdx += addBodies;
        		cntBallChain += 1;
        	} else if (allObjectTypes.get(i) == ObjectVars.Boulder) {
        		addBodies = EditorImageIO.ImageBoulder(json, allObjects.get(i), bodyIdx, cntBoulder);
        		bodyIdx += addBodies;
        		cntBoulder += 1;
        	} else if (allObjectTypes.get(i) == ObjectVars.Bridge) {
        		addBodies = EditorImageIO.ImageBridge(json, allObjects.get(i), bodyIdx, cntBridge);
        		bodyIdx += addBodies;
        		cntBridge += 1;
        	} else if (allObjectTypes.get(i) == ObjectVars.Crate) {
        		addBodies = EditorImageIO.ImageCrate(json, allObjects.get(i), bodyIdx, cntCrate);
        		bodyIdx += addBodies;
        		cntCrate += 1;
        	} else if ((allObjectTypes.get(i) == ObjectVars.DoorRed)|(allObjectTypes.get(i) == ObjectVars.DoorGreen)|(allObjectTypes.get(i) == ObjectVars.DoorBlue)) {
        		addBodies = EditorImageIO.ImageDoor(json, allObjects.get(i), bodyIdx, cntDoor, allObjectTypes.get(i));
        		bodyIdx += addBodies;
        		cntDoor += 1;
        	} else if (allObjectTypes.get(i) == ObjectVars.Finish) {
        		addBodies = EditorImageIO.ImageFinish(json, allObjects.get(i), bodyIdx);
        		bodyIdx += addBodies;
        	} else if (allObjectTypes.get(i) == ObjectVars.GateSwitch) {
        		//addBodies = EditorImageIO.ImageGateSwitch(json, allObjects.get(i), bodyIdx, cntGateSwitch);
        		addBodies = 1;
        		bodyIdx += addBodies;
        		cntGateSwitch += 1;
        	} else if (ObjectVars.IsGravity(allObjectTypes.get(i))) {
        		Vector2 gravityVec = new Vector2(allObjectArrows.get(i)[2]-allObjectArrows.get(i)[0],allObjectArrows.get(i)[3]-allObjectArrows.get(i)[1]);
        		addBodies = EditorImageIO.ImageGravity(json, allObjects.get(i), bodyIdx, cntGravity, gravityVec);
        		bodyIdx += addBodies;
        		cntGravity += 1;
        	} else if (ObjectVars.IsEmerald(allObjectTypes.get(i))) {
        		addBodies = EditorImageIO.ImageJewel(json, allObjects.get(i), allObjectTypes.get(i), bodyIdx, cntJewel);
        		bodyIdx += addBodies;
        		cntJewel += 1;
        	} else if ((allObjectTypes.get(i) == ObjectVars.KeyRed)|(allObjectTypes.get(i) == ObjectVars.KeyGreen)|(allObjectTypes.get(i) == ObjectVars.KeyBlue)) {
        		addBodies = EditorImageIO.ImageKey(json, allObjects.get(i), bodyIdx, cntKey, allObjectTypes.get(i));
        		bodyIdx += addBodies;
        		cntKey += 1;
        	} else if (allObjectTypes.get(i) == ObjectVars.Log) {
        		addBodies = EditorImageIO.ImageLog(json, allObjects.get(i), bodyIdx, cntLog);
        		bodyIdx += addBodies;
        		cntLog += 1;
        	} else if (allObjectTypes.get(i) == ObjectVars.Nitrous) {
        		addBodies = EditorImageIO.ImageNitrous(json, allObjects.get(i), bodyIdx, cntNitrous);
        		bodyIdx += addBodies;
        		cntNitrous += 1;
			} else if (allObjectTypes.get(i) == ObjectVars.Pendulum) {
				addBodies = EditorImageIO.ImagePendulum(json, allObjects.get(i), bodyIdx, cntPendulum);
				bodyIdx += addBodies;
				cntPendulum += 1;
			} else if (ObjectVars.IsPlanet(allObjectTypes.get(i))) {
				addBodies = EditorImageIO.ImagePlanet(json, allObjects.get(i), bodyIdx, cntPlanet, allObjectTypes.get(i));
				bodyIdx += addBodies;
				cntPlanet += 1;
        	} else if (allObjectTypes.get(i) == ObjectVars.Spike) {
        		addBodies = EditorImageIO.ImageSpike(json, allObjects.get(i), bodyIdx, cntSpike);
        		bodyIdx += addBodies;
        		cntSpike += 1;
        	} else if (allObjectTypes.get(i) == ObjectVars.SpikeZone) {
        		addBodies = 1;
        		bodyIdx += addBodies;
        		cntSpikeZone += 1;
        	} else if (allObjectTypes.get(i) == ObjectVars.Transport) {
        		addBodies = EditorImageIO.ImageTransport(json, allObjects.get(i), bodyIdx, cntTransport);
        		bodyIdx += addBodies;
        		cntTransport += 1;
        	} else if (allObjectTypes.get(i) == ObjectVars.TransportInvisible) {
        		addBodies = 2;
        		bodyIdx += addBodies;
        		cntTransportInvisible += 1;
        	}
        }
        json.endArray(); // End of image array
        // Add joints
        json.key("joint");
        json.array(); // Start of Joints array
        EditorJointIO.AddJoints(json, jointList);
        json.endArray(); // End of Joints array

        // Add decorations
        json.key("decorations");
        json.array();
        EditorDecorIO.AddDecorations(json, allDecors, allDecorTypes);
        json.endArray(); // End of decorations array
        // Set the initial gravity
        Vector2 gravityVec = new Vector2(allObjectArrows.get(0)[2]-allObjectArrows.get(0)[0],allObjectArrows.get(0)[3]-allObjectArrows.get(0)[1]);
        gravityVec.nor();
        json.key("gravity");
        json.object();
        float gravity = 0.0f;
        if (LevelVars.get(LevelVars.PROP_GRAVITY).equals("Earth")) gravity = B2DVars.GRAVITY_EARTH;
        else if (LevelVars.get(LevelVars.PROP_GRAVITY).equals("Mars")) gravity = B2DVars.GRAVITY_MARS;
        else if (LevelVars.get(LevelVars.PROP_GRAVITY).equals("Moon")) gravity = B2DVars.GRAVITY_MOON;
        else if (LevelVars.get(LevelVars.PROP_GRAVITY).equals("Zero")) gravity = B2DVars.GRAVITY_ZERO;
        else gravity = B2DVars.GRAVITY_EARTH;
        json.key("x").value(gravityVec.x*gravity);
        json.key("y").value(gravityVec.y*gravity);
        json.endObject();
        // Finally, close the object (which contains the whole json file)
    	json.key("positionIterations").value(3);
    	json.key("stepsPerSecond").value(60.0);
    	json.key("subStepping").value(false);
    	json.key("velocityIterations").value(8);
    	json.key("warmStarting").value(true);
    	json.endObject();
        // Write the file
//        try {
//        	 System.out.println(); // Remove "desktop/" and add "core/data/levelname.json" ---> Eventually this will need to be changed to the location of the compiled.jar file /levels or something like that.
//        	 String[] strspl = Gdx.files.getLocalStoragePath().split("/");
//        	 String outpath = "";
//        	 for (int i = 0; i<strspl.length-1; i++){
//        		 outpath += strspl[i] + "/";
//        	 }
//        	 outpath += "core/assets/data/levelname.json";
//        	 System.out.println("ERROR:: EditorIO before return statement -- This needs to be changed -- see comment above this line");
//        	 FileWriter file = new FileWriter(outpath);
//        	 file.write(json.toString());
//        	 file.flush();
//        	 file.close();
//     
//    	} catch (IOException e) {
//    		 e.printStackTrace();
//    	}
        // Return the string
        return json.toString();
	}

/*	public static void deserialize(String str) throws JSONException {
        JSONObject json = new JSONObject(str);

        // rigid bodies

        JSONArray bodiesElem = json.getJSONArray("rigidBodies");
        for (int i=0; i<bodiesElem.length(); i++) {
                JSONObject bodyElem = bodiesElem.getJSONObject(i);

                RigidBodyModel model = new RigidBodyModel();
                model.setName(bodyElem.getString("name"));

                String imgPath = bodyElem.isNull("imagePath") ? null : bodyElem.getString("imagePath");
                model.setImagePath(FilenameUtils.separatorsToSystem(imgPath));

                JSONObject originElem = bodyElem.getJSONObject("origin");
                model.getOrigin().x = (float) originElem.getDouble("x");
                model.getOrigin().y = (float) originElem.getDouble("y");

                JSONArray polygonsElem = bodyElem.getJSONArray("polygons");

                for (int ii=0; ii<polygonsElem.length(); ii++) {
                        PolygonModel polygon = new PolygonModel();
                        model.getPolygons().add(polygon);

                        JSONArray verticesElem = polygonsElem.getJSONArray(ii);
                        for (int iii=0; iii<verticesElem.length(); iii++) {
                                JSONObject vertexElem = verticesElem.getJSONObject(iii);
                                polygon.vertices.add(new Vector2(
                                        (float) vertexElem.getDouble("x"),
                                        (float) vertexElem.getDouble("y")));
                        }
                }

                JSONArray circlesElem = bodyElem.getJSONArray("circles");

                for (int ii=0; ii<circlesElem.length(); ii++) {
                        CircleModel circle = new CircleModel();
                        model.getCircles().add(circle);

                        JSONObject circleElem = circlesElem.getJSONObject(ii);
                        circle.center.x = (float) circleElem.getDouble("cx");
                        circle.center.y = (float) circleElem.getDouble("cy");
                        circle.radius = (float) circleElem.getDouble("r");
                }

                JSONArray shapesElem = bodyElem.getJSONArray("shapes");

                for (int ii=0; ii<shapesElem.length(); ii++) {
                        JSONObject shapeElem = shapesElem.getJSONObject(ii);
                        ShapeModel.Type type = ShapeModel.Type.valueOf(shapeElem.getString("type"));

                        ShapeModel shape = new ShapeModel(type);
                        model.getShapes().add(shape);

                        JSONArray verticesElem = shapeElem.getJSONArray("vertices");
                        for (int iii=0; iii<verticesElem.length(); iii++) {
                                JSONObject vertexElem = verticesElem.getJSONObject(iii);
                                shape.getVertices().add(new Vector2(
                                        (float) vertexElem.getDouble("x"),
                                        (float) vertexElem.getDouble("y")));
                        }

                        shape.close();
                }

                //Ctx.bodies.getModels().add(model);
        }

        // dynamic objects

        JSONArray objectsElem = json.getJSONArray("dynamicObjects");
        for (int i=0; i<objectsElem.length(); i++) {
                JSONObject objectElem = objectsElem.getJSONObject(i);

                DynamicObjectModel model = new DynamicObjectModel();
                model.setName(objectElem.getString("name"));

                //Ctx.objects.getModels().add(model);
        }
	}*/
	
	public File getImageFile(String imgPath) {
		if (imgPath == null) return null;
		File file = new File(projectFile.getParent(), imgPath);
		return file;
	}
}
