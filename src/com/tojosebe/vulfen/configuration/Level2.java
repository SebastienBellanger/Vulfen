package com.tojosebe.vulfen.configuration;

import java.util.ArrayList;
import java.util.List;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.game.Pong;
import com.tojosebe.vulfen.game.BonusItem.BonusItemType;
import com.vulfox.math.Vector2f;

public class Level2 extends Level {

	private int mWidth;
	private int mHeight;
	private int mLives = 3;
	private float mPenguinSize = 70;
	private float mCowSize = 85;

	public Level2(int levelNumber, int width, int height, float scale, int world) {
		super(levelNumber, 3000, 2000, -1000, scale, world);
		mWidth = width;
		mHeight = height;
		createLevelConfig();
		createPengiunGameConfiguration();
		createPengiunGameConfiguration();
		setBonusItemSequence(new BonusItemType[] { BonusItemType.GROWER,
				BonusItemType.SHRINKER });
	}

	private void createLevelConfig() {
		BowlConfiguration b = new BowlConfiguration();
		b.setLives(mLives);
		setBowlConfiguration(b);
		setEnemies(createEnemiesGameConfiguration());
		setPenguin(createPengiunGameConfiguration());
	}

	private Pong createPengiunGameConfiguration() {
		Pong penguin = new Pong();
		penguin.setImageResource(R.drawable.tojo);

		penguin.setHeight(mPenguinSize);
		penguin.setWidth(mPenguinSize);
		penguin.setPosition(new Vector2f(mWidth * 0.5f, mHeight * 0.9f));

		return penguin;
	}

	private List<Pong> createEnemiesGameConfiguration() {
		List<Pong> enemies = new ArrayList<Pong>();

		Pong enemy1 = new Pong();
		enemy1.setImageResource(R.drawable.sebe);
		enemy1.setHeight(mCowSize);
		enemy1.setWidth(mCowSize);
		enemy1.setPosition(new Vector2f(mWidth * 0.5f - mCowSize * getScale(),
				mHeight * 0.4f));

		Pong enemy2 = new Pong();
		enemy2.setImageResource(R.drawable.sebe);
		enemy2.setHeight(mCowSize);
		enemy2.setWidth(mCowSize);
		enemy2.setPosition(new Vector2f(mWidth * 0.5f + mCowSize * getScale(),
				mHeight * 0.4f));

		Pong enemy3 = new Pong();
		enemy3.setImageResource(R.drawable.sebe);
		enemy3.setHeight(mCowSize);
		enemy3.setWidth(mCowSize);
		enemy3.setPosition(new Vector2f(mWidth * 0.5f, mHeight * 0.6f));

		enemies.add(enemy1);
		enemies.add(enemy2);
		enemies.add(enemy3);

		return enemies;
	}

}
