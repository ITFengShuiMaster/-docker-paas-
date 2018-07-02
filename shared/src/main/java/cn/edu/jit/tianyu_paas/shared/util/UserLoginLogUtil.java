package cn.edu.jit.tianyu_paas.shared.util;

import cn.edu.jit.tianyu_paas.shared.entity.UserLoginLog;

import java.util.Date;

public class UserLoginLogUtil {

    public static UserLoginLog getUserLoginLog(Long userId, Integer result) {
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setUserId(userId);
        userLoginLog.setResult(result);
        userLoginLog.setGmtCreate(new Date());
        userLoginLog.setDay(DateUtil.getDayOfMonthOfToday());
        userLoginLog.setDayOfWeek(DateUtil.getDayOfWeekOfToday());
        userLoginLog.setMonth(DateUtil.getMonthOfYear());

        return userLoginLog;
    }
}
