package cn.edu.jit.tianyu_paas.ms.service;

import cn.edu.jit.tianyu_paas.ms.mapper.MessageMapper;
import cn.edu.jit.tianyu_paas.shared.entity.Message;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-02
 */
@Service
public class MessageService extends ServiceImpl<MessageMapper, Message> {
}
