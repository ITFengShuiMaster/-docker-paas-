package cn.edu.jit.tianyu_paas.web.mapper;

import cn.edu.jit.tianyu_paas.shared.entity.AppLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 汪继友
 * @since 2018-06-28
 */
public interface AppLogMapper extends BaseMapper<AppLog> {

    @Select("select al.* from app, app_log as al" +
            "where app.user_id = #{userId} and app.app_id = #{appId}" +
            "and al.app_id = app.app_id")
    List<AppLog> listAppLogByUserIdAndAppId(@Param("userId") long userId, @Param("appId") long appId);
}
