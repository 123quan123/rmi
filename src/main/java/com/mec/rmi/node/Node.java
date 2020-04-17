 package com.mec.rmi.node;

public class Node implements INetNode{
    private String ip;
    private int port;
    private int sendTime;
    
    public Node() {
    }
    
    public Node(int port) {
        this.port = port;
    }
    
    public Node(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
         return ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
         return port;
    }

    public int setSendTime() {
        return this.sendTime++;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + port;
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
        Node other = (Node)obj;
        if (ip == null) {
            if (other.ip != null)
                return false;
        } else if (!ip.equals(other.ip))
            return false;
        if (port != other.port)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Node [ip=" + ip + ", port=" + port + ", sendTime=" + sendTime + "]";
    }

    public int getSendTime() {
        
         return sendTime;
    }

}
