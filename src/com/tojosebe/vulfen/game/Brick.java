package com.tojosebe.vulfen.game;

import android.graphics.Bitmap;

import com.tojosebe.vulfen.component.SpriteComponent;
import com.vulfox.math.Vector2f;

/**
 * Corners:
 * 
 *        1-----------2
 *        |           |
 *        3-----------4
 */
public class Brick extends SpriteComponent {

	public enum Type {
		SOFT, MEDIUM, HARD
	};

	private Vector2f positionCorner1;
	private Vector2f positionCorner2;
	private Vector2f positionCorner3;
	private Vector2f positionCorner4;
	
	private Vector2f edgeUp;
	private Vector2f edgeRight;
	private Vector2f edgeDown;
	private Vector2f edgeLeft;
	
	private Type mType;

	public Brick(Type type, float x, float y, float width, float height,
			float scale, Bitmap bitmap) {
		super(bitmap, true, false, true, 200, x, y, width, height);
		mType = type;
		
		setWidth(width * scale);
		setHeight(height * scale);
		setPosition(new Vector2f(x * scale, y * scale));
		positionCorner1 = new Vector2f(getPosition().getX() - getWidth() * 0.5f, getPosition().getY() - getHeight() * 0.5f);
		positionCorner2 = new Vector2f(getPosition().getX() + getWidth() * 0.5f, getPosition().getY() - getHeight() * 0.5f);
		positionCorner3 = new Vector2f(getPosition().getX() - getWidth() * 0.5f, getPosition().getY() + getHeight() * 0.5f);
		positionCorner4 = new Vector2f(getPosition().getX() + getWidth() * 0.5f, getPosition().getY() + getHeight() * 0.5f);
		
		edgeUp = new Vector2f(getPosition().getX(), getPosition().getY() - getHeight() * 0.5f);
		edgeRight = new Vector2f(getPosition().getX() + getWidth() * 0.5f, getPosition().getY());
		edgeDown = new Vector2f(getPosition().getX(), getPosition().getY() + getHeight() * 0.5f);
		edgeLeft = new Vector2f(getPosition().getX() - getWidth() * 0.5f, getPosition().getY());
	}

	public Brick(Brick brick) {
		super(brick.getBitmap(), true, false, true, 200, brick.getPosition()
				.getX(), brick.getPosition().getY(), brick.getWidth(), brick
				.getHeight());
		mType = brick.mType;
		setWidth(brick.getWidth());
		setHeight(brick.getHeight());
		setPosition(new Vector2f(brick.getPosition()));
		positionCorner1 = new Vector2f(brick.positionCorner1);
		positionCorner2 = new Vector2f(brick.positionCorner2);
		positionCorner3 = new Vector2f(brick.positionCorner3);
		positionCorner4 = new Vector2f(brick.positionCorner4);
		
		edgeUp = new Vector2f(getPosition().getX(), getPosition().getY() - getHeight() * 0.5f);
		edgeRight = new Vector2f(getPosition().getX() + getWidth() * 0.5f, getPosition().getY());
		edgeDown = new Vector2f(getPosition().getX(), getPosition().getY() + getHeight() * 0.5f);
		edgeLeft = new Vector2f(getPosition().getX() - getWidth() * 0.5f, getPosition().getY());
	}
	
	public void draw(android.graphics.Canvas canvas) {
		super.draw(canvas);
	};

	/**
	 * @return the mType
	 */
	public Type getType() {
		return mType;
	}

	/**
	 * @param mType
	 *            the mType to set
	 */
	public void setType(Type mType) {
		this.mType = mType;
	}

	/**
	 * @return the mBitmap
	 */
	public Bitmap getBitmap() {
		return getSpriteBitmap();
	}

	public void setBitmap(Bitmap bitmap) {
		setSpriteBitmap(bitmap);
	}
	
	/**
	 * @return the positionCorner1
	 */
	public Vector2f getPositionCorner1() {
		return positionCorner1;
	}

	/**
	 * @param positionCorner1 the positionCorner1 to set
	 */
	public void setPositionCorner1(Vector2f positionCorner1) {
		this.positionCorner1 = positionCorner1;
	}

	/**
	 * @return the positionCorner2
	 */
	public Vector2f getPositionCorner2() {
		return positionCorner2;
	}

	/**
	 * @param positionCorner2 the positionCorner2 to set
	 */
	public void setPositionCorner2(Vector2f positionCorner2) {
		this.positionCorner2 = positionCorner2;
	}

	/**
	 * @return the positionCorner3
	 */
	public Vector2f getPositionCorner3() {
		return positionCorner3;
	}

	/**
	 * @param positionCorner3 the positionCorner3 to set
	 */
	public void setPositionCorner3(Vector2f positionCorner3) {
		this.positionCorner3 = positionCorner3;
	}

	/**
	 * @return the positionCorner4
	 */
	public Vector2f getPositionCorner4() {
		return positionCorner4;
	}

	/**
	 * @param positionCorner4 the positionCorner4 to set
	 */
	public void setPositionCorner4(Vector2f positionCorner4) {
		this.positionCorner4 = positionCorner4;
	}

	/**
	 * @return the edgeUp
	 */
	public Vector2f getEdgeUp() {
		return edgeUp;
	}

	/**
	 * @return the edgeRight
	 */
	public Vector2f getEdgeRight() {
		return edgeRight;
	}

	/**
	 * @return the edgeDown
	 */
	public Vector2f getEdgeDown() {
		return edgeDown;
	}

	/**
	 * @return the edgeLeft
	 */
	public Vector2f getEdgeLeft() {
		return edgeLeft;
	}

}
