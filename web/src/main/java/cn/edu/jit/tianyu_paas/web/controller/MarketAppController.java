package cn.edu.jit.tianyu_paas.web.controller;

import cn.edu.jit.tianyu_paas.shared.entity.MarketApp;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import cn.edu.jit.tianyu_paas.web.service.MarketAppService;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/marketApps")
public class MarketAppController {
    private final MarketAppService marketAppService;
    private HttpSession session;

    @Autowired
    public MarketAppController(HttpSession session, MarketAppService marketAppService) {
        this.session = session;
        this.marketAppService = marketAppService;
    }

    @ApiOperation("获取应用市场的资源信息")
    @GetMapping
    public TResult listMarketApps(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "9") Integer size) {
        return TResult.success(marketAppService.selectPage(new Page<MarketApp>(current, size)));
    }
}
