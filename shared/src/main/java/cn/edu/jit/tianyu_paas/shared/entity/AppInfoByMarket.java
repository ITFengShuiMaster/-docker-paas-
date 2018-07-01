package cn.edu.jit.tianyu_paas.shared.entity;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 汪继友
 * @since 2018-06-28
 */
public class AppInfoByMarket implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appId;
    /**
     * 应用市场的docker id
     */
    @Min(1)
    private Long marketAppId;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getMarketAppId() {
        return marketAppId;
    }

    public void setMarketAppId(Long marketAppId) {
        this.marketAppId = marketAppId;
    }

    @Override
    public String toString() {
        return "AppInfoByMarket{" +
                ", appId=" + appId +
                ", marketAppId=" + marketAppId +
                "}";
    }
}
