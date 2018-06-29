package cn.edu.jit.tianyu_paas.shared.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 汪继友
 * @since 2018-06-28
 */
public class MarketApp implements Serializable {

    @TableId(value = "market_app_id", type = IdType.AUTO)
    private Long marketAppId;
    /**
     * 应用名称
     */
    private String name;
    /**
     * 应用版本
     */
    private String version;
    /**
     * 所占内存，以MB为单位
     */
    private Integer memoryUsed;
    /**
     * 所占磁盘，以MB为单位
     */
    private Integer diskUsed;
    /**
     * 说明
     */
    private String illustration;
    /**
     * 图片的base64编码
     */
    private String imageBase64;
    private Date gmtCreate;

    public Long getMarketAppId() {
        return marketAppId;
    }

    public void setMarketAppId(Long marketAppId) {
        this.marketAppId = marketAppId;
    }

    public String getIllustration() {
        return illustration;
    }

    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(Integer memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public Integer getDiskUsed() {
        return diskUsed;
    }

    public void setDiskUsed(Integer diskUsed) {
        this.diskUsed = diskUsed;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "MarketApp{" +
                ", name=" + name +
                ", version=" + version +
                ", memoryUsed=" + memoryUsed +
                ", diskUsed=" + diskUsed +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
