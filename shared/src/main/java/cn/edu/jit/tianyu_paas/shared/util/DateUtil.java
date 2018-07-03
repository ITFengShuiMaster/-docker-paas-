package cn.edu.jit.tianyu_paas.shared.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static Date getBeginOfYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
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
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
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

    /**
     * 得到两个日期的周数差，以7天为一个周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getWeeksDiff(Date date1, Date date2) {
        return (int) (Math.abs(date1.getTime() - date2.getTime()) / 1000 / 60 / 60 / 24 / 7);
    }

    /**
     * 得到两个日期的年数差，以365天为一个年
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getYearsDiff(Date date1, Date date2) {
        return (int) (Math.abs(date1.getTime() - date2.getTime()) / 1000 / 60 / 60 / 24 / 365);
    }

    /**
     * 日期格式转换
     *
     * @param date
     * @author 卢越
     * @date 2018/6/30 9:51
     */
    public static String getSimpleDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取当天是本月中第几天
     *
     * @author 卢越
     * @date 2018/07/02 9:51
     */
    public static Integer getDayOfMonthOfToday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当天是一年中第几个月
     *
     * @author 卢越
     * @date 2018/07/02 9:51
     */
    public static Integer getMonthOfYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(getDayOfWeekOfToday());
        System.out.println(new Date());
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));

        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate2 = dateFormat2.parse("2018-06-25 00:00:00");


        System.out.println("*************");
        System.out.println(getWeeksDiff(new Date(), new Date()));
        System.out.println("*************");
        System.out.println(calendar.get(Calendar.MONTH) + 1);
        System.out.println("*************");
        System.out.println(getBeginOfYear());
    }
}
