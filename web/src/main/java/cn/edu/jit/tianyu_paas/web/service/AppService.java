package cn.edu.jit.tianyu_paas.web.service;

import cn.edu.jit.tianyu_paas.shared.entity.App;
import cn.edu.jit.tianyu_paas.web.mapper.AppMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppService extends ServiceImpl<AppMapper, App> {

    @Transactional(readOnly = true)
    public Page<App> selectAppListPage(App app, Integer current, Integer size) {
        // 当前页，条数 构造 page 对象
        Page<App> page = new Page<>(current, size);
        return page.setRecords(this.baseMapper.selectListAppPage(page, app));
    }
}
