/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.codedisaster.steamworks.SteamAPI;

/**
 *
 * @author rcooke
 */
public class GameVars implements Serializable {

	public static final String[] countryNames = {"Nationality", "Afghanistan", "Aland Islands", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bonaire", "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos Islands", "Colombia", "Comoros", "Congo", "Congo", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba", "Curacao", "Cyprus", "Czechia", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guernsey", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard Island and McDonald Islands", "Holy See", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea", "Korea", "Kosovo", "Kuwait", "Kyrgyzstan", "Lao People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macao", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Macedonia", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Barthelemy", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin", "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Sint Maarten", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States Minor Outlying Islands", "United States of America", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Viet Nam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna", "Western Sahara", "Yemen", "Zambia", "Zimbabwe"};

	private static final long serialVersionUID = 1L;

	// How many level skips are allowed
	public static final float textHeight = 0.024725739f; // Multiply this by SCRHEIGHT and this represents the required scale of the text, given the glyphText "My"
	public static final int skipsAllowed = 3; // Allow players to skip 3 levels
	public static final int numStore = 10; // Store the top 10 times of each level
	public static final int MaxTime = 10*60*1000;  // 10 minutes is the maximum time that a player can play for!
	public static final int DISPLAY_OFF = 0;
	public static final int DISPLAY_ON = 1;
	public static final int DISPLAY_SMARTON = 2;
	// Latest time
	public static int timerTotal = -1;
	public static int EmeraldDiamond = 0;
	// Player arrays
	public static int currentPlayer = -1;
	public static int currentSteamUser = -1;
	public static boolean personalBest = false;
	public static boolean worldRecord = false;
	public static int[] plyrID = new int[0];
	public static int[] plyrCountryID = new int[0];
	public static String[] plyrName = new String[0];
	public static ArrayList<Replay[]> plyrReplays = new ArrayList<Replay[]>();
	public static ArrayList<Replay[]> plyrReplaysDmnd = new ArrayList<Replay[]>();
	public static ArrayList<ArrayList<Integer>> plyrTimes = new ArrayList<ArrayList<Integer>>();
	public static ArrayList<ArrayList<Integer>> plyrTimesDmnd = new ArrayList<ArrayList<Integer>>();
	public static ArrayList<int[]> plyrControls = new ArrayList<int[]>();
	public static ArrayList<int[]> plyrDisplay = new ArrayList<int[]>();
	public static Boolean plyrFullscreen = true;
	public static ArrayList<boolean[]> plyrColDmnd = new ArrayList<boolean[]>();
	public static ArrayList<int[]> plyrLevelComplete = new ArrayList<int[]>();
	public static ArrayList<float[]> plyrBikeColor = new ArrayList<float[]>();
	public static ArrayList<Integer> plyrTotalTimes = new ArrayList<Integer>();
	public static ArrayList<Integer> plyrTotalTimesDmnd = new ArrayList<Integer>();

	// Get and Set options
	public static void SetCurrentPlayerOffline(int id) {
		currentPlayer = id;
	}

	// Get and Set options
	public static void SetCurrentPlayer(int id, String name) {
		for (int ii=0; ii < plyrID.length; ii++) {
			if (id==plyrID[ii]) {
				currentPlayer = ii;
				if (plyrName[currentPlayer].compareTo(name) != 0) {
					// Player has updated their name online - update it here too
					plyrName[currentPlayer] = name;
					SavePlayers();
				}
				return;
			}
		}
		// If we get to here and no player ID has been found, then we need to add it here
		AddPlayer(id, name);
		currentPlayer = plyrID.length-1;
		SavePlayers();
	}

	// Get and Set options
	public static void SetTimerTotal(int i) {
		timerTotal = i;
		if (i<0) SetEmeraldDiamond(0);
	}
	public static void SetEmeraldDiamond(int emdm) {EmeraldDiamond = emdm;}
	public static void SetWorldRecord(boolean wr) {worldRecord = wr;}
	public static void SetPersonalBest(boolean pb) {personalBest = pb;}

	public static int GetCurrentPlayer() {return currentPlayer;}

	public static boolean IsCountrySet() {
		return plyrCountryID[currentPlayer] != -1;
	}

