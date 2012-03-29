package com.tojosebe.vulfen.game;

public class Vector2f {

	private float mX;
	private float mY;
	
	public Vector2f()
	{
		mX = 0.0f;
		mY = 0.0f;
	}
	
	public Vector2f(float x, float y)
	{
		mX = x;
		mY = y;
	}
	
	public Vector2f(Vector2f other)
	{
		mX = other.mX;
		mY = other.mY;
	}
	
	public Vector2f copy()
	{
		Vector2f poolVector2f = Vector2fPool.getInstance().aquire();
		poolVector2f.set(mX, mY);
		return poolVector2f;
	}
	
	public float getX()
	{
		return mX;
	}
	
	public float getY()
	{
		return mY;
	}
	
	public void setX(float x)
	{
		mX = x;
	}
	
	public void setY(float y)
	{
		mY = y;
	}
	
	public void set(float x, float y)
	{
		mX = x;
		mY = y;
	}
	
	public void set(Vector2f other)
	{
		mX = other.mX;
		mY = other.mY;
	}
	
	public void setLength(float length)
	{
		normalizeT();
		mulT(length);
	}
	
	public float getLength()
	{
		return (float)Math.sqrt(mX * mX + mY * mY);
	}
	
	public float getLengthSquared()
	{
		return mX * mX + mY * mY;
	}
	
	public Vector2f normalize()
	{
		Vector2f poolVector2f = Vector2fPool.getInstance().aquire();
		poolVector2f.set(this);
		poolVector2f.normalizeT();
		return poolVector2f;	
	}
	
	public void normalizeT()
	{
		float length = getLength();
		divT(length);
	}
	
	public float dot(Vector2f other)
	{
		return mX * other.mX + mY * other.mY;
	}
	
	public Vector2f inv()
	{
		Vector2f poolVector2f = Vector2fPool.getInstance().aquire();
		poolVector2f.set(-mX, -mY);
		return poolVector2f;		
	}
	
	public void invT()
	{
		mX = -mX;
		mY = -mY;
	}
	
	public Vector2f mul(float scalar)
	{
		Vector2f poolVector2f = Vector2fPool.getInstance().aquire();
		poolVector2f.set(mX * scalar, mY * scalar);
		return poolVector2f;	
	}
	
	public Vector2f div(float scalar)
	{
		if(scalar == 0.0)
		{
			Vector2f poolVector2f = Vector2fPool.getInstance().aquire();
			poolVector2f.set(this);
			return poolVector2f;				
		}
		
		Vector2f poolVector2f = Vector2fPool.getInstance().aquire();
		poolVector2f.set(mX / scalar, mY / scalar);
		return poolVector2f;
	}
	
	public Vector2f add(Vector2f other)
	{
		Vector2f poolVector2f = Vector2fPool.getInstance().aquire();
		poolVector2f.set(mX + other.mX, mY + other.mY);
		return poolVector2f;
	}
	
	public Vector2f sub(Vector2f other)
	{
		Vector2f poolVector2f = Vector2fPool.getInstance().aquire();
		poolVector2f.set(mX - other.mX, mY - other.mY);
		return poolVector2f;
	}
	
	public void mulT(float scalar)
	{
		mX *= scalar;
		mY *= scalar;
	}
	
	public void divT(float scalar)
	{
		if(scalar == 0.0)
			return;
		
		mX /= scalar;
		mY /= scalar;
	}
	
	public void addT(Vector2f other)
	{
		mX += other.mX;
		mY += other.mY;
	}
	
	public void subT(Vector2f other)
	{
		mX -= other.mX;
		mY -= other.mY;
	}
}
