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
public class MarketAppPort implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long marketAppId;
    /**
     * 市场应用所需的端口
     */
    private Integer port;

    private Date gmtCreate;
    private Date gmtModify;


    public Long getMarketAppId() {
        return marketAppId;
    }

    public void setMarketAppId(Long marketAppId) {
        this.marketAppId = marketAppId;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    @Override
    public String toString() {
        return "MarketAppPort{" +
                ", marketAppId=" + marketAppId +
                ", port=" + port +
                "}";
    }
}