	public static boolean SetPlayerCountry(int idx) {
		boolean retval = false;
		// Check if a player has changed their nationality - if so, update Steam records.
		if (plyrCountryID[currentPlayer] != -1) retval = true;
		// Update and save
		plyrCountryID[currentPlayer] = idx;
		SavePlayers();
		if (!SteamAPI.isSteamRunning()) retval = false;
		return retval;
	}

	public static String GetPlayerCountry() {
		return countryNames[plyrCountryID[currentPlayer]];
	}

	public static int GetPlayerCountryIndex() { return plyrCountryID[currentPlayer]; }

	public static int[] GetPlayerDetails() {
		int[] details = new int[2];
		details[0] = plyrCountryID[currentPlayer];
		details[1] = -1; // This may be used one day to give the team ID
		return details;
	}

	// Get Player properties
	public static String GetPlayerName() {return plyrName[currentPlayer];}
	public static boolean GetPlayerFullscreen() {return plyrFullscreen;}

	public static void SetPlayerFullscreen(boolean value) {
		plyrFullscreen = value;
		SavePlayers();
	}
	public static int[] GetPlayerControls() {return plyrControls.get(currentPlayer);}
	public static String GetPlayerESCString() {return Input.Keys.toString(GameVars.plyrControls.get(GameVars.currentPlayer)[8]);}
	public static int GetPlayerDisplay(int index) {return plyrDisplay.get(currentPlayer)[index];}
	public static boolean[] GetPlayerDiamonds() {return plyrColDmnd.get(currentPlayer);}
	public static boolean GetPlayerDiamond(int level) {return plyrColDmnd.get(currentPlayer)[level];}
	public static int[] GetPlayerSkipLevel() {return plyrLevelComplete.get(currentPlayer);}
	public static float[] GetPlayerBikeColor() {return plyrBikeColor.get(currentPlayer);}
	public static void SetPlayerBikeColor(float[] rgb) {plyrBikeColor.set(currentPlayer, rgb.clone());}
	public static boolean SetPlayerControls(int index, int value) {
		int[] newControls = plyrControls.get(currentPlayer).clone();
		// Check if the key is not already being used
		for (int ii=0; ii<newControls.length; ii++) {
			if (newControls[ii] == value) return false;
		}
		// Not allowed to use ESC
		if (value == Keys.ESCAPE) return false;
		// Assign the new value
		newControls[index] = value;
		plyrControls.set(currentPlayer, newControls.clone());
		// Finally save the updates
		SavePlayers();
		return true;
	}

	public static boolean SetPlayerDisplay(int index, int value) {
		int[] newDisplay = plyrDisplay.get(currentPlayer).clone();
		newDisplay[index] = value;
		plyrDisplay.set(currentPlayer, newDisplay.clone());
		// Finally save the updates
		SavePlayers();
		return true;
	}

	public static void UpdateTotalTimesAllPlayers(boolean world) {
		// Check if every player's best total time beats any of the world total times
		for (int pp=0; pp<plyrID.length; pp++) {
			// Do the Player total times first
			plyrTotalTimes.set(pp, GetTotalTimesPlayer(pp, false));
			plyrTotalTimesDmnd.set(pp, GetTotalTimesPlayer(pp, true));
		}
		SavePlayers();
	}

	public static int[] GetAllPlayerTimes(int lvl, boolean diamond) {
		int[] allTimes = new int[plyrName.length];
		for (int pp=0; pp< plyrName.length; pp++) {
			if (diamond) allTimes[pp] = plyrTimesDmnd.get(pp).get(lvl);
			else allTimes[pp] = plyrTimes.get(pp).get(lvl);
		}
		return allTimes.clone();
	}

	public static int[] GetAllPlayerTotalTimes() {
		int[] allTimes = new int[plyrName.length];
		for (int pp=0; pp< plyrName.length; pp++) allTimes[pp] = GetTotalTimesPlayer(pp, true) + GetTotalTimesPlayer(pp, false);
		return allTimes.clone();
	}

