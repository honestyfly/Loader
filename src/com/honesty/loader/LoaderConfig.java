package com.honesty.loader;

import java.io.File;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

import com.honesty.cache.DiskLruCache;
import com.honesty.cache.MyLruCache;

public class LoaderConfig {
	private static final String TAG = "LoaderConfig";
	/** 需要动态获取 **/
	private int threadPoolSize = 3;
	public DiskLruCache mDiskLruCache = null;
	
	public boolean isDebug = true;
	public MyLruCache myLruCache = MyLruCache.getInstance();
	/**
	 * 设置硬盘缓存
	 * @param context
	 * @return
	 */
	public LoaderConfig setDiskLruCache(Context context) {
		try {
			File cacheDir = getDiskCacheDir(context, "bitmap");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			Log.d(TAG, "DiskLruCache : " + cacheDir.getPath());
			mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context),
					1, 10 * 1024 * 1024);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 设置线程池的数量
	 * 
	 * @param threadPoolSize
	 * @return
	 */
	public LoaderConfig setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
		return this;
	}
	/**
	 * 设置是否打印日志
	 * @param isDebug
	 * @return
	 */
	public LoaderConfig isDebug(boolean isDebug){
		this.isDebug = isDebug;
		myLruCache.isDebug(isDebug);
		return this;
	}

	/**
	 * 返回线程池的数量
	 * 
	 * @return
	 */
	public int getThreadPoolSize() {
		return threadPoolSize;
	}
	/**
	 * 获取缓存文件路径
	 * @param context
	 * @param uniqueName
	 * @return
	 */
	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}
	/**
	 * 获取版本号
	 * @param context
	 * @return
	 */
	public int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}
}
