package cn.edu.jit.tianyu_paas.im.service;

import cn.edu.jit.tianyu_paas.im.entity.Message;
import cn.edu.jit.tianyu_paas.im.entity.OfflineMessage;
import cn.edu.jit.tianyu_paas.im.mapper.MessageMapper;
import cn.edu.jit.tianyu_paas.im.mapper.OfflineMessageMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OfflineMessageService extends ServiceImpl<OfflineMessageMapper, OfflineMessage> {

}