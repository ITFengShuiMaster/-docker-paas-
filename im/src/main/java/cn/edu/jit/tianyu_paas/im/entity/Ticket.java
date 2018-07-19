package cn.edu.jit.tianyu_paas.im.entity;

import com.baomidou.mybatisplus.annotations.TableId;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-19
 */
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long userId;
    private String token;
    private Date gmtCreate;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                ", userId=" + userId +
                ", token=" + token +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
