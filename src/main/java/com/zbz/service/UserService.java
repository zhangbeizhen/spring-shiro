package com.zbz.service;

 
import com.zbz.model.User;

public interface UserService {

	User saveUser(User userDto);
	
	String passwordEncoder(String credentials, String salt);

	User getUser(String username);
}
