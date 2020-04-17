 package com.mec.provider.csframework.core;

import java.io.DataOutputStream;

import com.mec.provider.csframework.message.ENetCommand;
import com.mec.provider.csframework.message.IDealNetmessage;
import com.mec.provider.csframework.message.NetMessage;
import com.mec.util.MThreadPool;

public class ClientConversationDeal implements Runnable, IDealNetmessage {
    private ClientConversation node;
    private NetMessage message;
    
    @Override
    public void dealCommand(Object node, NetMessage message, DataOutputStream dos) {
        this.message = message;
        this.node = (ClientConversation) node;
        new MThreadPool().newInstance().execute(this);
    }
    
    @Override
    public void run() {
        ENetCommand command = message.getCommand();
//        String serviceName = message.getAction();
//        String ipPort = this.message.getPara();
//        String para[] = ipPort.split(":");
//        String ip = para[0];
//        int port = Integer.valueOf(para[1]);
//        Node node = new Node(ip, port); 
        switch (command) {
            case IS_ON_LINE:
                System.out.println("���������");
                break;
            case REGISTRY_FAIL:
                System.out.println("ע��ʧ��");
                break;
            case REGISTRY_SUCCESS:
                System.out.println("ע��ɹ�");
                break;
            case OUT_FAIL:
                System.out.println("ע��ʧ��");
                break;
            case OUT_SUCCESS:
                System.out.println("ע���ɹ�");
                this.node.setGoon(false);
                this.node.close();
                break;
            default:
                break;
        }
    }
    
}
