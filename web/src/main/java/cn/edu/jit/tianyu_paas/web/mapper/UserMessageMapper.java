package cn.edu.jit.tianyu_paas.web.mapper;

import cn.edu.jit.tianyu_paas.shared.entity.Message;
import cn.edu.jit.tianyu_paas.shared.entity.UserMessage;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-02
 */
public interface UserMessageMapper extends BaseMapper<UserMessage> {
    @Select("SELECT * from message as m where m.message_id in (SELECT um.message_id from user_message as um where um.`status` = 0 AND um.user_id = #{userId})")
    List<Message> selectByUserId(@Param("userId") Long userId);
}
