package cn.edu.jit.tianyu_paas.shared.entity;

import com.baomidou.mybatisplus.annotations.TableId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 汪继友
 * @since 2018-07-17
 */
public class CustomerServiceStaff implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客服人员的id，是im中用户的id
     */
    @TableId(value = "customer_service_staff_id")
    private Long customerServiceStaffId;
    @NotEmpty
    private String phone;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String nick;
    @NotEmpty
    private String realName;
    private Date gmtCreate;
    private Date gmtModified;
    @NotEmpty
    private String pwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Long getCustomerServiceStaffId() {
        return customerServiceStaffId;
    }

    public void setCustomerServiceStaffId(Long customerServiceStaffId) {
        this.customerServiceStaffId = customerServiceStaffId;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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
        return "CustomerServiceStaff{" +
        ", customerServiceStaffId=" + customerServiceStaffId +
        ", phone=" + phone +
        ", email=" + email +
        ", nick=" + nick +
        ", realName=" + realName +
        ", gmtCreate=" + gmtCreate +
        ", gmtModified=" + gmtModified +
        "}";
    }
}
