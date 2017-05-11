package com.xl.projectno.base;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <br>类描述: 消息处理类
 * <br>功能详细描述: 
 * 1. 提供post，send，broadcast等形式的消息处理
 * 2. 提供专门进行数据库操作的任务处理
 * 3. 提供专门进行耗时操作的任务处理（比如网络操作）
 * 
 * @author  huanglun
 * @date  [2014-1-7]
 */
public class MessageHandler {
	private static HandlerThread sWorkerThread = new HandlerThread("mpos-handler");
	private static Handler sWorker;
	/**
	 * 处理网络请求的线程池
	 */
	private static ExecutorService sThreadPools = Executors.newCachedThreadPool(new CustomThreadFactory());
	static {
		sWorkerThread.start();
		sWorker = new WorkHandler(sWorkerThread.getLooper());
	}
	
	/**
	 * 提交一个Runable到异步线程上，只允许提交数据库的操作
	 * 
	 * @param r
	 */
	public static void postRunnable(Runnable r) {
		sWorker.post(r);
	}
	
	/**
	 * 提交一个Runable到异步线程上
	 * 
	 * @param r
	 */
	public static void postRunnable(Runnable r, long delayMillis) {
		sWorker.postDelayed(r, delayMillis);
	}

	/**
	 * 提交一个Runable到异步线程上，提交的任务类型不限，一般用于耗时操作，例如:网络
	 * 
	 * @param r
	 */
	public static void postRunnableConcurrently(Runnable r) {
		sThreadPools.execute(r);
	}

	/**
	 * WorkHandler
	 * @author luopeihuan
	 */
	private static class WorkHandler extends Handler {

		public WorkHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				default :
					break;
			}
		}
	}
}