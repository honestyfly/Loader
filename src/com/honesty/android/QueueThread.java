package com.honesty.android;

import java.util.concurrent.BlockingQueue;

/**
 * @author honesty
 **/
public class QueueThread extends Thread {
	int threadId;
	private BlockingQueue<QueueData> queueDatas;

	public QueueThread(BlockingQueue<QueueData> queueDatas, int threadId) {
		this.queueDatas = queueDatas;
		this.threadId = threadId;
	}

	@Override
	public void run() {
		super.run();
		try {
			while (!isInterrupted()) {
				System.out.println("threadId:" + threadId + "等待");
				QueueData queueData = queueDatas.take();
				System.out.println("threadId:" + threadId + "take() ，index = " + queueData.getId());
				Thread.sleep(5000);
				System.out.println("threadId:" + threadId + "请求完毕 ，index = " + queueData.getId());
			}
			System.out.println("threadId:" + threadId + "关闭");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
