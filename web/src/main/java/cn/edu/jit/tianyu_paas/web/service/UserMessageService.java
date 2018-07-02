package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.Message;
import cn.edu.jit.tianyu_paas.shared.entity.UserMessage;
import cn.edu.jit.tianyu_paas.web.mapper.UserMessageMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-02
 */
@Service
public class UserMessageService extends ServiceImpl<UserMessageMapper, UserMessage> {

    public List<Message> selectByUserId(Long userId){
        return baseMapper.selectByUserId(userId);
    }
}
