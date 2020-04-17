package com.HTT.Util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContextHolder {
	private volatile static ClassPathXmlApplicationContext context;
	
	public SpringContextHolder() {
	}
	
	public SpringContextHolder(String configePath) {
		if (context == null) {
			synchronized (SpringContextHolder.class) {
				if (context == null) {
					context = new ClassPathXmlApplicationContext(configePath);
				}
			}
		}
	}
	
	public void close() {
		context.close();
		context = null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<?> klass) {
		return (T) context.getBean(klass);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName) {
		return (T) context.getBean(beanName);
	}
}
