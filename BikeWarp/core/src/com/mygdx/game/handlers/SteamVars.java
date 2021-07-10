/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import com.badlogic.gdx.Game;
import com.codedisaster.steamworks.*;
import com.mygdx.game.BikeGame;

import java.util.ArrayList;

/**
 *
 * @author rcooke
 */
public class SteamVars {
	public static final int NUMWRSHOW = 10;
	private static final String sep = "\n\n\n";
//	private static final String sep = "-------------------------\n";
	private static SteamUserStats userStatsEmerald, userStatsDiamond;
	private static SteamUserStats worldStatsEmerald, worldStatsDiamond;
	public static boolean isOnline=false;
	public static boolean userLoaded=false;
	// variables for total times
	public static boolean loadingTotalTimes = false;
	public static boolean updateCountry = false;
	private static String[] leaderboardNameEmerald;
	private static String[] leaderboardNameDiamond;
	private static final String leaderboardNameTotalTime = "TotalTime";
	public static boolean readyForNextLeaderboard;
	// Variables for current leaderboard
	private static SteamLeaderboardHandle currentLeaderboardEmerald;
	private static SteamLeaderboardHandle currentLeaderboardDiamond;
	private static int statsLoadedEmerald = 0, statsLoadedDiamond = 0;
	public static String currentDisplayString;
	public static ArrayList<String> recordMenuStringRanks, recordMenuStringNames, recordMenuStringTimes;
	public static ArrayList<Integer> recordMenuCountries;
	// Variables for world records
	public static String[] worldRecordNamesEmerald = new String[NUMWRSHOW];
	public static int[] worldRecordTimesEmerald = new int[NUMWRSHOW];
	public static int[] worldRecordCntryEmerald = new int[NUMWRSHOW];
	public static String[] worldRecordNamesDiamond = new String[NUMWRSHOW];
	public static int[] worldRecordTimesDiamond = new int[NUMWRSHOW];
	public static int[] worldRecordCntryDiamond = new int[NUMWRSHOW];
	// Variables for current player
	public static SteamID playerID;
	public static String playerName = "[unknown]";
	private static SteamFriends plyrFriends;
	public static int loadingLevel, currentLevel;
	public static int worldRecordDiamond, worldRecordEmerald;
	public static int playerBestEmerald, playerBestDiamond;
	public static int playerRankEmerald, playerRankDiamond;
	public static int playerTotalTimeEmerald, playerTotalTimeDiamond;

	/*
	****************************************************
	Friend Callbacks
	****************************************************
	*/

	private static SteamFriendsCallback friendsCallback = new SteamFriendsCallback() {
		@Override
		public void onSetPersonaNameResponse(boolean success, boolean localSuccess, SteamResult result) {}

		@Override
		public void onPersonaStateChange(SteamID steamID, SteamFriends.PersonaChange change) {}

		@Override
		public void onGameOverlayActivated(boolean active) {}

		@Override
		public void onGameLobbyJoinRequested(SteamID steamIDLobby, SteamID steamIDFriend) {}

		@Override
		public void onAvatarImageLoaded(SteamID steamID, int image, int width, int height) {}

		@Override
		public void onFriendRichPresenceUpdate(SteamID steamIDFriend, int appID) {}

		@Override
		public void onGameRichPresenceJoinRequested(SteamID steamIDFriend, String connect) {}

		@Override
		public void onGameServerChangeRequested(String server, String password) {}
	};

	/*
	****************************************************
	Emerald Callbacks
	****************************************************
	*/

