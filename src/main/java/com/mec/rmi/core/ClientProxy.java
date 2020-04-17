package com.mec.rmi.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.swing.JFrame;

import com.mec.rmi.annotation.MethodDialog;
import com.mec.rmi.dialog.MyDialog;

public class ClientProxy {
	private RMIClient rmiClient;

	public void setRmiClient(RMIClient rmiClient) {
		this.rmiClient = rmiClient;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<?> klass, final boolean isContinue) {
		ClassLoader loader = klass.getClassLoader();
		
		return (T) Proxy.newProxyInstance(loader, new Class<?>[]{klass}, new InvocationHandler() {
			
			public Object invoke(Object proxy, Method method, Object[] args) throws Exception{
				Object result = rmiClient.invokeMethod(method, args, isContinue);
				System.out.println("返回结果是：" + result);
				return result;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
    public <T> T getProxy(Class<?> klass, final JFrame parentFrame, final boolean isContinue) {
        ClassLoader loader = klass.getClassLoader();
        
        return (T) Proxy.newProxyInstance(loader, new Class<?>[]{klass}, new InvocationHandler() {
            
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result;                
                if (method.isAnnotationPresent(MethodDialog.class)) {
                    MethodDialog MethodDialog = method.getAnnotation(MethodDialog.class);
                    MyDialog dialog = new MyDialog(parentFrame, true).setCaption(MethodDialog.caption());
                    dialog.setMethod(method);
                    dialog.setArgus(args);
                    dialog.setRmiClient(rmiClient);
                    dialog.showDialog();
                    result = dialog.getResult();
                    System.out.println("返回结果是：" + result);
                    return result;
                } else {
                    result = rmiClient.invokeMethod(method,args,isContinue);
                }
                System.out.println("返回结果是：" + result);
                return result;
            }
        });
    }
	
}
