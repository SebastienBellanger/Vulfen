package com.tojosebe.vulfen.configuration;

import java.util.ArrayList;
import java.util.List;

import com.tojosebe.vulfen.game.BowlConfiguration;
import com.tojosebe.vulfen.game.Pong;
import com.tojosebe.vulfen.game.BonusItem.BonusItemType;

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
	
	private float mScale;
	
	private int mWorldNumber;
	
	private BonusItemType[] mBonusItemSequence = null;
	
	public Level(int levelNumber, int threeStarsScore, int twoStarsScore, int oneStarScore, float scale, int worldNumber) {
		mLevelNumber = levelNumber;
		mThreeStarsScore = threeStarsScore;
		mTwoStarsScore = twoStarsScore;
		mOneStarScore = oneStarScore;
		mScale = scale;
		mWorldNumber = worldNumber;
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
		Level levelCopy = new Level(mLevelNumber, mThreeStarsScore, mTwoStarsScore, mOneStarScore, mScale, mWorldNumber);
		levelCopy.mBowlConfiguration = new BowlConfiguration(mBowlConfiguration);
		levelCopy.mEnemies = new ArrayList<Pong>();
		for (Pong pong : mEnemies) {
			Pong pongCopy = new Pong(pong);
			levelCopy.mEnemies.add(pongCopy);
		}
		levelCopy.mPenguin = new Pong(mPenguin);
		
		BonusItemType[] bonusSequense = null;
		if (mBonusItemSequence != null) {
			bonusSequense = new BonusItemType[mBonusItemSequence.length];
			for (int i = 0; i < mBonusItemSequence.length; i++) {
				bonusSequense[i] = mBonusItemSequence[i];
			}
		}
		levelCopy.mBonusItemSequence = bonusSequense;
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

	/**
	 * @return the bonusItemSequence
	 */
	public BonusItemType[] getBonusItemSequence() {
		return mBonusItemSequence;
	}

	/**
	 * @param bonusItemSequence the bonusItemSequence to set
	 */
	public void setBonusItemSequence(BonusItemType[] bonusItemSequence) {
		this.mBonusItemSequence = bonusItemSequence;
	}

	/**
	 * @return the scale
	 */
	public float getScale() {
		return mScale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(float scale) {
		this.mScale = scale;
	}

	/**
	 * @return the worldNumber
	 */
	public int getWorldNumber() {
		return mWorldNumber;
	}

	/**
	 * @param worldNumber the worldNumber to set
	 */
	public void setWorldNumber(int worldNumber) {
		this.mWorldNumber = worldNumber;
	}
}