	private static SteamUserStatsCallback steamUserStatsCallbackEmerald = new SteamUserStatsCallback() {

		@Override
		public void onUserStatsReceived(long gameId, SteamID steamIDUser,
										SteamResult result)
		{
			playerID = steamIDUser;
			playerName = plyrFriends.getPersonaName();
			if (GameVars.GetCurrentPlayer() == -1) {
				GameVars.LoadPlayers();
				GameVars.SetCurrentPlayer(steamIDUser.getAccountID(), playerName, -1);
				// Set the display preference for the user
				BikeGame.UpdateDisplay();
				userLoaded = true;
			}
		}

		@Override
		public void onUserStatsStored(long gameId, SteamResult result) {}

		@Override
		public void onUserStatsUnloaded(SteamID steamIDUser) {}

		@Override
		public void onUserAchievementStored(long gameId,
											boolean isGroupAchievement, String achievementName,
											int curProgress, int maxProgress) {}

		@Override
		public void onLeaderboardFindResult(SteamLeaderboardHandle leaderboard,
											boolean found) {
			if (found) {
				if (loadingTotalTimes || updateCountry) {
					playerRankEmerald = -1;
					userStatsEmerald.downloadLeaderboardEntriesForUsers(leaderboard, new SteamID[] {playerID});
				} else {
					currentLeaderboardEmerald = leaderboard;
					// Download user's score
					userStatsEmerald.downloadLeaderboardEntriesForUsers(currentLeaderboardEmerald, new SteamID[] {playerID});
					// Download top 10
					worldStatsEmerald.downloadLeaderboardEntries(currentLeaderboardEmerald,
							SteamUserStats.LeaderboardDataRequest.Global, 0, 10);
				}
			} else {
				System.out.println("Leaderboard not found: " + userStatsEmerald.getLeaderboardName(leaderboard));
			}
		}

		@Override
		public void onLeaderboardScoresDownloaded(
				SteamLeaderboardHandle leaderboard,
				SteamLeaderboardEntriesHandle entries, int numEntries) {

			SteamLeaderboardEntry entry;
			int[] details;
			for (int i = 0; i < numEntries; i++) {
				entry = new SteamLeaderboardEntry();
				details = new int[1];
				if (userStatsEmerald.getDownloadedLeaderboardEntry(entries, i, entry, details)) {
					if (entry.getSteamIDUser().getAccountID() == playerID.getAccountID()) {
						playerBestEmerald = entry.getScore();
						playerRankEmerald = entry.getGlobalRank();
						if (loadingTotalTimes) {
							playerTotalTimeEmerald += entry.getScore();
							loadingLevel += 1;
							readyForNextLeaderboard = true;
						} else if (updateCountry) {
							details = GameVars.GetPlayerDetails();
							userStatsEmerald.uploadLeaderboardScore(leaderboard, SteamUserStats.LeaderboardUploadScoreMethod.ForceUpdate, entry.getScore(), details);
							loadingLevel += 1;
							readyForNextLeaderboard = true;
						}
					}
				}
			}
			// Check if user has not completed level
			if ((loadingTotalTimes|updateCountry) & (playerRankEmerald == -1)) {
				if (loadingTotalTimes) playerTotalTimeEmerald += GameVars.MaxTime;
				loadingLevel += 1;
				readyForNextLeaderboard = true;
			}
			statsLoadedEmerald++;
		}

		@Override
		public void onLeaderboardScoreUploaded(boolean success,
											   SteamLeaderboardHandle leaderboard, int score,
											   boolean scoreChanged, int globalRankNew, int globalRankPrevious) {
			if (scoreChanged && !updateCountry && (currentLevel>=1)) {
				GameVars.CheckTimes(currentLevel-1, false, score);
				if (globalRankNew == 1) GameVars.SetWorldRecord(true);
				else GameVars.SetPersonalBest(true);
			}
		}

		@Override
		public void onGlobalStatsReceived(long gameId, SteamResult result) {}
	};

	private static SteamUserStatsCallback steamWorldStatsCallbackEmerald = new SteamUserStatsCallback() {

		@Override
		public void onUserStatsReceived(long gameId, SteamID steamIDUser,
										SteamResult result) {}

		@Override
		public void onUserStatsStored(long gameId, SteamResult result) {}

		@Override
		public void onUserStatsUnloaded(SteamID steamIDUser) {}

		@Override
		public void onUserAchievementStored(long gameId,
											boolean isGroupAchievement, String achievementName,
											int curProgress, int maxProgress) {}

		@Override
		public void onLeaderboardFindResult(SteamLeaderboardHandle leaderboard,
											boolean found) {}

		@Override
		public void onLeaderboardScoresDownloaded(
				SteamLeaderboardHandle leaderboard,
				SteamLeaderboardEntriesHandle entries, int numEntries) {

			SteamLeaderboardEntry entry;
			int[] details;
			for (int i = 0; i < numEntries; i++) {
				entry = new SteamLeaderboardEntry();
				details = new int[1];
				if (worldStatsEmerald.getDownloadedLeaderboardEntry(entries, i, entry, details)) {
					if (entry.getGlobalRank() < NUMWRSHOW) {
						worldRecordNamesEmerald[entry.getGlobalRank()-1] = plyrFriends.getFriendPersonaName(entry.getSteamIDUser());
						worldRecordTimesEmerald[entry.getGlobalRank()-1] = entry.getScore();
						worldRecordCntryEmerald[entry.getGlobalRank()-1] = details[0];
					}
				}
			}
			statsLoadedEmerald++;
		}

		@Override
		public void onLeaderboardScoreUploaded(boolean success,
											   SteamLeaderboardHandle leaderboard, int score,
											   boolean scoreChanged, int globalRankNew, int globalRankPrevious) {}

		@Override
		public void onGlobalStatsReceived(long gameId, SteamResult result) {}
	};

