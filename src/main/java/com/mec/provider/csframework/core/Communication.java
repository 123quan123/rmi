 package com.mec.provider.csframework.core;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.mec.provider.csframework.message.NetMessage;

public abstract class Communication {
     private Socket socket;
     
     DataInputStream dis;
     DataOutputStream dos;
     
     private ICommunicationAction communicationAction;
     
     public Communication(Socket socket) throws IOException {
         this.socket = socket;
         this.dis = new DataInputStream(socket.getInputStream());
         this.dos = new DataOutputStream(socket.getOutputStream());
         System.out.println(socket);
     }

     public ICommunicationAction getCommunicationAction() {
        return communicationAction;
    }

    public void setCommunicationAction(ICommunicationAction communicationAction) {
        this.communicationAction = communicationAction;
    }

    public abstract void dealAbnormalDrop(String message);
     
    NetMessage read() throws IOException {
         String message = null;
         message = dis.readUTF();
         return new NetMessage(message);
     }
     
     void write(NetMessage message) throws IOException {
         String messageStr = message.toString();
         dos.writeUTF(messageStr);
     }
     
     boolean checkStatus() throws IOException{
//         System.out.println("½øÈë²éÑ¯");
        if (dis.available() > 0) {
            System.out.println("dis.available() > 0");
             NetMessage message = read();
             System.out.println("message"+ message);
             communicationAction.dealNetMessage(message, dis, dos);
             return true;
         }
         return false;
     }
    
     public void close() {
         if (socket != null) {
             try {
                 socket.close();
             } catch (IOException e) {
                 socket = null;
             }
         }
         if (dos != null) {
             try {
                 dos.close();
             } catch (IOException e) {
                 dos = null;
             }
         }
         if (dis != null) {
             try {
                 dis.close();
             } catch (IOException e) {
                 dis = null;
             }
         }
     }
     
}
