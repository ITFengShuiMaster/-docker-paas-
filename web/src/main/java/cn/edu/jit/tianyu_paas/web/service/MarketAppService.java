package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.MarketApp;
import cn.edu.jit.tianyu_paas.web.mapper.MarketAppMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class MarketAppService extends ServiceImpl<MarketAppMapper, MarketApp> {
    private RedisTemplate redisTemplate;

    @Autowired
    public MarketAppService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Cacheable(value = "marketApp", key = "#id.toString()")
    @Override
    public MarketApp selectById(Serializable id) {
        return super.selectById(id);
    }

    @Override
    public boolean insert(MarketApp entity) {
        if (super.insert(entity)) {
            redisTemplate.opsForValue().set("marketApp::" + entity.getMarketAppId().toString(), baseMapper.selectById(entity.getMarketAppId()));
            return true;
        }
        return false;
    }

    @CacheEvict(value = "marketApp", key = "#id.toString()")
    @Override
    public boolean deleteById(Serializable id) {
        return super.deleteById(id);
    }

    @Override
    public boolean update(MarketApp entity, Wrapper<MarketApp> wrapper) {
        if (super.update(entity, wrapper)) {
            redisTemplate.opsForValue().set("marketApp::" + entity.getMarketAppId().toString(), baseMapper.selectById(entity.getMarketAppId()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(MarketApp entity) {
        if (super.updateById(entity)) {
            redisTemplate.opsForValue().set("marketApp::" + entity.getMarketAppId().toString(), baseMapper.selectById(entity.getMarketAppId()));
            return true;
        }
        return false;
    }
}
