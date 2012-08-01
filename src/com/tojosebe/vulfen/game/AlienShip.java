package com.tojosebe.vulfen.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.tojosebe.vulfen.component.SpriteComponent;
import com.vulfox.math.Vector2f;
import com.vulfox.util.Vector2fPool;

public class AlienShip extends SpriteComponent {

	private Vector2f velocity;
	private boolean moving = false;
	private float radius;
	private Vector2f startPos;
	private int screenWidth;
	
	public AlienShip(Bitmap bitmap, boolean antialias,
			boolean animateFromLarge, boolean animateFromSmall,
			int animationTimeMillis, float x, float y, float width, float height, float scale, int screenWidth) {
		super(bitmap, antialias, animateFromLarge, animateFromSmall,
				animationTimeMillis, x, y, width, height);
		velocity = new Vector2f(50*scale, 0);
		radius = width*0.5f;
		startPos = new Vector2f(getPosition());
		this.screenWidth = screenWidth;
	}

	public void shoot(float x, float y) {
		
	}
	
	public void reset() {
		//TODO: reset positions here.
		moving = false;
		setPosition(startPos);
	}
	
	public void start() {
		moving = true;
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (moving || isFadingOut()) {
			super.draw(canvas);
		}
	}
	
	@Override
	public void update(float timeStep) {
		super.update(timeStep);
		
		
		if (moving) {
			//update position
			Vector2f positionDelta = Vector2fPool.getInstance().aquire();
			positionDelta.set(velocity);
			positionDelta.mulT(timeStep);
	
			getPosition().addT(positionDelta);
	
			Vector2fPool.getInstance().release(positionDelta);
			
			if (isOutSideScreen()) {
				reset();
			}
		}
	}
	
	private boolean isOutSideScreen() {
		boolean outside = false;
		if (getPosition().getX() - getWidth() *0.5f >= screenWidth) {
			outside = true;
		}
		return outside;
	}
	
	public void fadeOut() {
		super.fadeOut(10000);
		moving = false;
	}

	/**
	 * @return the started
	 */
	public boolean isMoving() {
		return moving;
	}

	/**
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}
	
	
	
}
