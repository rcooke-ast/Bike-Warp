/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game.handlers;

import com.mygdx.game.BikeGame;
import com.mygdx.game.states.GameState;
import com.mygdx.game.states.LevelOptions;
import com.mygdx.game.states.LevelSelectCustom;
import com.mygdx.game.states.LevelSelectGame;
import com.mygdx.game.states.MenuSelectPlayer;
import com.mygdx.game.states.MainMenu;
import com.mygdx.game.states.Editor;
import com.mygdx.game.states.MenuExit;
import com.mygdx.game.states.MenuRecords;
import com.mygdx.game.states.MenuReplay;
import com.mygdx.game.states.OptionColorSelect;
import com.mygdx.game.states.Play;

import java.util.Stack;

/**
 *
 * @author rcooke
 */
public class GameStateManager {
    private BikeGame game;
    private Stack<GameState> gameStates;
    public GameState editorWindow = null;
    
    public static final int MAINMENU = 100000;
    public static final int MENUEXIT = 100001;
    public static final int MENURECORDS = 100002;
    public static final int MENUOPTIONS = 100003;
    public static final int MENUOPTIONSCOLOR = 110003;
    public static final int MENUCUSTOM = 100004;
    public static final int MENULEVELS = 100005;
    public static final int MENUPLAYER = 100006;
    public static final int MENUREPLAY = 100007;
    public static final int PLAY = 200000;
    public static final int LEVELSELECT = 300000;
    public static final int LEVELOPTIONS = 300001;
    public static final int EDITOR = 400000;
    public static final int PEEK = 1;

    public GameStateManager(BikeGame game) {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(MENUPLAYER, null, -1, 0); // Set the starting State
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

    private GameState getState(int state, String editorScene, int levelID, int modeValue) {
        if (state == MENUPLAYER) return new MenuSelectPlayer(this);
        else if (state == MAINMENU) return new MainMenu(this);
        else if (state == MENUEXIT) return new MenuExit(this);
        else if (state == MENURECORDS) return new MenuRecords(this);
        //else if (state == MENUOPTIONS) return new MenuOptions(this);
        else if (state == MENUOPTIONSCOLOR) return new OptionColorSelect(this);
        else if (state == MENUCUSTOM) return new LevelSelectCustom(this);
        else if (state == MENULEVELS) return new LevelSelectGame(this);
        else if (state == LEVELOPTIONS) return new LevelOptions(this, levelID, modeValue);
        else if (state == MENUREPLAY) return new MenuReplay(this);
        else if (state == PLAY) return new Play(this, editorScene, levelID, modeValue);
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
