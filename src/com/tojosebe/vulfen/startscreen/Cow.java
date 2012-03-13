package com.tojosebe.vulfen.startscreen;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.vulfox.ImageLoader;
import com.vulfox.component.ImageComponent;
import com.vulfox.math.Vector2f;
import com.vulfox.util.Vector2fPool;

public class Cow {

	private ImageComponent imageComponent;

	private Vector2f velocity;
	private Vector2f position;

	private int dpi;
	
	private boolean movingDown = false;

	private int screenWidth;
	private int screenHeight;
	private int yOffsetDp;
	private int xOffsetDp;
	private int startY;

	public Cow(int resourceId, int dpi, Context context, int widthInDp,
			int xOffsetDp, int yOffsetDp, int screenWidth, int screenHeight) {
		this.dpi = dpi;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.xOffsetDp = xOffsetDp;
		this.yOffsetDp = yOffsetDp;

		float fraction = dpi / 160.0f;

		Bitmap cow = ImageLoader.loadFromResource(context, resourceId);

		imageComponent = new ImageComponent(cow);
		imageComponent.setWidthInDpAutoSetHeight(widthInDp, dpi);

		imageComponent.setPositionX(screenWidth / 2 - (screenWidth / 4)
				+ (int) (xOffsetDp * fraction));
		imageComponent.setPositionY(screenHeight / 2 + (screenHeight / 4)
				+ (int) (yOffsetDp * fraction));

		velocity = new Vector2f(0, 0);
		position = new Vector2f(imageComponent.getPositionX(),
				imageComponent.getPositionY());
		startY = imageComponent.getPositionY();

		// Resize the loaded bitmap with nice algorithms so that it looks nice.
		imageComponent.resizeBitmap();
	}

	private int getRandom(int n, int m) {
		Random rand = new Random(System.currentTimeMillis() + (int)position.getX() + (int)position.getY());
		return (int) rand.nextInt(m) + n; // random number n-m
	}

	public void update(float timeStep) {
		
		if (imageComponent.getPositionY() > startY) {
			reinitCow();
			movingDown = false;
		}
		
		if (velocity.getY() != 0 && !movingDown) {
		
			Vector2f positionDelta = Vector2fPool.getInstance().aquire();
			positionDelta.set(velocity);
			positionDelta.mulT(timeStep);
	
			position.addT(positionDelta);
			
			velocity.setY(velocity.getY()+30);
			
			if (velocity.getY() == 0) {
				movingDown = true;
			}
	
			Vector2fPool.getInstance().release(positionDelta);
			
		} else {
			int random = getRandom(0,1000);
			if (random == 42) {
				//Jump!
				velocity.setY(-500);
			}
		}
	}

	private void reinitCow() {
		
		float fraction = dpi / 160.0f;
		
		imageComponent.setPositionX(screenWidth / 2 - (screenWidth / 4)
				+ (int) (xOffsetDp * fraction));
		imageComponent.setPositionY(screenHeight / 2 + (screenHeight / 4)
				+ (int) (yOffsetDp * fraction));

		velocity = new Vector2f(0, 0);
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
