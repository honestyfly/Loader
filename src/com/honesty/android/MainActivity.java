package com.honesty.android;

import com.honesty.loader.Loader;
import com.honesty.loader.LoaderConfig;
import com.honesty.pool.Http;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

public class MainActivity extends Activity {
	
	public final static String[] imageThumbUrls = new String[] {  
        "http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",  
        "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",  
        "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",  
        "http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",  
        "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",  
        "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",  
        "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",  
        "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",  
        "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg",  
        "http://img.my.csdn.net/uploads/201407/26/1406383264_8243.jpg",  
        "http://img.my.csdn.net/uploads/201407/26/1406383248_3693.jpg",  
        "http://img.my.csdn.net/uploads/201407/26/1406383243_5120.jpg",  
        "http://img.my.csdn.net/uploads/201407/26/1406383242_3127.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406382765_7341.jpg"  
    };
	
	Loader allLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button trunButton = (Button)findViewById(R.id.trunButton);
		trunButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, SecondActivity.class));
			}
		});
		LoaderConfig loaderConfig = new LoaderConfig();
		loaderConfig.setDiskLruCache(getApplicationContext());
		loaderConfig.isDebug(false);
		
		allLoader = Loader.getInstrance();
		allLoader.setLoaderConfig(getApplicationContext(), loaderConfig);
		
		ImageView picImageView = (ImageView)findViewById(R.id.picImageView);
		String url = "http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg";
		allLoader.downImage(url);
		new MyThread(picImageView, url, new OnProgressListener() {
			
			@Override
			public void onResult(ImageView imageView,Bitmap bitmap) {
				imageView.setImageBitmap(bitmap);
			}
			
			@Override
			public void onProgress(int progress) {
				
			}
		}).start();

		ListView picListView = (ListView)findViewById(R.id.picListView);
		picListView.setAdapter(new PicAdapter());
		
	}
	
	class MyThread extends Thread{
		ImageView imageView;
		String url;
		OnProgressListener onProgressListener;
		public MyThread(ImageView imageView, String url , OnProgressListener onProgressListener){
			this.imageView = imageView;
			this.url = url;
			this.onProgressListener = onProgressListener;
		}
		@Override
		public void run() {
			super.run();
			final Bitmap bitmap = Http.getBitmapFromURL(url);
			runOnUiThread(new Runnable() {				
				@Override
				public void run() {
					if(onProgressListener != null){
						onProgressListener.onResult(imageView,bitmap);			
					}
				}
			});
		}
	}
	
	
	public interface OnProgressListener{
		void onProgress(int progress);
		void onResult(ImageView imageView,Bitmap bitmap);
	}
	
	
	class PicAdapter extends BaseAdapter{
		LayoutInflater inflater = getLayoutInflater();
		@Override
		public int getCount() {
			return imageThumbUrls.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(convertView == null){
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_pic, null);
				viewHolder.picImageView = (ImageView)convertView.findViewById(R.id.picImageView);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			allLoader.displayImage(viewHolder.picImageView, imageThumbUrls[position]);
			return convertView;
		}
		class ViewHolder{
			ImageView picImageView;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		allLoader.onDestory();
	}
}