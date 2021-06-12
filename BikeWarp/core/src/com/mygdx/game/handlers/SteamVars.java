/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

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
	public static int currentEmeraldPlayerRank, currentDiamondPlayerRank, currentEmeraldRankNumber, currentDiamondRankNumber, currentLevel;

	private static SteamUserStatsCallback steamUserStatsCallbackEmerald = new SteamUserStatsCallback() {

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
			currentEmeraldRankNumber = numEntries;
		}

		@Override
		public void onLeaderboardScoreUploaded(boolean success,
											   SteamLeaderboardHandle leaderboard, int score,
											   boolean scoreChanged, int globalRankNew, int globalRankPrevious) {
			if (scoreChanged) {
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
			currentDiamondRankNumber = numEntries;
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