	/*
	****************************************************
	Diamond Callbacks
	****************************************************
	*/

	private static SteamUserStatsCallback steamUserStatsCallbackDiamond = new SteamUserStatsCallback() {

		@Override
		public void onUserStatsReceived(long gameId, SteamID steamIDUser,
										SteamResult result) {}

		@Override
		public void onUserStatsStored(long gameId, SteamResult result) {}

		@Override
		public void onUserStatsUnloaded(SteamID steamIDUser) {}

		@Override
		public void onUserAchievementStored(long gameId,
											boolean isGroupAchievement, String achievementName,
											int curProgress, int maxProgress) {}

		@Override
		public void onLeaderboardFindResult(SteamLeaderboardHandle leaderboard,
											boolean found) {
			if (found) {
				if (loadingTotalTimes || updateCountry) {
					playerRankDiamond = -1;
					userStatsDiamond.downloadLeaderboardEntriesForUsers(leaderboard, new SteamID[] {playerID});
				} else {
					currentLeaderboardDiamond = leaderboard;
					// Download user's score
					userStatsDiamond.downloadLeaderboardEntriesForUsers(currentLeaderboardDiamond, new SteamID[] {playerID});
					// Download top 10
					worldStatsDiamond.downloadLeaderboardEntries(currentLeaderboardDiamond,
							SteamUserStats.LeaderboardDataRequest.Global, 0, 10);
				}
			} else {
				System.out.println("Leaderboard not found: " + userStatsDiamond.getLeaderboardName(leaderboard));
			}
		}

		@Override
		public void onLeaderboardScoresDownloaded(
				SteamLeaderboardHandle leaderboard,
				SteamLeaderboardEntriesHandle entries, int numEntries) {

			SteamLeaderboardEntry entry;
			int[] details;
			for (int i = 0; i < numEntries; i++) {
				entry = new SteamLeaderboardEntry();
				details = new int[1];
				if (userStatsDiamond.getDownloadedLeaderboardEntry(entries, i, entry, details)) {
					if (entry.getSteamIDUser().getAccountID() == playerID.getAccountID()) {
						playerBestDiamond = entry.getScore();
						playerRankDiamond = entry.getGlobalRank();
						if (loadingTotalTimes) {
							playerTotalTimeDiamond += entry.getScore();
							loadingLevel += 1;
							readyForNextLeaderboard = true;
						} else if (updateCountry) {
							details = GameVars.GetPlayerDetails();
							userStatsDiamond.uploadLeaderboardScore(leaderboard, SteamUserStats.LeaderboardUploadScoreMethod.ForceUpdate, entry.getScore(), details);
							loadingLevel += 1;
							readyForNextLeaderboard = true;
						}
					}
				}
			}
			// Check if user has not completed level
			if ((loadingTotalTimes||updateCountry) & (playerRankDiamond == -1)) {
				if (loadingTotalTimes) playerTotalTimeDiamond += GameVars.MaxTime;
				loadingLevel += 1;
				readyForNextLeaderboard = true;
			}
			statsLoadedDiamond++;
		}

		@Override
		public void onLeaderboardScoreUploaded(boolean success,
											   SteamLeaderboardHandle leaderboard, int score,
											   boolean scoreChanged, int globalRankNew, int globalRankPrevious) {
			if (scoreChanged && !updateCountry && (currentLevel>=1)) {
				GameVars.CheckTimes(currentLevel-1, true, score);
				if (globalRankNew == 1) GameVars.SetWorldRecord(true);
				else GameVars.SetPersonalBest(true);
			}
		}

		@Override
		public void onGlobalStatsReceived(long gameId, SteamResult result) {}
	};

