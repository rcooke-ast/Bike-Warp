package com.mygdx.game.handlers;

public class DecorVars {
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
    public static final int Grass = 29;
    public static final int LargeStone = 30;
    public static final int Waterfall = 31;
    public static final int Rain = 34;
    public static final int BinBag = 35;
	public static final int TyreStack = 36;
	public static final int Tree = 37;
	public static final int Rock = 38;

    // Define the vertices
    public static final float[] decorCircleRoadSign = {0.0f,0.0f,30.0f,0.0f};
    public static final float[] decorWaterfall = {0.0f,-1500.0f,1000.0f,-1500.0f,1000.0f,1500.0f,0.0f,1500.0f};
    public static final float[] decorRain = {0.0f,-1500.0f,1000.0f,-1500.0f,1000.0f,1500.0f,0.0f,1500.0f};
    
    // Define the textures that can be applied to platforms
    public static final String[] platformTextures = {"Default", "Asphalt", "Bark", "Bricks", "Bubbles", "Cracked Mud", "Grass", "Gravel", "Ice", "Lava", "Leaves", "Mars", "Metal (Black)",  "Metal (Plate)", "Moon", "Roof tile (green)", "Roof tile (red)", "Sand", "Shade", "Snow", "Steel", "Water", "Wood", "Wood Plancks (H)", "Wood Plancks (V)"};
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
	//public static final int texture = ;

    public static boolean IsRoadSign(int dTyp) {
    	if  ((dTyp == RoadSign_Stop) | (dTyp == RoadSign_DoNotEnter) | (dTyp == RoadSign_RampAhead) |
    			(dTyp == RoadSign_Bumps) | (dTyp == RoadSign_Motorbike) | (dTyp == RoadSign_NoMotorbike) |
    			(dTyp == RoadSign_ReduceSpeed) | (dTyp == RoadSign_Exclamation) | (dTyp == RoadSign_10) |
    			(dTyp == RoadSign_20) | (dTyp == RoadSign_30) | (dTyp == RoadSign_40) |
    			(dTyp == RoadSign_50) | (dTyp == RoadSign_60) | (dTyp == RoadSign_80) |
    			(dTyp == RoadSign_100) | (dTyp == RoadSign_Dash) | (dTyp == RoadSign_Dot)) {
    		return true;
    	} else return false;
    }

    public static boolean IsRect(int dTyp) {
    	if ((dTyp == BinBag) | (dTyp == TyreStack) | (dTyp == Tree) | (dTyp == Rock)) {
    		return true;
    	} else return false;
    }

    public static String[] GetPlatformTextures() {
    	return platformTextures.clone();
    }
    
    public static String GetPlatformTextureFromIndex(int idx) {
    	switch (idx) {
			case -1: return "Default";
			case textureDefault: return "Default";
			case textureAsphalt: return "Asphalt";
			case textureBark: return "Bark";
			case textureBricks: return "Bricks";
			case textureBubbles: return "Bubbles";
			case textureCrackedMud: return "Cracked Mud";
			case textureGrass: return "Grass";
			case textureGravel: return "Gravel";
			case textureIce: return "Ice";
			case textureLava: return "Lava";
			case textureLeaves: return "Leaves";
			case textureMars: return "Mars";
			case textureMetalBlack: return "Metal (Black)";
			case textureMetalPlate: return "Metal (Plate)";
			case textureMoon: return "Moon";
			case textureRoofTileGreen: return "Roof tile (green)";
			case textureRoofTileRed: return "Roof tile (red)";
			case textureSand: return "Sand";
			case textureShade: return "Shade";
			case textureSnow: return "Snow";
			case textureSteel: return "Steel";
			case textureWater: return "Water";
			case textureWood: return "Wood";
			case textureWoodPlancksH: return "Wood Plancks (H)";
			case textureWoodPlancksV: return "Wood Plancks (V)";
			default: return "Default";
		}
    }

	public static int GetPlatformIndexFromString(String textureName) {
		switch (textureName) {
			case "Default": return textureDefault;
			case "Asphalt": return textureAsphalt;
			case "Bark": return textureBark;
			case "Bricks": return textureBricks;
			case "Bubbles": return textureBubbles;
			case "Cracked Mud": return textureCrackedMud;
			case "Grass": return textureGrass;
			case "Gravel": return textureGravel;
			case "Ice": return textureIce;
			case "Lava": return textureLava;
			case "Leaves": return textureLeaves;
			case "Mars": return textureMars;
			case "Metal (Black)": return textureMetalBlack;
			case "Metal (Plate)": return textureMetalPlate;
			case "Moon": return textureMoon;
			case "Roof tile (green)": return textureRoofTileGreen;
			case "Roof tile (red)": return textureRoofTileRed;
			case "Sand": return textureSand;
			case "Shade": return textureShade;
			case "Snow": return textureSnow;
			case "Steel": return textureSteel;
			case "Water": return textureWater;
			case "Wood": return textureWood;
			case "Wood Plancks (H)": return textureWoodPlancksH;
			case "Wood Plancks (V)": return textureWoodPlancksV;
			default: return 0;
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
    	else return "";
    }

	public static String GetImageRect(int decorID, int idx) {
		if (decorID == BinBag) return "images/binbag.png";
		else if (decorID == TyreStack) return "images/tyrestack_" + String.format("%02d", idx) + ".png";
		else if (decorID == Tree) return "images/tree_" + String.format("%02d", idx) + ".png";
		else if (decorID == Rock) return "images/rock_" + String.format("%02d", idx) + ".png";
		// Make some default to stop errors
		return "images/error.png";
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
			if ((idx < 0) | (idx >= 8)) idx = 0;
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
		}
		// Generate the coordinates
		float[] coords = {-xsize+shiftX, -xsize*scale+shiftY, xsize+shiftX, -xsize*scale+shiftY, xsize+shiftX, xsize*scale+shiftY, -xsize+shiftX, xsize*scale+shiftY, idx};
		return coords;
	}
}
