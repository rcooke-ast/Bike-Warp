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
	public static final int NUMGAMELEVELS = 35; // This is the number of levels (1 is added to the string arrays below for the "menu" option)
    public static String[] gameLevelNames = new String[1+NUMGAMELEVELS];
    public static String[] gameLevelDescr = new String[1+NUMGAMELEVELS];
    public static String[] gameLevelFiles = new String[1+NUMGAMELEVELS];

    // Set the levels
    public static void initialise() {
    	// Return to Main Menu
    	gameLevelNames[0] = "Return to Main Menu";
    	gameLevelFiles[0] = null;
    	gameLevelDescr[0] = "Return to the Main Menu (or press Esc).";
    	// Level 1
    	gameLevelNames[1] = "1. Start your engine";
    	gameLevelFiles[1] = "data/gamelevels/startengine.lvl";
    	// Level 2
    	gameLevelNames[2] = "2. Hang tight!";
    	gameLevelFiles[2] = "data/gamelevels/hangtight.lvl";
    	// Level 3
    	gameLevelNames[3] = "3. Whoops";
    	gameLevelFiles[3] = "data/gamelevels/whoops.lvl";
    	// Level 4
    	gameLevelNames[4] = "4. Suspension";
    	gameLevelFiles[4] = "data/gamelevels/suspension.lvl";
    	// Level 5
    	gameLevelNames[5] = "5. Skyjumper";
    	gameLevelFiles[5] = "data/gamelevels/skyjumper.lvl";
    	// Level 6
    	gameLevelNames[6] = "6. Steve";
    	gameLevelFiles[6] = "data/gamelevels/steve01.lvl";
    	// Level 7
    	gameLevelNames[7] = "7. Spikey";
    	gameLevelFiles[7] = "data/gamelevels/spikey.lvl";
    	// Level 8
    	gameLevelNames[8] = "8. Boxed In";
    	gameLevelFiles[8] = "data/gamelevels/boxedin.lvl";
    	// Level 9
    	gameLevelNames[9] = "9. Looper";
    	gameLevelFiles[9] = "data/gamelevels/looper.lvl";
    	// Level 10
    	gameLevelNames[10] = "10. Downhill Slalom (Part 1)";
    	gameLevelFiles[10] = "data/gamelevels/downhillslalom1.lvl";
    	// Level 11
    	gameLevelNames[11] = "11. Downhill Slalom (Part 2)";
    	gameLevelFiles[11] = "data/gamelevels/downhillslalom2.lvl";
    	// Level 12
    	gameLevelNames[12] = "12. Bumpy Battle";
    	gameLevelFiles[12] = "data/gamelevels/bumpybattle.lvl";
    	// Level 13
    	gameLevelNames[13] = "13. Halfpipe";
    	gameLevelFiles[13] = "data/gamelevels/halfpipe.lvl";
    	// Level 14
    	gameLevelNames[14] = "14. Levitate";
    	gameLevelFiles[14] = "data/gamelevels/levitate.lvl";
    	// Level 15
    	gameLevelNames[15] = "15. Olympus Mons";
    	gameLevelFiles[15] = "data/gamelevels/olympusmons.lvl";
    	// Level 16
    	gameLevelNames[16] = "16. Spinner";
    	gameLevelFiles[16] = "data/gamelevels/spinner.lvl";
    	// Level 17
    	gameLevelNames[17] = "17. X marks the spot";
    	gameLevelFiles[17] = "data/gamelevels/xmarks.lvl";
    	// Level 18
    	gameLevelNames[18] = "18. Fine tooth comb";
    	gameLevelFiles[18] = "data/gamelevels/steve02.lvl";
    	// Level 19
    	gameLevelNames[19] = "19. Underground (Part 1)";
    	gameLevelFiles[19] = "data/gamelevels/underground.lvl";
    	// Level 20
    	gameLevelNames[20] = "20. Underground (Part 2)";
    	gameLevelFiles[20] = "data/gamelevels/undergroun2.lvl";
    	// Level 21
    	gameLevelNames[21] = "21. Power Jump";
    	gameLevelFiles[21] = "data/gamelevels/powerjump.lvl";
    	// Level 22
    	gameLevelNames[22] = "22. Ball Pit";
    	gameLevelFiles[22] = "data/gamelevels/ballpit.lvl";
    	// Level 23
    	gameLevelNames[23] = "23. Escape";
    	gameLevelFiles[23] = "data/gamelevels/escape.lvl";
    	// Level 24
    	gameLevelNames[24] = "24. Upside Down";
    	gameLevelFiles[24] = "data/gamelevels/upsidedown.lvl";
    	// Level 25
    	gameLevelNames[25] = "25. Mineshaft";
    	gameLevelFiles[25] = "data/gamelevels/mineshaft.lvl";
    	// Level 26
    	gameLevelNames[26] = "26. Throttle up!";
    	gameLevelFiles[26] = "data/gamelevels/throttle.lvl";
    	// Level 27
    	gameLevelNames[27] = "27. Downhill";
    	gameLevelFiles[27] = "data/gamelevels/downhill.lvl";
    	// Level 28
    	gameLevelNames[28] = "28. Jigsaw";
    	gameLevelFiles[28] = "data/gamelevels/jigsaw.lvl";
    	// Level 29
    	gameLevelNames[29] = "29. Boulder Dash";
    	gameLevelFiles[29] = "data/gamelevels/boulderdash.lvl";
    	// Level 30
    	gameLevelNames[30] = "30. X-port";
    	gameLevelFiles[30] = "data/gamelevels/xport.lvl";
    	// Level 31
    	gameLevelNames[31] = "31. Trip Trap";
    	gameLevelFiles[31] = "data/gamelevels/triptrap.lvl";
    	// Level 32
    	gameLevelNames[32] = "32. Move!";
    	gameLevelFiles[32] = "data/gamelevels/move.lvl";
    	// Level 33
    	gameLevelNames[33] = "33. Earthquake";
    	gameLevelFiles[33] = "data/gamelevels/earthquake.lvl";
    	// Level 34
    	gameLevelNames[34] = "34. Loop the Loop";
    	gameLevelFiles[34] = "data/gamelevels/looptheloop.lvl";
    	// Level 35
    	gameLevelNames[35] = "35. Snakes and Ladders";
    	gameLevelFiles[35] = "data/gamelevels/snakeladder.lvl";
    	
    	gameLevelDescr[1] = (String) GetRecordTimes(1);
    	// Finally, initialise the records strings
    	updateRecords();
    }

    public static String GetRecordTimes(int levid) {
    	String pb  = String.format("Personal Best\n%s\n\n", GameVars.getTimeString(GameVars.GetPlayerTimes(levid-1, 0)));
    	String pbd = String.format("Personal Best (diamond)\n%s\n\n", GameVars.getTimeString(GameVars.GetPlayerTimesDmnd(levid-1, 0)));
    	String wr  = String.format("World Record\n%s\n\n", GameVars.getTimeString(GameVars.GetWorldTimes(levid-1, 0)));
    	String wrd = String.format("World Record (diamond)\n%s", GameVars.getTimeString(GameVars.GetWorldTimesDmnd(levid-1, 0)));
    	return pb+pbd+wr+wrd;
    }
    
    public static String getLevelFile(int i) { return gameLevelFiles[i]; }
    public static String getLevelName(int i) { return gameLevelNames[i]; }

    public static void updateRecords() {
    	for (int i=0; i<NUMGAMELEVELS; i++) {
    		gameLevelDescr[i+1] = GetRecordTimes(i+1);
    	}
    }

}
