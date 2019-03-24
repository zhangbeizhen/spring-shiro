package com.zbz.test;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.SimpleHash;

import com.zbz.constants.UserConstants;

/**
 *  
 * @ClassName: CommonUtils
 * @author: ag
 * @date: 2019年3月23日 下午7:05:35
 */
public class CommonUtils {
	
	
	public static String getSalt(){
		String salt = DigestUtils.md5Hex(UUID.randomUUID().toString() + System.currentTimeMillis() + UUID.randomUUID().toString());
        System.out.println("盐: " + salt);
		return salt;
	}
	
	
	public static String passwordEncoder(String credentials, String salt) {
		
		Object object = new SimpleHash("MD5", credentials, salt, UserConstants.HASH_ITERATIONS);
		System.out.println("加密后密码: " + object.toString());
		return object.toString();
	}
	
	
	public static void main(String []args){
		System.out.println("测试开始.....");
		  
		passwordEncoder("admin",getSalt());
		passwordEncoder("user",getSalt());
		System.out.println("测试开始.....");
	}

}
