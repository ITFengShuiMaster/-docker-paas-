package cn.edu.jit.tianyu_paas.shared.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author 汪继友
 * @since 2018-06-28
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int TYPE_CUSTOMER_SERVICE = 1;
    public static final int TYPE_COMMON_USER = 0;

    @TableId(value = "user_id")
    private Long userId;
    /**
     * 姓名或昵称
     */
    @NotNull(message = "用户名不能为空")
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String pwd;
    /**
     * 用户头像,base64编码
     */
    private String headImg;
    /**
     * 注册时间
     */
    private Date gmtCreate;
    private Date gmtModified;
    /**
     * 用户是否激活 0 未激活 1 已激活
     */
    private Integer active;

    @TableField(exist = false)
    private List<App> apps;

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "User{" +
                ", userId=" + userId +
                ", name=" + name +
                ", phone=" + phone +
                ", email=" + email +
                ", pwd=" + pwd +
                ", headImg=" + headImg +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                "}";
    }
}
