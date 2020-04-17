 package com.mec.provider.csframework.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.mec.provider.csframework.message.ENetCommand;
import com.mec.provider.csframework.message.IDealNetmessage;
import com.mec.provider.csframework.message.NetMessage;
import com.mec.rmi.node.INetNode;
import com.mec.rmi.node.Node;

public class ClientConversation extends Communication implements Runnable, IConversationNode,
                               ICommunicationAction {
    private INetNode netNode;
    private long linkedTime;
    private IDealNetmessage dealNetMessage;
    private Client client;
    private volatile boolean goon = false;

    public ClientConversation(Client client, Socket socket) throws IOException {
        super(socket);
        this.client = client;
        String ip = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();
        this.netNode = new Node();
        this.netNode.setIp(ip);
        this.netNode.setPort(port);
        this.linkedTime = System.currentTimeMillis();
        this.setCommunicationAction(this);
        goon = true;
    }

    public IDealNetmessage getDealNetMessage() {
        return dealNetMessage;
    }

    public void setDealNetMessage(IDealNetmessage dealNetMessage) {
        this.dealNetMessage = dealNetMessage;
    }

    public INetNode getNetNode() {
        return netNode;
    }

    public void setNetNode(INetNode netNode) {
        this.netNode = netNode;
    }

    public long getLinkedTime() {
        return linkedTime;
    }

    public void setLinkedTime(long linkedTime) {
        this.linkedTime = linkedTime;
    }

    public void setGoon(boolean goon) {
        this.goon = goon;
    }

    @Override
    public void dealNetMessage(NetMessage message, DataInputStream dis, DataOutputStream dos) {
        dealNetMessage.dealCommand(this, message, dos);
    }

    @Override
    public void dealAbnormalDrop(String message) {
       while (!client.connectToServer()) {
           System.out.println("重新连接");
           client.connectToServer();
       }
       System.out.println("重新连接成功");
    }

    @Override
    public Object getNode() {
         return this;
    }

    public INetNode registryNode(String serviceName,INetNode node) {
        NetMessage message = new NetMessage();
        message.setCommand(ENetCommand.REGISTRY);
        message.setAction(serviceName);
        message.setPara(node.getIp() + ":" + String.valueOf(node.getPort()));
        
        try {
            write(message);
        } catch (IOException e) {
            dealAbnormalDrop("对端异常掉线");
            return null;
        }
        return netNode;
    }

    public INetNode outNode(String serviceName, INetNode node) {
        NetMessage message = new NetMessage();
        message.setCommand(ENetCommand.OUT);
        message.setAction(serviceName);
        message.setPara(node.getIp() + ":" + String.valueOf(node.getPort()));
        
        try {
            write(message);
        } catch (IOException e) {
            dealAbnormalDrop("对端异常掉线");
            return null;
        }

        return netNode;
    }

    @Override
    public void run() {
        while (goon) {

            NetMessage info;
            try {
                info = read();
                dealNetMessage.dealCommand(this, info, dos);
            } catch (IOException e) {

                dealAbnormalDrop("对端异常掉线");
                return;
            }
           
        }
    }

}
