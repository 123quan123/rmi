package com.mec.util;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 精准定时，定时 时间间隔 执行一次</br>
 * 三参数含义</br>
 * (int delay, ITimer task, boolean flag)</br>
 * 时间、任务、是延时时间还是次数   默认为True 延时时间</br>
 * @author Quan
 * @date 2020/01/14
 */
public class Timer2 implements Runnable {
	private static int DEFAULT_DELAY_TIME = 1000;
	private static int DEFAULT_DELAY_COUNT = Integer.MAX_VALUE;
    private static ThreadPoolExecutor threadPool;
	
	private int delayTime;
	private int delayCount;
	private ITimer Task;
	
	private volatile boolean goon;
    private volatile int doCount;
	
	public Timer2() {
		this(DEFAULT_DELAY_TIME, null, true);
	}
	
	public Timer2(int delayTime, ITimer task) {
		this(delayTime, task, true);
	}
	
	public void setGoon(boolean goon) {
        this.goon = goon;
    }

    public Timer2(ITimer task) {
		this(DEFAULT_DELAY_TIME, task, true);
	}
	
	public Timer2(int delay, ITimer task, boolean flag) {
		goon = false;
		doCount = 0;
		threadPool = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 10000, TimeUnit.MILLISECONDS,
		    new SynchronousQueue<Runnable>());
		if (flag == true) {
	        this.delayTime = delay;
	        delayCount = DEFAULT_DELAY_COUNT;
		} else {
            this.delayTime = DEFAULT_DELAY_TIME;
		    this.delayCount = delay;
		}
		this.Task = task;
	}
	
	public Timer2(int delayCount, int delayTime, ITimer task) throws Exception {
        goon = false;
        doCount = 0;
        threadPool = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 10000, TimeUnit.MILLISECONDS,
            new SynchronousQueue<Runnable>());
        if (delayTime == 0 || delayCount == 0) {
            throw new Exception("时间和次数不能为0");
        }
        this.delayTime = delayTime;
        this.delayCount = delayCount;
        this.Task = task;
    }

	public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    public int getDelayCount() {
        return delayCount;
    }

    public void setDelayCount(int delayCount) {
        this.delayCount = delayCount;
    }

    public ITimer getTask() {
		return Task;
	}

	public void setTask(ITimer task) {
		Task = task;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
	
	public boolean startWork() {
		if (this.Task == null) {
			System.out.println("无可计时的事");
			return false;
		}
		if (threadPool == null) {
		    System.out.println("未设置线程池");
		    return false;
		}
		if (goon == true) {
			System.out.println("已开启计时");
			return true;
		}
		goon= true;
		new Thread(this, "计时").start();
		
		return goon;
	}
	
	public boolean stopWork() {
		if (goon == false) {
			System.out.println("已关闭计时");
		}
		goon = false;
		threadPool.shutdown();
		System.out.println("计时器关闭成功");
		return !goon;
	}

	public boolean closeThreadPool() {
        if (threadPool.isShutdown()) {
            System.out.println("已关闭计时器线程池");
        }
        threadPool.shutdown();
        return threadPool.isShutdown();
    }
	
	public boolean closeThreadPoolNow() {
        if (threadPool.isShutdown()) {
            System.out.println("已立即关闭线程池");
        }
        threadPool.shutdownNow();
        return threadPool.isShutdown();
    }
    
	public void run() {
	    Thread thread = new Thread(new Worker(), "执行任务");
		while (goon) {
			try {
				synchronized (Task) {
				    threadPool.execute(thread);
                    Task.wait(delayTime);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	class Worker implements Runnable {

		public void run() {
		    if (delayCount <= doCount) {
		        stopWork();
		        Task.end();
		        return;
		    }
			Task.work();
			doCount++;
			System.out.println("count:" + doCount);
		}
	}

}
