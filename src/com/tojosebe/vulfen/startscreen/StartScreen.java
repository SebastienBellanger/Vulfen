package com.tojosebe.vulfen.startscreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

import com.tojosebe.vulfen.GameScreen;
import com.tojosebe.vulfen.R;
import com.tojosebe.vulfen.R.drawable;
import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ButtonComponent;
import com.vulfox.component.ImageComponent;
import com.vulfox.component.ScreenComponent;

public class StartScreen extends Screen {

	/** The dots per inch for the device. */
	private int dpi;
	
	private Cloud cloud1; 
	private Cloud cloud2;
	
	public StartScreen(int dpi) {
		this.dpi = dpi;
	}
	
	@Override
	protected void initialize() {
		
		cloud1 = new Cloud(R.drawable.cloud1, dpi, mContext, 200, 70, mWidth);
		cloud2 = new Cloud(R.drawable.cloud2, dpi, mContext, 60, 130, mWidth);
		
		//TODO: fixa så att cloudsen inte randomizas så mycket i y-led. De hamnar ovan på varandra.

		addScreenComponent(createBackground());
		addScreenComponent(createPlayButton());
		addScreenComponent(cloud1.getImageComponent());
		addScreenComponent(cloud2.getImageComponent());
		addScreenComponent(createTitle());
	}
	
	private ScreenComponent createTitle() {
		Bitmap title = ImageLoader.loadFromResource(mContext, R.drawable.title);
		ImageComponent imageComp = new ImageComponent(title);
		
		imageComp.setWidthInDpAutoSetHeight(250, dpi);
		imageComp.setPositionX((mWidth -imageComp.getWidth())/ 2);
		imageComp.setPositionYInDp(40, dpi);
		
		return imageComp;
	}

	@Override
	public void update(float timeStep) {
		cloud1.update(timeStep);
		cloud2.update(timeStep);
	}
	
	@Override
	public void draw(Canvas canvas) {
		cloud1.draw(canvas);
		cloud2.draw(canvas);
	}

	private ScreenComponent createPlayButton() {
		ButtonComponent button = new ButtonComponent("Play", 0xFFFF7733, 20, 0xFF221144, 0xFF226644, 250, 70, (mWidth-250)/2, (mHeight - 70)/2) {
			 @Override
			public void buttonClicked() {
				mScreenManager.addScreen(new GameScreen());
			}
		};
		return button;
	}

	private ImageComponent createBackground() {
		Bitmap background = ImageLoader.loadFromResource(mContext, R.drawable.background);
		ImageComponent imageComp = new ImageComponent(background);
		imageComp.setHeight(mHeight);
		imageComp.setWidth(mWidth);
		return imageComp;
	}
}
