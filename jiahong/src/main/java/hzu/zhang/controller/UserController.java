package hzu.zhang.controller;

import hzu.zhang.base.BaseController;
import hzu.zhang.base.ResultInfo;
import hzu.zhang.model.UserModel;
import hzu.zhang.service.UserService;
import hzu.zhang.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    /**
     * 用户登录
     */

    @Resource
    private UserService userService;
    @PostMapping("login")
    @ResponseBody /*表示当前会返回一个json格式给客户端*/
    public ResultInfo userLogin(String userName,String userPwd){

        ResultInfo resultInfo = new ResultInfo();

//        调用service层登录方法
        UserModel userModel = userService.userLogin(userName,userPwd);
//              设置ResultInfo的result值  （将数据返回给请求）
        resultInfo.setResult(userModel);


//        通过try catch捕获service层的异常，如果service层抛出异常，则表示登录失败，否则登录成功
//        try{
//
//
//
//        } catch (ParamsException p){
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//
//        }catch (Exception e){
//            resultInfo.setCode(500);
//            resultInfo.setMsg("登录失败！");
//        }
        return resultInfo;

    }

    /***
     * 用户修改密码
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param repeatPassword
     * @return
     */

    @GetMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,
                                         String oldPassword,String newPassword,String repeatPassword){
        ResultInfo resultInfo = new ResultInfo();

        //            获取cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
//            调用Service层的修改密码方法
//        userService.updatePassWord(userId, oldPassword, newPassword, repeatPassword);

//        try{
////            获取cookie中的userId
//            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
////            调用Service层的修改密码方法
//            userService.updatePassWord(userId, oldPassword, newPassword, repeatPassword);
//        }catch (ParamsException p){
//            resultInfo.setCode((p.getCode()));
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//
//
//        }catch (Exception e){
//            resultInfo.setCode(500);
//            resultInfo.setMsg("修改密码失败！");
//            e.printStackTrace();
//
//        }

        return resultInfo;
    }

    /**
     * 进入修改密码的页面
     *
     * @return
     */

    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }
}
