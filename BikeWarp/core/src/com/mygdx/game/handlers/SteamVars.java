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

	private static SteamUserStats userStatsEmerald, userStatsDiamond;
	public static boolean isOnline=false;
	private static SteamLeaderboardHandle[] leaderboardEmerald;
	private static SteamLeaderboardHandle[] leaderboardDiamond;
	private static String[] leaderboardNameEmerald;
	private static String[] leaderboardNameDiamond;
	private static int[] leaderboardNumberEmerald;
	private static int[] leaderboardNumberDiamond;
	public static SteamID steamPlayerID;
	private static boolean readyForNextLeaderboard;
	public static int loadingLevel, currentLevel;
	public static int worldRecordDiamond, worldRecordEmerald;
	public static int personalBestEmerald, personalBestDiamond;
	private static boolean emeraldComplete, diamondComplete;
	private static SteamUserStatsCallback steamUserStatsCallbackEmerald = new SteamUserStatsCallback() {

		@Override
		public void onUserStatsReceived(long gameId, SteamID steamIDUser,
										SteamResult result)
		{
			steamPlayerID = steamIDUser;
			if (GameVars.GetCurrentPlayer() == -1) {
				GameVars.LoadPlayers();
				GameVars.SetCurrentPlayer(steamIDUser.getAccountID());
				// Set the display preference for the user
				BikeGame.UpdateDisplay();
			}
		}

		@Override
		public void onUserStatsStored(long gameId, SteamResult result) {
		}

		@Override
		public void onUserStatsUnloaded(SteamID steamIDUser) {
		}

		@Override
		public void onUserAchievementStored(long gameId,
											boolean isGroupAchievement, String achievementName,
											int curProgress, int maxProgress) {
		}

		@Override
		public void onLeaderboardFindResult(SteamLeaderboardHandle leaderboard,
											boolean found) {
			System.out.println("Leaderboard find result: handle=" + leaderboard.toString() +
					", found=" + (found ? "yes" : "no"));
			if (found) {
				leaderboardEmerald[loadingLevel] = leaderboard;
				leaderboardNumberEmerald[loadingLevel] = userStatsEmerald.getLeaderboardEntryCount(leaderboard);
				System.out.println("Leaderboard: name=" + userStatsEmerald.getLeaderboardName(leaderboardEmerald[loadingLevel]) +
						", entries=" + leaderboardNumberEmerald[loadingLevel]);
				loadingLevel += 1;
				readyForNextLeaderboard = true;
			} else {
				System.out.println("Leaderboard not found: " + userStatsEmerald.getLeaderboardName(leaderboard));
			}
		}

		@Override
		public void onLeaderboardScoresDownloaded(
				SteamLeaderboardHandle leaderboard,
				SteamLeaderboardEntriesHandle entries, int numEntries) {
			// Get the world record value
//			SteamLeaderboardEntry entry = new SteamLeaderboardEntry();
//			if (userStatsEmerald.getDownloadedLeaderboardEntry(entries, 0, entry, null)) {
//				personalBestEmerald = entry.getScore();
//				System.out.println(String.format("%d %d", entry.getGlobalRank(), personalBestEmerald));
//			}
		}

		@Override
		public void onLeaderboardScoreUploaded(boolean success,
											   SteamLeaderboardHandle leaderboard, int score,
											   boolean scoreChanged, int globalRankNew, int globalRankPrevious) {
//			if (scoreChanged) {
//				System.out.println("The score has changed:");
//				System.out.println(score);
//				personalBestEmerald = score;
//				currentEmeraldPlayerRank = globalRankNew;
//				GameVars.StoreReplay(currentLevel, false);
//			}
		}

		@Override
		public void onGlobalStatsReceived(long gameId, SteamResult result) {
		}

	};

	private static SteamUserStatsCallback steamUserStatsCallbackDiamond = new SteamUserStatsCallback() {

		@Override
		public void onUserStatsReceived(long gameId, SteamID steamIDUser,
										SteamResult result)
		{
		}

		@Override
		public void onUserStatsStored(long gameId, SteamResult result) {
		}

		@Override
		public void onUserStatsUnloaded(SteamID steamIDUser) {

		}

		@Override
		public void onUserAchievementStored(long gameId,
											boolean isGroupAchievement, String achievementName,
											int curProgress, int maxProgress) {
		}

		@Override
		public void onLeaderboardFindResult(SteamLeaderboardHandle leaderboard,
											boolean found) {
//			System.out.println("Leaderboard find result: handle=" + leaderboard.toString() +
//					", found=" + (found ? "yes" : "no"));
			if (found) {
				leaderboardDiamond[loadingLevel-LevelsListGame.NUMGAMELEVELS-1] = leaderboard;
				leaderboardNumberDiamond[loadingLevel-LevelsListGame.NUMGAMELEVELS-1] = userStatsDiamond.getLeaderboardEntryCount(leaderboard);
				System.out.println("Leaderboard: name=" + userStatsDiamond.getLeaderboardName(leaderboardDiamond[loadingLevel-LevelsListGame.NUMGAMELEVELS-1]) +
						", entries=" + leaderboardNumberDiamond[loadingLevel-LevelsListGame.NUMGAMELEVELS-1]);
				loadingLevel += 1;
				readyForNextLeaderboard = true;
			} else {
				System.out.println("Leaderboard not found: " + userStatsDiamond.getLeaderboardName(leaderboard));
			}
		}

		@Override
		public void onLeaderboardScoresDownloaded(
				SteamLeaderboardHandle leaderboard,
				SteamLeaderboardEntriesHandle entries, int numEntries) {
			// Get the world record value
//			SteamLeaderboardEntry entry = new SteamLeaderboardEntry();
//			if (userStatsDiamond.getDownloadedLeaderboardEntry(entries, 0, entry, null)) {
//				worldRecordDiamond = entry.getScore();
//				System.out.println(String.format("diamond %d %d", entry.getGlobalRank(), worldRecordDiamond));
//			}
		}

		@Override
		public void onLeaderboardScoreUploaded(boolean success,
											   SteamLeaderboardHandle leaderboard, int score,
											   boolean scoreChanged, int globalRankNew, int globalRankPrevious) {
//			if (scoreChanged) {
//				currentDiamondPlayerRank = globalRankNew;
//				personalBestDiamond = score;
//				GameVars.StoreReplay(loadingLevel, true);
//			}
		}

		@Override
		public void onGlobalStatsReceived(long gameId, SteamResult result) {
		}

	};

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
		//A must before setting achievements
		userStatsEmerald.requestCurrentStats();
		userStatsDiamond.requestCurrentStats();
		isOnline=true;
		// Initialise the leaderboard handles
		return true;
	};

	public static float GetProgress() {
		// Return the progress on loading the leaderboards
		return ((float) loadingLevel)/((float) 2*LevelsListGame.NUMGAMELEVELS+3);
	}

	public static void ParseLeaderboards() {
		// Must call prepareLeaderboards before calling this function
		if (readyForNextLeaderboard) {
			if (loadingLevel < LevelsListGame.NUMGAMELEVELS+1) {
				userStatsEmerald.findLeaderboard(leaderboardNameEmerald[loadingLevel]);
			} else if (loadingLevel < 2*(LevelsListGame.NUMGAMELEVELS+1)) {
				userStatsDiamond.findLeaderboard(leaderboardNameDiamond[loadingLevel-LevelsListGame.NUMGAMELEVELS-1]);
			} else if (loadingLevel == 2*(LevelsListGame.NUMGAMELEVELS+1)) {
				System.out.println("TODO :: NEED TO DOWNLOAD THE TOTAL TOTAL TIME");
			} else {
				System.out.println("All leaderboards loaded");
			}
			readyForNextLeaderboard = false;
//			} else if (false) {
//				// Get stats now
//				// TODO :: Need to actually get the scores/records/ranks...
//				SteamID[] Users = {steamPlayerID}; // Local user steam id
//				userStatsEmerald.downloadLeaderboardEntriesForUsers(currentLeaderboardEmerald, Users);
//			}
		}
	}

	public static void PrepareLeaderboards() {
		// Generate a list of all available leaderboards
		// A handle to each of the leaderboards
		leaderboardEmerald = new SteamLeaderboardHandle[LevelsListGame.NUMGAMELEVELS+1];
		leaderboardDiamond = new SteamLeaderboardHandle[LevelsListGame.NUMGAMELEVELS+1];
		// The name of each leaderboard
		leaderboardNameEmerald = new String[LevelsListGame.NUMGAMELEVELS+1];
		leaderboardNameDiamond = new String[LevelsListGame.NUMGAMELEVELS+1];
		// The number of players that have completed these levels
		leaderboardNumberEmerald = new int[LevelsListGame.NUMGAMELEVELS+1];
		leaderboardNumberDiamond = new int[LevelsListGame.NUMGAMELEVELS+1];
		// TODO :: Need to include the total (emerald+diamond) leaderboard handle here too
		for (int ll=0; ll<LevelsListGame.NUMGAMELEVELS; ll++) {
			leaderboardNameEmerald[ll] = String.format("Level%02d_Emerald", ll+1);
			leaderboardNameDiamond[ll] = String.format("Level%02d_Diamond", ll+1);
		}
		leaderboardNameEmerald[LevelsListGame.NUMGAMELEVELS] = "TotalTime_Emerald";
		leaderboardNameDiamond[LevelsListGame.NUMGAMELEVELS] = "TotalTime_Diamond";
		// Initialise the variables - get ready for connecting to Steam leaderboards
		readyForNextLeaderboard = true;
		loadingLevel = 0;
	}

	public static boolean uploadTime(int millis, boolean emerald) {
		boolean result = false;
//		try {
//			if (emerald) {
//				if (currentLeaderboardEmerald != null) {
//					userStatsEmerald.uploadLeaderboardScore(currentLeaderboardEmerald, SteamUserStats.LeaderboardUploadScoreMethod.KeepBest, millis, null);
//				}
//				//save
//				result = userStatsEmerald.storeStats();
//			} else {
//				if (currentLeaderboardDiamond != null) {
//					userStatsDiamond.uploadLeaderboardScore(currentLeaderboardDiamond, SteamUserStats.LeaderboardUploadScoreMethod.KeepBest, millis, null);
//				}
//				//save
//				result = userStatsDiamond.storeStats();
//			}
//		} catch (Exception e) {
//			isOnline = false;
//			result = false;
//		}
		return result;
	}

	public static String RecordString() {
		return "NEED TO DO THIS IN STEAM VARS";
	}

	public void disconnect() {
		SteamAPI.shutdown();
	}
}