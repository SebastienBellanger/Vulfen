package com.tojosebe.vulfen.component;

import com.vulfox.util.ObjectPool;

public class MotionInputPool extends ObjectPool<MotionInput> {

	@Override
	protected MotionInput create() {
		return new MotionInput();
	}

	@Override
	protected void reset(MotionInput poolObject) {
		poolObject.clear();
	}
	
}
