package com.tojosebe.vulfen.startscreen;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.vulfox.ImageLoader;
import com.vulfox.component.ImageComponent;
import com.vulfox.math.Vector2f;
import com.vulfox.util.Vector2fPool;

public class Cloud {

	private ImageComponent imageComponent;

	private Vector2f velocity;
	private Vector2f position;

	private int dpi;
	
	private int screenWidth;

	public Cloud(int resourceId, int dpi, Context context, int xDp, int yDp, int screenWidth) {
		this.dpi = dpi;
		this.screenWidth = screenWidth;
		Bitmap cloud = ImageLoader.loadFromResource(context, resourceId);
		imageComponent = new ImageComponent(cloud);
		imageComponent.setWidthInDpAutoSetHeight(100, dpi);
		imageComponent.setPositionXInDp(xDp, dpi);
		imageComponent.setPositionYInDp(yDp, dpi);
		velocity = new Vector2f(getRandom(20, 40), 0);
		position = new Vector2f(imageComponent.getPositionX(),
				imageComponent.getPositionY());
	}

	private int getRandom(int n, int m) {
		Random rand = new Random(System.currentTimeMillis());
		return (int) rand.nextInt(m) + n; // random number n-m
	}

	public void update(float timeStep) {
		Vector2f positionDelta = Vector2fPool.getInstance().aquire();
		positionDelta.set(velocity);
		positionDelta.mulT(timeStep);

		position.addT(positionDelta);

		Vector2fPool.getInstance().release(positionDelta);

		if (isOutSideScreen()) {
			reinitCloud();
		}
	}

	private boolean isOutSideScreen() {
		boolean outside = false;
		if (imageComponent.getPositionX() >= screenWidth) {
			outside = true;
		}
		return outside;
	}

	private void reinitCloud() {
		imageComponent.setWidthInDpAutoSetHeight(100, dpi);
		imageComponent.setPositionX(-imageComponent.getWidth()); // set outside
																	// screen
		imageComponent.setPositionYInDp(getRandom(0, 150), dpi);
		velocity = new Vector2f(getRandom(20, 40), 0);
		position = new Vector2f(imageComponent.getPositionX(),
				imageComponent.getPositionY());
	}

	public void draw(Canvas canvas) {
		imageComponent.setPositionX((int) position.getX());
		imageComponent.setPositionY((int) position.getY());
		imageComponent.draw(canvas);
	}

	/**
	 * @return the imageComponent
	 */
	public ImageComponent getImageComponent() {
		return imageComponent;
	}

}
