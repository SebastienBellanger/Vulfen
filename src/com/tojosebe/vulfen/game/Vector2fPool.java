package com.tojosebe.vulfen.game;

public class Vector2fPool extends ObjectPool<Vector2f> {

	// Singleton implementation

	/**
	 * Static instance
	 */
	private static Vector2fPool mInstance = null;

	/**
	 * Get the static instance of the class
	 * @return The static instance
	 */
	public static Vector2fPool getInstance() {
		if (mInstance == null) {
			mInstance = new Vector2fPool();
		}

		return mInstance;
	}
	
	/**
	 * Protected constructor
	 */
	protected Vector2fPool() {

	}

	@Override
	protected Vector2f newInstance() {
		return new Vector2f();
	}

	@Override
	protected void resetObject(Vector2f poolObject) {
		poolObject.set(0.0f, 0.0f);
	}

}
