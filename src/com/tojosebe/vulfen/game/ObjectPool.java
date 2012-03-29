package com.tojosebe.vulfen.game;

import java.util.LinkedList;

public abstract class ObjectPool<E> {

	private LinkedList<E> mPoolObjects = new LinkedList<E>();
	
	protected abstract E newInstance();
	
	protected abstract void resetObject(E poolObject);
	
	public E aquire()
	{
		if(mPoolObjects.size() == 0)
		{
			return newInstance();
		}
		
		return mPoolObjects.removeFirst();
	}
	
	public void release(E poolObject)
	{		
		resetObject(poolObject);
		mPoolObjects.addLast(poolObject);
	}
}
