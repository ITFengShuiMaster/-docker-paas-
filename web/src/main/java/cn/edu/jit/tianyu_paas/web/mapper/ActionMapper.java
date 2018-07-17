package cn.edu.jit.tianyu_paas.web.mapper;

import cn.edu.jit.tianyu_paas.shared.entity.Action;
import cn.edu.jit.tianyu_paas.shared.entity.ActionDetail;
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
public interface ActionMapper extends BaseMapper<Action> {

    @Select("select ac.* from action as ac,app as a " +
            "where ac.app_id = a.app_id and a.user_id = #{userId} and a.app_id = #{appId}")
    List<Action> listAppActionByUserId(@Param("userId") long userId, @Param("appId") long appId);

    @Select("select count(*) from app, action" +
            "where app.user_id = #{userId} and app.app_id = #{appId}" +
            "and action.app_id = app.app_id and action.action_id = #{actionId}")
    int countActionIdByUserIdAndAppIdAndActionId(@Param("userId") long userId, @Param("appId") long appId,
                                                 @Param("actionId") long actionId);
}
