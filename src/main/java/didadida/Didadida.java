package timer.didadida;

public class Didadida implements Runnable {
	private static final int DEFAULT_dELAY_tIME = 1000;
	
	private int delayTime;
	private Object lock;
	private volatile boolean goon;
	private IDidadida didadida;
	
	public Didadida() {
		this(DEFAULT_dELAY_tIME, null);
	}
	
	public Didadida(int delayTime, IDidadida didadida) {
		lock = new Object();
		this.delayTime = delayTime;
		this.didadida = didadida;
	}
	
	public Didadida(int delayTime) {
		lock = new Object();
		this.delayTime = delayTime;
	}

	public void setDidadida(IDidadida didadida) {
		this.didadida = didadida;
	}
	
	public void startTimer() {
		this.goon = true;
		new Thread(new TimeWorker(), "timeWorker").start();
		new Thread(this, "didadida").start();
	}
	
	public void endTimer() {
		if (goon == false) {
			System.out.println("now had ended");
			return;
		}
		
		if (didadida == null) {
			System.out.println("鏃犱簨鍙仛锛�");
			return;
		}
		
		this.goon = false;
	}

	public void run() {
		while (goon) {
			synchronized (lock) {
				try {
					//鐪熸鐢ㄦ潵璁℃椂鐨勭嚎绋�
					lock.wait(delayTime);
					//杩欓噷鏄敜閱掍竴涓嚎绋嬶紝鐢ㄦ潵涓撻棬鎵цaction
					lock.notify();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class TimeWorker implements Runnable {
		
		public void run() {
			while (goon) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				didadida.doSomething();
			}
		}
		
	}
}
