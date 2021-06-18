package com.mygdx.game.handlers;

import com.mygdx.game.utilities.PolygonOperations;

import javax.sound.sampled.Port;

public class DecorVars {
	// Define the sounds that can be applied to certain decorations
	public static final String[] platformSounds = {"None", "Rain", "Waterfall", "Wind"};
	public static final String[] platformImages = {"Rain", "Snow", "Waterfall"};
	public static final int soundNone = 0;
	public static final int soundRain = 1;
	public static final int soundWaterfall = 2;
	public static final int soundWind = 3;

	public static final int imageRain = 0;
	public static final int imageSnow = 1;
	public static final int imageWaterfall = 2;

	// Define the Indices
    public static final int RoadSign_Stop = 0;
    public static final int RoadSign_DoNotEnter = 1;
    public static final int RoadSign_RampAhead = 2;
    public static final int RoadSign_Bumps = 3;
    public static final int RoadSign_Motorbike = 4;
    public static final int RoadSign_NoMotorbike = 5;
    public static final int RoadSign_ReduceSpeed = 6;
    public static final int RoadSign_Exclamation = 7;
    public static final int RoadSign_10 = 8;
    public static final int RoadSign_20 = 9;
    public static final int RoadSign_30 = 10;
    public static final int RoadSign_40 = 11;
    public static final int RoadSign_50 = 12;
    public static final int RoadSign_60 = 13;
    public static final int RoadSign_80 = 14;
    public static final int RoadSign_100 = 15;
    public static final int RoadSign_Dash = 16;
	public static final int RoadSign_Dot = 17;
	public static final int RoadSign_NoAliens = 18;
	public static final int RoadSign_Toxic = 19;
    public static final int Grass = 29;
    public static final int LargeStone = 30;
    public static final int Waterfall = 31;
    public static final int Rain = 34;
    public static final int BinBag = 35;
	public static final int TyreStack = 36;
	public static final int Tree = 37;
	public static final int Rock = 38;
	public static final int Planet = 39;
	public static final int Track = 40;
	public static final int Vehicle = 41;
	public static final int Misc = 42;
	public static final int Shade = 43;
	public static final int Portrait = 44;
	public static final int Text = 45;
	// The above numbers must not exceed 100, because of the surface textures

    // Define the vertices
    public static final float[] decorCircleRoadSign = {0.0f,0.0f,30.0f,0.0f};
    public static final float[] decorWaterfall = {0.0f,-1500.0f,1000.0f,-1500.0f,1000.0f,1500.0f,0.0f,1500.0f,1.0f,soundWaterfall,imageWaterfall};
	public static final float[] decorRain = {0.0f,-1500.0f,1000.0f,-1500.0f,1000.0f,1500.0f,0.0f,1500.0f,1.0f,soundRain,imageRain};
	public static final float[] decorShade = {0.0f,-1500.0f,1000.0f,-1500.0f,1000.0f,1500.0f,0.0f,1500.0f,1.0f};
	public static final float trackLength = 30.0f;
	public static final float[] decorTrack = {0.0f,0.0f,0.0f,10.0f}; // xpos, ypos, angle, number of segments

