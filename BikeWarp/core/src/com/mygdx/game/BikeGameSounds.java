package com.mygdx.game;
// Free sounds from http://soundbible.com/

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

public class BikeGameSounds {
	private static Sound sound;
	private static ArrayList<Sound> sounds;
	private static ArrayList<String> soundNames;

	public static void InitiateSounds() {
		// Initiate the arrays
		sounds = new ArrayList<Sound>();
		soundNames = new ArrayList<String>();
		// Load all sounds
		GetGameSounds();
	}

	public static Sound LoadBikeIdle() {
		return Gdx.audio.newSound(Gdx.files.internal("data/sounds/bike_idle.wav"));
	}

	public static Sound LoadBikeMove() {
		return Gdx.audio.newSound(Gdx.files.internal("data/sounds/bike_move.wav"));
	}

	public static Music LoadWaterfall() {
		return Gdx.audio.newMusic(Gdx.files.internal("data/sounds/waterfall.wav"));
	}

	public static Music LoadRain() {
		return Gdx.audio.newMusic(Gdx.files.internal("data/sounds/rainstorm.wav"));
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
	
	public static void PlaySound(int index, float volume) {
		sounds.get(index).play(volume);
	}

	public static void dispose() {
		sound.dispose();
		for (int i=0; i<sounds.size(); i++) {
			sounds.get(i).dispose();
		}
		sounds.clear();
		soundNames.clear();
	}

}
