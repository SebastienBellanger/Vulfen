package com.tojosebe.vulfen.component;

import android.graphics.Canvas;

public interface SceneComponent {

	void handleInput(MotionInput motionInput);
	
	void update(float timeStep);
	
	void draw(Canvas canvas);
	
}
