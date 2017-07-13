package org.dubbo.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dubbo.user.api.entity.User;
import com.dubbo.user.api.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	@ResponseBody
	public String hello(String name) {
		return userService.sayHello(name);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@ResponseBody
	public User login(String name,String password) {
		return userService.login(name, password);
	}
}
