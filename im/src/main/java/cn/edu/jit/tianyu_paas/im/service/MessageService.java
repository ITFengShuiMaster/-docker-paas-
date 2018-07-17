package cn.edu.jit.tianyu_paas.im.service;

import cn.edu.jit.tianyu_paas.im.entity.Message;
import cn.edu.jit.tianyu_paas.im.mapper.MessageMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MessageService extends ServiceImpl<MessageMapper, Message> {

}