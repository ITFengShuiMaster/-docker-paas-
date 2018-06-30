package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.AppPort;
import cn.edu.jit.tianyu_paas.web.mapper.AppPortMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AppPortService extends ServiceImpl<AppPortMapper,AppPort> {
}
