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
	public static final int NUMGAMELEVELS = 54; // This is the number of levels (1 is added to the string arrays below for the "menu" option)
    public static String[] gameLevelNames = new String[1+NUMGAMELEVELS];
    public static String[] gameLevelDescr = new String[1+NUMGAMELEVELS];
    public static String[] gameLevelFiles = new String[1+NUMGAMELEVELS];
    public static String[] gameLevelTips = new String[1+NUMGAMELEVELS];

    // Set the levels
    public static void initialise() {
    	InitialiseTips();
    	int lnum;
    	// Return to Main Menu
		lnum = 0;
    	gameLevelNames[lnum] = "Return to Main Menu";
    	gameLevelFiles[lnum] = null;
    	gameLevelDescr[lnum] = "Return to the Main Menu (or press Esc).";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Start Your Engine";
		gameLevelFiles[lnum] = "data/gamelevels/StartYourEngine.lvl";
		gameLevelTips[lnum] = "Tip: Collect the emerald jewel and head to the exit warp";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Philosopher Falls";
		gameLevelFiles[lnum] = "data/gamelevels/PhilosopherFalls.lvl";
		gameLevelTips[lnum] = "Tip: Use the 'b' key to bunny hop";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Same, But Different";
		gameLevelFiles[lnum] = "data/gamelevels/SameButDifferent.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". The Woodcutter";
		gameLevelFiles[lnum] = "data/gamelevels/TheWoodcutter.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Watering Hole";
		gameLevelFiles[lnum] = "data/gamelevels/WateringHoleDay.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Excavation";
    	gameLevelFiles[lnum] = "data/gamelevels/Excavation.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Le Petit Serpent";
    	gameLevelFiles[lnum] = "data/gamelevels/LePetitSerpent.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Demolish";
		gameLevelFiles[lnum] = "data/gamelevels/Demolish.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Race The Train!";
		gameLevelFiles[lnum] = "data/gamelevels/RaceTheTrain.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Star Shadows";
    	gameLevelFiles[lnum] = "data/gamelevels/StarShadows.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Watering Hole at Night";
		gameLevelFiles[lnum] = "data/gamelevels/WateringHoleNight.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Cascade";
		gameLevelFiles[lnum] = "data/gamelevels/Cascade.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Mahlasela Pass";
		gameLevelFiles[lnum] = "data/gamelevels/MahlaselaPass.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Mount Kilimanjaro";
		gameLevelFiles[lnum] = "data/gamelevels/Kilimanjaro.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Tree Hopper";
		gameLevelFiles[lnum] = "data/gamelevels/TreeHopper.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Levitate";
    	gameLevelFiles[lnum] = "data/gamelevels/Levitate.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Global Warming";
    	gameLevelFiles[lnum] = "data/gamelevels/GlobalWarming.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Logging";
    	gameLevelFiles[lnum] = "data/gamelevels/Logging.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Wormhole";
    	gameLevelFiles[lnum] = "data/gamelevels/Wormhole.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Over The Falls";
    	gameLevelFiles[lnum] = "data/gamelevels/OverTheFalls.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Shadow Trials";
		gameLevelFiles[lnum] = "data/gamelevels/ShadowTrials.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Abandoned";
		gameLevelFiles[lnum] = "data/gamelevels/Abandoned.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Tony The African Hawk";
    	gameLevelFiles[lnum] = "data/gamelevels/TonyTheAfricanHawk.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". In The Canopy";
    	gameLevelFiles[lnum] = "data/gamelevels/Canopy.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Lava Pits";
    	gameLevelFiles[lnum] = "data/gamelevels/LavaPits.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Petrichor (Petros)";
    	gameLevelFiles[lnum] = "data/gamelevels/Petrichor_Petros.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Petrichor (Ichor)";
    	gameLevelFiles[lnum] = "data/gamelevels/Petrichor_Ichor.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Gold Digger";
		gameLevelFiles[lnum] = "data/gamelevels/GoldDiggerEarth.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Snowy Peaks";
		gameLevelFiles[lnum] = "data/gamelevels/SnowyPeaks.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Lunatic";
    	gameLevelFiles[lnum] = "data/gamelevels/Lunatic.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Multiverse";
    	gameLevelFiles[lnum] = "data/gamelevels/Multiverse.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Barracks";
    	gameLevelFiles[lnum] = "data/gamelevels/Barracks.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Hiraeth (Part 1)";
    	gameLevelFiles[lnum] = "data/gamelevels/HiraethPart1.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Hiraeth (Part 2)";
    	gameLevelFiles[lnum] = "data/gamelevels/HiraethPart2.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Dinosaur";
		gameLevelFiles[lnum] = "data/gamelevels/Dinosaur.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Zig Zag";
		gameLevelFiles[lnum] = "data/gamelevels/ZigZag.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Lunar Digger";
    	gameLevelFiles[lnum] = "data/gamelevels/LunarDigger.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Free Will";
    	gameLevelFiles[lnum] = "data/gamelevels/FreeWill.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Short Supply";
    	gameLevelFiles[lnum] = "data/gamelevels/ShortSupply.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Ascraeus Mons";
    	gameLevelFiles[lnum] = "data/gamelevels/AscraeusMons.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Rigor Mortis";
    	gameLevelFiles[lnum] = "data/gamelevels/RigorMortis.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Lava Tube";
    	gameLevelFiles[lnum] = "data/gamelevels/LavaTube.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Olympus Mons";
    	gameLevelFiles[lnum] = "data/gamelevels/OlympusMons.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Swim Between the Flags";
    	gameLevelFiles[lnum] = "data/gamelevels/SwimBetweenTheFlags.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". The Myth of Sisyphus (Part 1)";
		gameLevelFiles[lnum] = "data/gamelevels/TheMythOfSisyphusPart1.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". The Myth of Sisyphus (Part 2)";
		gameLevelFiles[lnum] = "data/gamelevels/TheMythOfSisyphusPart2.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". The Myth of Sisyphus (Part 3)";
		gameLevelFiles[lnum] = "data/gamelevels/TheMythOfSisyphusPart3.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Adrift";
    	gameLevelFiles[lnum] = "data/gamelevels/Adrift.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Emerald Harvest";
    	gameLevelFiles[lnum] = "data/gamelevels/EmeraldHarvest.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Asteroid Field";
		gameLevelFiles[lnum] = "data/gamelevels/AsteroidField.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Space Station";
    	gameLevelFiles[lnum] = "data/gamelevels/SpaceStation.lvl";

		lnum += 1;
    	gameLevelNames[lnum] = String.valueOf(lnum)+". Solar System";
    	gameLevelFiles[lnum] = "data/gamelevels/SolarSystem.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Dreaming of Home";
		gameLevelFiles[lnum] = "data/gamelevels/DreamingofHome.lvl";

		lnum += 1;
		gameLevelNames[lnum] = String.valueOf(lnum)+". Is This The End?";
		gameLevelFiles[lnum] = "data/gamelevels/IsThisTheEnd.lvl";

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
    		return "data/gamelevels/StartYourEngine.lvl";
    	} else return gameLevelFiles[i];
    }

    public static String getLevelName(int i) { return gameLevelNames[i]; }

    public static void updateRecords() {
    	for (int i=0; i<NUMGAMELEVELS; i++) {
    		gameLevelDescr[i+1] = GetRecordTimes(i+1);
    	}
    }

}
