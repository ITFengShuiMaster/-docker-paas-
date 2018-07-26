package cn.edu.jit.tianyu_paas.ms.service;

import cn.edu.jit.tianyu_paas.ms.mapper.AppGroupMapper;
import cn.edu.jit.tianyu_paas.shared.entity.AppGroup;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class AppGroupService extends ServiceImpl<AppGroupMapper, AppGroup> {
}