	// Define the textures that can be applied to platforms
    public static final String[] platformTextures = {"Default", "Asphalt", "Asphalt (Blue)", "Bark", "Bark (H)", "Bark (Moss)", "Bark (Dark)", "Bricks", "Bubbles", "Cracked Mud", "Dirt", "Fog", "Fog Stain", "Fog Stain (grey)", "Grass", "Grass (Daisy)", "Grass (Short)", "Grass (Meadow)", "Gravel", "Gravel (Dark)", "Ice", "Lava", "Lava (Dark)", "Leaves", "Leaves (Green)", "Mars", "Mars (old)", "Metal (Black)",  "Metal (Plate)", "Metal (Dark Plate)", "Metal (Rust/Yellow)", "Moon", "Reptile", "Roof tile (green)", "Roof tile (red)", "Sand", "Shade", "Shade (Light)", "Snow", "Snow (light)", "Steel", "Water", "Wood", "Wood Plancks (D)", "Wood Plancks (H)", "Wood Plancks (V)"};
    public static final int textureDefault = 0;
	public static final int textureAsphalt = 1;
	public static final int textureBark = 2;
	public static final int textureBricks = 3;
	public static final int textureBubbles = 4;
	public static final int textureCrackedMud = 5;
	public static final int textureGrass = 6;
	public static final int textureGravel = 7;
	public static final int textureIce = 8;
	public static final int textureLava = 9;
	public static final int textureLeaves = 10;
	public static final int textureMars = 11;
	public static final int textureMetalBlack = 12;
	public static final int textureMetalPlate = 13;
	public static final int textureMoon = 14;
	public static final int textureRoofTileGreen = 15;
	public static final int textureRoofTileRed = 16;
	public static final int textureSand = 17;
	public static final int textureShade = 18;
	public static final int textureSnow = 19;
	public static final int textureSteel = 20;
	public static final int textureWater = 21;
	public static final int textureWood = 22;
	public static final int textureWoodPlancksH = 23;
	public static final int textureWoodPlancksV = 24;
	public static final int textureBarkMoss = 25;
	public static final int textureDirt = 26;
	public static final int textureGrassDaisy = 27;
	public static final int textureGrassShort = 28;
	public static final int textureGrassMeadow = 29;
	public static final int textureReptile = 30;
	public static final int textureFog = 31;
	public static final int textureFogStain = 32;
	public static final int textureBarkDark = 33;
	public static final int textureWoodPlancksD = 34;
	public static final int textureSnowLight = 35;
	public static final int textureBarkH = 36;
	public static final int textureGravelDark = 37;
	public static final int textureLavaDark = 38;
	public static final int textureShadeLight = 39;
	public static final int textureMarsOld = 40;
	public static final int textureMetalDarkPlate = 41;
	public static final int textureAsphaltBlue = 42;
	public static final int textureLeavesGreen = 43;
	public static final int textureFogStainGrey = 44;
	//public static final int texture = ;

	public static boolean IsRoadSign(int dTyp) {
    	if  ((dTyp == RoadSign_Stop) | (dTyp == RoadSign_DoNotEnter) | (dTyp == RoadSign_RampAhead) |
    			(dTyp == RoadSign_Bumps) | (dTyp == RoadSign_Motorbike) | (dTyp == RoadSign_NoMotorbike) |
    			(dTyp == RoadSign_ReduceSpeed) | (dTyp == RoadSign_Exclamation) | (dTyp == RoadSign_10) |
    			(dTyp == RoadSign_20) | (dTyp == RoadSign_30) | (dTyp == RoadSign_40) |
    			(dTyp == RoadSign_50) | (dTyp == RoadSign_60) | (dTyp == RoadSign_80) |
    			(dTyp == RoadSign_100) | (dTyp == RoadSign_Dash) | (dTyp == RoadSign_Dot) | (dTyp == RoadSign_NoAliens) | (dTyp == RoadSign_Toxic)) {
    		return true;
    	} else return false;
    }

    public static boolean IsRect(int dTyp) {
    	if ((dTyp == BinBag) | (dTyp == Planet) | (dTyp == TyreStack) | (dTyp == Tree) | (dTyp == Rock) | (dTyp == Vehicle) | (dTyp == Misc) | (dTyp == Portrait) | (dTyp == Text)) {
    		return true;
    	} else return false;
    }

	public static String[] GetPlatformTextures() {
		return platformTextures.clone();
	}

	public static String[] GetDecorSounds() {
		return platformSounds.clone();
	}

	public static String[] GetDecorImages() {
		return platformImages.clone();
	}

	public static float[] MakeTrack(float[] inarr) {
		float[] track = new float[8];
		track[0] = inarr[0];
		track[1] = inarr[1] - trackLength/6;
		track[2] = inarr[0] + inarr[3]*trackLength;
		track[3] = inarr[1] - trackLength/6;
		track[4] = inarr[0] + inarr[3]*trackLength;
		track[5] = inarr[1] + trackLength/6;
		track[6] = inarr[0];
		track[7] = inarr[1] + trackLength/6;
		// Now rotate
		PolygonOperations.RotateXYArray(track,inarr[2],inarr[0],inarr[1]);
		return track.clone();
	}

