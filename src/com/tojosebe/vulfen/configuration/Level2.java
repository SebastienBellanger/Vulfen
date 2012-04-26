package com.tojosebe.vulfen.configuration;

import java.util.ArrayList;
import java.util.List;

import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.game.BowlConfiguration;
import com.tojosebe.vulfen.game.Pong;
import com.vulfox.math.Vector2f;

public class Level2 extends Level {

	private int mWidth;
	private int mHeight;

	public Level2(int levelNumber, int width, int height) {
		super(levelNumber);
		mWidth = width;
		mHeight = height;
		createLevelConfig();
		createPengiunGameConfiguration();
		createPengiunGameConfiguration();
	}

	private void createLevelConfig() {
		setBowlConfiguration(new BowlConfiguration()); // Default values.
		setEnemies(createEnemiesGameConfiguration());
		setPenguin(createPengiunGameConfiguration());
	}

	private Pong createPengiunGameConfiguration() {
		Pong penguin = new Pong();
		penguin.setImageResource(R.drawable.tojo);

		penguin.setHeight(70);
		penguin.setWidth(70);
		penguin.setPosition(new Vector2f(mWidth * 0.5f, mHeight * 0.9f));
		
		return penguin;
	}

	private List<Pong> createEnemiesGameConfiguration() {
		List<Pong> enemies = new ArrayList<Pong>();
		
		Pong enemy1 = new Pong();
		enemy1.setImageResource(R.drawable.sebe);
		enemy1.setHeight(100);
		enemy1.setWidth(100);
		enemy1.setPosition(new Vector2f(mWidth * 0.5f, mHeight * 0.3f));
		
		Pong enemy2 = new Pong();
		enemy2.setImageResource(R.drawable.sebe);
		enemy2.setHeight(100);
		enemy2.setWidth(100);
		enemy2.setPosition(new Vector2f(mWidth * 0.5f, mHeight * 0.5f));
		
		Pong enemy3 = new Pong();
		enemy3.setImageResource(R.drawable.sebe);
		enemy3.setHeight(100);
		enemy3.setWidth(100);
		enemy3.setPosition(new Vector2f(mWidth * 0.5f, mHeight * 0.7f));
		
		enemies.add(enemy1);
		enemies.add(enemy2);
		enemies.add(enemy3);

		return enemies;
	}

}

