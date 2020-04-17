package com.mec.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MThreadPool {
	public static final int DEFAULT_CORE_POOL_SIZE = 20;
	public static final int DEFAULT_MAXIMUM_POOL_SIZE = 100;
	public static final long DEFAULT_KEEP_ALIVE_MILLITIME = 5000;
	
	private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
	private int maximumPoolSize = DEFAULT_MAXIMUM_POOL_SIZE;
	private long keepAliveTime = DEFAULT_KEEP_ALIVE_MILLITIME;
	
	private static volatile ThreadPoolExecutor threadPool;
	
	public MThreadPool() {
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}
	
	public ThreadPoolExecutor newInstance() {
		return newInstance(true);
	}
	
	public ThreadPoolExecutor newInstance(boolean singleton) {
		if (!singleton) {
			return new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
					keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
		}
		
		if (threadPool == null) {
			synchronized (MThreadPool.class) {
				if (threadPool == null) {
					threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 
							keepAliveTime, TimeUnit.MILLISECONDS, 
							new SynchronousQueue<Runnable>());
				}
			}
		}
		
		return threadPool;
	}
	
	public void execute(ThreadPoolExecutor executor, Runnable command) {
		executor.execute(command);
	}
	
	public void execute(Runnable command) {
		newInstance(true).execute(command);
	}
	
	public void close(boolean rightnow) {
		if (threadPool == null || threadPool.isShutdown()) {
			return;
		}
		
		if (rightnow) {
			threadPool.shutdownNow();
		} else {
			threadPool.shutdown();
		}
	}
	
}
