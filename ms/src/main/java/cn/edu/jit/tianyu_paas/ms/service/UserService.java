package cn.edu.jit.tianyu_paas.ms.service;

import cn.edu.jit.tianyu_paas.ms.mapper.UserMapper;
import cn.edu.jit.tianyu_paas.shared.entity.User;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 倪龙康
 * @since 2018-07-01
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
}
