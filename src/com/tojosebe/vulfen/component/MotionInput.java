package com.tojosebe.vulfen.component;

import android.view.MotionEvent;

/**
 * Holds data from a motion event as the motionEvent seems to be reused by the system
 */
public class MotionInput {
	
	private int mAction;	
	private float mX;
	private float mY;
	
	public void clear()
	{
		mAction = 0;
		mX = 0;
		mY = 0;
	}
	
	public void set(int action, float x, float y)
	{
		mAction = action;
		mX = x;
		mY = y;
	}
	
	public void set(MotionEvent motionEvent) {
		mAction = motionEvent.getAction();
		mX = motionEvent.getX();
		mY = motionEvent.getY();
	}
	
	public int getAction()
	{
		return mAction;
	}
	
	public float getX()
	{
		return mX;
	}
	
	public float getY()
	{
		return mY;
	}
}
