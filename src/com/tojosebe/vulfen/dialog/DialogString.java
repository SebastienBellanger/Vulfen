package com.tojosebe.vulfen.dialog;

public interface DialogString {

	enum TextSize {
		SMALL,
		MEDIUM,
		LARGE
	}
	
	String getContent();
	
	TextSize getTextSize();
	
	int getColor();
	
	int[] getPadding();
}
