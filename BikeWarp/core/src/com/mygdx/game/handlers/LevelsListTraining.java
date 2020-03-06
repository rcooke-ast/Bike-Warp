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
public class LevelsListTraining {
	public static final int NUMTRAINLEVELS = 12
			;
    public static String[] trainingLevelNames = new String[NUMTRAINLEVELS+1];
    public static String[] trainingLevelDescr = new String[NUMTRAINLEVELS+1];
    public static String[] trainingLevelFiles = new String[NUMTRAINLEVELS+1];

    // Set the training levels
    static {
    	// Return to Main Menu
    	trainingLevelNames[0] = "Return to Main Menu";
    	trainingLevelFiles[0] = null;
    	trainingLevelDescr[0] = "Return to the Main Menu (or press Esc).";
    	// Level 1
    	trainingLevelNames[1] = "1. Jewels and The Warp";
    	trainingLevelFiles[1] = "data/gamelevels/train01.lvl";
    	trainingLevelDescr[1] = "Collect all the golden jewels and then go to the exit warp. The exit warp will only open once you have collected enough golden jewels. Jewels can be collected with your head or your tires. Do not hit your head on the ground, or you will have to start again! Pressing the Esc key will cause the level to be restarted.";
    	// Level 2
    	trainingLevelNames[2] = "2. Keys and Gates";
    	trainingLevelFiles[2] = "data/gamelevels/train02.lvl";
    	trainingLevelDescr[2] = "Gates are locked, and can only be opened with a key. Match the color of the key to the color of the padlock, then drive through the gate.";
    	// Level 3
    	trainingLevelNames[3] = "3. Switches";
    	trainingLevelFiles[3] = "data/gamelevels/train03.lvl";
    	trainingLevelDescr[3] = "Some Gates are controlled by switches. Every gate has one switch. A red switch means a gate is closed, and a green switch means a gate is open. Open a switch by flicking it with a tire.";
    	// Level 4
    	trainingLevelNames[4] = "4. Spikes";
    	trainingLevelFiles[4] = "data/gamelevels/train04.lvl";
    	trainingLevelDescr[4] = "Spikes will pop your tires, and you will have to start the level again. Avoid touching spikes with your head and your tires.";
    	// Level 5
    	trainingLevelNames[5] = "5. Gravity";
    	trainingLevelFiles[5] = "data/gamelevels/train05.lvl";
    	trainingLevelDescr[5] = "You can change the gravity of the level by collecting the gravity arrows. The gravity will change to the direction the arrow is pointing.";
    	// Level 6
    	trainingLevelNames[6] = "6. Transportation";
    	trainingLevelFiles[6] = "data/gamelevels/train06.lvl";
    	trainingLevelDescr[6] = "Touch a transporter to be taken to a connected part of the level. To return again, touch the transporter you exit from. If you enter the blue side of a transporter, you will exit on the red side. Transporters take 5 seconds to charge after each time you use them.";
    	// Level 7
    	trainingLevelNames[7] = "7. Moving Platforms";
    	trainingLevelFiles[7] = "data/gamelevels/train07.lvl";
    	trainingLevelDescr[7] = "Catch a ride on a moving platform! These platforms move back and forth along a path. Don't miss your ride!";
    	// Level 8
    	trainingLevelNames[8] = "8. Falling Platforms";
    	trainingLevelFiles[8] = "data/gamelevels/train08.lvl";
    	trainingLevelDescr[8] = "Platforms with a (!) road sign will fall 5 seconds after you first touch them.";
    	// Level 9
    	trainingLevelNames[9] = "9. Tricks";
    	trainingLevelFiles[9] = "data/gamelevels/train09.lvl";
    	trainingLevelDescr[9] = "The rider can do some basic tricks, like bunny hop. Use this trick to get a little more air, or jump over obstacles.";
    	// Level 10
    	trainingLevelNames[10] = "10. Nitrous";
    	trainingLevelFiles[10] = "data/gamelevels/train10.lvl";
    	trainingLevelDescr[10] = "Collect some nitrous, and use it for an extreme boost in speed by holding down the 'nitrous' key. It slowly runs out, so use it sparingly!";
    	// Level 11
    	trainingLevelNames[11] = "11. Obstacles";
    	trainingLevelFiles[11] = "data/gamelevels/train11.lvl";
    	trainingLevelDescr[11] = "Look out for obstacles!";
    	// Level 12
    	trainingLevelNames[12] = "12. The Diamond";
    	trainingLevelFiles[12] = "data/gamelevels/train12.lvl";
    	trainingLevelDescr[12] = "Every level contains a diamond jewel that is hard to find and difficult to collect. If you collect a diamond jewel, you will not need to collect any more golden jewels, just go straight to the exit warp. To successfully collect the diamond jewel in each level, you must finish by exiting through the warp. Try to find the diamonds in all of these training levels.";
    }

    public static String getLevelFile(int i) { return trainingLevelFiles[i]; }
    public static String getLevelName(int i) { return trainingLevelNames[i]; }

}
