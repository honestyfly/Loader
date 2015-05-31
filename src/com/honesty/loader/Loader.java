package com.honesty.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.honesty.cache.DiskLruCache;
import com.honesty.pool.QueueThread;
import com.honesty.utils.Md5Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

/**
 *@author honesty
 **/
public class Loader {
	private static final String TAG = "Loader";
	LoaderConfig loaderConfig;
	/**阻塞队列**/
	BlockingQueue<ImageData> queueDatas = new LinkedBlockingQueue<ImageData>();
	List<Thread> threads = new ArrayList<Thread>();
	static Loader loader;
	private Loader(){
	}
	/**
	 * 单例类，返回Loader对象
	 * @return
	 */
	public static Loader getInstrance(){
		if(loader == null){
			loader = new Loader();			
		}
		return loader;
	}
	/**
	 * 设置配置参数并启动线程池
	 * @param loaderConfig
	 */
	public void setLoaderConfig(Context context,LoaderConfig loaderConfig){
		this.loaderConfig = loaderConfig;
		//开启线程池
		for(int i = 0 ; i < loaderConfig.getThreadPoolSize() ; i++){
			Thread t = new QueueThread(queueDatas,loaderConfig);
			t.start();
			threads.add(t);
		}
	}
	/**
	 * 设置默认配置参数并启动线程池
	 */
//	public void setDefaultLoaderConfig(Context context){
//		loaderConfig = new LoaderConfig(context);
//		setLoaderConfig(context,loaderConfig);
//	}
	public void downImage(String url){
		//判断内存缓存是否存在
		if(!loaderConfig.myLruCache.isBitmapCache((Md5Helper.toMD5(url)))){
			try {
				DiskLruCache.Snapshot snapshot = loaderConfig.mDiskLruCache.get(Md5Helper.toMD5(url));
				if(snapshot == null){
					System.out.println("下载图片");
					//硬盘缓存不存在，下载图片
					queueDatas.add(new ImageData(null, url));
				}
			} catch (Exception e) {
				e.printStackTrace();
				//硬盘缓存不存在，下载图片
				queueDatas.add(new ImageData(null, url));
			}
			
		}
	}
	/**
	 * 显示图片
	 * @param imageView
	 * @param url
	 */
	public void displayImage(ImageView imageView , String url){
		if(!getBitmapFromLruCache(imageView,url)){
			if(!getBitmapFromDiskLruCache(imageView,url)){
				queueDatas.add(new ImageData(imageView, url));
			}
		}
	}
	/**
	 * 从LruCache里获取图片
	 * @param url
	 * @return true获取图片，false没有图片
	 */
	public boolean getBitmapFromLruCache(ImageView imageView ,String url){
		Bitmap bitmap = loaderConfig.myLruCache.getLruBitmap(Md5Helper.toMD5(url));
		if(bitmap != null){
			//从内存里读取图片
			if(loaderConfig.isDebug){
				Log.d(TAG, "从内存里获取图片");
			}
			imageView.setImageBitmap(bitmap);
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 从硬盘缓存里读取图片
	 * @param url
	 * @return
	 */
	public boolean getBitmapFromDiskLruCache(ImageView imageView ,String url){
		try {
			DiskLruCache.Snapshot snapshot = loaderConfig.mDiskLruCache.get(Md5Helper.toMD5(url));
			if(snapshot != null){
				InputStream inputStream = snapshot.getInputStream(0);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				String str = " bitmap.getWidth() = " + bitmap.getWidth() + " bitmap.getHeight() = " + bitmap.getHeight() + "bitmap.getByteCount() = " + bitmap.getByteCount()/1024 + "k";
				if(loaderConfig.isDebug){
					Log.d(TAG, "从SD卡里获取图片 " + str);					
				}
				imageView.setImageBitmap(bitmap);
				// 将Bitmap对象添加到内存缓存当中
				loaderConfig.myLruCache.putLruBitmap(
						Md5Helper.toMD5(url), bitmap);
				return true;
			}else{
				if(loaderConfig.isDebug){
					Log.d(TAG, "SD卡里没有图片");					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 退出程序，释放资源
	 */
	public void onDestory(){
		for(Thread t : threads){
			t.interrupt();
		}
		//TODO 清空缓存
		loaderConfig.myLruCache.clearCache();
	}
}
