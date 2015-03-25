package com.honesty.loader;

import java.io.File;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import com.honesty.cache.DiskLruCache;
import com.honesty.cache.MyLruCache;

public class LoaderConfig {
	/** 需要动态获取 **/
	private int threadPoolSize = 5;
	public MyLruCache myLruCache = MyLruCache.getInstance();
	public DiskLruCache mDiskLruCache = null;

	public LoaderConfig(Context context) {
		try {
			File cacheDir = getDiskCacheDir(context, "bitmap");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context),
					1, 10 * 1024 * 1024);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	 * 返回线程池的数量
	 * 
	 * @return
	 */
	public int getThreadPoolSize() {
		return threadPoolSize;
	}

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
