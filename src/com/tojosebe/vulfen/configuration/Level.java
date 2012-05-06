package com.tojosebe.vulfen.configuration;

import java.util.ArrayList;
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
	
	private int mThreeStarsScore;
	private int mTwoStarsScore;
	private int mOneStarScore;
	
	private BowlConfiguration mBowlConfiguration;

	private List<Pong> mEnemies;
	
	private Pong mPenguin;
	
	public Level(int levelNumber, int threeStarsScore, int twoStarsScore, int oneStarScore) {
		mLevelNumber = levelNumber;
		mThreeStarsScore = threeStarsScore;
		mTwoStarsScore = twoStarsScore;
		mOneStarScore = oneStarScore;
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

	public Level getCopy() {
		Level levelCopy = new Level(mLevelNumber, mThreeStarsScore, mTwoStarsScore, mOneStarScore);
		levelCopy.mBowlConfiguration = new BowlConfiguration(mBowlConfiguration);
		levelCopy.mEnemies = new ArrayList<Pong>();
		for (Pong pong : mEnemies) {
			Pong pongCopy = new Pong(pong);
			levelCopy.mEnemies.add(pongCopy);
		}
		levelCopy.mPenguin = new Pong(mPenguin);
		return levelCopy;
	
	}

	public int getThreeStarsScore() {
		return mThreeStarsScore;
	}

	public int getTwoStarsScore() {
		return mTwoStarsScore;
	}

	public int getOneStarScore() {
		return mOneStarScore;
	}
}
