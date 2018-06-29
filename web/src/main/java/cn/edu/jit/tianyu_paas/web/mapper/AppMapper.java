package cn.edu.jit.tianyu_paas.web.mapper;

import cn.edu.jit.tianyu_paas.shared.entity.App;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
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
public interface AppMapper extends BaseMapper<App> {

    @Select({"<script>",
            "SELECT a.app_id, ap.group_name, a.name, a.status, a.memory_used from app as a ",
            "LEFT JOIN app_group as ap ",
            "ON a.app_group_id=ap.app_group_id ",
            "WHERE a.user_id=#{app.userId} ",
            "AND a.name LIKE '%${app.name}%' ",
            "<if test='app.status!=null'>",
            "AND a.status = #{app.status}",
            "</if>",
            "</script>"})
    List<App> selectListAppPage(Pagination page, @Param("app") App app);
}
