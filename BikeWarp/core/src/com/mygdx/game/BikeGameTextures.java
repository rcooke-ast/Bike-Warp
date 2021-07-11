package com.mygdx.game;
// Some free textures available from:
// https://www.pxfuel.com/en/search?q=seamless+texture
// https://www.wildtextures.com/category/free-textures/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.handlers.GameVars;

public class BikeGameTextures {
    public static AssetManager textureManager;
    public static Sprite[] allFlags;

    public static void InitiateTextures() {
        // Initiate the arrays
        textureManager = new AssetManager();
        //textureManager.finishLoading();
        // Load all level textures
        GetLevelImages();
        // Load any remaining menu textures
        GetMenuImages();
        // Load Decorations
        GetDecorations();
    }

    public static float BackgroundLimit(String fname) {
        if (fname == null) return 0.5f;
        if (fname.equalsIgnoreCase("background_waterfall")) return 0.35f;
        return 0.5f;
    }

    public static String GetTextureName(String file) {
        String fext, fname = "data/images/"+file;
        if (file.equals("grass_smooth_linrep")) fext = "data/images/grass_smooth.png";
        else if (file.equals("cracked_dirt_linrep")) fext = "data/images/cracked_dirt.png";
        else if (Gdx.files.internal(fname+".png").exists()) fext = fname + ".png";
        else if (Gdx.files.internal(fname+".jpg").exists()) fext = fname + ".jpg";
        else fext = "data/images/error.png";
        return fext;//Gdx.files.internal(fext).name();
    }

    private static void GetTexture(String file) {
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = TextureFilter.Linear;
        param.magFilter = TextureFilter.Linear;
        textureManager.load(GetTextureName(file), Texture.class, param);
    }

    public static void LoadFlags() {
        allFlags = new Sprite[GameVars.countryNames.length-1];
        for (int ff=0; ff<GameVars.countryNames.length-1; ff++) {
            allFlags[ff] = new Sprite(BikeGameTextures.LoadTexture(String.format("flag_%03d",ff)));
        }
    }

    private static void GetLevelImages() {
        // Load the items required for the splash screen
        GetTexture("bg_StarsSparse");
        GetTexture("menu_gamename");
        GetTexture("nitrous_tube");
        GetTexture("nitrous_fluid");
        GetTexture("finish_whirl");
        GetTexture("ground_shade");
        // Load Bike
        GetTexture("bike_white");
        GetTexture("bike_overlay");
        GetTexture("rear_suspension");
        GetTexture("front_suspension");
        GetTexture("bikewheel");
        GetTexture("bike_colortile");
        // Load Level images
        GetTexture("bolt");
        GetTexture("boulder");
        GetTexture("chain_link");
        GetTexture("crate1");
        GetTexture("crate2");
        GetTexture("finish_whirl");
        GetTexture("finish_ball");
        GetTexture("gate");
        GetTexture("gem_gold");
        GetTexture("gem_diamond");
        GetTexture("gravity");
        GetTexture("key_red");
        GetTexture("key_blue");
        GetTexture("key_green");
        GetTexture("log");
        GetTexture("nitrous_fire_01");
        GetTexture("nitrous_fire_02");
        GetRoadSigns();
        GetTexture("metalplate");
        GetTexture("nitrous");
        GetTexture("padlock_blue");
        GetTexture("padlock_green");
        GetTexture("padlock_red");
        GetTexture("planet_sun");
        GetTexture("planet_mercury");
        GetTexture("planet_venus");
        GetTexture("planet_earth");
        GetTexture("planet_earthgrey");
        GetTexture("planet_mars");
        GetTexture("planet_jupiter");
        GetTexture("planet_saturn");
        GetTexture("planet_uranus");
        GetTexture("planet_neptune");
        GetTexture("RS_exclamation");
        GetTexture("spike");
        GetTexture("switch_greenL");
        GetTexture("switch_redL");
        GetTexture("transport_spiral");
        GetTexture("waterfall");
        GetTexture("wood_link");
        GetTexture("vehicle_05");
        GetTexture("lander");
    }