	private static SteamUserStatsCallback steamWorldStatsCallbackDiamond = new SteamUserStatsCallback() {

		@Override
		public void onUserStatsReceived(long gameId, SteamID steamIDUser,
										SteamResult result) {}

		@Override
		public void onUserStatsStored(long gameId, SteamResult result) {}

		@Override
		public void onUserStatsUnloaded(SteamID steamIDUser) {}

		@Override
		public void onUserAchievementStored(long gameId,
											boolean isGroupAchievement, String achievementName,
											int curProgress, int maxProgress) {}

		@Override
		public void onLeaderboardFindResult(SteamLeaderboardHandle leaderboard,
											boolean found) {}

		@Override
		public void onLeaderboardScoresDownloaded(
				SteamLeaderboardHandle leaderboard,
				SteamLeaderboardEntriesHandle entries, int numEntries) {

			SteamLeaderboardEntry entry;
			int[] details;
			for (int i = 0; i < numEntries; i++) {
				entry = new SteamLeaderboardEntry();
				details = new int[1];
				if (worldStatsDiamond.getDownloadedLeaderboardEntry(entries, i, entry, details)) {
					if (entry.getGlobalRank() < NUMWRSHOW) {
						worldRecordNamesDiamond[entry.getGlobalRank()-1] = plyrFriends.getFriendPersonaName(entry.getSteamIDUser());
						worldRecordTimesDiamond[entry.getGlobalRank()-1] = entry.getScore();
						worldRecordCntryDiamond[entry.getGlobalRank()-1] = details[0];
					}
				}
			}
			statsLoadedDiamond++;
		}

		@Override
		public void onLeaderboardScoreUploaded(boolean success,
											   SteamLeaderboardHandle leaderboard, int score,
											   boolean scoreChanged, int globalRankNew, int globalRankPrevious) {}

		@Override
		public void onGlobalStatsReceived(long gameId, SteamResult result) {}
	};

	/*
	****************************************************
	Initialise Steam Connection
	****************************************************
	*/

	public static boolean initAndConnect(){
		try {
			SteamAPI.loadLibraries();
			if (!SteamAPI.init()) {
				System.out.println("Steamworks initialization error, e.g. Steam client not running");
				isOnline = false;
				return false;
			}
		} catch (SteamException e) {
			System.out.println("Error extracting or loading native libraries");
		}
		//Get stat object
		userLoaded = false;
		userStatsEmerald = new SteamUserStats(steamUserStatsCallbackEmerald);
		userStatsDiamond = new SteamUserStats(steamUserStatsCallbackDiamond);
		worldStatsEmerald = new SteamUserStats(steamWorldStatsCallbackEmerald);
		worldStatsDiamond = new SteamUserStats(steamWorldStatsCallbackDiamond);
		//A must before setting achievements
		userStatsEmerald.requestCurrentStats();
		userStatsDiamond.requestCurrentStats();
		worldStatsEmerald.requestCurrentStats();
		worldStatsDiamond.requestCurrentStats();
		// Setup the friends in order to get the names
		plyrFriends = new SteamFriends(friendsCallback);
		currentDisplayString = "";
		recordMenuStringNames = new ArrayList<String>();
		recordMenuStringRanks = new ArrayList<String>();
		recordMenuStringTimes = new ArrayList<String>();
		recordMenuCountries   = new ArrayList<Integer>();
		isOnline=true;
		// Initialise the leaderboard handles
		return true;
	};

	/*
	****************************************************
	The next routines are for the current leaderboard
	****************************************************
	*/

	private static void ResetTimes() {
		currentDisplayString = "Loading Records";
		recordMenuStringNames = new ArrayList<String>();
		recordMenuStringRanks = new ArrayList<String>();
		recordMenuStringTimes = new ArrayList<String>();
		recordMenuCountries   = new ArrayList<Integer>();
		playerBestEmerald = -1;
		playerBestDiamond = -1;
		playerRankEmerald = -1;
		playerRankDiamond = -1;
		statsLoadedEmerald = 0;
		statsLoadedDiamond = 0;
		for (int ii=0; ii<NUMWRSHOW; ii++) {
			worldRecordTimesEmerald[ii] = -1;
			worldRecordNamesEmerald[ii] = "";
			worldRecordCntryEmerald[ii] = -1;
			worldRecordTimesDiamond[ii] = -1;
			worldRecordNamesDiamond[ii] = "";
			worldRecordCntryDiamond[ii] = -1;
		}
	}

	public static void LoadPBWRtotal() {
		currentLeaderboardEmerald = null;
		currentLevel = -1;
		ResetTimes();
		if (SteamAPI.isSteamRunning()) {
			userStatsEmerald.findLeaderboard("TotalTime");
		} else {
			OfflineVariables(true);
		}
	}

