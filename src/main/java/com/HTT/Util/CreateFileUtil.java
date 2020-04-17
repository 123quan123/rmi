 package com.HTT.Util;

import java.io.File;
import java.io.IOException;

public class CreateFileUtil {
     public static boolean createFile(String destFileName) {
         File file = new File(destFileName);
         if(file.exists()) {
             System.out.println("���������ļ�" + destFileName + "ʧ�ܣ�Ŀ���ļ��Ѵ��ڣ�");
             return false;
             }
         if (destFileName.endsWith(File.separator)) {
             System.out.println("���������ļ�" + destFileName + "ʧ�ܣ�Ŀ���ļ�����ΪĿ¼��");
             return false;        }
         //�ж�Ŀ���ļ����ڵ�Ŀ¼�Ƿ����?
         if(!file.getParentFile().exists()) {
             //���Ŀ���ļ����ڵ�Ŀ¼�����ڣ��򴴽���Ŀ�?
             System.out.println("Ŀ���ļ�����Ŀ¼�����ڣ�׼����������");
             if(!file.getParentFile().mkdirs()) {
                 System.out.println("����Ŀ���ļ�����Ŀ¼ʧ�ܣ�");
                 return false;
                 }
             }
         //����Ŀ���ļ�
         try {
             if (file.createNewFile()) {
                 System.out.println("���������ļ�" + destFileName + "�ɹ���");
                 return true;
                 } else {
                     System.out.println("���������ļ�" + destFileName + "ʧ�ܣ�");
                     return false;
                     }
             } catch (IOException e) {
                 e.printStackTrace();
                 System.out.println("���������ļ�" + destFileName + "ʧ�ܣ�" + e.getMessage());
                 return false;
                 }
         }
     public static boolean createDir(String destDirName) {
         File dir = new File(destDirName);
         if (dir.exists()) {
             System.out.println("����Ŀ¼" + destDirName + "ʧ�ܣ�Ŀ��Ŀ¼�Ѿ�����");
             return false;
             }
         if (!destDirName.endsWith(File.separator)) {
             destDirName = destDirName + File.separator;
             }
         //����Ŀ¼
         if (dir.mkdirs()) {
             System.out.println("����Ŀ¼" + destDirName + "�ɹ���");
             return true;
             } else {
                 System.out.println("����Ŀ¼" + destDirName + "ʧ�ܣ�");
                 return false;
                 }
         }
     public static String createTempFile(String prefix, String suffix, String dirName) {
         File tempFile = null;
         if (dirName == null) {
             try{
                 //��Ĭ���ļ����´�����ʱ�ļ�
                 tempFile = File.createTempFile(prefix, suffix);
                 //������ʱ�ļ���·��
                 return tempFile.getCanonicalPath();
                 } catch (IOException e) {
                     e.printStackTrace();
                     System.out.println("������ʱ�ļ�ʧ�ܣ�" + e.getMessage());
                     return null;
                     }
             } else {
                 File dir = new File(dirName);
                 //�����ʱ�ļ�����Ŀ¼�����ڣ����ȴ���?
                 if (!dir.exists()) {
                     if (!CreateFileUtil.createDir(dirName)) {
                         System.out.println("������ʱ�ļ�ʧ�ܣ����ܴ�����ʱ�ļ����ڵ�Ŀ¼��");
                         return null;
                         }
                     }
                 try {
                     //��ָ��Ŀ¼�´�����ʱ�ļ�
                     tempFile = File.createTempFile(prefix, suffix, dir); 
                     return tempFile.getCanonicalPath();
                     } catch (IOException e) {
                         e.printStackTrace();
                         System.out.println("������ʱ�ļ�ʧ�ܣ�" + e.getMessage());
                         return null;
                         }
                 }
         }  
 }
