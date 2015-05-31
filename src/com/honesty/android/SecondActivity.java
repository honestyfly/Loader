package com.honesty.android;

import com.honesty.loader.Loader;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;

public class SecondActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Loader allLoader = Loader.getInstrance();		
		ImageView picImageView = (ImageView)findViewById(R.id.picImageView);
		allLoader.displayImage(picImageView, "http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg");
		
	}
	boolean isDestroy = false;
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isDestroy = true;
	}
}