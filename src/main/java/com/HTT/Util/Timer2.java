package com.HTT.Util;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ��׼��ʱ����ʱ ʱ���� ִ��һ��</br>
 * ����������</br>
 * (int delay, ITimer task, boolean flag)</br>
 * ʱ�䡢��������ʱʱ�仹�Ǵ���   Ĭ��ΪTrue ��ʱʱ��</br>
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
            throw new Exception("ʱ��ʹ��������?0");
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
			System.out.println("�޿ɼ�ʱ����");
			return false;
		}
		if (threadPool == null) {
		    System.out.println("δ�����̳߳�");
		    return false;
		}
		if (goon == true) {
			System.out.println("�ѿ�����ʱ");
			return true;
		}
		goon= true;
		new Thread(this, "��ʱ").start();
		
		return goon;
	}
	
	public boolean stopWork() {
		if (goon == false) {
			System.out.println("�ѹرռ�ʱ");
		}
		goon = false;
		threadPool.shutdown();
		System.out.println("��ʱ���رճɹ�");
		return !goon;
	}

	public boolean closeThreadPool() {
        if (threadPool.isShutdown()) {
            System.out.println("�ѹرռ�ʱ���̳߳�");
        }
        threadPool.shutdown();
        return threadPool.isShutdown();
    }
	
	public boolean closeThreadPoolNow() {
        if (threadPool.isShutdown()) {
            System.out.println("�������ر��̳߳�");
        }
        threadPool.shutdownNow();
        return threadPool.isShutdown();
    }
    
	@Override
	public void run() {
	    Thread thread = new Thread(new Worker(), "ִ������");
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

		@Override
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
