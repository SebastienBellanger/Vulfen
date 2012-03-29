package com.tojosebe.vulfen.game;

public class StringBuilderPool extends ObjectPool<StringBuilder> {

	// Singleton implementation

	/**
	 * Static instance
	 */
	private static StringBuilderPool mInstance = null;

	/**
	 * Get the static instance of the class
	 * @return The static instance
	 */
	public static StringBuilderPool getInstance() {
		if (mInstance == null) {
			mInstance = new StringBuilderPool();
		}

		return mInstance;
	}
	
	/**
	 * Protected constructor
	 */
	protected StringBuilderPool() {

	}
	
	private int mInitialLength = 128;
	
	public int getInitialLength()
	{
		return mInitialLength;
	}
	
	public void setInitialLength(int initialLength)
	{
		if(initialLength < 0)
			return;
		
		mInitialLength = initialLength;
	}
	
	@Override
	protected StringBuilder newInstance() {
		return new StringBuilder(mInitialLength);
	}

	@Override
	protected void resetObject(StringBuilder poolObject) {
		poolObject.setLength(0);
	}

}
