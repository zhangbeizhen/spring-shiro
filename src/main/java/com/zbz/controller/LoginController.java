package com.zbz.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

 
@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@GetMapping("/sys/login")
	public void login(String username, String password) {
		logger.info("登入开始....");
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
		SecurityUtils.getSubject().login(usernamePasswordToken);
		// 设置shiro的session过期时间
		SecurityUtils.getSubject().getSession().setTimeout(30 * 60 * 60 * 1000);
		logger.info("登入完成....");
	}

}
