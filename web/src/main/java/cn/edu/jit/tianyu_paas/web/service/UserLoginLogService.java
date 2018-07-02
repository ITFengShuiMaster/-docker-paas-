package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.UserLoginLog;
import cn.edu.jit.tianyu_paas.web.mapper.UserLoginLogMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserLoginLogService extends ServiceImpl<UserLoginLogMapper, UserLoginLog> {
}
