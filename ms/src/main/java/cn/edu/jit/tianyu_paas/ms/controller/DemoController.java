package cn.edu.jit.tianyu_paas.ms.controller;

import cn.edu.jit.tianyu_paas.ms.service.DemoService;
import cn.edu.jit.tianyu_paas.shared.entity.Demo;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.shared.util.TResultCode;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
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

    @GetMapping
    public TResult listDemos() {
        return TResult.success(demoService.selectList(new EntityWrapper<Demo>()));
    }

    @GetMapping("{demoId}")
    public TResult getDemoById(@PathVariable(required = true) Long demoId) {
        return TResult.success(demoService.selectById(demoId));
    }

    @PostMapping
    public TResult insertDemo(Demo demo) {
        demo.setGmtCreate(new Date());
        if (demoService.insert(demo)) {
            return TResult.success();
        }

        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    @PutMapping
    public TResult updateDemoById(Demo demo) {
        if (demoService.update(demo, new EntityWrapper<Demo>().eq("demo_id", demo.getDemoId()))) {
            return TResult.success();
        }

        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }

    @DeleteMapping("{demoId}")
    public TResult deleteDemoById(@PathVariable(required = true) Long demoId) {
        if (demoService.deleteById(demoId)) {
            return TResult.success();
        }

        return TResult.failure(TResultCode.BUSINESS_ERROR);
    }
}
