package cn.edu.jit.tianyu_paas.im.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-07
 */
public class User {

    public static final int TYPE_COMMON = 0;
    public static final int TYPE_CUSTOMER_SERVICE = 1;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    /**
     * 姓名或昵称
     */
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
    private String pwd;
    /**
     * 用户类型
     */
    private Integer type;
    /**
     * 用户头像,base64编码
     */
    private String headImg;
    /**
     * 注册时间
     */
    private Date gmtCreate;
    private Date gmtModified;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
