package cn.edu.jit.tianyu_paas.shared.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author 卢越
 * @since 2018-07-08
 */
public class UserActive implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 邮箱验证码
     */
    private String emailCode;
    /**
     * 邮箱验证码创建时间
     */
    private Date emailCodeGtmCreate;
    /**
     * 手机验证码
     */
    private String phoneCode;
    /**
     * 手机验证码创建时间
     */
    private Date phoneCodeGmtCreate;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    public Date getEmailCodeGtmCreate() {
        return emailCodeGtmCreate;
    }

    public void setEmailCodeGtmCreate(Date emailCodeGtmCreate) {
        this.emailCodeGtmCreate = emailCodeGtmCreate;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Date getPhoneCodeGmtCreate() {
        return phoneCodeGmtCreate;
    }

    public void setPhoneCodeGmtCreate(Date phoneCodeGmtCreate) {
        this.phoneCodeGmtCreate = phoneCodeGmtCreate;
    }

    @Override
    public String toString() {
        return "UserActive{" +
                ", userId=" + userId +
                ", emailCode=" + emailCode +
                ", emailCodeGtmCreate=" + emailCodeGtmCreate +
                ", phoneCode=" + phoneCode +
                ", phoneCodeGmtCreate=" + phoneCodeGmtCreate +
                "}";
    }
}
