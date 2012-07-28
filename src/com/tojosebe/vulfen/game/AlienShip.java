package com.tojosebe.vulfen.game;

import android.graphics.Bitmap;

import com.tojosebe.vulfen.component.SpriteComponent;

public class AlienShip extends SpriteComponent {

	public AlienShip(Bitmap bitmap, boolean antialias,
			boolean animateFromLarge, boolean animateFromSmall,
			int animationTimeMillis, float x, float y, float width, float height) {
		super(bitmap, antialias, animateFromLarge, animateFromSmall,
				animationTimeMillis, x, y, width, height);
	}

	public void shoot(float x, float y) {
		
	}
	
}
