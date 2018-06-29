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
public class Demo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "demo_id", type = IdType.AUTO)
    private Long demoId;
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
    private Date gmtCreate;


    public Long getDemoId() {
        return demoId;
    }

    public void setDemoId(Long demoId) {
        this.demoId = demoId;
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
        return "Demo{" +
                ", demoId=" + demoId +
                ", name=" + name +
                ", version=" + version +
                ", memoryUsed=" + memoryUsed +
                ", diskUsed=" + diskUsed +
                ", gmtCreate=" + gmtCreate +
                "}";
    }
}
