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
			System.out.println("�����Ѿ�����");
			return true;
		}
		
		if (threadPool == null) {
		    System.out.println("���������̳߳�");
		    return false;
		}
		if (port == 0)  {
			System.out.println("�˿ں�δ����");
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
			        clientCollectionThread = new Thread(clientCollection,"�ͻ��������߳�");
    	            threadPool.execute(clientCollectionThread);
    	            RMIServer.class.wait();
                    threadPool.execute(new Thread(this, "�ͻ��������߳�"));
                } catch (InterruptedException e) {
                     e.printStackTrace();
                }
            }
			System.out.println("�������ѿ���......");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean shutDown() {
		if (!goon) {
			System.out.println("����δ����");
			return false;
		}
		goon = false;
		close();
        System.out.println("33rmi�������ر�");
		return true;
 	}
	
	public boolean isShutDown() {
		return !goon;
	}

	@Override
	public void run() {
		while (goon) {
			try {
			    System.out.println("��������ʼ����......");
				Socket client = serverSocket.accept();
				System.out.println("�յ�һ���ͻ�����������");
				clientCollection.add(client);
			} catch (IOException e) {
				if (goon) {
					System.out.println("�������쳣崻�");
					goon = false;
					close();
					System.out.println("11rmi�������ر�");
				}
				return;
			}
		}
		close();
        System.out.println("22rmi�������ر�");
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
	                    System.out.println("����ͻ�������");
	                    threadPool.execute(new RMIServerConversation(clientList.remove(0)));
	                }
	                RMIServer.class.notify();
                }
			}
		}		
	}
	
}
