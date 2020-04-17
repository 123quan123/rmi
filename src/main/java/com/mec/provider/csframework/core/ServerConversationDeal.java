 package com.mec.provider.csframework.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import com.mec.dubbo.registry.RegistryAction;
import com.mec.provider.csframework.message.ENetCommand;
import com.mec.provider.csframework.message.IDealNetmessage;
import com.mec.provider.csframework.message.NetMessage;
import com.mec.rmi.node.INetNode;
import com.mec.rmi.node.Node;
import com.mec.util.MThreadPool;

public class ServerConversationDeal implements Runnable, IDealNetmessage {
    private ServerConversation node;
    private NetMessage message;
    private DataOutputStream dos;
    
    @Override
    public void dealCommand(Object node, NetMessage message, DataOutputStream dos) {
        this.message = message;
        this.node = (ServerConversation) node;
        this.dos = dos;
        new MThreadPool().newInstance().execute(this);
    }
    
    @Override
    public void run() {
        NetMessage message = new NetMessage();
        ENetCommand command = this.message.getCommand();
        String serviceName = this.message.getAction();
        String ipPort = this.message.getPara();
        String para[] = ipPort.split(":");
        String ip = para[0];
        int port = Integer.valueOf(para[1]);
        message.setAction(serviceName);
        message.setPara(ipPort);
        Node node = new Node(ip, port);
        this.node.setINetNode(node);
        switch (command) {
            case REGISTRY:
                System.out.println("注册: " + node);
                boolean ok = RegistryAction.registryService(serviceName, node);
                if (ok) {
                    message.setCommand(ENetCommand.REGISTRY_SUCCESS);
                    this.node.setRegistryNode(node);
                } else {
                    message.setCommand(ENetCommand.REGISTRY_FAIL);
                }
                try {
                    System.out.println(message);
                    
                    dos.writeUTF(message.toString());
                } catch (IOException e) {
                    this.node.dealAbnormalDrop(serviceName + ":" + ipPort);
                    this.node.close();
                }
                break;
            case OUT:
                System.out.println("注销");
                INetNode netnode = RegistryAction.outService(serviceName, node);
                if (netnode != null) {
                    message.setCommand(ENetCommand.OUT_SUCCESS);
                } else {
                    message.setCommand(ENetCommand.OUT_FAIL);
                }
                try {
                    if (message.getCommand() == ENetCommand.OUT_SUCCESS) {
                        List<ServerConversation> nodeList = Server.pollMap.get(Server.listNum);
                        if (nodeList.contains(this.node)) {
                            System.out.println("找到节点，准备删除" + this.node);
                            nodeList.remove(this.node);
                            Server.onLineCount--;
                            System.out.println(node + "删除成功");
                        }
                    }
                    dos.writeUTF(message.toString());
                } catch (IOException e) {
                    this.node.dealAbnormalDrop(serviceName + ":" + ipPort);
                    this.node.close();
                }
                break;
          
            default:
                break;
        }
    }
}
