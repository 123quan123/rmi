 package com.mec.provider.csframework.core;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.mec.provider.csframework.message.IDealNetmessage;
import com.mec.rmi.node.INetNode;
import com.mec.util.MThreadPool;

public class Client {
     private static final int DEFAULT_PORT = 54188;
     private static final String DEFAULT_IP = "192.168.79.1";
     
     private int port;
     private String ip;
     
     private Socket client;
     
     private ClientConversation clientConversation;
     private IDealNetmessage dealNetmessage;
     private IClientAction clientAction;
     
     public Client() {
         init();
     }
     
     public void setDealNetMessage(IDealNetmessage dealNetMessage) {
         this.dealNetmessage = dealNetMessage;
     }
     
     private void init() {
         this.port = DEFAULT_PORT;
         this.ip = DEFAULT_IP;
     }
     
     public void setClientAction(IClientAction clientAction) {
        this.clientAction = clientAction;
    }

    public boolean connectToServer() {
         if (clientAction == null || dealNetmessage == null) {
             System.out.println("请先设置clientAction或者dealNetmessage");
             return false;
         }
         if (ip != null && port != 0) {
             try {
                client = new Socket(ip, port);
                clientConversation = new ClientConversation(this, client);
                clientConversation.setDealNetMessage(this.dealNetmessage);
                new MThreadPool().newInstance().execute(clientConversation);
                return true;
            } catch (Exception e) {
                clientAction.canotConnect(this);
                return false;
            }
         }
         return false;
     }
     
     @SuppressWarnings("unused")
    public void registry(String ServiceName, INetNode node) {
         try {
            node.setIp(String.valueOf(InetAddress.getLocalHost().getHostAddress()));
        } catch (UnknownHostException e) {
        }
         clientConversation.registryNode(ServiceName, node);
         if (node == null) {
             clientAction.registryFail(ServiceName, node);
         }
     }
     
     public void out(String ServiceName, INetNode node) {
         try {
            node.setIp(String.valueOf(InetAddress.getLocalHost().getHostAddress()));
        } catch (UnknownHostException e) {
        }
         INetNode returnNode = clientConversation.outNode(ServiceName, node);
         if (returnNode == null) {
             clientAction.outFail(ServiceName, node);
         }
     }
     public void close() {
        clientConversation.close();
    }
     
}
