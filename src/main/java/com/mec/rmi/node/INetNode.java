 package com.mec.rmi.node;

public interface INetNode {

     void setIp(String ip);
     void setPort(int port);
     String getIp();
     int getPort();
     int setSendTime();
     int getSendTime();
}
