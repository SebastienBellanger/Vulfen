package com.tojosebe.vulfen.configuration;

import java.util.List;

import com.tojosebe.vulfen.game.BowlConfiguration;
import com.tojosebe.vulfen.game.Pong;

/**
 * This class represents a level. It holds all properties that is needed for
 * creating a unique level.
 * 
 */
public class Level {

	private int mLevelNumber;
	
	private BowlConfiguration mBowlConfiguration;

	private List<Pong> mEnemies;
	
	private Pong mPenguin;
	
	public Level(int levelNumber) {
		mLevelNumber = levelNumber;
	}
	
	/**
	 * @return the bowlConfiguration
	 */
	public BowlConfiguration getBowlConfiguration() {
		return mBowlConfiguration;
	}

	/**
	 * @param bowlConfiguration the bowlConfiguration to set
	 */
	public void setBowlConfiguration(BowlConfiguration bowlConfiguration) {
		this.mBowlConfiguration = bowlConfiguration;
	}

	/**
	 * @return the enemies
	 */
	public List<Pong> getEnemies() {
		return mEnemies;
	}

	/**
	 * @param enemies the enemies to set
	 */
	public void setEnemies(List<Pong> enemies) {
		this.mEnemies = enemies;
	}

	/**
	 * @return the penguin
	 */
	public Pong getPenguin() {
		return mPenguin;
	}

	/**
	 * @param penguin the penguin to set
	 */
	public void setPenguin(Pong penguin) {
		this.mPenguin = penguin;
	}

	/**
	 * @return the mLevelNumber
	 */
	public int getLevelNumber() {
		return mLevelNumber;
	}
}
