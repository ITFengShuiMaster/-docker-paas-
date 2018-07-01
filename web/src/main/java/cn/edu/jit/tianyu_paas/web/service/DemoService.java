package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.Demo;
import cn.edu.jit.tianyu_paas.web.mapper.DemoMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DemoService extends ServiceImpl<DemoMapper, Demo> {
}
