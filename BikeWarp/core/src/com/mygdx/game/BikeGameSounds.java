package com.mygdx.game;
// Free sounds from http://soundbible.com/

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class BikeGameSounds {
    private static Sound sound;
    public static Music rainSound, waterfallSound, windSound;
    private static ArrayList<Sound> sounds;
    private static ArrayList<String> soundNames;
    private static int menuSwitch, menuSelect;

    public static void InitiateSounds() {
        // Initiate the arrays
        sounds = new ArrayList<Sound>();
        soundNames = new ArrayList<String>();
        // Load all sounds
        GetGameSounds();
        // Load all music
        GetGameMusic();
        // Load the menu switch sound
        menuSwitch = GetSoundIndex("bike_switch");
        menuSelect = GetSoundIndex("finish");
    }

    public static Sound LoadBikeIdle() {
        return Gdx.audio.newSound(Gdx.files.internal("data/sounds/bike_idle.wav"));
    }

    public static Sound LoadBikeMove() {
        return Gdx.audio.newSound(Gdx.files.internal("data/sounds/bike_move.wav"));
    }

    public static Music LoadWaterfall() { return Gdx.audio.newMusic(Gdx.files.internal("data/sounds/waterfall.wav")); }

    public static Music LoadRain() {
        return Gdx.audio.newMusic(Gdx.files.internal("data/sounds/rainstorm.wav"));
    }

    public static Music LoadWind() {
        return Gdx.audio.newMusic(Gdx.files.internal("data/sounds/wind.wav"));
    }

    private static void GetGameMusic() {
        // If you add new ones, don't forget to dispose them!
        rainSound = LoadRain();
        waterfallSound = LoadWaterfall();
        windSound = LoadWind();
    }

    private static void GetSound (String file) {
        sound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/"+file+".mp3"));
        sounds.add(sound);
        soundNames.add(file);
    }

    private static void GetGameSounds() {
        GetSound("bike_switch");
        GetSound("bike_crash");
        GetSound("gem_collect");
        GetSound("diamond_collect");
        GetSound("nitrous");
        GetSound("key_collect");
        GetSound("gravity");
        GetSound("door");
        GetSound("switch");
        GetSound("transport");
        GetSound("finish");
        GetSound("collide");
    }

    public static int GetSoundIndex(String file) {
        for (int i=0; i<sounds.size(); i++) {
            if (file.equals(soundNames.get(i))) return i;
        }
        return -1;
    }

    public static void PlayMenuSwitch() {
        long noID = sounds.get(menuSwitch).play(0.1f);
    }

    public static void PlayMenuSelect() {
        long noID = sounds.get(menuSelect).play(0.5f);
    }

    public static void PlaySound(int index, float volume) {
        long noID = sounds.get(index).play(volume);
    }

    public static void StopSound(int index) {
        sounds.get(index).stop();
    }

    public static void StopAllSounds() {
        //if (soundBikeIdle != null) soundBikeIdle.setLooping(soundIDBikeIdle, false);
        if (waterfallSound != null) {
            waterfallSound.setLooping(false);
            waterfallSound.setVolume(0.0f);
        }
        if (rainSound != null) {
            rainSound.setLooping(false);
            rainSound.setVolume(0.0f);
        }
        if (windSound != null) {
            windSound.setLooping(false);
            windSound.setVolume(0.0f);
        }
    }

    public static void StartAllSounds() {
        // Start the waterfall music
        waterfallSound.setLooping(true);
        waterfallSound.setVolume(0.0f);
        waterfallSound.play();
        // Start the rain music
        rainSound.setLooping(true);
        rainSound.setVolume(0.0f);
        rainSound.play();
        // Start the wind music
        windSound.setLooping(true);
        windSound.setVolume(0.0f);
        windSound.play();
    }

    public static void dispose() {
        sound.dispose();
        for (int i=0; i<sounds.size(); i++) {
            sounds.get(i).dispose();
        }
        sounds.clear();
        soundNames.clear();
        // Dispose the music
        rainSound.dispose();
        waterfallSound.dispose();
        windSound.dispose();
    }

}
