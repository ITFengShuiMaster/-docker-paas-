package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.User;
import cn.edu.jit.tianyu_paas.web.mapper.UserMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    private RedisTemplate redisTemplate;

    @Autowired
    public UserService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Cacheable(value = "user", key = "#id.toString()")
    @Override
    public User selectById(Serializable id) {
        return super.selectById(id);
    }

    @Override
    public boolean insert(User entity) {
        if (super.insert(entity)) {
            redisTemplate.opsForValue().set("user::" + entity.getUserId().toString(), baseMapper.selectById(entity.getUserId()));
            return true;
        }
        return false;
    }

    @CacheEvict(value = "user", key = "#id.toString()")
    @Override
    public boolean deleteById(Serializable id) {
        return super.deleteById(id);
    }

    @Override
    public boolean update(User entity, Wrapper<User> wrapper) {
        if (super.update(entity, wrapper)) {
            redisTemplate.opsForValue().set("user::" + entity.getUserId().toString(), baseMapper.selectById(entity.getUserId()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(User entity) {
        if (super.updateById(entity)) {
            redisTemplate.opsForValue().set("user::" + entity.getUserId().toString(), baseMapper.selectById(entity.getUserId()));
            return true;
        }
        return false;
    }

}