	public static String GetSoundFromIndex(int idx) {
		switch (idx) {
			case -1: return "None";
			case soundNone: return "None";
			case soundRain: return "Rain";
			case soundWaterfall: return "Waterfall";
			case soundWind: return "Wind";
			default: return "None";
		}
	}

	public static int GetSoundIndexFromString(String soundName) {
		switch (soundName) {
			case "None": return soundNone;
			case "Rain": return soundRain;
			case "Waterfall": return soundWaterfall;
			case "Wind": return soundWind;
			default: return soundNone;
		}
	}

	public static String GetImageFromIndex(int idx) {
		switch (idx) {
			case -1: return "None";
			case imageRain: return "Rain";
			case imageSnow: return "Snow";
			case imageWaterfall: return "Waterfall";
			default: return "Waterfall";
		}
	}

	public static int GetImageIndexFromString(String soundName) {
		switch (soundName) {
			case "Rain": return imageRain;
			case "Snow": return imageSnow;
			case "Waterfall": return imageWaterfall;
			default: return soundRain;
		}
	}

	public static String GetPlatformTextureFromIndex(int idx) {
    	switch (idx) {
			case -1: return "Grass";
			case (textureDefault+100): return "Default";
			case (textureAsphalt+100): return "Asphalt";
			case (textureAsphaltBlue+100): return "Asphalt (Blue)";
			case (textureBark+100): return "Bark";
			case (textureBarkH+100): return "Bark (H)";
			case (textureBricks+100): return "Bricks";
			case (textureBubbles+100): return "Bubbles";
			case (textureCrackedMud+100): return "Cracked Mud";
			case (textureFog+100): return "Fog";
			case (textureFogStain+100): return "Fog Stain";
			case (textureFogStainGrey+100): return "Fog Stain (grey)";
			case (textureGrass+100): return "Grass";
			case (textureGravel+100): return "Gravel";
			case (textureGravelDark+100): return "Gravel (Dark)";
			case (textureIce+100): return "Ice";
			case textureLava+100: return "Lava";
			case textureLavaDark+100: return "Lava (Dark)";
			case textureLeaves+100: return "Leaves";
			case textureLeavesGreen+100: return "Leaves (Green)";
			case textureMars+100: return "Mars";
			case textureMarsOld+100: return "Mars (old)";
			case textureMetalBlack+100: return "Metal (Black)";
			case textureMetalPlate+100: return "Metal (Plate)";
			case textureMetalDarkPlate+100: return "Metal (Dark Plate)";
			case textureMoon+100: return "Moon";
			case textureRoofTileGreen+100: return "Roof tile (green)";
			case textureRoofTileRed+100: return "Roof tile (red)";
			case textureSand+100: return "Sand";
			case textureShade+100: return "Shade";
			case textureShadeLight+100: return "Shade (Light)";
			case textureSnow+100: return "Snow";
			case textureSnowLight+100: return "Snow (light)";
			case textureSteel+100: return "Steel";
			case textureWater+100: return "Water";
			case textureWood+100: return "Wood";
			case textureWoodPlancksH+100: return "Wood Plancks (H)";
			case textureWoodPlancksD+100: return "Wood Plancks (D)";
			case textureWoodPlancksV+100: return "Wood Plancks (V)";
			case textureBarkMoss+100: return "Bark (Moss)";
			case textureDirt+100: return "Dirt";
			case textureGrassDaisy+100: return "Grass (Daisy)";
			case textureGrassShort+100: return "Grass (Short)";
			case textureGrassMeadow+100: return "Grass (Meadow)";
			case textureReptile+100: return "Reptile";
			case (textureBarkDark+100): return "Bark (Dark)";
			default: return "Grass";
		}
    }

