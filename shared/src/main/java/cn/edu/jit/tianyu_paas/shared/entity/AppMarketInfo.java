package cn.edu.jit.tianyu_paas.shared.entity;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 汪继友
 * @since 2018-06-28
 */
public class AppMarketInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    /**
     * 应用市场的docker id
     */
    private Long marketDockerId;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getMarketDockerId() {
        return marketDockerId;
    }

    public void setMarketDockerId(Long marketDockerId) {
        this.marketDockerId = marketDockerId;
    }

    @Override
    public String toString() {
        return "AppMarketInfo{" +
                ", appId=" + appId +
                ", marketDockerId=" + marketDockerId +
                "}";
    }
}
