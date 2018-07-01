package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.Action;
import cn.edu.jit.tianyu_paas.web.mapper.ActionMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * /**
 * /**
 * <p>
 * Service
 * </p>
 *
 * @author 卢越
 * @since 2018-06-29
 */
@Service
public class ActionService extends ServiceImpl<ActionMapper, Action> {
    public List<Action> listAppActionByUserId(long userId, long appId) {
        return baseMapper.listAppActionByUserId(userId, appId);
    }

    public boolean isActionIdExist(long userId, long appId, long actionId) {
        return baseMapper.countActionIdByUserIdAndAppIdAndActionId(userId, appId, actionId) > 0;
    }
}
