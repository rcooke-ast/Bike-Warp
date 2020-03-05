package com.mygdx.game.handlers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rcooke
 */
public class LevelsListGame {
	public static final int NUMGAMELEVELS = 28; // This is the number of levels (1 is added to the string arrays below for the "menu" option)
    public static String[] gameLevelNames = new String[1+NUMGAMELEVELS];
    public static String[] gameLevelDescr = new String[1+NUMGAMELEVELS];
    public static String[] gameLevelFiles = new String[1+NUMGAMELEVELS];

    // Set the levels
    static {
    	// Return to Main Menu
    	gameLevelNames[0] = "Return to Main Menu";
    	gameLevelFiles[0] = null;
    	gameLevelDescr[0] = "Return to the Main Menu (or press Esc).";
    	// Level 1
    	gameLevelNames[1] = "1. Start your engine";
    	gameLevelFiles[1] = "data/gamelevels/startengine.lvl";
    	gameLevelDescr[1] = "Display records";
    	// Level 2
    	gameLevelNames[2] = "2. Whoops";
    	gameLevelFiles[2] = "data/gamelevels/whoops.lvl";
    	gameLevelDescr[2] = "Display records";
    	// Level 3
    	gameLevelNames[3] = "3. Suspension";
    	gameLevelFiles[3] = "data/gamelevels/suspension.lvl";
    	gameLevelDescr[3] = "Display records";
    	// Level 4
    	gameLevelNames[4] = "4. Skyjumper";
    	gameLevelFiles[4] = "data/gamelevels/skyjumper.lvl";
    	gameLevelDescr[4] = "Display records";
    	// Level 5
    	gameLevelNames[5] = "5. Steve 1";
    	gameLevelFiles[5] = "data/gamelevels/steve01.lvl";
    	gameLevelDescr[5] = "Display records";
    	// Level 6
    	gameLevelNames[6] = "6. Steve 2";
    	gameLevelFiles[6] = "data/gamelevels/steve02.lvl";
    	gameLevelDescr[6] = "Display records";
    	// Level 7
    	gameLevelNames[7] = "7. Boulder Dash";
    	gameLevelFiles[7] = "data/gamelevels/boulderdash.lvl";
    	gameLevelDescr[7] = "Display records";
    	// Level 8
    	gameLevelNames[8] = "8. Boxed In";
    	gameLevelFiles[8] = "data/gamelevels/boxedin.lvl";
    	gameLevelDescr[8] = "Display records";
    	// Level 9
    	gameLevelNames[9] = "9. Looper";
    	gameLevelFiles[9] = "data/gamelevels/looper.lvl";
    	gameLevelDescr[9] = "Display records";
    	// Level 10
    	gameLevelNames[10] = "10. Downhill Slalom (Part 1)";
    	gameLevelFiles[10] = "data/gamelevels/downhillslalom1.lvl";
    	gameLevelDescr[10] = "Display records";
    	// Level 11
    	gameLevelNames[11] = "11. Downhill Slalom (Part 2)";
    	gameLevelFiles[11] = "data/gamelevels/downhillslalom2.lvl";
    	gameLevelDescr[11] = "Display records";
    	// Level 12
    	gameLevelNames[12] = "12. Bumpy Battle";
    	gameLevelFiles[12] = "data/gamelevels/bumpybattle.lvl";
    	gameLevelDescr[12] = "Display records";
    	// Level 13
    	gameLevelNames[13] = "13. Halfpipe";
    	gameLevelFiles[13] = "data/gamelevels/halfpipe.lvl";
    	gameLevelDescr[13] = "Display records";
    	// Level 14
    	gameLevelNames[14] = "14. Levitate";
    	gameLevelFiles[14] = "data/gamelevels/levitate.lvl";
    	gameLevelDescr[14] = "Display records";
    	// Level 15
    	gameLevelNames[15] = "15. Olympus Mons";
    	gameLevelFiles[15] = "data/gamelevels/olympusmons.lvl";
    	gameLevelDescr[15] = "Display records";
    	// Level 16
    	gameLevelNames[16] = "16. Spinner";
    	gameLevelFiles[16] = "data/gamelevels/spinner.lvl";
    	gameLevelDescr[16] = "Display records";
    	// Level 17
    	gameLevelNames[17] = "17. X marks the spot";
    	gameLevelFiles[17] = "data/gamelevels/xmarks.lvl";
    	gameLevelDescr[17] = "Display records";
    	// Level 18
    	gameLevelNames[18] = "18. X-port";
    	gameLevelFiles[18] = "data/gamelevels/xport.lvl";
    	gameLevelDescr[18] = "Display records";
    	// Level 19
    	gameLevelNames[19] = "19. Underground";
    	gameLevelFiles[19] = "data/gamelevels/underground.lvl";
    	gameLevelDescr[19] = "Display records";
    	// Level 20
    	gameLevelNames[20] = "20. Underground 2";
    	gameLevelFiles[20] = "data/gamelevels/undergroun2.lvl";
    	gameLevelDescr[20] = "Display records";
    	// Level 21
    	gameLevelNames[21] = "21. Power Jump";
    	gameLevelFiles[21] = "data/gamelevels/powerjump.lvl";
    	gameLevelDescr[21] = "Display records";
    	// Level 22
    	gameLevelNames[22] = "22. Ball Pit";
    	gameLevelFiles[22] = "data/gamelevels/ballpit.lvl";
    	gameLevelDescr[22] = "Display records";
    	// Level 23
    	gameLevelNames[23] = "23. Escape";
    	gameLevelFiles[23] = "data/gamelevels/escape.lvl";
    	gameLevelDescr[23] = "Display records";
    	// Level 24
    	gameLevelNames[24] = "24. Upside Down";
    	gameLevelFiles[24] = "data/gamelevels/upsidedown.lvl";
    	gameLevelDescr[24] = "Display records";
    	// Level 25
    	gameLevelNames[25] = "25. Mineshaft";
    	gameLevelFiles[25] = "data/gamelevels/mineshaft.lvl";
    	gameLevelDescr[25] = "Display records";
    	// Level 26
    	gameLevelNames[26] = "26. Throttle up!";
    	gameLevelFiles[26] = "data/gamelevels/throttle.lvl";
    	gameLevelDescr[26] = "Display records";
    	// Level 27
    	gameLevelNames[27] = "27. Downhill";
    	gameLevelFiles[27] = "data/gamelevels/downhill.lvl";
    	gameLevelDescr[27] = "Display records";
    	// Level 28
    	gameLevelNames[28] = "28. Jigsaw";
    	gameLevelFiles[28] = "data/gamelevels/jigsaw.lvl";
    	gameLevelDescr[28] = "Display records";
    }

    public static String getLevelFile(int i) { return gameLevelFiles[i]; }
    public static String getLevelName(int i) { return gameLevelNames[i]; }

}
