package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.AppLog;
import cn.edu.jit.tianyu_paas.web.mapper.AppLogMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppLogService extends ServiceImpl<AppLogMapper, AppLog> {

    public List<AppLog> listAppLogByUserIdAndAppId(long userId, long appId) {
        return baseMapper.listAppLogByUserIdAndAppId(userId, appId);
    }
}
