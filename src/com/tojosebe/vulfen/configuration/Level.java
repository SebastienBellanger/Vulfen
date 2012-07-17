package com.tojosebe.vulfen.configuration;

import java.util.ArrayList;
import java.util.List;

import com.tojosebe.vulfen.game.BonusItem.BonusItemType;
import com.tojosebe.vulfen.game.Brick;
import com.tojosebe.vulfen.game.Pong;

/**
 * This class represents a level. It holds all properties that is needed for
 * creating a unique level.
 * 
 */
public class Level {

	private int mLevelNumber;
	private int mWorldNumber;
	
	private int mThreeStarsScore;
	private int mTwoStarsScore;
	private int mOneStarScore;
	
	private int mLives;
	
	private BowlConfiguration mBowlConfiguration;

	private List<Pong> mEnemies;
	
	private List<Brick> mBricks;
	
	private Pong mPenguin;
	
	private BonusItemType[] mBonusItemSequence = null;
	
	private int mBonusItemsPerRound = 1;
	
	private int mBonusItemPropability = 5; //1 in 5 i.e 20% chance each second.
	
	public Level() {
		mBowlConfiguration = new BowlConfiguration();
	}

	/**
	 * @return the bowlConfiguration
	 */
	public BowlConfiguration getBowlConfiguration() {
		return mBowlConfiguration;
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
	public void addEnemy(Pong enemy) {
		if (mEnemies == null) {
			mEnemies = new ArrayList<Pong>();
		}
		mEnemies.add(enemy);
	}
	
	/**
	 * @return the bricks
	 */
	public List<Brick> getBricks() {
		return mBricks;
	}

	/**
	 * @param brick the brick to set
	 */
	public void addBrick(Brick brick) {
		if (mBricks == null) {
			mBricks = new ArrayList<Brick>();
		}
		mBricks.add(brick);
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

	public int getThreeStarsScore() {
		return mThreeStarsScore;
	}

	public int getTwoStarsScore() {
		return mTwoStarsScore;
	}

	public int getOneStarScore() {
		return mOneStarScore;
	}
	
	public int setThreeStarsScore(int score) {
		return mThreeStarsScore = score;
	}

	public int setTwoStarsScore(int score) {
		return mTwoStarsScore = score;
	}

	public int setOneStarScore(int score) {
		return mOneStarScore = score;
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

	/**
	 * @return the bonusItemsPerRound
	 */
	public int getBonusItemsPerRound() {
		return mBonusItemsPerRound;
	}

	/**
	 * @param bonusItemsPerRound the bonusItemsPerRound to set
	 */
	public void setBonusItemsPerRound(int bonusItemsPerRound) {
		this.mBonusItemsPerRound = bonusItemsPerRound;
	}

	/**
	 * @return the bonusItemPropability
	 */
	public int getBonusItemPropability() {
		return mBonusItemPropability;
	}

	/**
	 * @param bonusItemPropability the bonusItemPropability to set
	 */
	public void setBonusItemPropability(int bonusItemPropability) {
		this.mBonusItemPropability = bonusItemPropability;
	}

	public void setBonusItemSequence(String sequence) {
		
		if (sequence == null || sequence.length() == 0) {
			return;
		}
		
		BonusItemType[] bonusItemSequence = new BonusItemType[sequence.length()];
		
		for (int i = 0; i < sequence.length(); i++) {
			String current = "" + sequence.charAt(i);
			if (current.equals("G")) {
				bonusItemSequence[i] = BonusItemType.GROWER;
			} else if (current.equals("S")) {
				bonusItemSequence[i] = BonusItemType.SHRINKER;
			} else if (current.equals("R")) {
				bonusItemSequence[i] = BonusItemType.BRICK_SMASHER;
			} else if (current.equals("L")) {
				bonusItemSequence[i] = BonusItemType.LIGHTNING;
			} else if (current.equals("D")) {
				bonusItemSequence[i] = BonusItemType.SPLITTER;
			} else if (current.equals("E")) {
				bonusItemSequence[i] = BonusItemType.EXPLODER;
			} else if (current.equals("U")) {
				bonusItemSequence[i] = BonusItemType.EXTRA_PONG;
			} 
		}
		
		setBonusItemSequence(bonusItemSequence);
		
	}

	/**
	 * @return the Lives
	 */
	public int getLives() {
		return mLives;
	}

	/**
	 * @param mLives the mLives to set
	 */
	public void setLives(int lives) {
		this.mLives = lives;
	}

	/**
	 * @param mLevelNumber the mLevelNumber to set
	 */
	public void setLevelNumber(int levelNumber) {
		this.mLevelNumber = levelNumber;
	}

}
