package com.tojosebe.vulfen;

import com.vulfox.math.Vector2f;
import com.vulfox.util.Vector2fPool;

public class Pong {

	public float mass;
	
	public float radius;
	
	public Vector2f position;
	
	public Vector2f velocity;
	
	public Vector2f force;
	
	public void update(float timeStep)
	{
		Vector2f velocityDelta = Vector2fPool.getInstance().aquire();
		Vector2f positionDelta = Vector2fPool.getInstance().aquire();
		
		force.div(mass, velocityDelta);
		velocityDelta.mulT(timeStep);
		
		velocity.addT(velocityDelta);
		positionDelta.set(velocity);
		positionDelta.mulT(timeStep);
		
		position.addT(positionDelta);
		
		Vector2fPool.getInstance().release(velocityDelta);
		Vector2fPool.getInstance().release(positionDelta);
	}
	
}