	public static void UpdatePlayerCountry() {
		// Must call PrepareAllLeaderboards() before calling this routine;
		if (readyForNextLeaderboard) {
			if (loadingLevel < LevelsListGame.NUMGAMELEVELS) {
				userStatsEmerald.findLeaderboard(leaderboardNameEmerald[loadingLevel]);
			} else if (loadingLevel < 2*LevelsListGame.NUMGAMELEVELS) {
				userStatsDiamond.findLeaderboard(leaderboardNameDiamond[loadingLevel-LevelsListGame.NUMGAMELEVELS]);
			} else if (loadingLevel == 2*LevelsListGame.NUMGAMELEVELS) {
				userStatsEmerald.findLeaderboard(leaderboardNameTotalTime);
			} else {
				updateCountry = false;
			}
			readyForNextLeaderboard = false;
		}
	}

	public static void LoadPBWR(int level) {
		currentLevel = level;
		ResetTimes();
		if (SteamAPI.isSteamRunning()) {
			userStatsEmerald.findLeaderboard(String.format("Level%02d_Emerald", level));
			userStatsDiamond.findLeaderboard(String.format("Level%02d_Diamond", level));
		} else {
			OfflineVariables(false);
		}
	}

	/*
	****************************************************
	The next routines are for the total times
	****************************************************
	*/

	public static float GetProgress() {
		// Return the progress on loading the leaderboards
		if (updateCountry) return ((float) loadingLevel)/((float) 2*LevelsListGame.NUMGAMELEVELS);
		else return ((float) loadingLevel)/((float) 2*LevelsListGame.NUMGAMELEVELS-1);
	}

	public static void DownloadAllLeaderboards() {
		// Must call prepareLeaderboards before calling this function
		if (readyForNextLeaderboard) {
			if (loadingLevel < LevelsListGame.NUMGAMELEVELS) {
				userStatsEmerald.findLeaderboard(leaderboardNameEmerald[loadingLevel]);
			} else if (loadingLevel < 2*LevelsListGame.NUMGAMELEVELS) {
				userStatsDiamond.findLeaderboard(leaderboardNameDiamond[loadingLevel-LevelsListGame.NUMGAMELEVELS]);
			} else {
				loadingTotalTimes = false;
			}
			readyForNextLeaderboard = false;
		}
	}

	public static void PrepareAllLeaderboards(int type) {
		// Generate a list of all available leaderboards for the total time
		loadingTotalTimes = false;
		updateCountry = false;
		if (type == 0) loadingTotalTimes = true;
		else if (type == 1) updateCountry = true;
		playerTotalTimeEmerald = 0;
		playerTotalTimeDiamond = 0;
		// The name of each leaderboard
		leaderboardNameEmerald = new String[LevelsListGame.NUMGAMELEVELS];
		leaderboardNameDiamond = new String[LevelsListGame.NUMGAMELEVELS];
		// TODO :: Need to include the total (emerald+diamond) leaderboard handle here too
		for (int ll=0; ll<LevelsListGame.NUMGAMELEVELS; ll++) {
			leaderboardNameEmerald[ll] = String.format("Level%02d_Emerald", ll+1);
			leaderboardNameDiamond[ll] = String.format("Level%02d_Diamond", ll+1);
		}
		// Initialise the variables - get ready for connecting to Steam leaderboards
		readyForNextLeaderboard = true;
		loadingLevel = 0;
	}

	public static boolean uploadTime(int millis, boolean diamond) {
		boolean result = false;
		int[] details = GameVars.GetPlayerDetails();
		try {
			if (diamond) {
				if (currentLeaderboardDiamond != null) {
					userStatsDiamond.uploadLeaderboardScore(currentLeaderboardDiamond, SteamUserStats.LeaderboardUploadScoreMethod.KeepBest, millis, details);
				}
//				//save
//				result = userStatsDiamond.storeStats();
			} else {
				if (currentLeaderboardEmerald != null) {
					userStatsEmerald.uploadLeaderboardScore(currentLeaderboardEmerald, SteamUserStats.LeaderboardUploadScoreMethod.KeepBest, millis, details);
				}
//				//save
//				result = userStatsEmerald.storeStats();
			}
		} catch (Exception e) {
			isOnline = false;
			result = false;
		}
		return result;
	}

	public static boolean uploadTotalTime() {
		// This should only be called after the
		// LoadPBWRtotal() routine has been called
		// and the total time leaderboard has been
		// loaded.
		boolean result = false;
		int millis;
		int[] details = GameVars.GetPlayerDetails();
		try {
			if ((currentLeaderboardEmerald != null) & (playerTotalTimeEmerald!=0) & (playerTotalTimeDiamond!=0)) {
				millis = playerTotalTimeEmerald + playerTotalTimeDiamond;
				userStatsEmerald.uploadLeaderboardScore(currentLeaderboardEmerald, SteamUserStats.LeaderboardUploadScoreMethod.KeepBest, millis, details);
				result = true;
			}
		} catch (Exception e) {
			isOnline = false;
			result = false;
		}
		return result;
	}

