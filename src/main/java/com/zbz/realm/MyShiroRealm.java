package com.zbz.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.zbz.mapper.PermissionDao;
import com.zbz.mapper.RoleDao;
import com.zbz.model.Permission;
import com.zbz.model.Role;
import com.zbz.model.User;
import com.zbz.service.UserService;
import com.zbz.utils.SpringUtil;
import com.zbz.utils.UserUtil;

@Service
public class MyShiroRealm extends AuthorizingRealm {

	private static final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		logger.info("权限认证开始......");
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		String username = usernamePasswordToken.getUsername();
		UserService userService = SpringUtil.getBean(UserService.class);
		User user = userService.getUser(username);
		if (user == null) {
			throw new UnknownAccountException("用户名不存在");
		}
		if (!user.getPassword()
				.equals(userService.passwordEncoder(new String(usernamePasswordToken.getPassword()), user.getSalt()))) {
			throw new IncorrectCredentialsException("密码错误");
		}
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(),
				ByteSource.Util.bytes(user.getSalt()), getName());
		UserUtil.setUserSession(user);
		logger.info("权限认证结束......");
		return authenticationInfo;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		logger.info("授权开始......");
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		User user = UserUtil.getCurrentUser();
		List<Role> roles = SpringUtil.getBean(RoleDao.class).listByUserId(user.getId());
		Set<String> roleNames = new HashSet<String>();
		if (roles != null && roles.size() > 0) {
			for (int i = 0; i < roles.size(); i++) {
				Role role = roles.get(i);
				if (!StringUtils.isEmpty(role.getName())) {
					roleNames.add(role.getName());
				}

			}
		}

		authorizationInfo.setRoles(roleNames);
		List<Permission> permissionList = SpringUtil.getBean(PermissionDao.class).listByUserId(user.getId());
		UserUtil.setPermissionSession(permissionList);
		Set<String> permissions = new HashSet<String>();
		if (permissionList != null && permissionList.size() > 0) {
			for (int i = 0; i < permissionList.size(); i++) {
				Permission permission = permissionList.get(i);
				if (!StringUtils.isEmpty(permission.getPermission())) {
					permissions.add(permission.getPermission());
				}

			}
		}
		authorizationInfo.setStringPermissions(permissions);
		logger.info("授权结束......");
		return authorizationInfo;
	}

}
