package cn.edu.jit.tianyu_paas.shared.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-01
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    /**
     * 用户登录结果，0成功，1失败
     */
    private Integer result;
    private Date gmtCreate;
    private Integer day;
    private Integer dayOfWeek;
    private Integer month;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "UserLoginLog{" +
                ", userId=" + userId +
                ", result=" + result +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
