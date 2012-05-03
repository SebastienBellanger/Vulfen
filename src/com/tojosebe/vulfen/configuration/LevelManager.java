package com.tojosebe.vulfen.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all levels and their properties.
 * 
 */
public class LevelManager {

	List<World> worlds;

	private int mWidth;
	private int mHeight;
	
	private static LevelManager instance; 
	
	public static LevelManager getInstance(int width, int height) {
		if (instance == null) {
			instance = new LevelManager(width, height);
		}
		return instance;
	}

	private LevelManager(int width, int height) {
		worlds = new ArrayList<World>();
		mWidth = width;
		mHeight = height;
		init();
	}

	private void init() {
		addWorld1Levels();
	}

	private void addWorld1Levels() {
		World world1 = new World();
		Level level1 = new Level1(1, mWidth, mHeight);
		Level level2 = new Level2(2, mWidth, mHeight);
		Level level3 = new Level3(3, mWidth, mHeight);
		world1.addLevel(level1);
		world1.addLevel(level2);
		world1.addLevel(level3);
		worlds.add(world1);
	}

	public int getNumberOfWorlds() {
		return worlds.size();
	}
	
	public Level getLevel(int worldNum, int levelNum) {
		Level level = worlds.get(worldNum-1).getLevels().get(levelNum-1);
		Level copy = level.getCopy();
		return copy;
	}

}
