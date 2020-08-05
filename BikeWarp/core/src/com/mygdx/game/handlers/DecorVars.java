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
    public static final int CollisionlessBG = 32;
    public static final int CollisionlessFG = 33;
    public static final int Rain = 34;
    public static final int BinBag = 35;
    public static final int TyreStack = 36;

    // Define the vertices
    public static final float[] decorCircleRoadSign = {0.0f,0.0f,30.0f,0.0f};
    public static final float[] decorWaterfall = {0.0f,-1500.0f,1000.0f,-1500.0f,1000.0f,1500.0f,0.0f,1500.0f};
    public static final float[] decorRain = {0.0f,-1500.0f,1000.0f,-1500.0f,1000.0f,1500.0f,0.0f,1500.0f};
    
    // Define the textures that can be applied to platforms
    public static final String[] platformTextures = {"Default", "Asphalt", "Bark", "Bricks", "Bubbles", "Cracked Mud", "Grass", "Gravel", "Ice", "Lava", "Leaves", "Mars", "Metal (Black)",  "Metal (Plate)", "Moon", "Roof tile (green)", "Roof tile (red)", "Sand", "Shade", "Snow", "Steel", "Water", "Wood", "Wood Plancks (H)", "Wood Plancks (V)"};

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
    	if ((dTyp == BinBag) | (dTyp == TyreStack)) {
    		return true;
    	} else return false;
    }

    public static String[] GetPlatformTextures() {
    	return platformTextures.clone();
    }
    
    public static String GetPlatformTextureFromIndex(int idx) {
    	if (idx == -1) return platformTextures[0];
    	return platformTextures[idx];
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
