package cn.edu.jit.tianyu_paas.web.controller;


import cn.edu.jit.tianyu_paas.shared.entity.Demo;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import cn.edu.jit.tianyu_paas.web.service.DemoService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author 卢越
 * @since 2018-07-06
 */
@RestController
@RequestMapping("/demos")
public class DemoController {
    private final DemoService demoService;
    private HttpSession session;

    @Autowired
    public DemoController(DemoService demoService, HttpSession session) {
        this.demoService = demoService;
        this.session = session;
    }

    @ApiOperation("获取demo列表")
    @GetMapping
    public TResult listDemos() {
        return TResult.success(demoService.selectList(new EntityWrapper<Demo>()));
    }

    @ApiOperation("根据id获取demo")
    @GetMapping("{demoId}")
    public TResult getDemoById(@PathVariable(required = true) Long demoId) {
        if (demoService.selectCount(new EntityWrapper<Demo>().eq("demo_id", demoId)) == 0) {
            return TResult.failure(TResultCode.RESULE_DATA_NONE);
        }
        return TResult.success(demoService.selectById(demoId));
    }
}
