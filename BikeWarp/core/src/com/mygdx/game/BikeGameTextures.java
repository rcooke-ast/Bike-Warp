package com.mygdx.game;
// Some free textures available from:
// https://www.pxfuel.com/en/search?q=seamless+texture
// https://www.wildtextures.com/category/free-textures/

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

    public static void InitiateTextures() {
        // Initiate the arrays
        textures = new ArrayList<Texture>();
        textureNames = new ArrayList<String>();
        menuTextures = new ArrayList<Texture>();
        menuTextureNames = new ArrayList<String>();
        decorTextures = new ArrayList<Texture>();
        decorTextureNames = new ArrayList<String>();
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

    private static void GetTexture (String file, int flag) {
        texture = new Texture(Gdx.files.internal("data/images/"+file+".png"));
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
        GetTexture("metal_pole_1x16",0);
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
        // Add some additional textures
        texture = new Texture(Gdx.files.internal("data/images/metal_grid.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        menuTextures.add(texture);
        menuTextureNames.add("metal_grid");
    }

    private static void GetDecorations() {
        GetTexture("grass_smooth",2);
        GetTexture("ground_bubbles",2);
        GetTexture("ground_cracked",2);
        GetTexture("ground_gravel",2);
        GetTexture("ground_mars",2);
        GetTexture("ground_moon",2);
        GetTexture("background_mountains",2);
        GetTexture("background_space",2);
        GetTexture("background_waterfall",2);
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
        GetTexture("RS_bumps",2);
        GetTexture("RS_dash",2);
        GetTexture("RS_dot",2);
        GetTexture("RS_donotenter",2);
        GetTexture("RS_exclamation",2);
        GetTexture("RS_motorbike",2);
        GetTexture("RS_nomotorbike",2);
        GetTexture("RS_rampahead",2);
        GetTexture("RS_reducespeed",2);
        GetTexture("RS_speed_10",2);
        GetTexture("RS_speed_20",2);
        GetTexture("RS_speed_30",2);
        GetTexture("RS_speed_40",2);
        GetTexture("RS_speed_50",2);
        GetTexture("RS_speed_60",2);
        GetTexture("RS_speed_80",2);
        GetTexture("RS_speed_100",2);
        GetTexture("RS_stop",2);
        GetTexture("metal_pole_1x16",2);
        GetTexture("binbag",2);
        for (int i=0; i<8; i++) GetTexture("rock_"+String.format("%02d", i), 2);
        for (int i=0; i<8; i++) GetTexture("tree_"+String.format("%02d", i), 2);
        for (int i=0; i<12; i++) GetTexture("tyrestack_"+String.format("%02d", i), 2);
        GetTexture("error",2);
        // Add some additional textures
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
        }
        return null;
    }

    public static void dispose () {
        for (int i=0; i<textures.size(); i++) {
            textures.get(i).dispose();
        }
        textures.clear();
        textureNames.clear();
        for (int i=0; i<menuTextures.size(); i++) {
            menuTextures.get(i).dispose();
        }
        menuTextures.clear();
        menuTextureNames.clear();
        for (int i=0; i<decorTextures.size(); i++) {
            decorTextures.get(i).dispose();
        }
        decorTextures.clear();
        decorTextureNames.clear();
    }

}
