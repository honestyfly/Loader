package com.honesty.android;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {
	
	int index = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	boolean isDestroy = false;
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isDestroy = true;
	}
}