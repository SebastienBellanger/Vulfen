package com.tojosebe.vulfen.configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;
import android.util.Log;

import com.tojosebe.vulfen.game.Brick;
import com.tojosebe.vulfen.game.Pong;


/**
 * Holds all levels and their properties.
 * 
 */
public class LevelManager {

	private static List<World> worlds;

	private static int mWidth;
	private static int mHeight;
	private static int mPenguinSize = 70;
	
	private static boolean inited = false;
	
	
	public static void init(AssetManager mgr, int width, int height) {

		if (inited) {
			return;
		}
		
		mWidth = width;
		mHeight = height;
		
		//Find out how many worlds we have.
		//Find out how many levels for every world by counting files.
		//create world list.
		
		worlds = new ArrayList<World>();
		
		int numberOfWorlds = worldCount(mgr);
		
		for (int i = 0; i < numberOfWorlds; i++) {
			int levelCount = levelCount(mgr, i);
			World world = new World(levelCount, i);
			worlds.add(world);
		}
		
                
        inited = true;
    }
	
	public static void displayFiles(AssetManager mgr, String path) {
	    try {
	        String list[] = mgr.list(path);
	        if (list != null)
	            for (int i=0; i<list.length; ++i)
	                {
	                    Log.v("Assets:", path +"/"+ list[i]);
	                    displayFiles(mgr, path + "/" + list[i]);
	                }
	    } catch (IOException e) {
	        Log.v("List error:", "can't list" + path);
	    }

	}
	
	private static int worldCount(AssetManager mgr) {
		int count = 0;
		try {
	        String list[] = mgr.list("levels");
	        if (list != null) {
	        	count = list.length;
	        }
	    } catch (IOException e) {
	        Log.v("List error:", "can't list worlds");
	    }
		return count;
	}
	
	private static int levelCount(AssetManager mgr, int worldIndex) {
		int count = 0;
		try {
	        String list[] = mgr.list("levels/world" + (worldIndex + 1));
	        if (list != null) {
	        	count = list.length;
	        }
	    } catch (IOException e) {
	        Log.v("List error:", "can't list worlds");
	    }
		return count;
	}


	public static int getNumberOfWorlds() {
		return worlds.size();
	}
	
	public static Level getLevel(int worldNum, int levelNum, float scale, AssetManager mgr) {
		Level level = worlds.get(worldNum-1).getLevel(levelNum-1, scale, mgr);
//		Level copy = level.getCopy();
		return level;
	}

	public static Level loadLevel(int worldNumber, int levelNumber, float scale, AssetManager mgr) {
		
		Level level = null;
		
        try {
        	
        	InputStream is = mgr.open("levels/world" + (worldNumber+1) + "/" + (worldNumber+1) + "_" + (levelNumber+1));
        	BufferedReader br = new BufferedReader(new InputStreamReader(is));
        	String line = null;
            
            level = new Level();
            
            while ((line = br.readLine()) != null) {
            	line = new String(line.getBytes(),"UTF-8");
            	
                if (!line.trim().equals("") && ! line.startsWith("-")) {
    				parseLine(scale, level, line); 
                }
            }
            
            Pong penguin = new Pong(null, true, false, false, 0, mWidth * 0.5f, mHeight * 0.9f, mPenguinSize * scale, mPenguinSize * scale);
            penguin.setType(Pong.Type.PENGUIN);

    		level.setPenguin(penguin);
    		level.setLevelNumber(levelNumber);
    		
            worlds.get(worldNumber).addLevel(level, levelNumber);
            
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

		return level;
	}

	private static void parseLine(float scale, Level level, String line) {
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		
		if (line.contains("COW:")) {
			line = line.replace("COW:", "");
			String[] numbers = line.split(",");
			x = Integer.parseInt(numbers[0]);
			y = Integer.parseInt(numbers[1]);
			width = Integer.parseInt(numbers[2]);
			height = Integer.parseInt(numbers[3]);
			
			Pong pong = new Pong(null, true, false, true, 250, x * scale, y * scale, width * scale, height * scale);
			pong.setType(Pong.Type.COW);
			level.addEnemy(pong);
			
		} else if (line.contains("PIG:")) {
			line = line.replace("PIG:", "");
			String[] numbers = line.split(",");
			x = Integer.parseInt(numbers[0]);
			y = Integer.parseInt(numbers[1]);
			width = Integer.parseInt(numbers[2]);
			height = Integer.parseInt(numbers[3]);
//    					GameObject go = new GameObject(ClickableObject.PIG, this, x, y, width, height);
//    					gameObjects.add(go); TODO: add pigs.
		} else if (line.contains("BRICK1:")) {
			line = line.replace("BRICK1:", "");
			String[] numbers = line.split(",");
			x = Integer.parseInt(numbers[0]);
			y = Integer.parseInt(numbers[1]);
			width = Integer.parseInt(numbers[2]);
			height = Integer.parseInt(numbers[3]);
			Brick brick = new Brick(Brick.Type.SOFT, x, y, width, height, scale, null);
			level.addBrick(brick);
		} else if (line.contains("BRICK2:")) {
			line = line.replace("BRICK2:", "");
			String[] numbers = line.split(",");
			x = Integer.parseInt(numbers[0]);
			y = Integer.parseInt(numbers[1]);
			width = Integer.parseInt(numbers[2]);
			height = Integer.parseInt(numbers[3]);
			Brick brick = new Brick(Brick.Type.MEDIUM, x, y, width, height, scale, null);
			level.addBrick(brick);
		} else if (line.contains("BRICK3:")) {
			line = line.replace("BRICK3:", "");
			String[] numbers = line.split(",");
			x = Integer.parseInt(numbers[0]);
			y = Integer.parseInt(numbers[1]);
			width = Integer.parseInt(numbers[2]);
			height = Integer.parseInt(numbers[3]);
			Brick brick = new Brick(Brick.Type.HARD, x, y, width, height, scale, null);
			level.addBrick(brick);
		} else if (line.contains("FISHES:")) {
			line = line.replace("FISHES:", "");
			level.setBonusItemSequence(line);
		} else if (line.contains("LIVES:")) {
			line = line.replace("LIVES:", "");
			level.setLives(Integer.parseInt(line));
		}  else if (line.contains("STARS:")) {
			line = line.replace("STARS:", "");
			String[] numbers = line.split(",");
			int star1 = Integer.parseInt(numbers[0]);
			int star2 = Integer.parseInt(numbers[1]);
			int star3 = Integer.parseInt(numbers[2]);
			level.setThreeStarsScore(star3);
			level.setTwoStarsScore(star2);
			level.setOneStarScore(star1);
		}
	}

	public static int getNumberOfLevels(int worldIndex) {
		return worlds.get(worldIndex).getNumberOfLevels();
	}

}
