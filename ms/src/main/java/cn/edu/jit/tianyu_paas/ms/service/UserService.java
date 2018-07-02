package cn.edu.jit.tianyu_paas.ms.service;

import cn.edu.jit.tianyu_paas.ms.mapper.UserMapper;
import cn.edu.jit.tianyu_paas.shared.entity.User;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 卢越
 * @date 2018-07-01
 * @
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    /**
     * @author 卢越
     * @date 2018-07-01
     * 查询指定天数之内登录的用户
     * @param days
     * @param pagination
     * @return List<User>
     */
    public Page<User> selectAccessUsers(Integer days, Integer views, Pagination pagination) {
        Page<User> page = new Page<User>(pagination.getCurrent(), pagination.getSize());
        return page.setRecords(baseMapper.selectAccessUsers(days, views, page));
    }

    /**
     * @author 卢越
     * @date 2018-07-01
     * 查询一个月未登录过的用户
     * @return List<User>
     */
    public List<User> selectUnloginUsersInThreeMonth(){
        return baseMapper.listUnloginUsersInThreeMonth();
    }

    /**
     * @author 卢越
     * @date 2018-07-01
     * 查询欠费用户
     * @return List<User>
     */
    public List<User> selectArrearsUsers(){
        return baseMapper.listArrearsUsers();
    }
}