	public static int GetPlatformIndexFromString(String textureName, int offset) {
		int offs = 0;
		if (offset != 0) offs += 100;
		switch (textureName) {
			case "Default": {
				if (offset == 0) return textureDefault;
				else return Grass;
			}
			case "Asphalt": return textureAsphalt+offs;
			case "Asphalt (Blue)": return textureAsphaltBlue+offs;
			case "Bark": return textureBark+offs;
			case "Bark (H)": return textureBarkH+offs;
			case "Bricks": return textureBricks+offs;
			case "Bubbles": return textureBubbles+offs;
			case "Cracked Mud": return textureCrackedMud+offs;
			case "Fog": return textureFog+offs;
			case "Fog Stain": return textureFogStain+offs;
			case "Fog Stain (grey)": return textureFogStainGrey+offs;
			case "Grass": return textureGrass+offs;
			case "Gravel": return textureGravel+offs;
			case "Gravel (Dark)": return textureGravelDark+offs;
			case "Ice": return textureIce+offs;
			case "Lava": return textureLava+offs;
			case "Lava (Dark)": return textureLavaDark+offs;
			case "Leaves": return textureLeaves+offs;
			case "Leaves (Green)": return textureLeavesGreen+offs;
			case "Mars": return textureMars+offs;
			case "Mars (old)": return textureMarsOld+offs;
			case "Metal (Black)": return textureMetalBlack+offs;
			case "Metal (Plate)": return textureMetalPlate+offs;
			case "Metal (Dark Plate)": return textureMetalDarkPlate+offs;
			case "Moon": return textureMoon+offs;
			case "Roof tile (green)": return textureRoofTileGreen+offs;
			case "Roof tile (red)": return textureRoofTileRed+offs;
			case "Sand": return textureSand+offs;
			case "Shade": return textureShade+offs;
			case "Shade (Light)": return textureShadeLight+offs;
			case "Snow": return textureSnow+offs;
			case "Snow (light)": return textureSnowLight+offs;
			case "Steel": return textureSteel+offs;
			case "Water": return textureWater+offs;
			case "Wood": return textureWood+offs;
			case "Wood Plancks (H)": return textureWoodPlancksH+offs;
			case "Wood Plancks (D)": return textureWoodPlancksD+offs;
			case "Wood Plancks (V)": return textureWoodPlancksV+offs;
			case "Bark (Moss)": return textureBarkMoss+offs;
			case "Dirt": return textureDirt+offs;
			case "Grass (Daisy)": return textureGrassDaisy+offs;
			case "Grass (Short)": return textureGrassShort+offs;
			case "Grass (Meadow)": return textureGrassMeadow+offs;
			case "Reptile": return textureReptile+offs;
			case "Bark (Dark)": return textureBarkDark+offs;
			default: {
				if (offset == 0) return 0;
				else return Grass;
			}
		}
	}

	public static int GetObjectNumber(String modeParent) {
    	if (modeParent.equals("Sign (10)")) return RoadSign_10;
    	else if (modeParent.equals("Sign (20)")) return RoadSign_20;
    	else if (modeParent.equals("Sign (30)")) return RoadSign_30;
    	else if (modeParent.equals("Sign (40)")) return RoadSign_40;
    	else if (modeParent.equals("Sign (50)")) return RoadSign_50;
    	else if (modeParent.equals("Sign (60)")) return RoadSign_60;
    	else if (modeParent.equals("Sign (80)")) return RoadSign_80;
    	else if (modeParent.equals("Sign (100)")) return RoadSign_100;
    	else if (modeParent.equals("Sign (Bumps Ahead)")) return RoadSign_Bumps;
    	else if (modeParent.equals("Sign (Dash)")) return RoadSign_Dash;
    	else if (modeParent.equals("Sign (Dot)")) return RoadSign_Dot;
    	else if (modeParent.equals("Sign (Do Not Enter)")) return RoadSign_DoNotEnter;
    	else if (modeParent.equals("Sign (Exclamation)")) return RoadSign_Exclamation;
    	else if (modeParent.equals("Sign (Motorbikes)")) return RoadSign_Motorbike;
    	else if (modeParent.equals("Sign (No Motorbikes)")) return RoadSign_NoMotorbike;
    	else if (modeParent.equals("Sign (Ramp Ahead)")) return RoadSign_RampAhead;
    	else if (modeParent.equals("Sign (Reduce Speed)")) return RoadSign_ReduceSpeed;
		else if (modeParent.equals("Sign (Stop)")) return RoadSign_Stop;
		else if (modeParent.equals("Sign (No Aliens)")) return RoadSign_NoAliens;
    	else return -1;
    }

