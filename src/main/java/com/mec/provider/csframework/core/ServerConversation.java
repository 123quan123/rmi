 package com.mec.provider.csframework.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.mec.dubbo.registry.ProviderNodePool;
import com.mec.provider.csframework.message.ENetCommand;
import com.mec.provider.csframework.message.IDealNetmessage;
import com.mec.provider.csframework.message.NetMessage;
import com.mec.rmi.node.INetNode;
import com.mec.rmi.node.Node;

public class ServerConversation extends Communication implements IConversationNode,
                        ICommunicationAction {
    private INetNode netNode;
    private long linkedTime;
    private IDealNetmessage dealNetMessage;
    private INetNode registryNode;
    
    public ServerConversation(Socket socket) throws IOException {
        super(socket);
        String ip = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();
        this.netNode = new Node();
        this.netNode.setIp(ip);
        this.netNode.setPort(port);
        this.linkedTime = System.currentTimeMillis();
        this.setCommunicationAction(this);
    }
    
    public IDealNetmessage getDealNetMessage() {
        return dealNetMessage;
    }

    public void setDealNetMessage(IDealNetmessage dealNetMessage) {
        this.dealNetMessage = dealNetMessage;
    }
    
    public long getLinkedTime() {
        return linkedTime;
    }

    public void setINetNode(INetNode netNode) {
        this.netNode = netNode;
    }
    
    public INetNode getNetNode() {
        return netNode;
    }
    
    public INetNode getRegistryNode() {
        return registryNode;
    }

    public void setRegistryNode(INetNode registryNode) {
        this.registryNode = registryNode;
    }

    public boolean checkAlive() {
        NetMessage message = new NetMessage();
        message.setCommand(ENetCommand.IS_ON_LINE);
        try {
            write(message);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    @Override
    public void dealAbnormalDrop(String ipPort) {
        String para[] = ipPort.split(":");
        String serviceName = para[0];
        String ip = para[1];
        int port = Integer.valueOf(para[2]);
        
        ProviderNodePool.removeNetNode(serviceName, new Node(ip, port));
        System.out.println("移除节点"+ipPort);
    }

    @Override
    public void dealNetMessage(NetMessage message, DataInputStream dis, DataOutputStream dos) {
        ServerConversation node = ServerConversation.this;
        //开启线程去处理信息（//注册/注销）
        System.out.println("处理信息");
        if (dealNetMessage != null) {
            dealNetMessage.dealCommand(node, message, dos);
        }
         
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((netNode == null) ? 0 : netNode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServerConversation other = (ServerConversation)obj;
        if (netNode == null) {
            if (other.netNode != null)
                return false;
        } else if (!netNode.equals(other.netNode))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ConversationNode [netNode=" + netNode + "]";
    }
    
    @Override
    public Object getNode() {
         return this;
    }


}