    private static void GetMenuImages() {
        GetTexture("records_tile");
        GetTexture("records_dots");
        GetTexture("menu_arrow");
        GetTexture("menu_ChangePlayer");
        GetTexture("menu_DesignLevel");
        GetTexture("menu_Exit");
        GetTexture("menu_Multiplayer");
        GetTexture("menu_Options");
        GetTexture("menu_SinglePlayer");
        GetTexture("menu_Training");
        GetTexture("menu_WatchReplays");
        GetTexture("menu_WorldRecords");
        GetTexture("menu_shaft");
        GetTexture("menu_wheel");
        GetTexture("menu_black");
        GetTexture("metalpole_black");
        GetTexture("metalpole_blackcorner");
        // Add some additional textures
        GetRepeatedTextures();
    }

    private static void GetRepeatedTextures() {
        // Metal grid
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = TextureFilter.Linear;
        param.magFilter = TextureFilter.Linear;
        param.wrapU = TextureWrap.Repeat;
        param.wrapV = TextureWrap.Repeat;
//        textureManager.load(Gdx.files.internal("data/images/metal_grid.png").name(), Texture.class, param);
        textureManager.load("data/images/metal_grid.png", Texture.class, param);
        // Grass
        param = new TextureLoader.TextureParameter();
        param.minFilter = TextureFilter.Linear;
        param.magFilter = TextureFilter.Linear;
        param.wrapU = TextureWrap.Repeat;
        param.wrapV = TextureWrap.Repeat;
        textureManager.load("data/images/grass_smooth.png", Texture.class, param);
//        textureManager.load(Gdx.files.internal("data/images/grass_smooth.png").name(), Texture.class, param);
        // The above is called ^^^ decorTextureNames.add("grass_smooth_linrep");
        // Cracked dirt
        param = new TextureLoader.TextureParameter();
        param.minFilter = TextureFilter.Linear;
        param.magFilter = TextureFilter.Linear;
        param.wrapU = TextureWrap.Repeat;
        param.wrapV = TextureWrap.ClampToEdge;
        textureManager.load("data/images/cracked_dirt.png", Texture.class, param);
//        textureManager.load(Gdx.files.internal("data/images/cracked_dirt.png").name(), Texture.class, param);
        // The above is called ^^^ decorTextureNames.add("cracked_dirt_linrep");
    }

    private static void GetRoadSigns() {
        GetTexture("RS_bumps");
        GetTexture("RS_dash");
        GetTexture("RS_dot");
        GetTexture("RS_donotenter");
        GetTexture("RS_exclamation");
        GetTexture("RS_motorbike");
        GetTexture("RS_nomotorbike");
        GetTexture("RS_rampahead");
        GetTexture("RS_reducespeed");
        GetTexture("RS_speed_10");
        GetTexture("RS_speed_flag0");
        GetTexture("RS_speed_30");
        GetTexture("RS_speed_40");
        GetTexture("RS_speed_50");
        GetTexture("RS_speed_60");
        GetTexture("RS_speed_80");
        GetTexture("RS_speed_100");
        GetTexture("RS_stop");
        GetTexture("RS_noaliens");
        GetTexture("RS_toxic");
        GetTexture("metal_pole_1x16");
    }

