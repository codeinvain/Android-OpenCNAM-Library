package com.tomdignan.android.opencnam.library.teststub;

import android.app.Application;

public class StubApplication extends Application {
	private static Application INSTANCE;
	
	@Override
	public void onCreate() {
		super.onCreate();
		INSTANCE = this;
	}
	
	public static Application getApplication() {
		return INSTANCE;
	}
}
