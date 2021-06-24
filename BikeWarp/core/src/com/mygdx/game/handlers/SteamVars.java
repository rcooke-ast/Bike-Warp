/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import com.codedisaster.steamworks.*;
import com.mygdx.game.BikeGame;

/**
 *
 * @author rcooke
 */
public class SteamVars {
	private static final int NUMWRSHOW = 10;
	private static SteamUserStats userStatsEmerald, userStatsDiamond;
	private static SteamUserStats worldStatsEmerald, worldStatsDiamond;
	public static boolean isOnline=false;
	// variables for total times
	private static boolean loadingTotalTimes = false;
	private static SteamLeaderboardHandle[] leaderboardEmerald;
	private static SteamLeaderboardHandle[] leaderboardDiamond;
	private static String[] leaderboardNameEmerald;
	private static String[] leaderboardNameDiamond;
	private static int[] leaderboardNumberEmerald;
	private static int[] leaderboardNumberDiamond;
	private static boolean readyForNextLeaderboard;
	// Variables for current leaderboard
	private static SteamLeaderboardHandle currentLeaderboardEmerald;
	private static SteamLeaderboardHandle currentLeaderboardDiamond;
	private static int currentLeaderboardEmeraldNumber;
	private static int currentLeaderboardDiamondNumber;
	private static int statsLoadedEmerald = 0, statsLoadedDiamond = 0;
	public static String currentDisplayString;
	public static String recordMenuStringRanks, recordMenuStringNames, recordMenuStringTimes;
	// Variables for world records
	public static String[] worldRecordNamesEmerald = new String[NUMWRSHOW];
	public static int[] worldRecordTimesEmerald = new int[NUMWRSHOW];
	public static String[] worldRecordNamesDiamond = new String[NUMWRSHOW];
	public static int[] worldRecordTimesDiamond = new int[NUMWRSHOW];
	// Variables for current player
	public static SteamID playerID;
	public static String playerName = "[unknown]";
	private static SteamFriends plyrFriends;
	public static int loadingLevel, currentLevel;
	public static int worldRecordDiamond, worldRecordEmerald;
	public static int playerBestEmerald, playerBestDiamond;
	public static int playerRankEmerald, playerRankDiamond;

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
				GameVars.SetCurrentPlayer(steamIDUser.getAccountID(), playerName);
				// Set the display preference for the user
				BikeGame.UpdateDisplay();
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
				if (loadingTotalTimes) {
					leaderboardEmerald[loadingLevel] = leaderboard;
					leaderboardNumberEmerald[loadingLevel] = userStatsEmerald.getLeaderboardEntryCount(leaderboard);
					loadingLevel += 1;
					readyForNextLeaderboard = true;
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
			for (int i = 0; i < numEntries; i++) {
				entry = new SteamLeaderboardEntry();
				if (userStatsEmerald.getDownloadedLeaderboardEntry(entries, i, entry, null)) {
					if (entry.getSteamIDUser().getAccountID() == playerID.getAccountID()) {
						playerBestEmerald = entry.getScore();
						playerRankEmerald = entry.getGlobalRank();
					}
				}
			}
			statsLoadedEmerald++;
		}

