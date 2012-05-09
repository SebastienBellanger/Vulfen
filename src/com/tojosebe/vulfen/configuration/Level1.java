package com.tojosebe.vulfen.configuration;

import java.util.ArrayList;
import java.util.List;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.game.BowlConfiguration;
import com.tojosebe.vulfen.game.Pong;
import com.vulfox.math.Vector2f;

public class Level1 extends Level {

	private int mWidth;
	private int mHeight;
	private int mLives = 2;
	private float mPenguinSize = 70;
	private float mCowSize = 85;

	public Level1(int levelNumber, int width, int height, float scale, int world) {
		super(levelNumber, 4000, 2500, 1000, scale, world);
		mWidth = width;
		mHeight = height;
		createLevelConfig();
		createPengiunGameConfiguration();
		createPengiunGameConfiguration();
		setBonusItemSequence(null); // No bonus items for first level
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
		enemy1.setPosition(new Vector2f(mWidth * 0.5f, mHeight * 0.5f));
		enemies.add(enemy1);

		return enemies;
	}

}
