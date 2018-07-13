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
     * 用户邮箱
     */
    private String userEmail;
    /**
     * 邮箱验证码
     */
    private String emailCode;
    /**
     * 邮箱验证码创建时间
     */
    private Date emailCodeGtmCreate;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    @Override
    public String toString() {
        return "UserActive{" +
                ", userId=" + userEmail +
                ", emailCode=" + emailCode +
                ", emailCodeGtmCreate=" + emailCodeGtmCreate +
                "}";
    }
}
