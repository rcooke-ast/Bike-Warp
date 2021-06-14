/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import com.badlogic.gdx.Game;
import com.codedisaster.steamworks.*;

import java.io.*;

/**
 *
 * @author rcooke
 */
public class SteamVars implements Serializable {

	private static SteamUserStats userStatsEmerald, userStatsDiamond;
	public static boolean isOnline=false;
	private static SteamLeaderboardHandle currentLeaderboardEmerald;
	private static SteamLeaderboardHandle currentLeaderboardDiamond;
	public static SteamID steamPlayerID;
	public static int currentEmeraldPlayerRank, currentDiamondPlayerRank, currentEmeraldRankNumber, currentDiamondRankNumber, currentLevel;
	public static int worldRecordDiamond, worldRecordEmerald;
	public static int personalBestEmerald, personalBestDiamond;

	private static SteamUserStatsCallback steamUserStatsCallbackEmerald = new SteamUserStatsCallback() {

		@Override
		public void onUserStatsReceived(long gameId, SteamID steamIDUser,
										SteamResult result)
		{
			steamPlayerID = steamIDUser;
			if (GameVars.currentPlayer == -1) {
				GameVars.LoadPlayers();
				GameVars.SetCurrentPlayer(steamIDUser.getAccountID());
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
//			System.out.println("Leaderboard find result: handle=" + leaderboard.toString() +
//					", found=" + (found ? "yes" : "no"));
			if (found) {
				currentLeaderboardEmerald = leaderboard;
//				System.out.println("Leaderboard: name=" + userStats.getLeaderboardName(leaderboard) +
//						", entries=" + userStats.getLeaderboardEntryCount(leaderboard));
			} else {
				System.out.println("Leaderboard not found: " + userStatsEmerald.getLeaderboardName(leaderboard));
			}
		}

		@Override
		public void onLeaderboardScoresDownloaded(
				SteamLeaderboardHandle leaderboard,
				SteamLeaderboardEntriesHandle entries, int numEntries) {
			// Grab the number of entries
			//currentEmeraldRankNumber = numEntries;
			// Get the world record value
			SteamLeaderboardEntry entry = new SteamLeaderboardEntry();
			int[] details = new int[16];
			if (userStatsEmerald.getDownloadedLeaderboardEntry(entries, 0, entry, details)) {
				personalBestEmerald = entry.getScore();
			}
			System.out.println(String.format("%d %d", entry.getGlobalRank(), personalBestEmerald));

		}

		@Override
		public void onLeaderboardScoreUploaded(boolean success,
											   SteamLeaderboardHandle leaderboard, int score,
											   boolean scoreChanged, int globalRankNew, int globalRankPrevious) {
			if (scoreChanged) {
				System.out.println("The score has changed:");
				System.out.println(score);
				currentEmeraldPlayerRank = globalRankNew;
				GameVars.StoreReplay(currentLevel, false);
			}
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
				currentLeaderboardDiamond = leaderboard;
//				System.out.println("Leaderboard: name=" + userStats.getLeaderboardName(leaderboard) +
//						", entries=" + userStats.getLeaderboardEntryCount(leaderboard));
			} else {
				System.out.println("Leaderboard not found: " + userStatsDiamond.getLeaderboardName(leaderboard));
			}
		}

		@Override
		public void onLeaderboardScoresDownloaded(
				SteamLeaderboardHandle leaderboard,
				SteamLeaderboardEntriesHandle entries, int numEntries) {
			// Grab the number of entries
			currentDiamondRankNumber = numEntries;
			// Get the world record value
			SteamLeaderboardEntry entry = new SteamLeaderboardEntry();
			int[] details = new int[16];
			if (userStatsEmerald.getDownloadedLeaderboardEntry(entries, 0, entry, details)) {
				worldRecordDiamond = entry.getScore();
			}
			System.out.println(String.format("diamond %d %d", entry.getGlobalRank(), worldRecordDiamond));
		}

		@Override
		public void onLeaderboardScoreUploaded(boolean success,
											   SteamLeaderboardHandle leaderboard, int score,
											   boolean scoreChanged, int globalRankNew, int globalRankPrevious) {
			if (scoreChanged) {
				currentDiamondPlayerRank = globalRankNew;
				GameVars.StoreReplay(currentLevel, true);
			}
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

	public static void prepareLeaderboards(int levelID) {
		currentEmeraldPlayerRank = -1; // TODO :: NEED TO FIX THIS
		userStatsEmerald.findLeaderboard(String.format("Level%02d_Emerald", levelID+1));
		userStatsDiamond.findLeaderboard(String.format("Level%02d_Diamond", levelID+1));
		currentLevel = levelID;
	}

	public static int GetWorldRecord(boolean diamond) {
		System.out.println(currentLeaderboardDiamond);
		return 0;
	}

	public static int GetPlayerRank() {
		SteamID[] Users = { steamPlayerID }; // Local user steam id
		userStatsEmerald.downloadLeaderboardEntriesForUsers(currentLeaderboardEmerald, Users);
		return currentEmeraldPlayerRank;
	}

	public static boolean uploadTime(int millis, boolean emerald) {
		boolean result = false;
		try {
			if (emerald) {
				if (currentLeaderboardEmerald != null) {
					userStatsEmerald.uploadLeaderboardScore(currentLeaderboardEmerald, SteamUserStats.LeaderboardUploadScoreMethod.KeepBest, millis, null);
				}
				//save
				result = userStatsEmerald.storeStats();
			} else {
				if (currentLeaderboardDiamond != null) {
					userStatsDiamond.uploadLeaderboardScore(currentLeaderboardDiamond, SteamUserStats.LeaderboardUploadScoreMethod.KeepBest, millis, null);
				}
				//save
				result = userStatsDiamond.storeStats();
			}
		} catch (Exception e) {
			isOnline = false;
			result = false;
		}
		return result;
	}

	public void disconnect(){
		SteamAPI.shutdown();
	};

}