    public static String GetObjectName(int objNumber) {
    	if (objNumber == RoadSign_10) return "10";
    	else if (objNumber == RoadSign_20) return "20";
    	else if (objNumber == RoadSign_30) return "30";
    	else if (objNumber == RoadSign_40) return "40";
    	else if (objNumber == RoadSign_50) return "50";
    	else if (objNumber == RoadSign_60) return "60";
    	else if (objNumber == RoadSign_80) return "80";
    	else if (objNumber == RoadSign_100) return "100";
    	else if (objNumber == RoadSign_Bumps) return "Bumps Ahead";
    	else if (objNumber == RoadSign_Dash) return "Dash";
    	else if (objNumber == RoadSign_Dot) return "Dot";
    	else if (objNumber == RoadSign_DoNotEnter) return "Do Not Enter";
    	else if (objNumber == RoadSign_Exclamation) return "Exclamation";
    	else if (objNumber == RoadSign_Motorbike) return "Motorbikes";
    	else if (objNumber == RoadSign_NoMotorbike) return "No Motorbikes";
    	else if (objNumber == RoadSign_RampAhead) return "Ramp Ahead";
    	else if (objNumber == RoadSign_ReduceSpeed) return "Reduce Speed";
		else if (objNumber == RoadSign_Stop) return "Stop";
		else if (objNumber == RoadSign_NoAliens) return "No Aliens";
    	else return "";
    }

    public static String GetRoadSignFilename(int decorID) {
		if (decorID == DecorVars.RoadSign_Stop) {
			return "images/RS_stop.png";
		} else if (decorID == DecorVars.RoadSign_DoNotEnter) {
			return "images/RS_donotenter.png";
		} else if (decorID == DecorVars.RoadSign_Bumps) {
			return "images/RS_bumps.png";
		} else if (decorID == DecorVars.RoadSign_Exclamation) {
			return "images/RS_exclamation.png";
		} else if (decorID == DecorVars.RoadSign_Motorbike) {
			return "images/RS_motorbike.png";
		} else if (decorID == DecorVars.RoadSign_NoMotorbike) {
			return "images/RS_nomotorbike.png";
		} else if (decorID == DecorVars.RoadSign_RampAhead) {
			return "images/RS_rampahead.png";
		} else if (decorID == DecorVars.RoadSign_ReduceSpeed) {
			return "images/RS_reducespeed.png";
		} else if (decorID == DecorVars.RoadSign_10) {
			return "images/RS_speed_10.png";
		} else if (decorID == DecorVars.RoadSign_20) {
			return "images/RS_speed_20.png";
		} else if (decorID == DecorVars.RoadSign_30) {
			return "images/RS_speed_30.png";
		} else if (decorID == DecorVars.RoadSign_40) {
			return "images/RS_speed_40.png";
		} else if (decorID == DecorVars.RoadSign_50) {
			return "images/RS_speed_50.png";
		} else if (decorID == DecorVars.RoadSign_60) {
			return "images/RS_speed_60.png";
		} else if (decorID == DecorVars.RoadSign_80) {
			return "images/RS_speed_80.png";
		} else if (decorID == DecorVars.RoadSign_100) {
			return "images/RS_speed_100.png";
		} else if (decorID == DecorVars.RoadSign_Dash) {
			return "images/RS_dash.png";
		} else if (decorID == DecorVars.RoadSign_Dot) {
			return "images/RS_dot.png";
		} else if (decorID == DecorVars.RoadSign_NoAliens) {
			return "images/RS_noaliens.png";
		} else if (decorID == DecorVars.RoadSign_Toxic) {
			return "images/RS_toxic.png";
		}
		return "images/error.png";
	}

