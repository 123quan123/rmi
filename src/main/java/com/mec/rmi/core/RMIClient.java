package com.mec.rmi.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;

import com.mec.loadbalance.core.INetNodeSelector;
import com.mec.rmi.node.INetNode;
import com.mec.util.ITimer;
import com.mec.util.Timer2;

public class RMIClient {
    private static final int DEFAULT_PORT = 54199;
    private static final String DEFAULT_IP = "192.168.41.1";
    
    
    
	private int rmiPort;
	private String rmiIp;
	
	private Socket socket;
	private INetNodeSelector netNodeSelector;
	
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private volatile boolean ok;
	private volatile boolean waiting;
	private String resultStr;
	
	public RMIClient() {
	    ok = true;
	    waiting = true;
	    if (netNodeSelector == null) {
	        setRmiIp(DEFAULT_IP);
	        setRmiPort(DEFAULT_PORT);
	    } else {
	        INetNode netNode = netNodeSelector.getRightNode();
	        setRmiIp(netNode.getIp());
	        setRmiPort(netNode.getPort());
	    }
    }
	
	public RMIClient(int port, String ip) {
        ok = true;
        waiting = true;
		setRmiIp(ip);
		setRmiPort(port);
	}

	public int getRmiPort() {
		return rmiPort;
	}

	public void setRmiPort(int rmiPort) {
		this.rmiPort = rmiPort;
	}

	public String getRmiIp() {
		return rmiIp;
	}

	public void setRmiIp(String rmiIp) {
		this.rmiIp = rmiIp;
	}
	
	public INetNodeSelector getNetNodeSelector() {
        return netNodeSelector;
    }

    public void setNetNodeSelector(INetNodeSelector netNodeSelector) {
        this.netNodeSelector = netNodeSelector;
    }

    boolean connectToServer() {
		if (rmiIp == null || rmiPort == 0){
			return false;
		}
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			System.out.println("�ͻ���������");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

    public Object invokeMethod(Method method, Object[] args, boolean isContinue) throws Exception{
        boolean con = connectToServer();
        if (!con && isContinue) {
            con = connectToServer();
            while (!con && isContinue) {
                System.out.println("��������");
            }
        }
        if (!con) {
            throw new Exception("����ʧ��");
        }
		Object result = null;
		ArgumensMaker argumensMaker = new ArgumensMaker();
		if (args != null) {
		    int index = 0;
		  for (Object arg : args) {
            index ++;
		  }
		}
		try {
			dos.writeUTF(ArgumensMaker.GSON.toJson(method.getName().hashCode()));
        	dos.writeUTF(argumensMaker.mapToJson());
        	String okStr = dis.readUTF();
        	if (okStr.equalsIgnoreCase("true") || okStr.equalsIgnoreCase("false")) {
        	    if (okStr.equalsIgnoreCase("false")) {
        	        close();
                    throw new Exception("δ�ҵ���Ӧ�ķ���");
        	    }
        	}
        	
            ITimer task = new ITimer() {
                public void work() {
                    try {
                        if (dis.available() > 0) {
                            ok = false;
                        }
                    } catch (IOException e) {
                        System.out.println("-----------------------------");
                    }
                }
                public void end() {
                  waiting = false;
                }
            };
            Timer2 timer2 = new Timer2(10, task, false);
            timer2.startWork();

            while (ok && waiting) {
            }

            if (ok) {
                close();
                throw new Exception("�������ӳ٣����Ժ�����");
            }
            timer2.stopWork();
            resultStr = dis.readUTF();

            System.out.println("resultStr"+resultStr);
            if (resultStr.equalsIgnoreCase("{}")) {
                close();
                return "ok";
            }
            result = ArgumensMaker.GSON.fromJson(resultStr, method.getGenericReturnType());
            
            close();
        } catch (IOException e) {
           close();
           throw new Exception("�������쳣崻�");
        }
		
		return result;
	}
	
	private void close() {
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
