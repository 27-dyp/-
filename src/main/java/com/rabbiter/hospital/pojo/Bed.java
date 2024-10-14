package com.rabbiter.hospital.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonProperty;

@TableName("bed")
public class Bed {
    @TableId(value = "b_id")
    @JsonProperty("bId")
    private Integer bId;
    @JsonProperty("pId")
    private Integer pId;
    @JsonProperty("pName")
    private String pName;
    @JsonProperty("dId")
    private int dId;
    @JsonProperty("dName")
    private String dName;
    @JsonProperty("bState")
    private Integer bState;
    @JsonProperty("bStart")
    private String bStart;
    @JsonProperty("bReason")
    private String bReason;
    @Version
    private Integer version;

    public Bed() {
    }

    public Bed(int bId, int pId, int dId,String pName,String dName, Integer bState, String bStart, String bReason, Integer version) {
        this.bId = bId;
        this.pId = pId;
        this.dId = dId;
        this.pName = pName;
        this.dName = dName;
        this.bState = bState;
        this.bStart = bStart;
        this.bReason = bReason;
        this.version = version;
    }

    public int getBId() {
        return bId;
    }

    public void setBId(int bId) {
        this.bId = bId;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getdId() {
        return dId;
    }

    public void setdId(int dId) {
        this.dId = dId;
    }

    public String getpName() {return pName;}

    public void setpName(String pName) {this.pName = pName;}

    public String getdName() {return dName;}

    public void setdName(String dName) {this.dName = dName;}

    public Integer getBState() {
        return bState;
    }

    public void setBState(Integer bState) {
        this.bState = bState;
    }

    public String getBStart() {
        return bStart;
    }

    public void setBStart(String bStart) {
        this.bStart = bStart;
    }

    public String getBReason() {
        return bReason;
    }

    public void setBReason(String bReason) {
        this.bReason = bReason;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }



    @Override
    public String toString() {
        return "Bed{" +
                "bId=" + bId +
                ", pId=" + pId +
                ", pName='" + pName + '\'' +
                ", dId=" + dId +
                ", dName='" + dName + '\'' +
                ", bState=" + bState +
                ", bStart='" + bStart + '\'' +
                ", bReason='" + bReason + '\'' +
                ", version=" + version +
                '}';
    }
}
