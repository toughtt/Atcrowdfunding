package com.atguigu.atcrowdfunding.manager.service.Impl;

import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.exception.LoginFailException;
import com.atguigu.atcrowdfunding.manager.dao.UserMapper;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.util.Const;
import com.atguigu.atcrowdfunding.util.MD5Util;
import com.atguigu.atcrowdfunding.util.Page;
import com.atguigu.atcrowdfunding.vo.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wall
 * @data 20 - 11:56
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page queryPage(Map paramMap) {
        Page page = new Page((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));
        Integer startIndex = page.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<User> datas = userMapper.queryList(paramMap);
        Integer totalsize = userMapper.queryCount(paramMap);

        page.setDatas(datas);
        page.setTotalsize(totalsize);
        return page;
    }

    /*
    @Override
    public Page queryPage(Integer pageno, Integer pagesize) {
        Page page = new Page(pageno,pagesize);
        Integer startIndex = page.getStartIndex();
       List<User> datas = userMapper.queryList(startIndex,pagesize);
       Integer totalsize = userMapper.queryCount();

        page.setDatas(datas);
        page.setTotalsize(totalsize);
        return page;
    }*/

    @Override
    public User queryUserLogin(Map<String, Object> paramMap) {
        User user = userMapper.queryUserLogin(paramMap);
        if(user==null){
            throw new LoginFailException("用户账号或密码不正确");
        }
        return user;
    }

    @Override
    public int saveUser(User user) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();

        String createtime = sdf.format(date);

        user.setCreatetime(createtime);

        user.setUserpswd(MD5Util.digest(Const.PASSWORD));
        return userMapper.insert(user);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    public int deleteUser(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteBatchUser(Integer[] ids) {
        int totalCount = 0;
        for (Integer id : ids) {
            int count = userMapper.deleteByPrimaryKey(id);
            totalCount+=count;
        }
        if(totalCount!=ids.length){
            throw new RuntimeException("批量删除失败");
        }
        return totalCount;
    }

    @Override
    public int deleteBatchUserByVo(Data data) {
        List<User> userList = data.getUserList();
        return userMapper.deleteBatchUserByVo( userList);
    }

    @Override
    public List<Role> querAllRole() {
        return userMapper.querAllRole();
    }

    @Override
    public List<Integer> queryRoleByUserid(Integer id) {
        return userMapper.queryRoleByUserid(id);
    }

    @Override
    public int saveUserRoleRelationship(Integer userid, Data data) {
        return userMapper.saveUserRoleRelationship(userid, data);
    }

    @Override
    public int deleteUserRoleRelationship(Integer userid, Data data) {
        return userMapper.deleteUserRoleRelationship(userid,data);
    }
}
