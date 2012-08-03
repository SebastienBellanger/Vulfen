package com.tojosebe.vulfen.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.tojosebe.vulfen.component.SpriteComponent;
import com.vulfox.math.Vector2f;
import com.vulfox.util.Vector2fPool;

public class AlienShip extends SpriteComponent {

	private Vector2f velocity;
	private boolean moving;
	private float radius;
	private float mScale;
	private Vector2f startPos;
	private int screenWidth;
	private boolean canShoot;
	private boolean shooting;
	private int shotsLeft = 1;
	private Paint shootPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private boolean dead = false;
	private long mShootStartTime;
	private int mTotalShootTime = 300;
	private int mCanShootTime = 2000;
	private int color1 = 0xFFFF0000;
	private int color2 = 0xFF00FF00;
	private Pong pongToShoot;
	
	public AlienShip(Bitmap bitmap, boolean antialias,
			boolean animateFromLarge, boolean animateFromSmall,
			int animationTimeMillis, float x, float y, float width, float height, float scale, int screenWidth) {
		super(bitmap, antialias, animateFromLarge, animateFromSmall,
				animationTimeMillis, x, y, width, height);
		mScale = scale;
		velocity = new Vector2f(100*scale, 0);
		radius = 0.8f*width*0.5f;
		startPos = new Vector2f(getPosition());
		this.screenWidth = screenWidth;
		shootPaint.setColor(color1);
		shootPaint.setStrokeWidth(5*scale);
		shootPaint.setStyle(Paint.Style.STROKE); 
	}


	public void shoot(Pong pong) {
		shotsLeft--;
		if (mShootStartTime == 0) {
			pongToShoot = pong;
			shooting = true;
		}
		mShootStartTime = System.currentTimeMillis();
	}	
	
	public void reset() {
		//TODO: reset positions here.
		moving = false;
		setPosition(new Vector2f(startPos));
		canShoot = false;
		shotsLeft = 1;
		dead = false;
		mShootStartTime = 0;
		pongToShoot = null;
	}
	
	public void start() {
		if (!dead) {
			moving = true;
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (moving || isFadingOut()) {
			super.draw(canvas);
		}
		
		
		
		if (shooting) {
			long timeSinceStart = System.currentTimeMillis() - mShootStartTime;
			if (timeSinceStart > mTotalShootTime) {
				canShoot = false;
				shooting = false;
			} else {
				if (shootPaint.getColor() == color1) {
					shootPaint.setColor(color2);
				} else {
					shootPaint.setColor(color1);
				}
				if (pongToShoot != null) {
					canvas.drawLine(pongToShoot.getPosition().getX(), pongToShoot.getPosition().getY(), getPosition().getX(), getPosition().getY(), shootPaint);
				}
			}
			
		}
		
	}
	
	@Override
	public void update(float timeStep) {
		super.update(timeStep);
		
		
		if (moving) {

			if (isOutSideScreen()) {
				reset();
			} else {
				
				//update position
				Vector2f positionDelta = Vector2fPool.getInstance().aquire();
				positionDelta.set(velocity);
				positionDelta.mulT(timeStep);
		
				getPosition().addT(positionDelta);
		
				Vector2fPool.getInstance().release(positionDelta);
				
				if (shotsLeft > 0 && getPosition().getX() > screenWidth * 0.5f) {
					canShoot = true;
//					if (mCanShootStartTime == 0) {
//						
//						velocity.setX(0.0f);
//					} else {
//						long timeSinceStart = System.currentTimeMillis() - mCanShootStartTime;
//						if (timeSinceStart > mCanShootTime) {
//							velocity.setX(100*mScale);
//						}
//					}
				}
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

	/**
	 * @return the shooting
	 */
	public boolean canShoot() {
		return canShoot;
	}
	
	/**
	 * @return the shooting
	 */
	public boolean isShooting() {
		return shooting;
	}

	/**
	 * @return the dead
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * @param dead the dead to set
	 */
	public void setDead(boolean dead) {
		this.dead = dead;
		moving = false;
		setPosition(new Vector2f(startPos));
		canShoot = false;
		shotsLeft = 1;
		pongToShoot = null;
	}

	
}
