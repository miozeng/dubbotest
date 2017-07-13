package com.dubbo.user.api.service;

import com.dubbo.user.api.entity.User;

public interface UserService {
	
	public String sayHello(String name);
	
	public User login(String name,String password);

}
