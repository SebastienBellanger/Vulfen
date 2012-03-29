package com.tojosebe.vulfen.game;

import java.util.HashMap;

public class IntStrings {

	// Singleton implementation

	/**
	 * Static instance
	 */
	private static IntStrings mInstance = null;

	/**
	 * Get the static instance of the class
	 * @return The static instance
	 */
	public static IntStrings getInstance() {
		if (mInstance == null) {
			mInstance = new IntStrings();
		}

		return mInstance;
	}
	
	/**
	 * Protected constructor
	 */
	protected IntStrings() {

	}
	
	private HashMap<Integer, String> mStrings = new HashMap<Integer, String>();
	
	public String getString(int value)
	{
		if(!mStrings.containsKey(value))
		{
			mStrings.put(value, Integer.toString(value));
		}
		
		return mStrings.get(value);
	}
}
