package hzu.zhang.service;

import hzu.zhang.base.BaseService;
import hzu.zhang.dao.UserMapper;
import hzu.zhang.model.UserModel;
import hzu.zhang.utils.AssertUtil;
import hzu.zhang.utils.Md5Util;
import hzu.zhang.utils.UserIDBase64;
import hzu.zhang.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;


    /**
     * 用户登录
     *      1.参数判断，判断用户姓名，用户密码非空
     *        如果参数为空，抛出异常（异常被控制层捕获或处理）
     *      2.调用数据访问层，通过用户名查询，返回用户对象
     *      3.判断用户对象是否为空
     *        如果对象为空，抛出异常（异常被控制捕获或处理）
     *      4.判断密码是否正确，比较客户端传递的用户密码与数据路中查询的用户对象中的用户密码
     *        如果密码不相等，抛出异常（异常控制层捕获并处理）
     *      5.如果密码正确，登录成功
     *
     * @param userName
     * @param userPwd
     */
    public UserModel userLogin(String userName,String userPwd){
        /* 1.参数判断，判断用户姓名，用户密码非空*/
        checkLoginParams(userName,userPwd);

        /*2.调用*/
        User user = userMapper.queryUserByName(userName);

        /*3.判断用户对象是否为空*/
        AssertUtil.isTrue(user==null,"用户姓名不存在");

        /*4.判断密码是否正确，比较客户端传递的用户密码与数据路中查询的用户对象中的用户密码*/
        checkUserPwd(userPwd,user.getUserPwd());

        /*返回构建的用户对象*/
        return buildUserInfo(user);

    }

    /**
     * 修改密码
     * 1.通过用户ID查询用户记录，返回用户对象
     * 2.参数校验
     *  待更新用户记录是否存在（用户对象是否空）
     *  原始密码是否为空
     *  原始密码是否正确（查询的用户对象中的用户ID是否与原始密码一致）
     *  判断新密码是否为空
     *  判断新密码是否与原始密码一致（不允许新密码与原始密码）
     *  判断确认密码是否为空
     *  判断确认密码是否与新密码一致
     * 3.设置用户的新密码
     *   需要将新密码通过指定算法进行加密（md5加密）
     * 4.执行更新，判断受影响的行数
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassWord(Integer userId, String oldPwd, String newPwd, String repeatPwd){
//        通过用户ID查询用户记录，返回用户对象
        User user = userMapper.selectByPrimaryKey(userId);
//          判断用户记录是否存在
        AssertUtil.isTrue(null==user,"待更新记录不存在！");

//        参数校验
        checkPasswordParams(user,oldPwd,newPwd,repeatPwd);

//        设置用户的新密码
        user.setUserPwd(Md5Util.encode(newPwd));

//        执行更新，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败");


    }


    /***
     * 修改密码的参数校验
     *
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
//        判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码不能为空");
//        判断原始密码是否正确（查询的用户对象中的用户密码是否原始密码一致）
        AssertUtil.isTrue(user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不争取我");

//        判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空");
//        判断新密码是否与原始密码一致
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原始密码相同");


//        判断确认密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"确认密码不能为空");
//        判断确认密码是否与新密码一致
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"确认密码与新密码不一致");
    }

    /**
     * 构建需要返回给客户端的用户对象
     * @param user
     */

    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
//        userModel.setUserId(user.getId());
//        设置加密的用户ID
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 密码判断
     *  先将客户端传递的密码加密，再与数据库中的密码比较
     * @param userPwd
     * @param pwd
     */

    private void checkUserPwd(String userPwd, String pwd) {
        /*将客户端传递的密码加密*/
        userPwd = Md5Util.encode(userPwd);
        /*判断密码是否相等*/
        AssertUtil.isTrue(!userPwd.equals(pwd),"用户密码不正确");
    }


    /**
     * 参数判断
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        //  验证用户姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户姓名不能为空！");
        //  验证用户密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不为空");
    }
}
