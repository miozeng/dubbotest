package com.dubbo.user.core;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboUserServiceApplication {
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("classpath*:applicationContext*.xml");
	}
}
