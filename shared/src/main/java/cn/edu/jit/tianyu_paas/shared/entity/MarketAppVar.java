package cn.edu.jit.tianyu_paas.shared.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author 倪龙康
 * @since 2018-07-09
 */
public class MarketAppVar implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long marketAppId;
    /**
     * 所需的变量
     */
    private String varName;
    private String value;
    private String varExplain;
    private Date gmtCreate;
    private Date gmtModify;


    public Long getMarketAppId() {
        return marketAppId;
    }

    public void setMarketAppId(Long marketAppId) {
        this.marketAppId = marketAppId;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getVarExplain() {
        return varExplain;
    }

    public void setVarExplain(String varExplain) {
        this.varExplain = varExplain;
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
        return "MarketAppVar{" +
                ", marketAppId=" + marketAppId +
                ", varName=" + varName +
                ", value=" + value +
                ", gmtCreate=" + gmtCreate +
                ", gmtModify=" + gmtModify +
                "}";
    }
}
