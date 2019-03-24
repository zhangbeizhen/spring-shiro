package com.zbz.controller;

 
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zbz.mapper.UserDao;
import com.zbz.model.User;
import com.zbz.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private UserDao userDao;

	@GetMapping("/save")
	@RequiresPermissions("sys:user:add")
	public User saveUser(@RequestBody  User user) {
		User u = userService.getUser(user.getUsername());
		if (u != null) {
			throw new IllegalArgumentException(user.getUsername() + "已存在");
		}
		return userService.saveUser(user);
	}
 
   //根据用户id获取用户 
	@GetMapping("/{id}")
	@RequiresPermissions("sys:user:query")
	public User user(@PathVariable Long id) {
		logger.info("根据用户id获取用户开始");
		User user = userDao.getById(id);
		logger.info("根据用户id获取用户 结束");
		return user;
	}

}
