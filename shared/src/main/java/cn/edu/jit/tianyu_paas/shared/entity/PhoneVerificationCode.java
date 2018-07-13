package cn.edu.jit.tianyu_paas.shared.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author 倪龙康
 * @since 2018-07-10
 */
public class PhoneVerificationCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号码
     */
    private String phone;
    /**
     * 手机验证码
     */
    private String phoneCode;
    private Date gmtCreate;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "PhoneVerificationCode{" +
                ", phone=" + phone +
                ", phoneCode=" + phoneCode +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