    private static void GetDecorations() {
        GetTexture("grass_smooth");
        GetTexture("ground_bubbles");
        GetTexture("ground_cracked");
        GetTexture("ground_gravel");
        GetTexture("ground_mars");
        GetTexture("ground_moon");
        GetTexture("background_mountains");
        GetTexture("background_milkyway");
        GetTexture("background_shootingstar");
        GetTexture("background_stars");
        GetTexture("background_waterfall");
        GetTexture("bg_sunset");
        GetTexture("bg_Astronaut");
        GetTexture("bg_AuroraTrees");
        GetTexture("bg_BubbleBlue");
        GetTexture("bg_EarthAtNight");
        GetTexture("bg_Earth");
        GetTexture("bg_GalaxyAndromeda");
        GetTexture("bg_GalaxyDusty");
        GetTexture("bg_GalaxySpiral");
        GetTexture("bg_GalaxyWhite");
        GetTexture("bg_MilkyWayMountains");
        GetTexture("bg_MilkyWayRocks");
        GetTexture("bg_MilkyWayTallRocks");
        GetTexture("bg_MilkyWay_BlueTorch");
        GetTexture("bg_MilkyWay_ShootingStar2");
        GetTexture("bg_MoonFull");
        GetTexture("bg_MoonGibbous");
        GetTexture("bg_MoonRising");
        GetTexture("bg_MountainStarsBlue");
        GetTexture("bg_MountainStarsYellow");
        GetTexture("bg_NebulaBlue");
        GetTexture("bg_NebulaBlueOrange");
        GetTexture("bg_NebulaOrange");
        GetTexture("bg_NebulaPink");
        GetTexture("bg_NebulaRedGreen");
        GetTexture("bg_ShuttleLaunch");
        GetTexture("bg_planets_1");
        GetTexture("bg_StarCircles");
        GetTexture("bg_Starfield");
        GetTexture("bg_Stargazer");
        GetTexture("bg_StarsBlue");
        GetTexture("bg_StarsBlueDust");
        GetTexture("bg_StarsBlueGreen");
        GetTexture("bg_StarsBluePurple");
        GetTexture("bg_StarsCloudsBlueOrange");
        GetTexture("bg_StarsDusty");
        GetTexture("bg_StarsOrange");
        GetTexture("bg_StarsPurple");
        GetTexture("bg_StarsPurpleDust");
        GetTexture("bg_StarsPurpleOrange");
        GetTexture("bg_StarsRed");
        GetTexture("bg_StarsRocksBluePink");
        GetTexture("bg_TreesStarsGreen");
        GetTexture("bg_Waterfall3");
        GetTexture("foreground_bushes");
        GetTexture("foreground_plants");
        GetTexture("foreground_trees");
        GetTexture("sky_bluesky");
        GetTexture("sky_dusk");
        GetTexture("sky_evening");
        GetTexture("sky_islands");
        GetTexture("sky_mars");
        GetTexture("sky_moon");
        GetTexture("sky_sunrise");
        // Now load the texture decorations that are used in the Editor
        GetTexture("records_stone");
        GetTexture("records_stone_dark");
        GetTexture("records_stone_menu");
        GetTexture("binbag");
        for (int i=0; i<8; i++) GetTexture("rock_"+String.format("%02d", i));
        for (int i=0; i<12; i++) GetTexture("tree_"+String.format("%02d", i));
        for (int i=0; i<12; i++) GetTexture("tyrestack_"+String.format("%02d", i));
        for (int i=0; i<7; i++) GetTexture("vehicle_"+String.format("%02d", i));
        for (int i=0; i<9; i++) GetTexture("portrait_"+String.format("%02d", i));
        for (int i=0; i<8; i++) GetTexture("text_"+String.format("%02d", i));
        for (int i=0; i<8; i++) GetTexture("text_"+String.format("%02d", i));
        for (int i=0; i<250; i++) GetTexture("flag_"+String.format("%03d", i));
        GetTexture("misc_emerald");
        GetTexture("misc_diamond");
        GetTexture("misc_diary");
        GetTexture("misc_log");
        GetTexture("misc_sisyphus");
        GetTexture("misc_solarpanel");
        // Planets
        GetTexture("planet_moon");
        GetTexture("planet_supernova");
        GetTexture("planet_supernova_green");
        GetTexture("planet_radiodish");
        GetTexture("planet_dish");
        GetTexture("planet_dishbase");
        GetTexture("planet_dishbolt");
        // Load an error image just in case something fails
        GetTexture("error");
        // Add some additional textures
        GetTexture("track");
        GetTexture("shade");
        GetTexture("shadeback");
    }

    public static Texture LoadTexture(String file) {
        return textureManager.get(GetTextureName(file), Texture.class);
    }

    public static void dispose () {
        textureManager.dispose();
    }

}
