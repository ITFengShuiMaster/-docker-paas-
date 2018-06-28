package cn.edu.jit.tianyu_paas.shared.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 获取今天的开始时间
     *
     * @return
     */
    public static Date getBeginOfToday() {
        return getBeginOfDate(new Date());
    }

    /**
     * 获取本周一的开始时间
     *
     * @return
     */
    public static Date getBeginOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getBeginOfDate(calendar.getTime());
    }

    public static Date getBeginOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getBeginOfDate(calendar.getTime());
    }

    /**
     * 获取某个日期的开始时间（0点）
     *
     * @param date
     * @return
     */
    public static Date getBeginOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 得到今天是周几
     *
     * @return
     */
    public static int getDayOfWeekOfToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // java中sunday是第一天，所以要减1
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        // 如果是sunday，减1变成0。
        if (dayOfWeek == 0)
            dayOfWeek = 7;
        return dayOfWeek;
    }

    /**
     * 得到两个日期的月数差，以30天为一个月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getMonthDiff(Date date1, Date date2) {
        return (int) (Math.abs(date1.getTime() - date2.getTime()) / 1000 / 60 / 60 / 24 / 30);
    }

    public static void main(String[] args) {
        System.out.println(getDayOfWeekOfToday());
    }
}