		@Override
		public void onLeaderboardScoreUploaded(boolean success,
											   SteamLeaderboardHandle leaderboard, int score,
											   boolean scoreChanged, int globalRankNew, int globalRankPrevious) {
			if (scoreChanged) {
				GameVars.StoreReplay(currentLevel-1, false);
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
			for (int i = 0; i < numEntries; i++) {
				entry = new SteamLeaderboardEntry();
				if (worldStatsEmerald.getDownloadedLeaderboardEntry(entries, i, entry, null)) {
					if (entry.getGlobalRank() < NUMWRSHOW) {
						worldRecordNamesEmerald[entry.getGlobalRank()-1] = plyrFriends.getFriendPersonaName(entry.getSteamIDUser());
						worldRecordTimesEmerald[entry.getGlobalRank()-1] = entry.getScore();
					}
				}
			}
			currentLeaderboardEmeraldNumber = worldStatsEmerald.getLeaderboardEntryCount(leaderboard);
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
				if (loadingTotalTimes) {
					leaderboardDiamond[loadingLevel] = leaderboard;
					leaderboardNumberDiamond[loadingLevel] = userStatsDiamond.getLeaderboardEntryCount(leaderboard);
					loadingLevel += 1;
					readyForNextLeaderboard = true;
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
			for (int i = 0; i < numEntries; i++) {
				entry = new SteamLeaderboardEntry();
				if (userStatsDiamond.getDownloadedLeaderboardEntry(entries, i, entry, null)) {
					if (entry.getSteamIDUser().getAccountID() == playerID.getAccountID()) {
						playerBestDiamond = entry.getScore();
						playerRankDiamond = entry.getGlobalRank();
					}
				}
			}
			statsLoadedDiamond++;
		}

		@Override
		public void onLeaderboardScoreUploaded(boolean success,
											   SteamLeaderboardHandle leaderboard, int score,
											   boolean scoreChanged, int globalRankNew, int globalRankPrevious) {
			if (scoreChanged) {
				GameVars.StoreReplay(currentLevel-1, true);
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
			for (int i = 0; i < numEntries; i++) {
				entry = new SteamLeaderboardEntry();
				if (worldStatsDiamond.getDownloadedLeaderboardEntry(entries, i, entry, null)) {
					if (entry.getGlobalRank() < NUMWRSHOW) {
						worldRecordNamesDiamond[entry.getGlobalRank()-1] = plyrFriends.getFriendPersonaName(entry.getSteamIDUser());
						worldRecordTimesDiamond[entry.getGlobalRank()-1] = entry.getScore();
					}
				}
			}
			currentLeaderboardDiamondNumber = worldStatsDiamond.getLeaderboardEntryCount(leaderboard);
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
		recordMenuStringNames = "";
		recordMenuStringRanks = "";
		recordMenuStringTimes = "";
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
		recordMenuStringNames = "Loading Records";
		recordMenuStringRanks = "";
		recordMenuStringTimes = "";
		playerBestEmerald = -1;
		playerBestDiamond = -1;
		playerRankEmerald = -1;
		playerRankDiamond = -1;
		statsLoadedEmerald = 0;
		statsLoadedDiamond = 0;
		for (int ii=0; ii<NUMWRSHOW; ii++) {
			worldRecordTimesEmerald[ii] = -1;
			worldRecordNamesEmerald[ii] = "";
			worldRecordTimesDiamond[ii] = -1;
			worldRecordNamesDiamond[ii] = "";
		}
	}

	public static void LoadPBWR(int level) {
		ResetTimes();
		userStatsEmerald.findLeaderboard(String.format("Level%02d_Emerald", level));
		userStatsDiamond.findLeaderboard(String.format("Level%02d_Diamond", level));
		currentLevel = level;
	}

	/*
	****************************************************
	The next routines are for the total times
	****************************************************
	*/

	public static float GetProgress() {
		// Return the progress on loading the leaderboards
		return ((float) loadingLevel)/((float) 2*LevelsListGame.NUMGAMELEVELS+1);
	}

	public static void ParseAllLeaderboards() {
		// Must call prepareLeaderboards before calling this function
		if (readyForNextLeaderboard) {
			if (loadingLevel < LevelsListGame.NUMGAMELEVELS) {
				userStatsEmerald.findLeaderboard(leaderboardNameEmerald[loadingLevel]);
			} else if (loadingLevel < 2*LevelsListGame.NUMGAMELEVELS) {
				userStatsDiamond.findLeaderboard(leaderboardNameDiamond[loadingLevel-LevelsListGame.NUMGAMELEVELS]);
			} else {
				System.out.println("All leaderboards loaded");
				loadingTotalTimes = false;
			}
			readyForNextLeaderboard = false;
		}
	}

	public static void PrepareAllLeaderboards() {
		// Generate a list of all available leaderboards for the total time
		loadingTotalTimes = true;
		// A handle to each of the leaderboards
		leaderboardEmerald = new SteamLeaderboardHandle[LevelsListGame.NUMGAMELEVELS];
		leaderboardDiamond = new SteamLeaderboardHandle[LevelsListGame.NUMGAMELEVELS];
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
		try {
			if (diamond) {
				if (currentLeaderboardDiamond != null) {
					userStatsDiamond.uploadLeaderboardScore(currentLeaderboardDiamond, SteamUserStats.LeaderboardUploadScoreMethod.KeepBest, millis, null);
				}
//				//save
//				result = userStatsDiamond.storeStats();
			} else {
				if (currentLeaderboardEmerald != null) {
					userStatsEmerald.uploadLeaderboardScore(currentLeaderboardEmerald, SteamUserStats.LeaderboardUploadScoreMethod.KeepBest, millis, null);
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

	public static void RecordString() {
		if (!SteamAPI.isSteamRunning()) {
			// TODO :: Need to deal with offline records
			currentDisplayString = "Loading Records";
			return;
		}
		if ((statsLoadedEmerald==2) & (statsLoadedDiamond==2)) {
			String pb, pbd, rnk, rnkd;
			if (GameVars.GetLevelStatus(currentLevel-1)==0) {
				pb = "Level not yet complete!\n";
				if (currentLevel == LevelsListGame.NUMGAMELEVELS) pbd = "\n";
				else if (GameVars.CanSkip()) pbd = "Press 's' to skip this level\n\n";
				else pbd = "No skips left!\n\n";
			} else if (GameVars.GetLevelStatus(currentLevel-1)==2) {
				pb = "Level skipped!";
				pbd = "\n\n";
			} else {
				if (playerRankEmerald == -1) rnk = "Unranked";
				else rnk = String.format("World Ranking: %d", playerRankEmerald);
				if (playerRankDiamond == -1) rnkd = "Unranked";
				else rnkd = String.format("World Ranking: %d", playerRankDiamond);
				pb  = String.format("Emerald PB\n%s\n%s\n\n", GameVars.getTimeString(playerBestEmerald), rnk);
				pbd = String.format("Diamond PB\n%s\n%s\n\n", GameVars.getTimeString(playerBestDiamond), rnkd);
			}
			String wr, wrd;
			if (worldRecordTimesEmerald[0] == -1) wr  = "No Emerald WR\n\n";
			else wr  = String.format("Emerald WR\n%s  -  %s\n\n", worldRecordNamesEmerald[0], GameVars.getTimeString(worldRecordTimesEmerald[0]));
			if (worldRecordTimesDiamond[0] == -1) wrd  = "No Diamond WR";
			else wrd = String.format("Diamond WR\n%s  -  %s", worldRecordNamesDiamond[0], GameVars.getTimeString(worldRecordTimesDiamond[0]));
			// Put it all together for the current string to display
			currentDisplayString = pb+pbd+wr+wrd;
		} else if (statsLoadedEmerald > 2) {
			statsLoadedEmerald = 2;
		} else if (statsLoadedDiamond > 2) {
			statsLoadedDiamond = 2;
		} else currentDisplayString = "Loading Records";
		return;
	}

	public static void RecordStringMenu(boolean diamond) {
		if (!SteamAPI.isSteamRunning()) {
			// TODO :: Need to deal with offline records
			// Probably can just set the variables below
			currentDisplayString = "Steam offline";
			playerName = "";
			playerBestEmerald = 0;
			playerBestDiamond = 0;
			return;
		}
		if (diamond) {
			if (statsLoadedDiamond==2) {
				recordMenuStringNames = "";
				recordMenuStringRanks = "";
				recordMenuStringTimes = "";
				for (int ww=0; ww < worldRecordNamesDiamond.length; ww++) {
					recordMenuStringRanks += String.format("%d\n", ww+1);
					recordMenuStringNames += String.format("%s\n", worldRecordNamesDiamond[ww]);
					if (worldRecordNamesDiamond[ww].compareTo("")==0) recordMenuStringTimes += "\n";
					else recordMenuStringTimes += String.format("%s\n", GameVars.getTimeString(worldRecordTimesDiamond[ww]));
				}
				if (playerRankDiamond == 11) {
					recordMenuStringRanks += String.format("%s", playerRankDiamond);
					recordMenuStringNames += String.format("%s", playerName);
					recordMenuStringTimes += String.format("%s", playerBestDiamond);
				} else if (playerRankDiamond > 11) {
					recordMenuStringRanks += String.format(":\n%s\n", playerRankDiamond);
					recordMenuStringNames += String.format(":\n%s\n", playerName);
					recordMenuStringTimes += String.format(":\n%s\n", playerBestDiamond);
				} else if (playerRankDiamond == -1) {
					recordMenuStringRanks += String.format(":\n%s\n", " ");
					recordMenuStringNames += String.format(":\n%s - unranked\n", playerName);
					recordMenuStringTimes += String.format(":\n%s\n", " ");
				}
			} else if (statsLoadedDiamond>2) {
				statsLoadedDiamond = 2;
			} else {
				recordMenuStringNames = "Loading Records";
				recordMenuStringRanks = "";
				recordMenuStringTimes = "";
				return;
			}
		} else {
			if (statsLoadedEmerald==2) {
				recordMenuStringNames = "";
				recordMenuStringRanks = "";
				recordMenuStringTimes = "";
				for (int ww=0; ww < worldRecordNamesEmerald.length; ww++) {
					recordMenuStringRanks += String.format("%d\n", ww+1);
					recordMenuStringNames += String.format("%s\n", worldRecordNamesEmerald[ww]);
					if (worldRecordNamesEmerald[ww].compareTo("")==0) recordMenuStringTimes += "\n";
					else recordMenuStringTimes += String.format("%s\n", GameVars.getTimeString(worldRecordTimesEmerald[ww]));
				}
				if (playerRankEmerald == 11) {
					recordMenuStringRanks += String.format("%s", playerRankEmerald);
					recordMenuStringNames += String.format("%s", playerName);
					recordMenuStringTimes += String.format("%s", GameVars.getTimeString(playerBestEmerald));
				} else if (playerRankEmerald > 11) {
					recordMenuStringRanks += String.format(":\n%s\n", playerRankEmerald);
					recordMenuStringNames += String.format(":\n%s\n", playerName);
					recordMenuStringTimes += String.format(":\n%s\n", GameVars.getTimeString(playerBestEmerald));
				} else if (playerRankEmerald == -1) {
					recordMenuStringRanks += String.format(":\n%s\n", " ");
					recordMenuStringNames += String.format(":\n%s\n", playerName);
					recordMenuStringTimes += String.format(":\n%s\n", GameVars.getTimeString(playerBestEmerald));
				}
			} else if (statsLoadedEmerald>2) {
				statsLoadedEmerald = 2;
			} else {
				recordMenuStringNames = "Loading Records";
				recordMenuStringRanks = "";
				recordMenuStringTimes = "";
				return;
			}
		}
//		if (statsLoadedEmerald==2) {
//			String pb, pbd, rnk, rnkd;
//			if (GameVars.GetLevelStatus(currentLevel-1)==0) {
//				pb = "Level not yet complete!\n";
//				if (currentLevel == LevelsListGame.NUMGAMELEVELS) pbd = "\n";
//				else if (GameVars.CanSkip()) pbd = "Press 's' to skip this level\n\n";
//				else pbd = "No skips left!\n\n";
//			} else if (GameVars.GetLevelStatus(currentLevel-1)==2) {
//				pb = "Level skipped!";
//				pbd = "\n\n";
//			} else {
//				if (playerRankEmerald == -1) rnk = String.format("Unranked", currentLeaderboardEmeraldNumber);
//				else rnk = String.format("World Ranking: %d", playerRankEmerald);
//				if (playerRankDiamond == -1) rnkd = String.format("Unranked", currentLeaderboardDiamondNumber);
//				else rnkd = String.format("World Ranking: %d", playerRankDiamond);
//				pb  = String.format("Emerald PB\n%s\n%s\n\n", GameVars.getTimeString(playerBestEmerald), rnk);
//				pbd = String.format("Diamond PB\n%s\n%s\n\n", GameVars.getTimeString(playerBestDiamond), rnkd);
//			}
//			String wr, wrd;
//			if (worldRecordTimesEmerald[0] == -1) wr  = "No Emerald World Record\n\n";
//			else wr  = String.format("Emerald World Record\n%s\n%s\n\n", worldRecordNamesEmerald[0], GameVars.getTimeString(worldRecordTimesEmerald[0]));
//			if (worldRecordTimesDiamond[0] == -1) wrd  = "No Diamond World Record";
//			else wrd = String.format("Diamond World Record\n%s\n%s", worldRecordNamesDiamond[0], GameVars.getTimeString(worldRecordTimesDiamond[0]));
//			// Put it all together for the current string to display
//			currentDisplayString = pb+pbd+wr+wrd;
//		} else if (statsLoadedEmerald > 2) {
//			statsLoadedEmerald = 2;
//		} else if (statsLoadedDiamond > 2) {
//			statsLoadedDiamond = 2;
//		} else currentDisplayString = "Loading Records";
	}

	public void disconnect() {
		SteamAPI.shutdown();
	}
}