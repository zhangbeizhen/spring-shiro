package com.zbz.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.zbz.constants.UserConstants;
import com.zbz.mapper.UserDao;
import com.zbz.model.User;
//import com.zbz.model.User.Status;
import com.zbz.service.UserService;
import com.zbz.utils.UserUtil;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Override
	@Transactional
	public User saveUser(User userDto) {
		User user = userDto;
		user.setSalt(DigestUtils
				.md5Hex(UUID.randomUUID().toString() + System.currentTimeMillis() + UUID.randomUUID().toString()));
		user.setPassword(passwordEncoder(user.getPassword(), user.getSalt()));
		userDao.save(user);
		saveUserRoles(user.getId(), userDto.getRoleIds());
		logger.info("新增用户: ", user.getUsername());
		return user;
	}

	private void saveUserRoles(Long userId, List<Long> roleIds) {
		if (roleIds != null) {
			userDao.deleteUserRole(userId);
			if (!CollectionUtils.isEmpty(roleIds)) {
				userDao.saveUserRoles(userId, roleIds);
			}
		}
	}

	@Override
	public String passwordEncoder(String credentials, String salt) {
		Object object = new SimpleHash("MD5", credentials, salt, UserConstants.HASH_ITERATIONS);
		return object.toString();
	}

	@Override
	public User getUser(String username) {
		return userDao.getUser(username);
	}

}
