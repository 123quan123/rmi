 package com.mec.rmi.core;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.HTT.Util.PackageScanner;
import com.mec.rmi.annotation.RMIInterfaces;

public class MethodFactory {
    private static Map<Integer, MethodDef> methodPool;
    
    static {
        methodPool = new HashMap<Integer, MethodDef>();
    }
     
     public MethodFactory() {
     }
     
     public void registryMethod(Class<?> interfaces, Class<?> klass) {
         if (interfaces == null || klass == null 
             || !interfaces.isInterface() || klass.isInterface()
             || !interfaces.isAssignableFrom(klass)) {
             return;
         }
        try {
             Method[] methods = interfaces.getDeclaredMethods();
            Object object = klass.newInstance();
            for (Method method : methods) {
               int methodCode = method.getName().hashCode();
               MethodDef md = new MethodDef(method, object);
               methodPool.put(methodCode, md);
           }
        } catch (InstantiationException e) {
             e.printStackTrace();
        } catch (IllegalAccessException e) {
             e.printStackTrace();
        }
     }
     
     public void collectionMethod(String path) {
         new PackageScanner() {
            
            @Override
            public void dealClass(Class<?> klass) {
                if (klass.isAnnotationPresent(RMIInterfaces.class)) {
                    RMIInterfaces interfaces = klass.getAnnotation(RMIInterfaces.class);
                    Class<?>[] interfacesKlasses = interfaces.rmiInterfaces();
                    for (Class<?> class1 : interfacesKlasses) {
                        registryMethod(class1, klass);
                    }
                }
            }
        }.packageScanner(path);
     }
     
     public static MethodDef getMethodObject(int methodCode) {
         return methodPool.get(methodCode);
     }
     
     public static Collection<MethodDef> getValues() {
         return methodPool.values();
     }
}
