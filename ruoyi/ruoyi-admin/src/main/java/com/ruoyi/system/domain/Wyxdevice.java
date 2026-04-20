package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备列表对象 wyxdevice
 * 
 * @author ruoyi
 * @date 2026-04-16 19:31:38
 */
public class Wyxdevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Excel(name = "id")
    private Long id;

    /** 所有者用户id */
    @Excel(name = "所有者用户id")
    private Long userid;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private String deviceType;

    /** 房间id */
    @Excel(name = "房间id")
    private Long roomId;

    /** 房间名称 */
    @Excel(name = "房间名称")
    private String roomName;

    /** 设备开关状态 */
    @Excel(name = "设备开关状态")
    private String status;

    /** 是否在线 */
    @Excel(name = "是否在线")
    private String online;

    /** value */
    @Excel(name = "value")
    private String value;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUserid(Long userid) 
    {
        this.userid = userid;
    }

    public Long getUserid() 
    {
        return userid;
    }

    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }

    public void setDeviceType(String deviceType) 
    {
        this.deviceType = deviceType;
    }

    public String getDeviceType() 
    {
        return deviceType;
    }

    public void setRoomId(Long roomId) 
    {
        this.roomId = roomId;
    }

    public Long getRoomId() 
    {
        return roomId;
    }

    public void setRoomName(String roomName) 
    {
        this.roomName = roomName;
    }

    public String getRoomName() 
    {
        return roomName;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setOnline(String online) 
    {
        this.online = online;
    }

    public String getOnline() 
    {
        return online;
    }

    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userid", getUserid())
            .append("deviceName", getDeviceName())
            .append("deviceType", getDeviceType())
            .append("roomId", getRoomId())
            .append("roomName", getRoomName())
            .append("status", getStatus())
            .append("online", getOnline())
            .append("value", getValue())
            .toString();
    }
}
