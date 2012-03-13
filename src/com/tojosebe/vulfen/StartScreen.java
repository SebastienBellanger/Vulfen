package com.tojosebe.vulfen;

import android.graphics.Bitmap;

import com.vulfox.ImageLoader;
import com.vulfox.Screen;
import com.vulfox.component.ButtonComponent;
import com.vulfox.component.ImageComponent;
import com.vulfox.component.TextButtonComponent;

public class StartScreen extends Screen {

	@Override
	protected void initialize() {
		Bitmap background = ImageLoader.loadFromResource(mContext, R.drawable.background);
		
		ImageComponent imageComp = new ImageComponent(background);
		
		imageComp.setHeight(mHeight);
		imageComp.setWidth(mWidth);
		
		ButtonComponent button = new TextButtonComponent("Play", 0xFFFF7733, 20, 0xFF221144, 0xFF226644, 250, 70, (mWidth-250)/2, (mHeight - 70)/2) {
			 @Override
			public void buttonClicked() {
				mScreenManager.addScreen(new WorldScreen());
			}
		};
		
		addScreenComponent(imageComp);
		addScreenComponent(button);
	}
}
