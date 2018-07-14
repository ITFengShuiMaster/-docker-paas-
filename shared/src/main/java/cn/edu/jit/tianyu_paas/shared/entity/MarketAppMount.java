package cn.edu.jit.tianyu_paas.shared.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author 倪龙康
 * @since 2018-07-13
 */
public class MarketAppMount implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long marketAppId;
    /**
     * 可挂载的目录
     */
    private String mountName;
    private Date gmtCreate;


    public Long getMarketAppId() {
        return marketAppId;
    }

    public void setMarketAppId(Long marketAppId) {
        this.marketAppId = marketAppId;
    }

    public String getMountName() {
        return mountName;
    }

    public void setMountName(String mountName) {
        this.mountName = mountName;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "MarketAppMount{" +
                ", marketAppId=" + marketAppId +
                ", mountName=" + mountName +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
