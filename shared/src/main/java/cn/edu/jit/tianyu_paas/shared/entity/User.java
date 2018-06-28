package cn.edu.jit.tianyu_paas.shared.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 汪继友
 * @since 2018-06-28
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱地址（用户名）
     */
    private String email;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 用户头像,base64编码
     */
    private String headImg;
    /**
     * 注册时间
     */
    private Date gmtCreate;
    private Date gmtModified;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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
                ", phone=" + phone +
                ", email=" + email +
                ", pwd=" + pwd +
                ", realName=" + realName +
                ", headImg=" + headImg +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                "}";
    }
}
