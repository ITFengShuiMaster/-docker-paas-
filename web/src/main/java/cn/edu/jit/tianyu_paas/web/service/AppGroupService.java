package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.AppGroup;
import cn.edu.jit.tianyu_paas.web.mapper.AppGroupMapper;
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
    private RedisTemplate redisTemplate;

    @Autowired
    public AppGroupService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Cacheable(value = "appGroup", key = "#id.toString()")
    @Override
    public AppGroup selectById(Serializable id) {
        return super.selectById(id);
    }

    @Override
    public boolean insert(AppGroup entity) {
        if (super.insert(entity)) {
            redisTemplate.opsForValue().set("appGroup::" + entity.getAppGroupId().toString(), baseMapper.selectById(entity.getAppGroupId()));
            return true;
        }
        return false;
    }

    @CacheEvict(value = "appGroup", key = "#id.toString()")
    @Override
    public boolean deleteById(Serializable id) {
        return super.deleteById(id);
    }

    @Override
    public boolean update(AppGroup entity, Wrapper<AppGroup> wrapper) {
        if (super.update(entity, wrapper)) {
            redisTemplate.opsForValue().set("appGroup::" + entity.getAppGroupId().toString(), baseMapper.selectById(entity.getAppGroupId()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(AppGroup entity) {
        if (super.updateById(entity)) {
            redisTemplate.opsForValue().set("appGroup::" + entity.getAppGroupId().toString(), baseMapper.selectById(entity.getAppGroupId()));
            return true;
        }
        return false;
    }
}
