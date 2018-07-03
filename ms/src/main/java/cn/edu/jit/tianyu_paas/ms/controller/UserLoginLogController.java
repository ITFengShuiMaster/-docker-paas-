package cn.edu.jit.tianyu_paas.ms.controller;


import cn.edu.jit.tianyu_paas.ms.service.UserLoginLogService;
import cn.edu.jit.tianyu_paas.shared.entity.UserLoginLog;
import cn.edu.jit.tianyu_paas.shared.util.DateUtil;
import cn.edu.jit.tianyu_paas.shared.util.TResult;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logs")
public class UserLoginLogController {

    private final UserLoginLogService userLoginLogService;
    private HttpSession session;

    @Autowired
    public UserLoginLogController(HttpSession session, UserLoginLogService userLoginLogService) {
        this.session = session;
        this.userLoginLogService = userLoginLogService;
    }

    /**
     * 获取访问量接口，
     *
     * @param status "status表示获取当天、当周还是当月, 对应参数值分别为0,1,2"
     * @author 卢越
     * @returnTResult
     * @since 2018-07-01
     */
    @GetMapping("/views")
    public TResult getViewsBydayOrWeekOrMonth(@RequestParam(value = "status", defaultValue = "0") Integer status) {
        Integer counts = 0;
        if (status.equals(0)) {
            counts = userLoginLogService.selectCount(new EntityWrapper<UserLoginLog>().between("gmt_create", DateUtil.getBeginOfToday(), new Date()));
        } else if (status.equals(1)) {
            counts = userLoginLogService.selectCount(new EntityWrapper<UserLoginLog>().between("gmt_create", DateUtil.getBeginOfWeek(), new Date()));
        } else {
            counts = userLoginLogService.selectCount(new EntityWrapper<UserLoginLog>().between("gmt_create", DateUtil.getBeginOfMonth(), new Date()));
        }
        return TResult.success(counts);
    }

    /**
     * 返回每周各天平均访问量，
     *
     * @return TResult
     * @author 卢越
     * @since 2018-07-01
     */
    @GetMapping("/views-day-of-week-avg")
    public TResult listViewsByDaysOfWeek() {
        List<Map<String, Object>> views = userLoginLogService.selectMaps(new EntityWrapper<UserLoginLog>().setSqlSelect("COUNT(*) as views, day_of_week").groupBy("day_of_week"));

        int weeks = DateUtil.getWeeksDiff(cn.edu.jit.tianyu_paas.shared.global.Constants.DATE_OF_START, new Date());

        if (weeks == 0) {
            return TResult.success(views);
        }

        for (int i = 0; i < views.size(); i++) {
            Map map = views.get(i);
            map.put("views", (Long) map.get("views") / weeks);
        }

        return TResult.success(views);
    }

    /**
     * 返回每月各天平均访问量，
     *
     * @return TResult
     * @author 卢越
     * @since 2018-07-01
     */
    @GetMapping("/views-day-of-month-avg")
    public TResult listViewsDayOfMonth() {
        List<Map<String, Object>> views = userLoginLogService.selectMaps(new EntityWrapper<UserLoginLog>().setSqlSelect("COUNT(*) as views, day").groupBy("day"));

        int months = DateUtil.getMonthDiff(cn.edu.jit.tianyu_paas.shared.global.Constants.DATE_OF_START, new Date());

        if (months == 0) {
            return TResult.success(views);
        }

        for (int i = 0; i < views.size(); i++) {
            Map map = views.get(i);
            map.put("views", (Long) map.get("views") / months);
        }

        return TResult.success(views);
    }

    /**
     * 返回每年各月平均访问量，
     *
     * @return
     * @author 卢越
     * @since 2018-07-01
     */
    @GetMapping("/views-month-of-year-avg")
    public TResult listViewsMonthOfYear() {
        List<Map<String, Object>> views = userLoginLogService.selectMaps(new EntityWrapper<UserLoginLog>().setSqlSelect("COUNT(*) as views, month").groupBy("month"));

        int years = DateUtil.getYearsDiff(cn.edu.jit.tianyu_paas.shared.global.Constants.DATE_OF_START, new Date());

        if (years == 0) {
            return TResult.success(views);
        }

        for (int i = 0; i < views.size(); i++) {
            Map map = views.get(i);
            map.put("views", (Long) map.get("views") / years);
        }

        return TResult.success(views);
    }

    /**
     * 返回本月每天访问量，
     *
     * @return
     * @author 卢越
     * @since 2018-07-01
     */
    @GetMapping("/views-day-of-month")
    public TResult listViewsByDayOfMonth() {
        List<Map<String, Object>> views = userLoginLogService.selectMaps(new EntityWrapper<UserLoginLog>().setSqlSelect("COUNT(*) as views, day").between("gmt_create", DateUtil.getBeginOfMonth(), new Date()).groupBy("day"));

        return TResult.success(views);
    }

    /**
     * 返回本月每天访问量，
     *
     * @return
     * @author 卢越
     * @since 2018-07-01
     */
    @GetMapping("/views-month-of-year")
    public TResult listViewsByMonthOfYear() {
        List<Map<String, Object>> views = userLoginLogService.selectMaps(new EntityWrapper<UserLoginLog>().setSqlSelect("COUNT(*) as views, month").between("gmt_create", DateUtil.getBeginOfYear(), new Date()).groupBy("month"));

        return TResult.success(views);
    }
}