	public static String GetImageRect(int decorID, int idx) {
		if (decorID == BinBag) return "images/binbag.png";
		else if (decorID == TyreStack) return "images/tyrestack_" + String.format("%02d", idx) + ".png";
		else if (decorID == Tree) return "images/tree_" + String.format("%02d", idx) + ".png";
		else if (decorID == Rock) return "images/rock_" + String.format("%02d", idx) + ".png";
		else if (decorID == Planet) return "images/planet_" + GetPlanetFromNumber(idx) + ".png";
		else if (decorID == Vehicle) return "images/vehicle_" + String.format("%02d", idx) + ".png";
		else if (decorID == Misc) return "images/misc_" + GetMiscFromNumber(idx) + ".png";
		else if (decorID == Shade) {
			if (idx == 0) return "images/shadeback.png";
			else return "images/shade.png";
		}
		else if (decorID == Portrait) return "images/portrait_" + String.format("%02d", idx) + ".png";
		else if (decorID == Text) return "images/text_" + String.format("%02d", idx) + ".png";
		// Make some default to stop errors
		return "images/error.png";
	}

	private static String GetPlanetFromNumber(int idx) {
		String planetName = "sun";
		switch (idx) {
			case 0:
				planetName = "sun";
				break;
			case 1:
				planetName = "mercury";
				break;
			case 2:
				planetName = "venus";
				break;
			case 3:
				planetName = "earth";
				break;
			case 4:
				planetName = "mars";
				break;
			case 5:
				planetName = "jupiter";
				break;
			case 6:
				planetName = "saturn";
				break;
			case 7:
				planetName = "uranus";
				break;
			case 8:
				planetName = "neptune";
				break;
			case 9:
				planetName = "moon";
				break;
			case 10:
				planetName = "supernova";
				break;
			case 11:
				planetName = "radiodish";
				break;
			case 12:
				planetName = "dishbase";
				break;
			case 13:
				planetName = "dish";
				break;
			case 14:
				planetName = "dishbolt";
				break;
			case 15:
				planetName = "earthgrey";
				break;
			default:
				break;
		}
		return planetName;
	}

	private static String GetMiscFromNumber(int idx) {
		String miscName = "emerald";
		switch (idx) {
			case 0:
				miscName = "emerald";
				break;
			case 1:
				miscName = "diamond";
				break;
			case 2:
				miscName = "diary";
				break;
			case 3:
				miscName = "log";
				break;
			case 4:
				miscName = "sisyphus";
				break;
			case 5:
				miscName = "solarpanel";
				break;
			default:
				break;
		}
		return miscName;
	}

	public static float[] GetCoordRect(int decorID, int idx) {
		return GetRectMultiple(decorID, idx, 0.0f, 0.0f).clone();
	}

	public static float[] GetNextRectMultiple(int decorID, int idx, float shiftX, float shiftY) {
		// idx is the current index, which this routine shifts to be the next index
		return GetRectMultiple(decorID, idx+1, shiftX, shiftY);
	}
	
