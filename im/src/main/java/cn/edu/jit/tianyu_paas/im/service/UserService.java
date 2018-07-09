package cn.edu.jit.tianyu_paas.im.service;

import cn.edu.jit.tianyu_paas.im.entity.User;
import cn.edu.jit.tianyu_paas.im.mapper.UserMapper;
import cn.edu.jit.tianyu_paas.shared.util.PassUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public User authenticateUser(String username, String pwd) {
        User user = selectOne(new EntityWrapper<User>().eq("phone", username).or().eq("email", username));
        if (user == null || !user.getPwd().equals(PassUtil.getMD5(pwd))) {
            return null;
        }
        user.setPwd("");
        return user;
    }

    public static void main(String[] args) {
    }

}