 package com.mec.rmi.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.net.Socket;

public class RMIServerConversation implements Runnable {
     private Socket client;
     private DataInputStream dis;
     private DataOutputStream dos;
     
     public RMIServerConversation(Socket client) {
         this.client = client;
         try {
             dis = new DataInputStream(client.getInputStream());
             dos = new DataOutputStream(client.getOutputStream());
         } catch (IOException e) {
              e.printStackTrace();
         }
     }
     
     private Object[] getPara(Method method, ArgumensMaker argumensMaker) throws Exception {
         int paraLen = method.getParameterCount();
         if (paraLen == 0) {
             return null;
         }
         int index = 0;
         Object[] para = new Object[paraLen];
         for (Parameter parameter : method.getParameters()) {
             Type type = parameter.getParameterizedType();
             Object obj = argumensMaker.getValue("arg" + index, type);
             para[index] = obj;
             index++;
         }
         return para;
     }
     
    @Override
    public void run() {
         try {
            String methodStr = dis.readUTF();
            MethodDef md = MethodFactory.getMethodObject(Integer.valueOf(methodStr));
            if (md == null) {
                dos.writeUTF("false");
                System.out.println("没有此方法");
                try {
                    while(true) {
                        dis.readUTF();
                    }
                } catch (IOException e) {
                    close();
                }
                return;
            } else {
                dos.writeUTF("true");
            }

            String argsStr = dis.readUTF();
            ArgumensMaker argumensMaker = new ArgumensMaker(argsStr);

            Method method = md.getMethod();
            Object object = md.getObject();
            Object result = null;
            try {
                Object[] para = getPara(method, argumensMaker);
           
                if (para != null) {
                    System.out.println("///////////////////////////////");
                    System.out.println("method" + method);
                    System.out.println("class" + object.getClass());
                    System.out.println("para" + para);
                    result = method.invoke(object, para);
                    System.out.println("result" + result);
                    System.out.println("///////////////////////////////");
                 } else {
                    result = method.invoke(object);
                 }
                
            } catch (Exception e) {
                 System.out.println("调用函数出错");
                 close();
                 return;
            }
                String resultStr = (result == null) ? "{}" : ArgumensMaker.GSON.toJson(result, method.getGenericReturnType());
                dos.writeUTF(resultStr);
             
            System.out.println("处理客户端【" + client.getInetAddress() + "】完成");
        } catch (IOException e) {
            close();
        }
         close();
    }
    

    private void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                client = null;
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
