package com.zbz.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.zbz.model.Role;

@Mapper
public interface RoleDao {

	@Select("select * from sys_role r inner join sys_role_user ru on r.id = ru.roleId where ru.userId = #{userId}")
	List<Role> listByUserId(Long userId);
}
