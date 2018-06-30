package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.web.mapper.AppMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 卢越
 * @date 2018-6-30 10.48
 */
@Service
public class AppService extends ServiceImpl<AppMapper, App> {

    public Page<App> listAppsByNameAndStatus(App app, Pagination pagination) {
        // 当前页，条数 构造 page 对象
        Page<App> page = new Page<>(pagination.getCurrent(), pagination.getSize());
        return page.setRecords(baseMapper.selectListAppPage(app, page));
    }
}
