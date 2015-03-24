package com.honesty.pool;

import java.util.concurrent.BlockingQueue;

import com.honesty.android.ImageData;


/**
 * @author honesty
 **/
public class QueueThread extends Thread {
	
	private BlockingQueue<ImageData> queueDatas;

	public QueueThread(BlockingQueue<ImageData> queueDatas) {
		this.queueDatas = queueDatas;
	}

	@Override
	public void run() {
		super.run();
		try {
			while (!isInterrupted()) {
				ImageData queueData = queueDatas.take();
				Thread.sleep(5000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
