package com.atguigu.atcrowdfunding.manager.dao;

import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.vo.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

	/*User queryUserogin(Map<String, Object> paramMap);*/

    User queryUserLogin(Map<String, Object> paramMap);

    /*List<User> queryList(@Param("startIndex") Integer startIndex, @Param("pagesize") Integer pagesize);

    Integer queryCount();*/

    List<User> queryList(Map<String,Object> paramMap);

    Integer queryCount(Map<String,Object> paramMap);

    /*int deleteBatchUserByVo(@Param("userList") List<User> userList);*/

    int deleteBatchUserByVo( List<User> userList);
    List<Role> querAllRole();

    List<Integer> queryRoleByUserid(Integer id);

    int saveUserRoleRelationship(@Param("userid")Integer userid, @Param("data")Data data);

    int deleteUserRoleRelationship(@Param("userid")Integer userid, @Param("data")Data data);
}