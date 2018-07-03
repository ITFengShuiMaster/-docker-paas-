package cn.edu.jit.tianyu_paas.ms.mapper;

import cn.edu.jit.tianyu_paas.shared.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 卢越
 * @date 2018-07-01
 * @
 */

public interface UserMapper extends BaseMapper<User> {

    /**
     * @param days
     * @param pagination
     * @return
     * @author 卢越
     * @date 2018-07-01
     * 查询从过去（几天）到现在登录过的用户
     */
    @Select("SELECT u.user_id as userId, u.name as name, u.phone as phone, u.email as email, u.head_img as headImg, u.gmt_create as gmtCreate, u.gmt_modified as gmtModified " +
            "from `user` as u " +
            "where u.user_id in (SELECT ull.user_id FROM user_login_log as ull WHERE ull.gmt_create BETWEEN DATE_SUB(NOW(), INTERVAL #{days} DAY) AND NOW() GROUP BY user_id HAVING COUNT(*) > #{views})")
    List<User> selectAccessUsers(@Param("days") Integer days, @Param("views") Integer views, Pagination pagination);

    /**
     * @return
     * @author 卢越
     * @date 2018-07-01
     * 查询30天未登录过的用户登录过的用户
     */
    @Select("SELECT u.user_id as userId, u.name as name, u.phone as phone, u.email as email, u.head_img as headImg, u.gmt_create as gmtCreate, u.gmt_modified as gmtModified from `user` as u where u.user_id in (SELECT ull.user_id from user_login_log as ull where ull.gmt_create <= DATE_SUB(CURDATE(), INTERVAL 30 DAY) AND ull.user_id NOT IN (SELECT ull.user_id from user_login_log as ull where ull.gmt_create >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)))")
    List<User> listUnloginUsersInThreeMonth();

    /**
     * @return
     * @author 卢越
     * @date 2018-07-01
     * 查询欠费用户
     */
    @Select("SELECT u.user_id as userId, u.name as name, u.phone as phone, u.email as email, u.head_img as headImg, u.gmt_create as gmtCreate, u.gmt_modified as gmtModified from `user` as u where u.user_id in (SELECT ud.user_id from user_dynamic as ud where ud.balance < 0)")
    List<User> listArrearsUsers();
}