	public static int[] SortedRanks(final int[] times) {
		// Go through all times and rank them from fastest (0) to slowest. -1 = unfinished
		final Integer[] idx = new Integer[times.length];
		for (int ii=0; ii<times.length; ii++) idx[ii] = ii;
		Arrays.sort(idx, new Comparator<Integer>() {
			@Override public int compare(final Integer o1, final Integer o2) {
				return Integer.compare(times[o1], times[o2]);
			}
		});
		// Initialise and reset ranks
		int ranks[] = new int[SteamVars.NUMWRSHOW];
		for (int ii=0; ii<SteamVars.NUMWRSHOW; ii++) ranks[ii] = -1;
		// Now go through and assign the ranks
		int cntr=0;
		for (int ii=0; ii<times.length; ii++) {
			if ((times[idx[ii]] != -1) & (cntr < SteamVars.NUMWRSHOW)) {
				ranks[cntr] = idx[ii];
				cntr++;
			}
		}
		return ranks.clone();
	}

	public static int GetPlayerRankFromTimes(int[] times, int thisTime) {
		int rank = 1;
		if (thisTime == -1) return -1;
		else {
			for (int tt=0; tt<times.length; tt++) {
				if ((times[tt] != -1) && (times[tt]<thisTime)) rank += 1;
			}
		}
		return rank;
	}

	public static int GetPlayerRank(int lvl, boolean diamond) {
		int thisPlyrTime;
		if (diamond) thisPlyrTime = plyrTimesDmnd.get(currentPlayer).get(lvl);
		else thisPlyrTime = plyrTimes.get(currentPlayer).get(lvl);
		return GetPlayerRankFromTimes(GetAllPlayerTimes(lvl, diamond), thisPlyrTime);
	}

	public static int GetPlayerTimes(int lvl) { return plyrTimes.get(currentPlayer).get(lvl); }

	public static int GetPlayerTimesDmnd(int lvl) {return plyrTimesDmnd.get(currentPlayer).get(lvl);}

	public static int GetTotalTimes() {return GetTotalTimesPlayer(currentPlayer, true)+GetTotalTimesPlayer(currentPlayer, false);}

	public static int GetTotalTimesPlayer(int plyr, boolean diamond) {
		int totalTime = 0;
		int timeVal;
		int numLevels = LevelsListGame.NUMGAMELEVELS;
		for (int ll=0; ll<numLevels; ll++) {
			// First get the relevant time
			if (diamond) timeVal = plyrTimesDmnd.get(plyr).get(ll);
			else timeVal = plyrTimes.get(plyr).get(ll);
			// Check the time is valid
			if (timeVal != -1) totalTime += timeVal;
			else totalTime += MaxTime;
		}
		return totalTime;
	}

	public static int[] GetEmeraldComplete() {
		int nlev = 0;
		for (int i=0; i<LevelsListGame.NUMGAMELEVELS; i++) {
			if (plyrLevelComplete.get(currentPlayer)[i]==1) nlev++;
		}
		int[] emeraldList = new int[nlev];
		int cntr = 0;
		for (int i=0; i<LevelsListGame.NUMGAMELEVELS; i++) {
			if (plyrLevelComplete.get(currentPlayer)[i]==1) {
				emeraldList[cntr] = i;
				cntr++;
			};
		}
		return emeraldList;
	}

	public static int[] GetDiamondComplete() {
		int nlev = 0;
		for (int i=0; i<LevelsListGame.NUMGAMELEVELS; i++) {
			if (IsDiamondCollected(i)) nlev++;
		}
		int[] diamondList = new int[nlev];
		int cntr = 0;
		for (int i=0; i<LevelsListGame.NUMGAMELEVELS; i++) {
			if (IsDiamondCollected(i)) {
				diamondList[cntr] = i;
				cntr++;
			};
		}
		return diamondList;
	}

    public static String getTimeString(int time) {
    	String retval = "--:--:---";
    	if (time > 0) {
        	// time is in milliseconds
            int MSecs = time%1000;
        	int Secs  = ((time-MSecs)%60000)/1000;
        	int Mins  = ((time-MSecs-1000*Secs)%3600000)/60000;
			int Hour  = (time-MSecs-1000*Secs-60000*Mins)/3600000;
			if (Hour > 0) {
				retval = String.format("%d:%02d:%02d.%03d", Hour, Mins, Secs, MSecs);
			} else {// if (Mins > 0) {
				retval = String.format("%02d:%02d.%03d", Mins, Secs, MSecs);
			}/* else {
				retval = String.format("%02d.%03d", Secs, MSecs);
			}
			*/
    	}
		return retval;
    }

