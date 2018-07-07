package cn.edu.jit.tianyu_paas.ms.service;

import cn.edu.jit.tianyu_paas.ms.mapper.DemoMapper;
import cn.edu.jit.tianyu_paas.shared.entity.Demo;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DemoService extends ServiceImpl<DemoMapper, Demo> {
}
