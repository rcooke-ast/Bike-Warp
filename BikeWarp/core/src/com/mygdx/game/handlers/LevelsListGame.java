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
    	gameLevelTips[1] = "Tip: Collect the emerald jewel and head to the exit warp";
    	// Level 2
    	gameLevelNames[2] = "2. Same Same But Different";
    	gameLevelFiles[2] = "data/gamelevels/02_SameSameButDifferent.lvl";
    	gameLevelTips[2] = "Tip: Use the 'b' key to bunny hop";
    	// Level 3
    	gameLevelNames[3] = "3. Hang Tight!";
    	gameLevelFiles[3] = "data/gamelevels/03_HangTight.lvl";
    	// Level 4
    	gameLevelNames[4] = "4. Excavation";
    	gameLevelFiles[4] = "data/gamelevels/04_Excavation.lvl";
    	// Level 5
    	gameLevelNames[5] = "5. Black Mamba";
    	gameLevelFiles[5] = "data/gamelevels/05_BlackMamba.lvl";
    	// Level 6
    	gameLevelNames[6] = "6. Demolish";
    	gameLevelFiles[6] = "data/gamelevels/06_Demolish.lvl";
    	// Level 7
    	gameLevelNames[7] = "7. Bumpy Battle";
    	gameLevelFiles[7] = "data/gamelevels/07_BumpyBattle.lvl";
    	// Level 8
    	gameLevelNames[8] = "8. Cascade";
    	gameLevelFiles[8] = "data/gamelevels/08_Cascade.lvl";
		// Level 9
		gameLevelNames[9] = "9. Kilimanjaro";
		gameLevelFiles[9] = "data/gamelevels/09_Kilimanjaro.lvl";
		// Level 9 3/4
    	gameLevelNames[10] = "9 and 3/4. Levitate";
    	gameLevelFiles[10] = "data/gamelevels/09-75_Levitate.lvl";
    	// Level 10
    	gameLevelNames[11] = "10. Upside Downhill";
    	gameLevelFiles[11] = "data/gamelevels/10_UpsideDownhill.lvl";
    	// Level 11
    	gameLevelNames[12] = "11. Logging";
    	gameLevelFiles[12] = "data/gamelevels/11_Logging.lvl";
    	// Level 12
    	gameLevelNames[13] = "12. Tantalising";
    	gameLevelFiles[13] = "data/gamelevels/12_Tantalising.lvl";
    	// Level 13
    	gameLevelNames[14] = "13. Over The Falls";
    	gameLevelFiles[14] = "data/gamelevels/13_OverTheFalls.lvl";
    	// Level 14
    	gameLevelNames[15] = "14. Trials";
    	gameLevelFiles[15] = "data/gamelevels/14_Trials.lvl";
    	// Level 15
    	gameLevelNames[16] = "15. Tony The African Hawk";
    	gameLevelFiles[16] = "data/gamelevels/15_TonyTheAfricanHawk.lvl";
    	// Level 16
    	gameLevelNames[17] = "16. Big Tree Monument";
    	gameLevelFiles[17] = "data/gamelevels/16_BigTreeMonument.lvl";
    	// Level 17
    	gameLevelNames[18] = "17. Lava Pits";
    	gameLevelFiles[18] = "data/gamelevels/17_LavaPits.lvl";
    	// Level 18
    	gameLevelNames[19] = "18. Black Mountain (Day)";
    	gameLevelFiles[19] = "data/gamelevels/18_BlackMountainDay.lvl";
    	// Level 19
    	gameLevelNames[20] = "19. Black Mountain (Night)";
    	gameLevelFiles[20] = "data/gamelevels/19_BlackMountainNight.lvl";
		// Level 20
		gameLevelNames[21] = "20. Mwanda Peaks";
		gameLevelFiles[21] = "data/gamelevels/20_MwandaPeaks.lvl";
    	// Level 21
    	gameLevelNames[22] = "21. Lunatic";
    	gameLevelFiles[22] = "data/gamelevels/21_Lunatic.lvl";
    	// Level 22
    	gameLevelNames[23] = "22. Multiverse";
    	gameLevelFiles[23] = "data/gamelevels/22_Multiverse.lvl";
    	// Level 23
    	gameLevelNames[24] = "23. Escape Room";
    	gameLevelFiles[24] = "data/gamelevels/23_EscapeRoom.lvl";
    	// Level 24
    	gameLevelNames[25] = "24. Barracks";
    	gameLevelFiles[25] = "data/gamelevels/24_Barracks.lvl";
    	// Level 25
    	gameLevelNames[26] = "25. Hiraeth";
    	gameLevelFiles[26] = "data/gamelevels/25_Hiraeth.lvl";
    	// Level 26
    	gameLevelNames[27] = "26. Asteroid";
    	gameLevelFiles[27] = "data/gamelevels/26_Asteroid.lvl";
    	// Level 27
    	gameLevelNames[28] = "27. Dinosaur";
    	gameLevelFiles[28] = "data/gamelevels/27_Dinosaur.lvl";
    	// Level 28
    	gameLevelNames[29] = "28. Gold Digger";
    	gameLevelFiles[29] = "data/gamelevels/28_GoldDigger.lvl";
    	// Level 29
    	gameLevelNames[30] = "29. Free Will";
    	gameLevelFiles[30] = "29_FreeWill.lvl";
    	// Level 30
    	gameLevelNames[31] = "30. Short Supply";
    	gameLevelFiles[31] = "data/gamelevels/30_ShortSupply.lvl";
    	// Level 31
    	gameLevelNames[32] = "31. Ascraeus Mons";
    	gameLevelFiles[32] = "data/gamelevels/31_AscraeusMons.lvl";
    	// Level 32
    	gameLevelNames[33] = "32. Rigor Mortis";
    	gameLevelFiles[33] = "data/gamelevels/32_RigorMortis.lvl";
    	// Level 33
    	gameLevelNames[34] = "33. Lava Tube";
    	gameLevelFiles[34] = "data/gamelevels/33_LavaTube.lvl";
    	// Level 34
    	gameLevelNames[35] = "34. Olympus Mons";
    	gameLevelFiles[35] = "data/gamelevels/34_OlympusMons.lvl";
    	// Level 35
    	gameLevelNames[36] = "35. The Message";
    	gameLevelFiles[36] = "data/gamelevels/35_TheMessage.lvl";
    	// Level 36
    	gameLevelNames[37] = "36. Adrift";
    	gameLevelFiles[37] = "data/gamelevels/36_Adrift.lvl";
    	// Level 37
    	gameLevelNames[38] = "37. Emerald Harvest";
    	gameLevelFiles[38] = "data/gamelevels/37_EmeraldHarvest.lvl";
    	// Level 38
    	gameLevelNames[39] = "38. Alone";
    	gameLevelFiles[39] = "data/gamelevels/38_Alone.lvl";
    	// Level 39
    	gameLevelNames[40] = "39. Solar System";
    	gameLevelFiles[40] = "data/gamelevels/39_SolarSystem.lvl";
    	// Level 40
    	gameLevelNames[41] = "40. Dreaming of Home";
    	gameLevelFiles[41] = "data/gamelevels/40_DreamingofHome.lvl";

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
