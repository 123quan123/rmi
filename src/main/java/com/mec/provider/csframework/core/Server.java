 package com.mec.provider.csframework.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;

import com.mec.provider.csframework.message.IDealNetmessage;

/**
 * ���������ӵĳ�����
 * <ol>
 * ���ܣ�
 * <li>����</li>
 * <li>�ر�</li>
 * <li>��������socket��������ѯ����</li>
 * </ol>
 * @author Quan
 * @date 2020/01/17
 * @version 0.0.1
 */
public class Server implements Runnable {
    private static final int LIST_NUM = 5;
    private static final int DEFAULT_PORT = 54188;
    private static final String DEFAULT_IP = "192.168.79.1";
    private static final int DEFAULT_LIST_NUM = 1;
    private static final int DEFAULT_ONE_LIST_MAXNUM = 10;
    private static int oneListMaxnum = DEFAULT_ONE_LIST_MAXNUM;
    
    static volatile int onLineCount;
    static volatile int listNum = DEFAULT_LIST_NUM;
    public static Map<Integer,List<ServerConversation>> pollMap
                    = new HashMap<Integer, List<ServerConversation>>();
     
    private int port;
    private String ip;
     
    private ThreadPoolExecutor threadPool;
    private ServerSocket serverSocket;
    private PollPool pollPool;
    
    private volatile boolean goon;
    private Object objectLock;
    private IDealNetmessage dealNetmessage;
    
    static {
        for (int i = 0; i < LIST_NUM; i++) {
            pollMap.put(i+1, new CopyOnWriteArrayList<ServerConversation>());
        }
    }
    
    public Server() {
        goon = false;
        port = DEFAULT_PORT;
        ip = DEFAULT_IP;
        objectLock = new Object();
    }
    

    public void setDealNetMessage(IDealNetmessage dealNetMessage) {
        this.dealNetmessage = dealNetMessage;
    }
    

    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPoolExecutor threadPool) {
        this.threadPool = threadPool;
    }

    public boolean startServer() {
        if (dealNetmessage == null) {
            System.out.println("��������dealNetmessage");
            return false;
        }
        if (goon) {
            System.out.println("�����˷������Ѿ�����");
            return true;
        }
        if (threadPool == null) {
            System.out.println("�����˷��������������̳߳�");
            return false;
        }
        if (port == 0)  {
            System.out.println("�����˷������˿ں�δ����");
            return false;
        }
        try {
            goon = true;
            serverSocket = new ServerSocket(port);
            System.out.println(serverSocket);
            synchronized (objectLock) {
                pollPool = new PollPool();
                threadPool.execute(pollPool);
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                     e.printStackTrace();
             }
            }
            new Thread(this, "����������߳�").start();
            System.out.println("�����˷������ѿ���......");
        } catch (IOException e) {
             e.printStackTrace();
        }
        return true;
    }
    
    public boolean shutDown() {
        if (!goon) {
            System.out.println("�����˷�����δ����");
            return false;
        }
        goon = false;
        return true;
    }
    
    public boolean isShutDown() {
        return !goon;
    }

    @Override
    public void run() {
        Socket serverClient = null;
        while (goon) {
            try {
                System.out.println("�����˷�������ʼ����......");
                serverClient = serverSocket.accept();
                System.out.println("�����˷������յ�һ���ͻ�����������");
                if (serverClient == null) {
                    System.out.println("����������˷�����δ����");
                } ServerConversation conversationNode;
                try {
                    conversationNode = new ServerConversation(serverClient);
                    conversationNode.setDealNetMessage(dealNetmessage);
                    pollPool.add(conversationNode);
                } catch (IOException e) {
                     e.printStackTrace();
                }
            } catch (IOException e) {
                if (goon) {
                    System.out.println("�����˷������쳣崻�");
                    goon = false;
                    close();
                }
                e.printStackTrace();
            }
        }
        close();
    }
    
    public void close() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                serverSocket = null;
            }
        }
    }
    
    /**
     * ��ѯ��
     * <ol>
     * ���ܣ�
     * <li>��ѯ���Ӷ��м���Ƿ�����Ϣ��Ҫ����</li>
     * <li>����Ҫ��������Ӹ��߳�����</li>
     * <li>ά����ѯ����</li>
     * </ol>
     * @author Quan
     * @date 2020/01/17
     * @version 0.0.1
     */
    class PollPool implements Runnable {
        
        public PollPool() {
            onLineCount = 0;
        }
        
        public int getOnLineCount() {
            return onLineCount;
        }

        public void setOnLineCount(int onLineCount) {
            Server.onLineCount = onLineCount;
        }

        public ThreadPoolExecutor getThreadPool() {
            return threadPool;
        }

        public int getListNum() {
            return listNum;
        }

        public void setListNum(int listNum) {
            Server.listNum = listNum;
        }

        public int getOneListMaxnum() {
            return oneListMaxnum;
        }

        public void setOneListMaxnum(int oneListMaxnum) {
            Server.oneListMaxnum = oneListMaxnum;
        }

        public synchronized boolean add(ServerConversation serverClientNode) {
            List<ServerConversation> cList = pollMap.get(listNum);
            if (cList.size() < DEFAULT_ONE_LIST_MAXNUM ) {
               cList.add(serverClientNode);
               System.out.println("�б���Conversation������"+cList.size());
               onLineCount++;
               return true;
            } else if (listNum < LIST_NUM) {
                listNum++;
                List<ServerConversation> newList = new CopyOnWriteArrayList<ServerConversation>();
                newList.add(serverClientNode);
                threadPool.execute(new PollList(newList));
                pollMap.put(listNum, newList);
                onLineCount++;
                return true;
            } else {
                System.out.println("�ܾ����Ӳ���");
            }
            return false;
        }
        
        public boolean remove(ServerConversation conversationNode) {
            if (conversationNode == null) {
                return false;
            }
            synchronized (pollMap) {
                for (int index = 0; index < pollMap.size(); index ++) {
                    List<ServerConversation> nodeList = pollMap.get(index+1);
                    for (ServerConversation conversationNode2 : nodeList) {
                        if (conversationNode.equals(conversationNode2)) {
                            nodeList.remove(conversationNode);
                            return true;
                        }
                    }
                }
            }
            return false;
        }
       
        
        @Override
        public void run() {
            synchronized (objectLock) {
                objectLock.notify();
            }
            System.out.println("������ѯ���߳�");
            List<ServerConversation> nodeList = pollMap.get(1);
            PollList pollList = new PollList(nodeList);
            threadPool.execute(pollList);
        }
    }
}