	// Check the player and world record times
	public static void CheckTimes(int lvl, boolean diamond, int timerTotal) {
		int timePrev;
		if (diamond) {
			timePrev = plyrTimesDmnd.get(currentPlayer).get(lvl);
			if ((timerTotal < timePrev) | (timePrev==-1)) {
				ArrayList<Integer> copyTimes = (ArrayList<Integer>) plyrTimesDmnd.get(currentPlayer).clone();
				copyTimes.set(lvl, timerTotal);
				plyrTimesDmnd.set(currentPlayer, copyTimes);
				GameVars.StoreReplay(lvl, diamond);
				GameVars.SetPersonalBest(true);
				SavePlayers();
			}
		} else {
			timePrev = plyrTimes.get(currentPlayer).get(lvl);
			if ((timerTotal < timePrev) | (timePrev==-1)) {
				ArrayList<Integer> copyTimes = (ArrayList<Integer>) plyrTimes.get(currentPlayer).clone();
				copyTimes.set(lvl, timerTotal);
				plyrTimes.set(currentPlayer, copyTimes);
				GameVars.StoreReplay(lvl, diamond);
				GameVars.SetPersonalBest(true);
				SavePlayers();
			}
		}
	}

	// Is options
	public static boolean IsDiamondCollected(int lvl) {return plyrColDmnd.get(currentPlayer)[lvl];}
	public static boolean IsSkipLevel(int lvl) {
		if (plyrLevelComplete.get(currentPlayer)[lvl] == 2) return true;
		else return false;
	}
	
	// Set options
	public static void SetDiamond(int lvl) {
		boolean[] copyDmnd = plyrColDmnd.get(currentPlayer).clone();
		copyDmnd[lvl] = true;
		plyrColDmnd.set(currentPlayer, copyDmnd);
		SavePlayers();
	}

	public static void SetSkipLevel(int lvl) {
		if (CanSkip() && (lvl < LevelsListGame.NUMGAMELEVELS-1)) {
			int[] copyLevComp = plyrLevelComplete.get(currentPlayer).clone();
			copyLevComp[lvl] = 2;
			plyrLevelComplete.set(currentPlayer, copyLevComp);
			SavePlayers();
		}
	}

	public static void SetLevelComplete(int lvl) {
		int[] copyLevComp = plyrLevelComplete.get(currentPlayer);
		copyLevComp[lvl] = 1;
		plyrLevelComplete.set(currentPlayer, copyLevComp);
		SavePlayers();
	}

