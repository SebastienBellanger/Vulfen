package com.tojosebe.vulfen;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.tojosebe.vulfen.component.WorldButton;
import com.vulfox.Screen;

public class WorldScreen extends Screen {
	
	private final int BUTTON_OFFSET = 40;

	private int mScrollY = 0;
	private int mListHeight = BUTTON_OFFSET;
	
	private Rect mBackgroundRect = new Rect();
	private Paint mBackgroundPaint = new Paint();
	
	private List<WorldButton> mButtons = new ArrayList<WorldButton>();
	
	private float mLastY;
	
	@Override
	protected void initialize() {
		
		addWorld("World 1", 25, 10);
		addWorld("World 2", 25, 0);
		addWorld("Extras", 25, 0);
		addWorld("Custom Levels", 25, 0);
		addWorld("Pinball world", 25, 0);
		addWorld("Hello there :-)", 25, 0);
		
		mBackgroundRect.set(0, 0, mWidth, mHeight);
		mBackgroundPaint.setColor(Color.BLACK);
	}
	
	@Override
	public void handleInput(MotionEvent motionEvent) {
		
		int action = motionEvent.getAction();
		
		if(action == MotionEvent.ACTION_DOWN) {
			mLastY = motionEvent.getY();
		}
		else if(action == MotionEvent.ACTION_MOVE) {
			
			float yDelta = motionEvent.getY() - mLastY;
			mLastY = motionEvent.getY();
			
			mScrollY -= yDelta;
			
			if(mScrollY  < 0) {
				mScrollY = 0;
			} else if(mScrollY > mListHeight - mHeight) {
				mScrollY = Math.max(0, mListHeight - mHeight);
			}
			
			recalulateButtons();
		}
		
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect(mBackgroundRect, mBackgroundPaint);
	}
	
	@Override
	public boolean handleBackPressed() {
		return mScreenManager.removeScreen(this);
	}
	
	private void recalulateButtons() {
		int yPosition = BUTTON_OFFSET - mScrollY;
		for (int i = 0; i < mButtons.size(); i++) {			
			mButtons.get(i).setPositionY(yPosition);
			yPosition += mButtons.get(i).getHeight() + BUTTON_OFFSET;
		}
	}
	
	private void addWorld(String worldName, int totalStages, int clearedStages) {

		WorldButton worldButton = new WorldButton(worldName, totalStages, clearedStages) {
			@Override
			public void buttonClicked() {
				mScreenManager.addScreen(new GameScreen());
			}
			
		};
		worldButton.setPositionX((mWidth - worldButton.getWidth()) / 2);
		
		int positionY = mListHeight - mScrollY;	
		worldButton.setPositionY(positionY);
			
		addScreenComponent(worldButton);
		mButtons.add(worldButton);
		
		mListHeight += worldButton.getHeight() + BUTTON_OFFSET;
	}
}
