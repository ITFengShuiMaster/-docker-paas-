package cn.edu.jit.tianyu_paas.im.service;

import cn.edu.jit.tianyu_paas.im.entity.Message;
import cn.edu.jit.tianyu_paas.im.entity.User;
import cn.edu.jit.tianyu_paas.im.mapper.MessageMapper;
import cn.edu.jit.tianyu_paas.im.mapper.UserMapper;
import cn.edu.jit.tianyu_paas.shared.util.PassUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MessageService extends ServiceImpl<MessageMapper, Message> {

}