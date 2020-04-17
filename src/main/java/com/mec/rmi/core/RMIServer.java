package com.mec.rmi.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class RMIServer implements Runnable {
	private static final int DEFAULT_PORT = 54199;
	
	private int port = DEFAULT_PORT;
	private ThreadPoolExecutor threadPool;

    private clientCollection clientCollection;
	private ServerSocket serverSocket;
	private boolean goon;
	private Thread clientCollectionThread;
	
	public RMIServer() {
		goon = false;
	}

	public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPoolExecutor threadPool) {
        this.threadPool = threadPool;
    }

    public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
    public boolean startRMIServer() {
		if (goon) {
			System.out.println("服务已经开启");
			return true;
		}
		
		if (threadPool == null) {
		    System.out.println("请先设置线程池");
		    return false;
		}
		if (port == 0)  {
			System.out.println("端口号未设置");
			return false;
		}
		try {
		    goon = true;
		    clientCollection = new clientCollection();
			serverSocket = new ServerSocket(port);
			String mip = null;
		        InetAddress address;
		        try {
		            address = InetAddress.getLocalHost();
		            mip = address.getHostAddress();
		            System.out.println("mip: " + mip);
		        } catch (UnknownHostException e) {
		            e.printStackTrace();
		        }
			System.out.println("port: " + port);
			synchronized (RMIServer.class) {
			    try {
			        clientCollectionThread = new Thread(clientCollection,"客户端连接线程");
    	            threadPool.execute(clientCollectionThread);
    	            RMIServer.class.wait();
                    threadPool.execute(new Thread(this, "客户端侦听线程"));
                } catch (InterruptedException e) {
                     e.printStackTrace();
                }
            }
			System.out.println("服务器已开启......");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean shutDown() {
		if (!goon) {
			System.out.println("服务未启动");
			return false;
		}
		goon = false;
		close();
        System.out.println("33rmi服务器关闭");
		return true;
 	}
	
	public boolean isShutDown() {
		return !goon;
	}

	@Override
	public void run() {
		while (goon) {
			try {
			    System.out.println("服务器开始侦听......");
				Socket client = serverSocket.accept();
				System.out.println("收到一个客户端连接请求");
				clientCollection.add(client);
			} catch (IOException e) {
				if (goon) {
					System.out.println("服务器异常宕机");
					goon = false;
					close();
					System.out.println("11rmi服务器关闭");
				}
				return;
			}
		}
		close();
        System.out.println("22rmi服务器关闭");
	}
	
	private void close() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				serverSocket = null;
			} finally {
                threadPool.shutdownNow();
            }
		}
	}
	
	class clientCollection implements Runnable {
		private List<Socket> clientList;
		
		public clientCollection() {
			if (clientList == null) {
				clientList = new LinkedList<Socket>();
			}
		}
		
		public void add(Socket client) {
		    clientList.add(client);
		}
		
		@Override
		public void run() {
			while (goon) {
			    synchronized (RMIServer.class) {
	                if (!clientList.isEmpty()) {
	                    System.out.println("处理客户端连接");
	                    threadPool.execute(new RMIServerConversation(clientList.remove(0)));
	                }
	                RMIServer.class.notify();
                }
			}
		}		
	}
	
}