	public static float[] GetRectMultiple(int decorID, int idx, float shiftX, float shiftY) {
		float xsize = 100.0f, scale=1.0f;
		if (decorID == BinBag) {
		    xsize = 30.0f;
		    scale = 1.21679f;
		    idx = 0;
		} else if (decorID == Rock) {
			xsize = 100.0f;
			if ((idx < 0) | (idx >= 8)) idx = 0;
			switch (idx) {
				case 0 :
					scale = 0.6044386422976501f;
					break;
				case 1 :
					scale = 0.47058823529411764f;
					break;
				case 2 :
					scale = 0.49919871794871795f;
					break;
				case 3 :
					xsize *= 2.0f;
					scale = 1.2442348008385744f;
					break;
				case 4 :
					xsize *= 1.5f;
					scale = 0.7419354838709677f;
					break;
				case 5 :
					xsize *= 3.0f;
					scale = 0.3079390537289495f;
					break;
				case 6 :
					xsize *= 2.0f;
					scale = 0.529886914378029f;
					break;
				case 7 :
					xsize *= 3.0f;
					scale = 0.28784313725490196f;
					break;
				default :
					break;
			}
		} else if (decorID == Tree) {
			xsize = 300.0f;
			if ((idx < 0) | (idx >= 12)) idx = 0;
			switch (idx) {
			case 0 :
				scale = 1.364605543710f;
				break;
			case 1 :
				scale = 1.1059907834101383f;
				break;
			case 2 :
				scale = 1.0f;
				break;
			case 3 :
				scale = 0.75f;
				break;
			case 4 :
				xsize *= 0.6f;
				scale = 0.75f;
				break;
			case 5 :
				scale = 1.0878186968838528f;
				break;
			case 6 :
				scale = 0.8666666666666667f;
				break;
			case 7 :
				xsize *= 1.2f;
				scale = 1.0f;
				break;
			case 8 :
				xsize = 12.0f;
				scale = 1.796875f;
			case 9 :
				xsize = 10.0f;
				scale = 2.34375f;
			case 10 :
				xsize = 10.0f;
				scale = 1.8125f;
			case 11 :
				xsize = 15.0f;
				scale = 1.3046875f;
			default :
				break;
			}
		} else if (decorID == TyreStack) {
			xsize = 50.0f;
			if ((idx < 0) | (idx >= 12)) idx = 0;
			switch (idx) {
				case 0 :
					scale = 0.52450980f;
					break;
				case 1 :
					scale = 0.52450980f;
					break;
				case 2 :
					xsize *= 1.5f;
					scale = 1.139705f;
					break;
				case 3 :
					xsize *= 1.5f;
					scale = 2.6094527f;
					break;
				case 4 :
					xsize *= 1.2f;
					scale = 1.4199029f;
					break;
				case 5 :
					xsize *= 0.6f;
					scale = 1.0f;
					break;
				case 6 :
					xsize *= 0.6f;
					scale = 1.0f;
					break;
				case 7 :
					xsize *= 0.72f;
					scale = 1.0f;
					break;
				case 8 :
					xsize *= 0.72f;
					scale = 1.0f;
					break;
				case 9 :
					xsize *= 0.2316f;
					scale = 2.725f;
					break;
				case 10 :
					xsize *= 0.6f;
					scale = 0.455696f;
					break;
				case 11 :
//				scale = 1.422680f;
					scale = 0.71f;
					break;
				default :
					break;
			}
		} else if (decorID == Planet) {
			xsize = 628.41345f;
			scale = 1.0f;
			if ((idx < 0) | (idx >= 16)) idx = 0;
			switch (idx) {
				case 0:
					xsize = 628.41345f; // Sun
					break;
				case 1:
					xsize = 19.0f; // Mercury
					break;
				case 2:
					xsize = 41.350018f; // Venus
					break;
				case 3:
					xsize = 40.488487f; // Earth
					break;
				case 4:
					xsize = 26.898338f; // Mars
					break;
				case 5:
					xsize = 320.4396f; //Jupiter
					break;
				case 6:
					xsize = 629.70020f; //Saturn
					scale = 0.4258493f;
					break;
				case 7:
					xsize = 115.536896f; // Uranus
					break;
				case 8:
					xsize = 115.536896f; // Neptune
					break;
				case 9:
					xsize = 40.488487f*0.2725f; // Moon
					break;
				case 10:
					xsize = 100.0f; // Supernova
					break;
				case 11:
					xsize = 500.0f; // Radio Telescope
					scale = 741.0f/586.0f;
					break;
				case 12:
					xsize = (363.0f/586.0f)*500.0f; // Radio Telescope Base
					scale = 143.0f/363.0f;
					break;
				case 13:
					xsize = 500.0f; // Radio Telescope Dish
					scale = 637.0f/586.0f;
					break;
				case 14:
					xsize = 400.0f; // Radio dish bolt
					scale = 194.0f/305.0f;
				case 15:
					xsize = 40.488487f; // Earth Grey
					break;
				default:
					break;
			}
		} else if (decorID == Vehicle) {
			xsize = 400.0f;
			scale = 1.0f;
			if ((idx < 0) | (idx >= 7)) idx = 0;
			switch (idx) {
				case 0:
					xsize = 400.0f; // Excavator
					scale = 868.0f/1314.0f;
					break;
				case 1:
					xsize = 400.0f; // Wrecking Ball
					scale = 0.9552941176470588f;
					break;
				case 2:
					xsize = 400.0f; // Digger
					scale = 334.0f/620.0f;
					break;
				case 3:
					xsize = 600.0f; // Dump truck
					scale = 342.0f/865.0f;
					break;
				case 4:
					xsize = 350.0f; // Van
					scale = 606.0f/1520.0f;
					break;
				case 5:
					xsize = 500.0f; // UFO
					scale = 273.0f/1234.0f;
					break;
				case 6:
					xsize = 40.0f; // hubcap
					scale = 1.0f;
					break;
				default:
					break;
			}
		} else if (decorID == Misc) {
			xsize = 10.0f;
			scale = 1.0f;
			if ((idx < 0) | (idx >= 6)) idx = 0;
			switch (idx) {
				case 0:
					xsize = 30.0f; // Emerald
					scale = 1.0f;
					break;
				case 1:
					xsize = 30.0f; // Diamond
					scale = 1.0f;
					break;
				case 2:
					xsize = 50.0f; // Diary
					scale = 447.0f/452.0f;
					break;
				case 3:
					xsize = 25.0f; // Log
					scale = 1.0f;
					break;
				case 4:
					xsize = 100.0f; // Sisyphus
					scale = 553.0f/735.0f;
					break;
				case 5:
					xsize = 100.0f; // Solar panel
					scale = 1024.0f/360.0f;
					break;
				default:
					break;
			}
		} else if (decorID == Portrait) {
			xsize = 400.0f;
			scale = 1.0f;
			if ((idx < 0) | (idx >= 9)) idx = 0;
			switch (idx) {
				case 0:
					scale = 2364.0f/1920.0f;
					break;
				case 1:
					scale = 1.0f;
					break;
				case 2:
					scale = 592.0f/474.0f;
					break;
				case 3:
					scale = 1283.0f/1600.0f;
					break;
				case 4:
					scale = 1067.0f/850.0f;
					break;
				case 5:
					scale = 636.0f/526.0f;
					break;
				case 6:
					scale = 1048.0f/1181.0f;
					break;
				case 7:
					scale = 1190.0f/861.0f;
					break;
				case 8:
					scale = 769.0f/567.0f;
					break;
				default:
					break;
			}
		} else if (decorID == Text) {
			xsize = 400.0f;
			scale = 1.0f;
			if ((idx < 0) | (idx >= 8)) idx = 0;
			switch (idx) {
				case 0:
					scale = 150.0f/600.0f;
					break;
				case 1:
					scale = 150.0f/600.0f;
					break;
				case 2:
					scale = 150.0f/600.0f;
					break;
				case 3:
					scale = 150.0f/600.0f;
					break;
				case 4:
					scale = 150.0f/600.0f;
					break;
				case 5:
					scale = 150.0f/600.0f;
					break;
				case 6:
					scale = 150.0f/600.0f;
					break;
				case 7:
					scale = 150.0f/600.0f;
					break;
				default:
					break;
			}
		}
		// Generate the coordinates
		float[] coords = {-xsize+shiftX, -xsize*scale+shiftY, xsize+shiftX, -xsize*scale+shiftY, xsize+shiftX, xsize*scale+shiftY, -xsize+shiftX, xsize*scale+shiftY, idx};
		return coords;
	}

	public static float[] MakeMoveableRect(int decorID, int idx, float shiftX, float shiftY) {
		return GetRectMultiple(decorID, idx, shiftX, shiftY); // This is the UFO
	}
}
