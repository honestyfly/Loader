package com.honesty.android;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {
	Thread t1;
	Thread t2;
	int index = 0;
	BlockingQueue<QueueData> queueDatas = new LinkedBlockingQueue<QueueData>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		for(int i = 0 ; i < 10 ; i++){
			oneData();
		}
		System.out.println("开启线程");
		t1 = new QueueThread(queueDatas, 1);
		t1.start();
		t2 = new QueueThread(queueDatas, 2);
		t2.start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(!isDestroy){
					try {
						Thread.sleep(10000);
						//oneData();
						queueDatas.clear();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	private void oneData(){
		QueueData queueData = new QueueData();
		queueData.setId(index);
		queueDatas.add(queueData);
		index ++ ;
	}
	boolean isDestroy = false;
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isDestroy = true;
		t1.interrupt();
		t2.interrupt();
	}
}