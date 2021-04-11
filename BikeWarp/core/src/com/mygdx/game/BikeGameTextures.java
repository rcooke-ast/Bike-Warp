package com.mygdx.game;
// Some free textures available from:
// https://www.pxfuel.com/en/search?q=seamless+texture
// https://www.wildtextures.com/category/free-textures/

import java.io.File;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

public class BikeGameTextures {
    private static Texture texture;
    private static ArrayList<Texture> textures;
    private static ArrayList<String> textureNames;
    private static ArrayList<Texture> menuTextures;
    private static ArrayList<String> menuTextureNames;
    private static ArrayList<Texture> decorTextures;
    private static ArrayList<String> decorTextureNames;
//    private static ArrayList<Texture> levelTextures;
//    private static ArrayList<String> levelTextureNames;
//    private static ArrayList<String> levelTexturesLoaded;

    public static void InitiateTextures() {
        // Initiate the arrays
        textures = new ArrayList<Texture>();
        textureNames = new ArrayList<String>();
        menuTextures = new ArrayList<Texture>();
        menuTextureNames = new ArrayList<String>();
        decorTextures = new ArrayList<Texture>();
        decorTextureNames = new ArrayList<String>();
//        levelTextures = new ArrayList<Texture>();
//        levelTextureNames = new ArrayList<String>();
//        levelTexturesLoaded = new ArrayList<String>();
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

//    public static boolean IsLevelLoaded(String levelname) {
//        for (int ll=0; ll<levelTexturesLoaded.size(); ll++) {
//            if (levelname.equalsIgnoreCase(levelTexturesLoaded.get(ll))) return true;
//        }
//        return false;
//    }
//
//    public static void AddLevelTexture(Texture texture, String levelname, String name) {
//        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//        levelTextures.add(texture);
//        levelTextureNames.add(levelname+"_"+name);
//        levelTexturesLoaded.add(levelname);
//    }

    private static void GetTexture(String file, int flag) {
        String fext, fname = "data/images/"+file;
        if (Gdx.files.internal(fname+".png").exists()) fext = fname + ".png";
        else if (Gdx.files.internal(fname+".jpg").exists()) fext = fname + ".jpg";
        else fext = "data/images/error.png";
        texture = new Texture(Gdx.files.internal(fext));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        if (flag==0) {
            textures.add(texture);
            textureNames.add(file);
        } else if (flag==1) {
            menuTextures.add(texture);
            menuTextureNames.add(file);
        } else if (flag==2) {
            decorTextures.add(texture);
            decorTextureNames.add(file);
        }
    }

    private static void GetLevelImages() {
        // Load Bike
        GetTexture("bike_white",0);
        GetTexture("bike_overlay",0);
        GetTexture("rear_suspension",0);
        GetTexture("front_suspension",0);
        GetTexture("bikewheel",0);
        // Load Level images
        GetTexture("bolt",0);
        GetTexture("boulder",0);
        GetTexture("chain_link",0);
        GetTexture("crate1",0);
        GetTexture("crate2",0);
        GetTexture("finish_whirl",0);
        GetTexture("finish_ball",0);
        GetTexture("gate",0);
        GetTexture("gem_gold",0);
        GetTexture("gem_diamond",0);
        GetTexture("gravity",0);
        GetTexture("key_red",0);
        GetTexture("key_blue",0);
        GetTexture("key_green",0);
        GetTexture("log",0);
        GetRoadSigns(0);
        GetTexture("metalplate",0);
        GetTexture("nitrous",0);
        GetTexture("nitrous_tube",0);
        GetTexture("nitrous_fluid",0);
        GetTexture("padlock_blue",0);
        GetTexture("padlock_green",0);
        GetTexture("padlock_red",0);
        GetTexture("planet_sun",0);
        GetTexture("planet_mercury",0);
        GetTexture("planet_venus",0);
        GetTexture("planet_earth",0);
        GetTexture("planet_mars",0);
        GetTexture("planet_jupiter",0);
        GetTexture("planet_saturn",0);
        GetTexture("planet_uranus",0);
        GetTexture("planet_neptune",0);
        GetTexture("RS_exclamation",0);
        GetTexture("spike",0);
        GetTexture("switch_greenL",0);
        GetTexture("switch_redL",0);
        GetTexture("transport_spiral",0);
        GetTexture("waterfall",0);
        GetTexture("wood_link",0);
        GetTexture("vehicle_05", 0);
    }

    private static void GetMenuImages() {
        GetTexture("menu_arrow",1);
        GetTexture("menu_ChangePlayer",1);
        GetTexture("menu_DesignLevel",1);
        GetTexture("menu_Exit",1);
        GetTexture("menu_Multiplayer",1);
        GetTexture("menu_Options",1);
        GetTexture("menu_SinglePlayer",1);
        GetTexture("menu_Training",1);
        GetTexture("menu_WatchReplays",1);
        GetTexture("menu_WorldRecords",1);
        GetTexture("menu_shaft",1);
        GetTexture("menu_wheel",1);
        GetTexture("menu_black",1);
        GetTexture("metalpole_black",1);
        GetTexture("metalpole_blackcorner",1);
        GetTexture("menu_gamename",1);
        // Add some additional textures
        texture = new Texture(Gdx.files.internal("data/images/metal_grid.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        menuTextures.add(texture);
        menuTextureNames.add("metal_grid");
    }

    private static void GetRoadSigns(int flag) {
        GetTexture("RS_bumps",flag);
        GetTexture("RS_dash",flag);
        GetTexture("RS_dot",flag);
        GetTexture("RS_donotenter",flag);
        GetTexture("RS_exclamation",flag);
        GetTexture("RS_motorbike",flag);
        GetTexture("RS_nomotorbike",flag);
        GetTexture("RS_rampahead",flag);
        GetTexture("RS_reducespeed",flag);
        GetTexture("RS_speed_10",flag);
        GetTexture("RS_speed_flag0",flag);
        GetTexture("RS_speed_30",flag);
        GetTexture("RS_speed_40",flag);
        GetTexture("RS_speed_50",flag);
        GetTexture("RS_speed_60",flag);
        GetTexture("RS_speed_80",flag);
        GetTexture("RS_speed_100",flag);
        GetTexture("RS_stop",flag);
        GetTexture("RS_noaliens",flag);
        GetTexture("RS_toxic",flag);
        GetTexture("metal_pole_1x16",flag);
    }

    private static void GetDecorations() {
        GetTexture("grass_smooth",2);
        GetTexture("ground_bubbles",2);
        GetTexture("ground_cracked",2);
        GetTexture("ground_gravel",2);
        GetTexture("ground_mars",2);
        GetTexture("ground_moon",2);
        //GetTexture("background_aurora",2);
        GetTexture("background_mountains",2);
        GetTexture("background_milkyway",2);
        GetTexture("background_shootingstar",2);
        GetTexture("background_stars",2);
        GetTexture("background_waterfall",2);
        GetTexture("bg_sunset", 2);
        GetTexture("bg_sunset_mud", 2);
        GetTexture("bg_Astronaut", 2);
        GetTexture("bg_BubbleBlue", 2);
        GetTexture("bg_EarthAtNight", 2);
        GetTexture("bg_GalaxyAndromeda", 2);
        GetTexture("bg_GalaxyDusty", 2);
        GetTexture("bg_GalaxySpiral", 2);
        GetTexture("bg_GalaxyWhite", 2);
        GetTexture("bg_MilkyWayMountains", 2);
        GetTexture("bg_MilkyWayRocks", 2);
        GetTexture("bg_MilkyWayTallRocks", 2);
        GetTexture("bg_MilkyWay_BlueTorch", 2);
        GetTexture("bg_MilkyWay_ShootingStar2", 2);
        GetTexture("bg_MoonFull", 2);
        GetTexture("bg_MoonGibbous", 2);
        GetTexture("bg_MoonRising", 2);
        GetTexture("bg_MountainStarsBlue", 2);
        GetTexture("bg_MountainStarsYellow", 2);
        GetTexture("bg_NebulaBlue", 2);
        GetTexture("bg_NebulaBlueOrange", 2);
        GetTexture("bg_NebulaOrange", 2);
        GetTexture("bg_NebulaRedGreen", 2);
        GetTexture("bg_ShuttleLaunch", 2);
        GetTexture("bg_planets_1", 2);
        GetTexture("bg_StarCircles", 2);
        GetTexture("bg_Stargazer", 2);
        GetTexture("bg_StarsBlue", 2);
        GetTexture("bg_StarsBlueDust", 2);
        GetTexture("bg_StarsBlueGreen", 2);
        GetTexture("bg_StarsBluePurple", 2);
        GetTexture("bg_StarsCloudsBlueOrange", 2);
        GetTexture("bg_StarsDusty", 2);
        GetTexture("bg_StarsOrange", 2);
        GetTexture("bg_StarsPurple", 2);
        GetTexture("bg_StarsPurpleDust", 2);
        GetTexture("bg_StarsPurpleOrange", 2);
        GetTexture("bg_StarsRed", 2);
        GetTexture("bg_StarsRocksBluePink", 2);
        GetTexture("bg_StarsSparse", 2);
        GetTexture("bg_TreesStarsGreen", 2);
        GetTexture("foreground_bushes",2);
        GetTexture("foreground_plants",2);
        GetTexture("foreground_trees",2);
        GetTexture("sky_bluesky",2);
        GetTexture("sky_dusk",2);
        GetTexture("sky_evening",2);
        GetTexture("sky_islands",2);
        GetTexture("sky_mars",2);
        GetTexture("sky_moon",2);
        GetTexture("sky_sunrise",2);
        //GetTexture("cracked_dirt");
        //GetTexture("dirt");
        //GetTexture("dirt_04_craziwolf");
        //GetTexture("dirt_04_craziwolf_copy");
        //GetTexture("dirt_dark");
        // Now load the texture decorations that are used in the Editor
        GetTexture("records_stone",2);
        GetTexture("records_stone_dark",2);
        GetTexture("records_stone_menu",2);
        GetRoadSigns(2);
        GetTexture("binbag",2);
        for (int i=0; i<8; i++) GetTexture("rock_"+String.format("%02d", i), 2);
        for (int i=0; i<8; i++) GetTexture("tree_"+String.format("%02d", i), 2);
        for (int i=0; i<12; i++) GetTexture("tyrestack_"+String.format("%02d", i), 2);
        for (int i=0; i<6; i++) GetTexture("vehicle_"+String.format("%02d", i), 2);
        GetTexture("misc_emerald", 2);
        GetTexture("misc_diamond", 2);
        // Planets
        GetTexture("planet_sun",2);
        GetTexture("planet_mercury",2);
        GetTexture("planet_venus",2);
        GetTexture("planet_earth",2);
        GetTexture("planet_mars",2);
        GetTexture("planet_jupiter",2);
        GetTexture("planet_saturn",2);
        GetTexture("planet_uranus",2);
        GetTexture("planet_neptune",2);
        GetTexture("planet_moon",2);
        GetTexture("planet_supernova",2);
        GetTexture("planet_supernova_green",2);
        // Load an error image just in case something fails
        GetTexture("error",2);
        // Add some additional textures
        GetTexture("track",2);
        // Add grass
        texture = new Texture(Gdx.files.internal("data/images/grass_smooth.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        decorTextures.add(texture);
        decorTextureNames.add("grass_smooth_linrep");
        // Add cracked dirt
        texture = new Texture(Gdx.files.internal("data/images/cracked_dirt.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        texture.setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);
        decorTextures.add(texture);
        decorTextureNames.add("cracked_dirt_linrep");
    }

    public static Texture LoadTexture(String file, int flag) {
        // flag = 0 loads all textures needed by the levels
        // flag = 1 loads menu textures
        // flag = 2 loads decoration textures
        // flag = 3 loads level textures
        if (flag==0) {
            for (int i=0; i<textures.size(); i++) {
                if (file.equals(textureNames.get(i))) return textures.get(i);
            }
        } else if (flag==1) {
            for (int i=0; i<menuTextures.size(); i++) {
                if (file.equals(menuTextureNames.get(i))) return menuTextures.get(i);
            }
        } else if (flag==2) {
            for (int i=0; i<decorTextures.size(); i++) {
                if (file.equals(decorTextureNames.get(i))) return decorTextures.get(i);
            }
//        } else if (flag==3) {
//            for (int i=0; i<levelTextures.size(); i++) {
//                System.out.println("BLAH "+levelTextureNames.get(i));
//                if (file.equals(levelTextureNames.get(i))) return levelTextures.get(i);
//            }
        }
        return null;
    }

    public static void dispose () {
        // Clear game textures
        for (int i=0; i<textures.size(); i++) {
            textures.get(i).dispose();
        }
        textures.clear();
        textureNames.clear();
        // Clear menu textures
        for (int i=0; i<menuTextures.size(); i++) {
            menuTextures.get(i).dispose();
        }
        menuTextures.clear();
        menuTextureNames.clear();
        // Clear decoration images
        for (int i=0; i<decorTextures.size(); i++) {
            decorTextures.get(i).dispose();
        }
        decorTextures.clear();
        decorTextureNames.clear();
        // Clear level images
//        for (int i=0; i<levelTextures.size(); i++) {
//            levelTextures.get(i).dispose();
//        }
//        levelTextures.clear();
//        levelTextureNames.clear();
//        levelTexturesLoaded.clear();
    }

}
