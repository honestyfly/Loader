package com.honesty.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

public class MyLruCache {
	private static final String TAG = "MyLruCache";
	private boolean isDebug = true;
	int maxMemory = (int) Runtime.getRuntime().maxMemory();  
    // 使用最大可用内存值的1/8作为缓存的大小。  
    int cacheSize = maxMemory / 8; 
	LruCache<String, Bitmap> bitmapCache;

	private MyLruCache() {
		if(isDebug){
			Log.d(TAG, "#最大内存 ： "+maxMemory/1024/1024);
			System.out.println("缓存大小：" +cacheSize/1024/1024);			
		}
		bitmapCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}			
		};
	}

	private static MyLruCache instance;

	public static MyLruCache getInstance() {
		if (instance == null) {
			instance = new MyLruCache();
		}
		return instance;
	}
	public void isDebug(boolean isDebug){
		this.isDebug = isDebug;
	}
	/**
	 * 获取图片
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap getLruBitmap(String key) {
		return bitmapCache.get(key);
	}
	/**
	 * 缓存是否存在
	 * @param key
	 * @return
	 */
	public boolean isBitmapCache(String key){
		if (bitmapCache.get(key) == null) {
			return false;
		}
		return true;
	}

	/**
	 * 存放图片
	 * 
	 * @param key
	 * @param bitmap
	 */
	public void putLruBitmap(String key, Bitmap bitmap) {
		synchronized (bitmapCache) {
			if (bitmapCache.get(key) == null) {
				bitmapCache.put(key, bitmap);
				String str = "bitmapCache.hitCount():" + bitmapCache.hitCount() + " , bitmapCache.size() =" + bitmapCache.size()/1024 + "k";
				if(isDebug){
					Log.d(TAG, str);					
				}
			}
		}
	}
	/**
	 * 清空缓存
	 */
	public void clearCache(){
		if(bitmapCache != null){
			if(bitmapCache.size() > 0){
				bitmapCache.evictAll();
			}
		}
		bitmapCache = null;
	}
}
