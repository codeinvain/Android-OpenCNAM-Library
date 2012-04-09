package com.tomdignan.android.opencnam.library.teststub;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * This activity has no purpose other than to cause the SDK tools to
 * generate an APK that will be installed on the device.
 * 
 * @author tom
 */
public class StubActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView text = new TextView(this);
		text.setText("stub");
		setContentView(text);
	}
}