	public static Object deepClone(Object object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Replay GetReplay(int lvl, boolean diamond) {
		ReplayVars.ResetReplayCounter();
		if (diamond) {
			return (Replay) deepClone(plyrReplaysDmnd.get(currentPlayer)[lvl]);
		} else {
			return (Replay) deepClone(plyrReplays.get(currentPlayer)[lvl]);
		}
	}

	public static void StoreReplay(int lvl, boolean diamond) {
		Replay[] allReplays;
		if (diamond) {
			allReplays = plyrReplaysDmnd.get(currentPlayer).clone();
			allReplays[lvl] = ReplayVars.CopyOfCurrentReplay();
			plyrReplaysDmnd.set(currentPlayer, allReplays);
		} else {
			allReplays = plyrReplays.get(currentPlayer).clone();
			allReplays[lvl] = ReplayVars.CopyOfCurrentReplay();
			plyrReplays.set(currentPlayer, allReplays);
		}
		SavePlayers();
	}

	// Add Player
	public static void AddPlayer(int steamID, String name) {
		// Add the player Account ID
		int[] oldIDs = plyrID.clone();
		plyrID = new int[1+oldIDs.length];
		for (int i=0;i<oldIDs.length;i++) plyrID[i] = oldIDs[i];
		// make sure the steamID doesn't already exist (only for when Steam is offline)
		for (int ss=0; ss< plyrID.length-1; ss++) {
			if (plyrID[ss] <= -2) steamID -= 1;
		}
		plyrID[oldIDs.length] = steamID;
		// Add the player name
		String[] oldNames = plyrName.clone();
		plyrName = new String[1+oldNames.length];
		for (int i=0;i<oldNames.length;i++) plyrName[i] = oldNames[i];
		plyrName[oldNames.length] = name;
		// Add the player country
		oldIDs = plyrCountryID.clone();
		plyrCountryID = new int[1+plyrCountryID.length];
		for (int i=0;i<oldIDs.length;i++) plyrCountryID[i] = oldIDs[i];
		plyrCountryID[oldIDs.length] = -1;
		// Add the player times
		plyrTimes.add(GetEmptyTimes(LevelsListGame.NUMGAMELEVELS));
		plyrTimesDmnd.add(GetEmptyTimes(LevelsListGame.NUMGAMELEVELS));
		// Add the player controls
		plyrDisplay.add(GetDefaultDisplay());
		// Add the player controls
		plyrControls.add(GetDefaultControls());
		// Add an empty diamonds array
		plyrColDmnd.add(FalseBoolean(LevelsListGame.NUMGAMELEVELS));
		// Add an empty level skip array
		plyrLevelComplete.add(ZeroInt(LevelsListGame.NUMGAMELEVELS));
		// Add a default Bike color
		plyrBikeColor.add(GetDefaultBikeColor());
		// Add the total times
		plyrTotalTimes.add(-1);
		plyrTotalTimesDmnd.add(-1);
		// Add the Replays
		plyrReplays.add(new Replay[LevelsListGame.NUMGAMELEVELS]);
		plyrReplaysDmnd.add(new Replay[LevelsListGame.NUMGAMELEVELS]);
	}

	// Methods
	public static boolean CanSkip() {
		int numSkips=0;
		for (int i=0; i<plyrLevelComplete.get(currentPlayer).length; i++) {
			if (plyrLevelComplete.get(currentPlayer)[i]==2) numSkips += 1;
		}
		if (numSkips<skipsAllowed) return true;
		else return false;
	}

	public static int SkipsLeft() {
		int numSkips=0;
		for (int i=0; i<plyrLevelComplete.get(currentPlayer).length; i++) {
			if (plyrLevelComplete.get(currentPlayer)[i]==2) numSkips += 1;
		}
		return (skipsAllowed-numSkips);
	}

	public static int GetNumLevels() {
		int numLevels=2;
		for (int i=0; i<plyrLevelComplete.get(currentPlayer).length; i++) {
			if (plyrLevelComplete.get(currentPlayer)[i]>0) numLevels += 1;
		}
		// Reduce by one if all levels are complete
		if (numLevels==2+LevelsListGame.NUMGAMELEVELS) numLevels--; 
		return numLevels;
	}

	public static int GetNumLevelsDiamond() {
		int numLevels=2;
		for (int i=0; i<plyrLevelComplete.get(currentPlayer).length; i++) {
			if (plyrLevelComplete.get(currentPlayer)[i]>0) numLevels += 1;
		}
		// Reduce by one if all levels are complete
		if (numLevels==2+LevelsListGame.NUMGAMELEVELS) numLevels--;
		return numLevels;
	}

	public static int GetLevelStatus(int lev) {
		if (lev == -1) return -1;
		return plyrLevelComplete.get(currentPlayer)[lev];
	}
	
	public static float[] GetDefaultBikeColor() {
	    //mBatch.setColor(1, 0, 1, 1); // Magenta
	    //mBatch.setColor(0.1f,0.5f,1.0f,1.0f); // Blue
		//float[] color = new float[]{0.1f,0.5f,1.0f,1.0f};  // Blue
		//float[] color = new float[]{0.1568627450980392f, 0.23529411764705882f, 0.5882352941176471f, 1.0f};  // Midnight blue
		float[] color = new float[]{0.9f,0.9f,0.9f};  // Off white
		return color.clone();
	}

	public static int[] GetDefaultControls() {
		int[] controls = new int[9];
		controls[0] = Keys.UP;
		controls[1] = Keys.DOWN;
		controls[2] = Keys.RIGHT;
		controls[3] = Keys.LEFT;
		controls[4] = Keys.SPACE;
		controls[5] = Keys.B;
		controls[6] = Keys.N;
		controls[7] = Keys.R;
		controls[8] = Keys.Q;
		return controls.clone();
	}

	public static int[] GetDefaultDisplay() {
		int[] controls = new int[6];
		controls[0] = DISPLAY_ON; // Clock
		controls[1] = DISPLAY_ON; // Personal Record
		controls[2] = DISPLAY_ON; // World Record
		controls[3] = DISPLAY_SMARTON; // Emerald Count
		controls[4] = DISPLAY_SMARTON; // Key Count
		controls[5] = DISPLAY_SMARTON; // Nitrous Count
		return controls.clone();
	}

	@SuppressWarnings("unchecked")
	public static void LoadPlayers() {
		try {
			FileInputStream fi = new FileInputStream(new File("ABstate.dat"));
			ObjectInputStream oi = new ObjectInputStream(fi);

			// Read objects
			plyrID = (int[]) oi.readObject();
			plyrName = (String[]) oi.readObject();
			plyrCountryID = (int[]) oi.readObject();
			plyrFullscreen = (Boolean) oi.readObject();
			plyrTimes = (ArrayList<ArrayList<Integer>>) oi.readObject();
			plyrTimesDmnd = (ArrayList<ArrayList<Integer>>) oi.readObject();
			plyrTotalTimes = (ArrayList<Integer>) oi.readObject();
			plyrTotalTimesDmnd = (ArrayList<Integer>) oi.readObject();
			plyrControls = (ArrayList<int[]>) oi.readObject();
			plyrDisplay = (ArrayList<int[]>) oi.readObject();
			plyrColDmnd = (ArrayList<boolean[]>) oi.readObject();
			plyrLevelComplete = (ArrayList<int[]>) oi.readObject();
			plyrBikeColor = (ArrayList<float[]>) oi.readObject();
			plyrReplays = (ArrayList<Replay[]>) oi.readObject();
			plyrReplaysDmnd = (ArrayList<Replay[]>) oi.readObject();
			// Close files
			oi.close();
			fi.close();
		} catch (FileNotFoundException e) {
			System.out.println("Players file not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream for players file");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		GenerateFakeTimes();
	}

	public static void SavePlayers() {
		FileOutputStream f;
		try {
			f = new FileOutputStream(new File("ABstate.dat"));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(plyrID);
			o.writeObject(plyrName);
			o.writeObject(plyrCountryID);
			o.writeObject(plyrFullscreen);
			o.writeObject(plyrTimes);
			o.writeObject(plyrTimesDmnd);
			o.writeObject(plyrTotalTimes);
			o.writeObject(plyrTotalTimesDmnd);
			o.writeObject(plyrControls);
			o.writeObject(plyrDisplay);
			o.writeObject(plyrColDmnd);
			o.writeObject(plyrLevelComplete);
			o.writeObject(plyrBikeColor);
			o.writeObject(plyrReplays);
			o.writeObject(plyrReplaysDmnd);

			// Close the file
			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("Players file not found!");
		} catch (IOException e) {
			System.out.println("IO error");
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Integer> GetEmptyTimes(int numLevels) {
		ArrayList<Integer> times = new ArrayList<Integer>();
		for (int i=0; i<numLevels; i++) times.add(-1);
		return (ArrayList<Integer>) times.clone();
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String[]> GetEmptyNames(int numLevels) {
		ArrayList<String[]> names = new ArrayList<String[]>();
		String[] emptyNames = new String[numStore]; // Store the top 10 times in each level
		for (int i=0; i<numStore; i++) emptyNames[i] = ""; // "" means that a time has not been recorded for this level
		// Add a copy of this array for all levels
		for (int i=0; i<numLevels; i++) names.add(emptyNames.clone());
		return (ArrayList<String[]>) names.clone();
	}

	public static String[] GetEmptyStringArray(int num) {
		String[] emptySA = new String[num]; // Store the top 10 times in each level
		for (int i=0; i<num; i++) emptySA[i] = ""; // "" means that a time has not been recorded
		return emptySA.clone();
	}

	public static int[] GetEmptyIntArray(int num) {
		int[] emptyIA = new int[num]; // Store the top 10 times in each level
		for (int i=0; i<num; i++) emptyIA[i] = -1; // -1 means that a time has not been recorded
		return emptyIA.clone();
	}

	public static boolean[] FalseBoolean(int num) {
		boolean[] empty = new boolean[num];
		for (int i=0; i<num; i++) empty[i]=false;
		return empty.clone();
	}

	public static int[] ZeroInt(int num) {
		int[] empty = new int[num];
		for (int i=0; i<num; i++) empty[i]=0;
		return empty.clone();
	}

	public static int[] ValueInt(int num, int value) {
		int[] empty = new int[num];
		for (int i=0; i<num; i++) empty[i]=value;
		return empty.clone();
	}

	private static void GenerateFakeTimes() {
		AddFakePlayer("Ryan");
		AddFakePlayer("Steve");
		AddFakePlayer("Who");
		AddFakePlayer("Else");
		AddFakePlayer("Wants");
		AddFakePlayer("To");
		AddFakePlayer("Play");
		AddFakePlayer("Yada");
		AddFakePlayer("Dodo");
		AddFakePlayer("Bingo");
		AddFakePlayer("Hello");
		AddFakePlayer("NotSteve");
		AddFakePlayer("WhoAmI");
		AddFakePlayer("RunningOutOfNames");
		SavePlayers();
	}

	private static void AddFakePlayer(String name) {
		int steamID = -1;
		// Add the player Account ID
		int[] oldIDs = plyrID.clone();
		plyrID = new int[1+oldIDs.length];
		for (int i=0;i<oldIDs.length;i++) plyrID[i] = oldIDs[i];
		// make sure the steamID doesn't already exist (only for when Steam is offline)
		for (int ss=0; ss< plyrID.length-1; ss++) {
			if (plyrID[ss] <= -2) steamID -= 1;
		}
		plyrID[oldIDs.length] = steamID;
		// Add the player name
		String[] oldNames = plyrName.clone();
		plyrName = new String[1+oldNames.length];
		for (int i=0;i<oldNames.length;i++) plyrName[i] = oldNames[i];
		plyrName[oldNames.length] = name;
		// Add the player country
		oldIDs = plyrCountryID.clone();
		plyrCountryID = new int[1+plyrCountryID.length];
		for (int i=0;i<oldIDs.length;i++) plyrCountryID[i] = oldIDs[i];
		plyrCountryID[oldIDs.length] = 3+plyrCountryID.length;
		// Add the player times
		plyrTimes.add(RandomTimes(LevelsListGame.NUMGAMELEVELS));
		plyrTimesDmnd.add(RandomTimes(LevelsListGame.NUMGAMELEVELS));
		// Add the player controls
		plyrDisplay.add(GetDefaultDisplay());
		// Add the player controls
		plyrControls.add(GetDefaultControls());
		// Add an empty diamonds array
		plyrColDmnd.add(FalseBoolean(LevelsListGame.NUMGAMELEVELS));
		// Add an empty level skip array
		plyrLevelComplete.add(ValueInt(LevelsListGame.NUMGAMELEVELS, 1));
		// Add a default Bike color
		plyrBikeColor.add(GetDefaultBikeColor());
		// Add the total times
		plyrTotalTimes.add(-1);
		plyrTotalTimesDmnd.add(-1);
		// Add the Replays
		plyrReplays.add(new Replay[LevelsListGame.NUMGAMELEVELS]);
		plyrReplaysDmnd.add(new Replay[LevelsListGame.NUMGAMELEVELS]);
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<Integer> RandomTimes(int numLevels) {
		int min = 10000, max = 11500;
		ArrayList<Integer> times = new ArrayList<Integer>();
		for (int l=0; l<numLevels; l++) {
			times.add(getRandomNumberInRange(min, max));
		}
		return (ArrayList<Integer>) times.clone();
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<String[]> RandomNames(int numLevels) {
		ArrayList<String[]> names = new ArrayList<String[]>();
		String[] emptyNames = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"}; // Generate 10 names
		for (int l=0; l<numLevels; l++) {
			names.add(emptyNames.clone());
		}
		return (ArrayList<String[]>) names.clone();
	}

	private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
