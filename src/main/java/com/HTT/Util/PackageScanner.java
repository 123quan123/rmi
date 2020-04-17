package com.HTT.Util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class PackageScanner {
	
	public PackageScanner() {
	}
	
	public abstract void dealClass(Class<?> klass);
	
	private void dealClassFile(String rootPackage, File curFile) {
		String fileName = curFile.getName();
//		System.out.println("sc   " + fileName);
		if (fileName.endsWith(".class")) {
			fileName = fileName.replaceAll(".class", "");
			try {
//				System.out.println("...  " + rootPackage + "." + fileName);
				Class<?> klass = Class.forName(rootPackage + "." + fileName);
				dealClass(klass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void dealDirectory(String rootPackage, File curFile) {
		File[] fileList = curFile.listFiles();

//		System.out.println("00  " + rootPackage);
		
		for (File file : fileList) {
			if (file.isDirectory()) {
			//按照文件处理
				dealDirectory(rootPackage + '.' + file.getName(), file);
			} else if (file.isFile()) {
			//按照.class处理
				dealClassFile(rootPackage, file);
			}
		} 
	}
	
	private void dealJarPackage(URL url) {
		try {
			JarURLConnection connection = (JarURLConnection) url.openConnection();
			JarFile jarFile = connection.getJarFile();
			Enumeration<JarEntry> jarEntries = jarFile.entries();
			while (jarEntries.hasMoreElements()) {
				JarEntry jar = jarEntries.nextElement();
				if(jar.isDirectory() || !jar.getName().endsWith(".class")) {
					continue;
				}
				String jarName = jar.getName();
				jarName = jarName.replace(".class", "");
				jarName = jarName.replace("/", ".");
				
				try {
					System.out.println(jarName);
					Class<?> klass = Class.forName(jarName);
					dealClass(klass);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void packageScanner(String packageName) {
		String rootPacksge = packageName;
//		System.out.println("packageName : " + packageName);
		packageName = packageName.replace(".", "/");
//		System.out.println("packageName : " + packageName);
		URL url = Thread.currentThread().getContextClassLoader().getResource(packageName);
//		System.out.println("URL : " + url);
		if (url.getProtocol().equals("file")) {
			URI uri;
			try {
				uri = url.toURI();
				File root = new File(uri);
				dealDirectory(rootPacksge, root);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else {
			dealJarPackage(url);
		}
	}
}
