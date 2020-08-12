package com.mygdx.game.handlers;

import com.mygdx.game.utilities.EditorIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rcooke
 */
public class LevelsListCustom {
	public static int NUMCUSTOMLEVELS;
    public static String[] customLevelNames;
    public static String[] customLevelDescr;
    public static String[] customLevelFiles;
    public static String[] customLevelTimes;

    // Set the training levels
    public static void initialise() {
    	// Get the latest levels list
    	customLevelNames = EditorIO.LoadLevelNames(new String[] {"Return to Main Menu"});
    	NUMCUSTOMLEVELS = customLevelNames.length-1;
        customLevelDescr = new String[NUMCUSTOMLEVELS+1];
        customLevelFiles = new String[NUMCUSTOMLEVELS+1];
        customLevelTimes = new String[NUMCUSTOMLEVELS+1];
    	// Return to Main Menu
    	customLevelFiles[0] = null;
    	customLevelDescr[0] = "Return to the Main Menu\n(or press Esc)";
    	SetNames();
    	updateRecords();
    }

    public static void SetNames() {
    	for (int i=1; i<NUMCUSTOMLEVELS+1; i++) {
            customLevelDescr[i] = "";
            customLevelFiles[i] = EditorIO.levelDir + customLevelNames[i] + ".lvl";
    	}
    }
    
    public static String getLevelFile(int i) { return customLevelFiles[i]; }
    public static String getLevelName(int i) { return customLevelNames[i]; }

    public static void updateRecords() {
    	for (int i=1; i<NUMCUSTOMLEVELS+1; i++) {
    		customLevelTimes[i] = "No times are stored for custom levels";
    	}
    }

}
