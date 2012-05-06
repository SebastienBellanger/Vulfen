package com.tojosebe.vulfen.dialog;

public interface CanvasDialogString {

	enum TextSize {
		SMALL,
		MEDIUM,
		LARGE
	}
	
	String getContent();
	
	TextSize getTextSize();
}
