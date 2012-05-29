package com.tojosebe.vulfen.configuration;

import java.util.ArrayList;
import java.util.List;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.game.Pong;
import com.tojosebe.vulfen.game.BonusItem.BonusItemType;
import com.vulfox.math.Vector2f;

public class Level5 extends Level {

	private int mWidth;
	private int mHeight;
	private int mLives = 4;
	private float mPenguinSize = 70;
	private float mCowSize = 85;

	public Level5(int levelNumber, int width, int height, float scale, int world) {
		super(levelNumber, 9000, 8000, 7000, scale, world);
		mWidth = width;
		mHeight = height;
		createLevelConfig();
		setBonusItemSequence(new BonusItemType[] { BonusItemType.GROWER,
				BonusItemType.SHRINKER });
		setBonusItemsPerRound(100);
		setBonusItemPropability(1);
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
		enemy3.setPosition(new Vector2f(mWidth * 0.3f, mHeight * 0.6f));

		Pong enemy4 = new Pong();
		enemy4.setImageResource(R.drawable.sebe);
		enemy4.setHeight(mCowSize);
		enemy4.setWidth(mCowSize);
		enemy4.setPosition(new Vector2f(mWidth * 0.5f, mHeight * 0.6f));

		Pong enemy5 = new Pong();
		enemy5.setImageResource(R.drawable.sebe);
		enemy5.setHeight(mCowSize);
		enemy5.setWidth(mCowSize);
		enemy5.setPosition(new Vector2f(mWidth * 0.7f, mHeight * 0.6f));
		
		Pong enemy6 = new Pong();
		enemy6.setImageResource(R.drawable.sebe);
		enemy6.setHeight(mCowSize);
		enemy6.setWidth(mCowSize);
		enemy6.setPosition(new Vector2f(mWidth - mCowSize, mCowSize));

		
		Pong enemy7 = new Pong();
		enemy7.setImageResource(R.drawable.sebe);
		enemy7.setHeight(mCowSize);
		enemy7.setWidth(mCowSize);
		enemy7.setPosition(new Vector2f(mCowSize, mCowSize));


		enemies.add(enemy1);
		enemies.add(enemy2);
		enemies.add(enemy3);
		enemies.add(enemy4);
		enemies.add(enemy5);
		enemies.add(enemy6);
		enemies.add(enemy7);

		return enemies;
	}

}
