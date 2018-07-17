package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.Ticket;
import cn.edu.jit.tianyu_paas.web.mapper.TicketMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 汪继友
 * @since 2018-07-17
 */
@Service
public class TicketService extends ServiceImpl<TicketMapper, Ticket> {

    public Ticket getTicketByToken(String token) {
        return selectOne(new EntityWrapper<Ticket>().eq("token", token));
    }
}
