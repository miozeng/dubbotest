package com.dubbo.user.core.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.dubbo.user.api.entity.User;
import com.dubbo.user.api.service.UserService;
import com.dubbo.user.core.dao.UserDao;

public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	public String sayHello(String name) {
		return "hello "+name;
	}

	public User login(String name, String password) {
		return userDao.findUniqueByProperty("name", name);
	}

}
