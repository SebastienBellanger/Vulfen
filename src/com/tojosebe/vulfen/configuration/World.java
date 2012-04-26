package com.tojosebe.vulfen.configuration;

import java.util.ArrayList;
import java.util.List;

public class World {

	List<Level> levels;

	/**
	 * @return the levels
	 */
	public List<Level> getLevels() {
		return levels;
	}

	/**
	 * @param levels
	 *            the levels to set
	 */
	public void setLevels(List<Level> levels) {
		this.levels = levels;
	}

	public void addLevel(Level level1) {
		if (this.levels == null) {
			this.levels = new ArrayList<Level>();
		}
		this.levels.add(level1);
	}
}