	public static void RecordString() {
		if (((statsLoadedEmerald==2) & (statsLoadedDiamond==2)) || (!SteamAPI.isSteamRunning())) {
			String pretxt, pb, pbd, rnk, rnkd;
			pretxt = "";
			pb = "";
			pbd = "";
			if (GameVars.GetLevelStatus(currentLevel-1)==0) {
				pretxt = "\nLevel not yet complete!\n";
				if (currentLevel == LevelsListGame.NUMGAMELEVELS) pretxt += "\n";
				else if (GameVars.CanSkip()) pretxt += "Press 's' to skip this level\n";
				else pretxt += "No skips left!\n";
			} else if (GameVars.GetLevelStatus(currentLevel-1)==2) {
				pretxt = "Level skipped!\n";
			} else {
				if (SteamAPI.isSteamRunning()) {
					if (playerRankEmerald == -1) rnk = "Unranked";
					else rnk = String.format("World Ranking: %d", playerRankEmerald);
					if (playerRankDiamond == -1) rnkd = "Unranked";
					else rnkd = String.format("World Ranking: %d", playerRankDiamond);
					pb  = String.format("Emerald PB\n%s\n%s\n\n", GameVars.getTimeString(playerBestEmerald), rnk);
					pbd = String.format("Diamond PB\n%s\n%s\n\n", GameVars.getTimeString(playerBestDiamond), rnkd);
				} else {
					rnk = "Unranked";
					rnkd = "Unranked";
					pb  = String.format("Emerald PB\n%s\n%s\n\n", GameVars.getTimeString(GameVars.GetPlayerTimes(currentLevel-1)), rnk);
					pbd = String.format("Diamond PB\n%s\n%s\n\n", GameVars.getTimeString(GameVars.GetPlayerTimesDmnd(currentLevel-1)), rnkd);
				}
			}
			String wr, wrd;
			if (SteamAPI.isSteamRunning()) {
				if (worldRecordTimesEmerald[0] == -1) wr  = "No Emerald WR\n";
				else wr  = String.format("Emerald WR\n%s  -  %s\n", worldRecordNamesEmerald[0], GameVars.getTimeString(worldRecordTimesEmerald[0]));
				if (worldRecordTimesDiamond[0] == -1) wrd  = "No Diamond WR\n";
				else wrd = String.format("Diamond WR\n%s  -  %s\n", worldRecordNamesDiamond[0], GameVars.getTimeString(worldRecordTimesDiamond[0]));
			} else {
				wr  = "Emerald WR\nSteam Offline\n";
				wrd = "Diamond WR\nSteam Offline\n";
			}
			// Put it all together for the current string to display
			currentDisplayString = pretxt+sep+pb+wr+sep+pbd+wrd+sep;
		} else if (statsLoadedEmerald > 2) {
			statsLoadedEmerald = 2;
		} else if (statsLoadedDiamond > 2) {
			statsLoadedDiamond = 2;
		} else {
			if (SteamAPI.isSteamRunning()) currentDisplayString = "Loading Records";
			else currentDisplayString = "Steam Offline";
		}
	}

	public static void OfflineVariables(boolean total) {
		// This routine fills in all of the records arrays with the info from ABstate.dat
		int[] timesD, timesE, ranksD, ranksE;
		playerName = GameVars.GetPlayerName();
		if (total) {
			timesE = GameVars.GetAllPlayerTotalTimes();
			ranksE = GameVars.SortedRanks(timesE.clone());
			for (int rr=0; rr<NUMWRSHOW; rr++) {
				if (ranksE[rr] != -1) {
					worldRecordNamesEmerald[rr] = GameVars.plyrName[ranksE[rr]];
					worldRecordTimesEmerald[rr] = timesE[ranksE[rr]];
					worldRecordCntryEmerald[rr] = GameVars.plyrCountryID[ranksE[rr]];
				}
			}
			playerBestEmerald = GameVars.GetTotalTimes();
			playerRankEmerald = GameVars.GetPlayerRankFromTimes(timesE, playerBestEmerald);
			statsLoadedEmerald = 2;
		} else {
			// Fill in diamond arrays
			timesD = GameVars.GetAllPlayerTimes(currentLevel-1, true);
			ranksD = GameVars.SortedRanks(timesD.clone());
			for (int rr=0; rr<NUMWRSHOW; rr++) {
				if (ranksD[rr] != -1) {
					worldRecordNamesDiamond[rr] = GameVars.plyrName[ranksD[rr]];
					worldRecordTimesDiamond[rr] = timesD[ranksD[rr]];
					worldRecordCntryDiamond[rr] = GameVars.plyrCountryID[ranksD[rr]];
				}
			}
			playerBestDiamond = GameVars.GetPlayerTimesDmnd(currentLevel-1);
			playerRankDiamond = GameVars.GetPlayerRank(currentLevel-1, true);
			statsLoadedDiamond = 2;
			// Fill in emerald arrays
			timesE = GameVars.GetAllPlayerTimes(currentLevel-1, false);
			ranksE = GameVars.SortedRanks(timesE.clone());
			for (int rr=0; rr<NUMWRSHOW; rr++) {
				if (ranksE[rr] != -1) {
					worldRecordNamesEmerald[rr] = GameVars.plyrName[ranksE[rr]];
					worldRecordTimesEmerald[rr] = timesE[ranksE[rr]];
					worldRecordCntryEmerald[rr] = GameVars.plyrCountryID[ranksE[rr]];
				}
			}
			playerBestEmerald = GameVars.GetPlayerTimes(currentLevel-1);
			playerRankEmerald = GameVars.GetPlayerRank(currentLevel-1, false);
			statsLoadedEmerald = 2;
		}
	}

