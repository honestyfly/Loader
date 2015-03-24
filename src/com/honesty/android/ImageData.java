package com.honesty.android;

import android.widget.ImageView;

/**
 *@author honesty
 **/
public class ImageData {
	private ImageView imageView;
	private String url;
	public ImageData(ImageView imageView,String url){
		this.imageView = imageView;
		this.url = url;
	}
	public ImageView getImageView() {
		return imageView;
	}
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}	
}