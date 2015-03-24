package com.honesty.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import com.honesty.android.ImageData;
import com.honesty.pool.QueueThread;

import android.widget.ImageView;

/**
 *@author honesty
 **/
public class Loader {
	/**阻塞队列**/
	BlockingQueue<ImageData> queueDatas = new LinkedBlockingQueue<ImageData>();
	List<Thread> threads = new ArrayList<Thread>();
	Loader loader;
	private Loader(){
		//开启线程池
		for(int i = 0 ; i < 5 ; i++){
			Thread t = new QueueThread(queueDatas);
			t.start();
			threads.add(t);
		}
	}
	/**
	 * 单例类，返回Loader对象
	 * @return
	 */
	public Loader getInstrance(){
		if(loader == null){
			loader = new Loader();
		}
		return loader;
	}
	public void displayImage(ImageView imageView , String url){
		queueDatas.add(new ImageData(imageView, url));
	}
	/**
	 * 退出程序，释放资源
	 */
	public void onDestory(){
		for(Thread t : threads){
			t.interrupt();
		}
		//TODO 清空缓存
	}
}
