package com.tojosebe.vulfen.game;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.tojosebe.vulfen.component.SpriteComponent;
import com.vulfox.math.Vector2f;
import com.vulfox.util.Logger;
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
	private boolean comeFromLeft = false;
	private Random rand = new Random(System.currentTimeMillis());
	
	public AlienShip(Bitmap bitmap, boolean antialias,
			boolean animateFromLarge, boolean animateFromSmall,
			int animationTimeMillis, float x, float y, float width, float height, float scale, int screenWidth) {
		super(bitmap, antialias, animateFromLarge, animateFromSmall,
				animationTimeMillis, x, y, width, height);
		mScale = scale;
		comeFromLeft = rand.nextBoolean();
		if (comeFromLeft) {
			velocity = new Vector2f(100 * scale, 0);
			startPos = new Vector2f(getPosition());
		} else {
			velocity = new Vector2f(-100 * scale, 0);
			startPos = new Vector2f(getPosition());
		}
		radius = 0.8f*width*0.5f;
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
		moving = false;
		
		comeFromLeft = rand.nextBoolean(); 
		
		Vector2f newStartPos = new Vector2f(startPos);
		if (comeFromLeft) {
			velocity = new Vector2f(100 * mScale, 0);
		} else {
			velocity = new Vector2f(-100 * mScale, 0);
			newStartPos.setX(Math.abs(newStartPos.getX()) + screenWidth);
		}
		setPosition(newStartPos);
		
		Log.d("BAPELSIN", "newStartPos:" + newStartPos.toString() + comeFromLeft);
		
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
					canvas.drawLine(pongToShoot.getPosition().getX(), pongToShoot.getPosition().getY(), getPosition().getX(), getPosition().getY() + 20 * mScale, shootPaint);
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
		
				if (comeFromLeft) {
					if (shotsLeft > 0 && getPosition().getX() > screenWidth * 0.5f) {
						canShoot = true;
					}
				} else {
					if (shotsLeft > 0 && getPosition().getX() < screenWidth * 0.5f) {
						canShoot = true;
					}
				}
		
				Vector2fPool.getInstance().release(positionDelta);
	
			}
			
		}
	}
	
	private boolean isOutSideScreen() {
		boolean outside = false;
		if (comeFromLeft) {
			if (getPosition().getX() - getWidth() *0.5f >= screenWidth) {
				outside = true;
			}
		} else {
			if (getPosition().getX() <= - getWidth() *0.5f) {
				outside = true;
			}
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