	public static void RecordStringMenu(boolean diamond) {
		// If Steam is offline, you must call OfflineVariables() first!!
//		if (!SteamAPI.isSteamRunning())
//			// Reset
//			recordMenuStringNames = new ArrayList<String>();
//			recordMenuStringRanks = new ArrayList<String>();
//			recordMenuStringTimes = new ArrayList<String>();
//			recordMenuCountries   = new ArrayList<Integer>();
//			// Start with some dummy variables
//			recordMenuStringNames.add("Steam offline");
//			recordMenuStringRanks.add("");
//			recordMenuStringTimes.add("");
//			recordMenuCountries.add(-1);
//			// Now put the player details in
//			playerName = GameVars.GetPlayerName();
//			if (currentLevel == -1) {
//				// Total time
//				playerBestEmerald = GameVars.GetTotalTimes(false)+GameVars.GetTotalTimes(true);
//				playerBestDiamond = -1; // Not used for total times
//			} else {
//				playerBestEmerald = GameVars.GetPlayerTimes(currentLevel-1);
//				playerBestDiamond = GameVars.GetPlayerTimesDmnd(currentLevel-1);
//			}
//			// Add in the player info
//			recordMenuStringRanks.add("");
//			recordMenuStringNames.add(String.format("%s", playerName));
//			recordMenuCountries.add(GameVars.GetPlayerCountryIndex());
//			if (diamond) recordMenuStringTimes.add(String.format("%s", GameVars.getTimeString(playerBestDiamond)));
//			else recordMenuStringTimes.add(String.format("%s", GameVars.getTimeString(playerBestEmerald)));
//			return;
//		}
		if (diamond) {
			if (statsLoadedDiamond==2) {
				recordMenuStringNames = new ArrayList<String>();
				recordMenuStringRanks = new ArrayList<String>();
				recordMenuStringTimes = new ArrayList<String>();
				recordMenuCountries   = new ArrayList<Integer>();
				for (int ww=0; ww < worldRecordNamesDiamond.length; ww++) {
					if (worldRecordCntryDiamond[ww]==-1) {
						// No time available for this entry.
						recordMenuStringRanks.add("");
						recordMenuStringNames.add("");
						recordMenuStringTimes.add("");
						recordMenuCountries.add(-1);
					} else {
						recordMenuStringRanks.add(String.format("%d", ww+1));
						recordMenuStringNames.add(String.format("%s", worldRecordNamesDiamond[ww]));
						recordMenuCountries.add(worldRecordCntryDiamond[ww]);
						if (worldRecordNamesDiamond[ww].compareTo("")==0) recordMenuStringTimes.add("");
						else recordMenuStringTimes.add(String.format("%s", GameVars.getTimeString(worldRecordTimesDiamond[ww])));
					}
				}
				if (playerRankDiamond == 11) {
					recordMenuStringRanks.add(String.format("%s", playerRankDiamond));
					recordMenuStringNames.add(String.format("%s", playerName));
					recordMenuStringTimes.add(String.format("%s", GameVars.getTimeString(playerBestDiamond)));
					recordMenuCountries.add(GameVars.GetPlayerCountryIndex());
				} else if (playerRankDiamond > 11) {
					recordMenuStringRanks.add("");
					recordMenuStringNames.add("");
					recordMenuStringTimes.add("");
					recordMenuCountries.add(-1);
					recordMenuStringRanks.add(String.format("%s", playerRankDiamond));
					recordMenuStringNames.add(String.format("%s", playerName));
					recordMenuStringTimes.add(String.format("%s", GameVars.getTimeString(playerBestDiamond)));
					recordMenuCountries.add(GameVars.GetPlayerCountryIndex());
				} else if (playerRankDiamond == -1) {
					recordMenuStringRanks.add("");
					recordMenuStringNames.add("");
					recordMenuStringTimes.add("");
					recordMenuCountries.add(-1);
					recordMenuStringRanks.add("");
					recordMenuStringNames.add(String.format("%s  (unranked)", playerName));
					recordMenuStringTimes.add(String.format("%s", GameVars.getTimeString(-1)));
					recordMenuCountries.add(GameVars.GetPlayerCountryIndex());
				}
			} else if (statsLoadedDiamond>2) {
				statsLoadedDiamond = 2;
			} else {
				recordMenuStringNames = new ArrayList<String>();
				recordMenuStringRanks = new ArrayList<String>();
				recordMenuStringTimes = new ArrayList<String>();
				recordMenuCountries   = new ArrayList<Integer>();
				recordMenuStringNames.add("Loading Records");
				recordMenuStringRanks.add("");
				recordMenuStringTimes.add("");
				recordMenuCountries.add(-1);
				return;
			}
		} else {
			if (statsLoadedEmerald==2) {
				recordMenuStringNames = new ArrayList<String>();
				recordMenuStringRanks = new ArrayList<String>();
				recordMenuStringTimes = new ArrayList<String>();
				recordMenuCountries   = new ArrayList<Integer>();
				for (int ww=0; ww < worldRecordNamesEmerald.length; ww++) {
					if (worldRecordCntryEmerald[ww]==-1) {
						// No time available for this entry.
						recordMenuStringRanks.add("");
						recordMenuStringNames.add("");
						recordMenuStringTimes.add("");
						recordMenuCountries.add(-1);
					} else {
						recordMenuStringRanks.add(String.format("%d", ww+1));
						recordMenuStringNames.add(String.format("%s", worldRecordNamesEmerald[ww]));
						recordMenuCountries.add(worldRecordCntryEmerald[ww]);
						if (worldRecordNamesEmerald[ww].compareTo("")==0) recordMenuStringTimes.add("");
						else recordMenuStringTimes.add(String.format("%s", GameVars.getTimeString(worldRecordTimesEmerald[ww])));
					}
				}
				if (playerRankEmerald == 11) {
					recordMenuStringRanks.add(String.format("%s", playerRankEmerald));
					recordMenuStringNames.add(String.format("%s", playerName));
					recordMenuStringTimes.add(String.format("%s", GameVars.getTimeString(playerBestEmerald)));
					recordMenuCountries.add(GameVars.GetPlayerCountryIndex());
				} else if (playerRankEmerald > 11) {
					recordMenuStringRanks.add("");
					recordMenuStringNames.add("");
					recordMenuStringTimes.add("");
					recordMenuCountries.add(-1);
					recordMenuStringRanks.add(String.format("%s", playerRankEmerald));
					recordMenuStringNames.add(String.format("%s", playerName));
					recordMenuStringTimes.add(String.format("%s", GameVars.getTimeString(playerBestEmerald)));
					recordMenuCountries.add(GameVars.GetPlayerCountryIndex());
				} else if (playerRankEmerald == -1) {
					recordMenuStringRanks.add("");
					recordMenuStringNames.add("");
					recordMenuStringTimes.add("");
					recordMenuCountries.add(-1);
					recordMenuStringRanks.add("");
					recordMenuStringNames.add(String.format("%s  (unranked)", playerName));
					recordMenuStringTimes.add(String.format("%s", GameVars.getTimeString(-1)));
					recordMenuCountries.add(GameVars.GetPlayerCountryIndex());
				}
			} else if (statsLoadedEmerald>2) {
				statsLoadedEmerald = 2;
			} else {
				recordMenuStringNames = new ArrayList<String>();
				recordMenuStringRanks = new ArrayList<String>();
				recordMenuStringTimes = new ArrayList<String>();
				recordMenuCountries   = new ArrayList<Integer>();
				recordMenuStringNames.add("Loading Records");
				recordMenuStringRanks.add("");
				recordMenuStringTimes.add("");
				recordMenuCountries.add(-1);
			}
		}
	}

	public void disconnect() {
		SteamAPI.shutdown();
	}
}