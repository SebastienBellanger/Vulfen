package com.tojosebe.vulfen.configuration;

import android.content.res.AssetManager;

public class World {

	
	Level[] mLevels;
	
	int mWorldNumber;
	
	public World(int amountOfLevels, int worldNumber) {
		mWorldNumber = worldNumber;
		mLevels = new Level[amountOfLevels];
	}

	public void addLevel(Level levelToAdd, int index) {
		mLevels[index] = levelToAdd;
	}
	
	public int getNumberOfLevels() {
		return mLevels.length;
	}
	
	public Level getLevel(int index, float scale, AssetManager mgr) {
		
		Level levelToReturn = null;
		if (mLevels[index] != null) {
			levelToReturn = mLevels[index];
		} else {
			levelToReturn = LevelManager.loadLevel(mWorldNumber, index, scale, mgr);
		}
		
		return levelToReturn;
	}
}
