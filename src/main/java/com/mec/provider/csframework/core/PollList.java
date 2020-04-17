package com.mec.provider.csframework.core;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * <ol>
 * ���ܣ���ѯ��
 * <li>��ѯ�ڵ�</li>
 * </ol>
 * @author Quan
 * @date 2020/03/05
 * @version 0.0.1
 */
 public class PollList implements Runnable {
       List<ServerConversation> nodeList;
       private volatile boolean shutDown;
        
       public PollList(List<ServerConversation> nodeList) {
           this.nodeList = nodeList;
           shutDown = false;
       }

        public void setShutDown(boolean shutDown) {
        this.shutDown = shutDown;
    }
        /**
         * checkStatus��д��available()��⣬�ź����ǣ����ֲ��˶Զ�崻�
         */
        @Override
        public void run() {
            while (!shutDown) { 
                Iterator<ServerConversation> iterator = nodeList.iterator();
                while (iterator.hasNext()) {
                    ServerConversation node = iterator.next();
                    try {
                        node.checkStatus();
                    } catch (IOException e) {
                    }
                }
            }
        }
 }