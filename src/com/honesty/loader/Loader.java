package com.honesty.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.honesty.cache.DiskLruCache;
import com.honesty.pool.QueueThread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 *@author honesty
 **/
public class Loader {
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
	public void setDefaultLoaderConfig(Context context){
		loaderConfig = new LoaderConfig(context);
		setLoaderConfig(context,loaderConfig);
	}
	/**
	 * 显示图片
	 * @param imageView
	 * @param url
	 */
	public void displayImage(ImageView imageView , String url){
		if(!getBitmapFromLruCache(url)){
			if(!getBitmapFromDiskLruCache(url)){
				queueDatas.add(new ImageData(imageView, url));
			}
		}
	}
	/**
	 * 从LruCache里获取图片
	 * @param url
	 * @return true获取图片，false没有图片
	 */
	public boolean getBitmapFromLruCache(String url){
		Bitmap bitmap = loaderConfig.myLruCache.getLruBitmap(url);
		if(bitmap != null){
			//从内存里读取图片
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
	public boolean getBitmapFromDiskLruCache(String url){
		try {
			DiskLruCache.Snapshot snapshot = loaderConfig.mDiskLruCache.get(url);
			if(snapshot != null){
				InputStream inputStream = snapshot.getInputStream(0);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				return true;
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
