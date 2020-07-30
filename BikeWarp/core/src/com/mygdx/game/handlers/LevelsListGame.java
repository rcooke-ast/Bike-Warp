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
	public static final int NUMGAMELEVELS = 40; // This is the number of levels (1 is added to the string arrays below for the "menu" option)
    public static String[] gameLevelNames = new String[1+NUMGAMELEVELS];
    public static String[] gameLevelDescr = new String[1+NUMGAMELEVELS];
    public static String[] gameLevelFiles = new String[1+NUMGAMELEVELS];
    public static String[] gameLevelTips = new String[1+NUMGAMELEVELS];

    // Set the levels
    public static void initialise() {
    	InitialiseTips();
    	// Return to Main Menu
    	gameLevelNames[0] = "Return to Main Menu";
    	gameLevelFiles[0] = null;
    	gameLevelDescr[0] = "Return to the Main Menu (or press Esc).";
    	// Level 1
    	gameLevelNames[1] = "1. Start Your Engine";
    	gameLevelFiles[1] = "data/gamelevels/01_StartYourEngine.lvl";
    	gameLevelTips[1] = "Tip: Collect the gold jewel and head to the exit warp";
    	// Level 2
    	gameLevelNames[2] = "2. Training";
    	gameLevelFiles[2] = "data/gamelevels/02_Training.lvl";
    	gameLevelTips[2] = "Tip: Use the 'b' key to bunny hop";
    	// Level 3
    	gameLevelNames[3] = "3. Hang Tight!";
    	gameLevelFiles[3] = "data/gamelevels/03_HangTight.lvl";
    	// Level 4
    	gameLevelNames[4] = "4. TBD";
    	gameLevelFiles[4] = "TBD";
    	// Level 5
    	gameLevelNames[5] = "5. Levitate";
    	gameLevelFiles[5] = "data/gamelevels/05_Levitate.lvl";
    	// Level 6
    	gameLevelNames[6] = "6. Excavation";
    	gameLevelFiles[6] = "data/gamelevels/06_Excavation.lvl";
    	// Level 7
    	gameLevelNames[7] = "7. Demolish";
    	gameLevelFiles[7] = "data/gamelevels/07_Demolish.lvl";
    	// Level 8
    	gameLevelNames[8] = "8. Big Tree Monument";
    	gameLevelFiles[8] = "data/gamelevels/08_BigTreeMonument.lvl";
    	// Level 9
    	gameLevelNames[9] = "9. Reservoir";
    	gameLevelFiles[9] = "data/gamelevels/09_Reservoir.lvl";
    	// Level 10
    	gameLevelNames[10] = "10. Bumpy Battle";
    	gameLevelFiles[10] = "data/gamelevels/10_BumpyBattle.lvl";
    	// Level 11
    	gameLevelNames[11] = "11. Black Mountain";
    	gameLevelFiles[11] = "data/gamelevels/11_BlackMountain.lvl";
    	// Level 12
    	gameLevelNames[12] = "12. TBD";
    	gameLevelFiles[12] = "TBD";
    	// Level 13
    	gameLevelNames[13] = "13. TBD";
    	gameLevelFiles[13] = "TBD";
    	// Level 14
    	gameLevelNames[14] = "14. TBD";
    	gameLevelFiles[14] = "TBD";
    	// Level 15
    	gameLevelNames[15] = "15. Upside Downhill";
    	gameLevelFiles[15] = "data/gamelevels/15_UpsideDownhill.lvl";
    	// Level 16
    	gameLevelNames[16] = "16. Logging";
    	gameLevelFiles[16] = "data/gamelevels/16_Logging.lvl";
    	// Level 17
    	gameLevelNames[17] = "17. Tantalising";
    	gameLevelFiles[17] = "data/gamelevels/17_Tantalising.lvl";
    	// Level 18
    	gameLevelNames[18] = "18. Over The Falls";
    	gameLevelFiles[18] = "data/gamelevels/18_OverTheFalls.lvl";
    	// Level 19
    	gameLevelNames[19] = "19. Absail";
    	gameLevelFiles[19] = "data/gamelevels/19_Absail.lvl";
    	// Level 20
    	gameLevelNames[20] = "20. Trials";
    	gameLevelFiles[20] = "data/gamelevels/20_Trials.lvl";
    	// Level 21
    	gameLevelNames[21] = "21. Lunatic";
    	gameLevelFiles[21] = "data/gamelevels/21_Lunatic.lvl";
    	// Level 22
    	gameLevelNames[22] = "22. Multiverse";
    	gameLevelFiles[22] = "data/gamelevels/22_Multiverse.lvl";
    	// Level 23
    	gameLevelNames[23] = "23. Escape Room";
    	gameLevelFiles[23] = "data/gamelevels/23_EscapeRoom.lvl";
    	// Level 24
    	gameLevelNames[24] = "24. Barracks";
    	gameLevelFiles[24] = "data/gamelevels/24_Barracks.lvl";
    	// Level 25
    	gameLevelNames[25] = "25. Hiraeth";
    	gameLevelFiles[25] = "data/gamelevels/25_Hiraeth.lvl";
    	// Level 26
    	gameLevelNames[26] = "26. Asteroid";
    	gameLevelFiles[26] = "data/gamelevels/26_Asteroid.lvl";
    	// Level 27
    	gameLevelNames[27] = "27. Dinosaur";
    	gameLevelFiles[27] = "data/gamelevels/27_Dinosaur.lvl";
    	// Level 28
    	gameLevelNames[28] = "28. Gold Digger";
    	gameLevelFiles[28] = "data/gamelevels/28_GoldDigger.lvl";
    	// Level 29
    	gameLevelNames[29] = "29. TBD";
    	gameLevelFiles[29] = "TBD";
    	// Level 30
    	gameLevelNames[30] = "30. TBD";
    	gameLevelFiles[30] = "TBD";
    	// Level 31
    	gameLevelNames[31] = "31. Ascraeus Mons";
    	gameLevelFiles[31] = "data/gamelevels/31_AscraeusMons.lvl";
    	// Level 32
    	gameLevelNames[32] = "32. Rigor Mortis";
    	gameLevelFiles[32] = "data/gamelevels/32_RigorMortis.lvl";
    	// Level 33
    	gameLevelNames[33] = "33. Lava Tube";
    	gameLevelFiles[33] = "data/gamelevels/33_LavaTube.lvl";
    	// Level 34
    	gameLevelNames[34] = "34. Olympus Mons";
    	gameLevelFiles[34] = "data/gamelevels/34_OlympusMons.lvl";
    	// Level 35
    	gameLevelNames[35] = "35. TBD";
    	gameLevelFiles[35] = "TBD";
    	// Level 36
    	gameLevelNames[36] = "36. TBD";
    	gameLevelFiles[36] = "TBD";
    	// Level 37
    	gameLevelNames[37] = "37. TBD";
    	gameLevelFiles[37] = "TBD";
    	// Level 38
    	gameLevelNames[38] = "38. Adrift";
    	gameLevelFiles[38] = "data/gamelevels/38_Adrift.lvl";
    	// Level 39
    	gameLevelNames[39] = "39. Emerald Harvest";
    	gameLevelFiles[39] = "data/gamelevels/39_EmeraldHarvest.lvl";
    	// Level 40
    	gameLevelNames[40] = "40. Alone";
    	gameLevelFiles[40] = "data/gamelevels/40_Alone.lvl";

    	// Finally, initialise the records strings
    	updateRecords();
    }

    private static void InitialiseTips() {
    	for (int ll=0; ll < 1+NUMGAMELEVELS; ll++) {
        	gameLevelTips[ll] = "";    		
    	}
    }
    
    public static String GetRecordTimes(int levid) {
    	String pb, pbd;
    	if (GameVars.GetLevelStatus(levid-1)==0) {
    		pb = "Level not yet complete!\n";
    		if (levid == LevelsListGame.NUMGAMELEVELS) pbd = "\n";
    		else if (GameVars.CanSkip()) pbd = "Press 's' to skip this level\n\n";
    		else pbd = "No skips left!\n\n";
    	} else if (GameVars.GetLevelStatus(levid-1)==2) {
    		pb = "Level skipped!";
    		pbd = "\n\n";
    	} else {
        	pb  = String.format("Personal Best\n%s\n\n", GameVars.getTimeString(GameVars.GetPlayerTimes(levid-1, 0)));
        	pbd = String.format("Personal Best (diamond)\n%s\n\n", GameVars.getTimeString(GameVars.GetPlayerTimesDmnd(levid-1, 0)));    		
    	}
    	String wr  = String.format("World Record\n%s\n\n", GameVars.getTimeString(GameVars.GetWorldTimes(levid-1, 0)));
    	String wrd = String.format("World Record (diamond)\n%s", GameVars.getTimeString(GameVars.GetWorldTimesDmnd(levid-1, 0)));
    	return pb+pbd+wr+wrd;
    }
    
    public static String getLevelFile(int i) { 
    	if (gameLevelFiles[i].equalsIgnoreCase("TBD")) {
    		return "data/gamelevels/01_StartYourEngine.lvl";
    	} else return gameLevelFiles[i];
    }

    public static String getLevelName(int i) { return gameLevelNames[i]; }

    public static void updateRecords() {
    	for (int i=0; i<NUMGAMELEVELS; i++) {
    		gameLevelDescr[i+1] = GetRecordTimes(i+1);
    	}
    }

}
