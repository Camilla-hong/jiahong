package hzu.zhang.dao;

import hzu.zhang.base.BaseMapper;
import hzu.zhang.vo.User;
import org.apache.ibatis.annotations.Mapper;


public interface UserMapper extends BaseMapper<User,Integer> {

//    通过用户名查询用户记录，返回用户对象
    public User queryUserByName(String userName);


}