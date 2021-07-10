/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.SteamAPI;
import com.mygdx.game.BikeGame;
import com.mygdx.game.states.*;

import java.io.File;
import java.util.Stack;

/**
 *
 * @author rcooke
 */
public class GameStateManager {
    private BikeGame game;
    private Stack<GameState> gameStates;
    public GameState editorWindow = null;
    public boolean isPlaying = false;
    
    public static final int MAINMENU = 100000;
    public static final int MENUEXIT = 100001;
    public static final int MENURECORDS = 100002;
    public static final int MENUOPTIONS = 100003;
    public static final int MENURECORDSDISPLAY = 100004;
    public static final int MENULEVELS = 100005;
    public static final int MENUREPLAYPB = 100006;
    public static final int MENUREPLAY = 100007;
    public static final int MENUOPTIONSCOLOR = 110008;
    public static final int MENUOPTIONSCONTROLS = 110009;
    public static final int MENUOPTIONSHUDDISP = 110010;
    public static final int MENUPLAYER = 110011;
    public static final int MENURECORDSTOTAL = 100012;
    public static final int MENUREPLAYCUSTOM = 100013;
    public static final int MENUSELECTCOUNTRY = 100014;
    public static final int PLAY = 200000;
    public static final int LEVELOPTIONS = 300001;
    public static final int EDITOR = 400000;
    public static final int PEEK = 1;

    public GameStateManager(BikeGame game) {
        this.game = game;
        gameStates = new Stack<GameState>();
//        if (GameVars.currentPlayer == -1) GameVars.LoadPlayers();
		File directory = new File(ReplayVars.replayDir);
	    if (!directory.exists()) directory.mkdir();
        // Set the starting State
        if (SteamAPI.isSteamRunning()) {
            // Fix the splashscreen for those that prefer windowed borderless
            if (!GameVars.GetPlayerFullscreen()) {
                this.game.resize(Gdx.graphics.getWidth()* this.game.SplashScreenScale, Gdx.graphics.getHeight()* this.game.SplashScreenScale);
                Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth()*this.game.SplashScreenScale, Gdx.graphics.getHeight()* this.game.SplashScreenScale);
            }
            // Now load the right menu
            if (GameVars.IsCountrySet()) {
                Gdx.input.setCursorCatched(true);
                pushState(MAINMENU, null, -1, 0);
            } else {
                pushState(MENUSELECTCOUNTRY, null, -1, 0);
            }
        } else {
            // Fix the splashscreen for those that prefer windowed borderless
            if (!GameVars.GetPlayerFullscreen()) {
                this.game.resize(Gdx.graphics.getWidth() * this.game.SplashScreenScale, Gdx.graphics.getHeight() * this.game.SplashScreenScale);
                Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
            // Now load the right menu
            BikeGame.UpdateDisplay();
            pushState(MENUPLAYER, null, -1, 0);
        }
    }
    
    public BikeGame game() { return game; }
    
    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    public void dispose() {
        if (game != null) game.dispose();
        if (editorWindow != null) editorWindow.dispose();
        this.dispose();
    }

    public void SetPlaying(boolean play) {
    	isPlaying = play;
    }

    private GameState getState(int state, String editorScene, int levelID, int modeValue) {
        if (state == MAINMENU) return new MainMenu(this);
        else if (state == PLAY) return new Play(this, editorScene, levelID, modeValue);
        else if (state == MENUPLAYER) return new MenuSelectPlayer(this, modeValue);
        else if (state == MENUEXIT) return new MenuExit(this);
        else if (state == MENURECORDS) return new MenuRecords(this);
        else if (state == MENURECORDSDISPLAY) return new MenuRecordsDisplay(this, modeValue);
        else if (state == MENURECORDSTOTAL) return new MenuRecordsTotal(this);
        else if (state == MENUOPTIONS) return new MenuOptions(this);
        else if (state == MENUOPTIONSCOLOR) return new OptionColorSelect(this);
        else if (state == MENUOPTIONSCONTROLS) return new OptionChangeControls(this);
        else if (state == MENUOPTIONSHUDDISP) return new OptionChangeHUDDisplay(this);
//        else if (state == MENUCUSTOM) return new LevelSelectCustom(this);
        else if (state == MENULEVELS) return new LevelSelectGame(this);
        else if (state == LEVELOPTIONS) return new LevelOptions(this, levelID, modeValue);
        else if (state == MENUREPLAY) return new MenuReplay(this);
        else if (state == MENUREPLAYPB) return new MenuReplayPB(this, modeValue);
        else if (state == MENUREPLAYCUSTOM) return new MenuReplayCustom(this);
        else if (state == MENUSELECTCOUNTRY) return new MenuSelectCountry(this, modeValue);
        else if (state == EDITOR) return new Editor(this);
        //else if (state == LEVELSELECT) return new LevelSelect(this);
        return null;
    }
    
    public void setState(int state, boolean store, String scene, int levelID, int train) {
        if (!store) popState();
    	if (state != PEEK) pushState(state, scene, levelID, train);
    }

    public void pushState(int state, String scene, int levelID, int train) {
        gameStates.push(getState(state, scene, levelID, train));
    }
    
    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }

}
