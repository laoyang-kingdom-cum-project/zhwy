package com.ruoyi.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Warning;
import com.ruoyi.system.domain.Wyxdevice;
import com.ruoyi.system.domain.ServiceOrder;
import com.ruoyi.system.domain.Activity;
import com.ruoyi.system.domain.Members;
import com.ruoyi.system.service.IWarningService;
import com.ruoyi.system.service.IWyxdeviceService;
import com.ruoyi.system.service.IServiceOrderService;
import com.ruoyi.system.service.IActivityService;
import com.ruoyi.system.service.IMembersService;

/**
 * 数据可视化大屏控制器
 * 
 * @author ruoyi
 * @date 2026-04-22
 */
@RestController
@RequestMapping("/system/dashboard")
public class DashboardController extends BaseController
{
    @Autowired
    private IWarningService warningService;
    
    @Autowired
    private IWyxdeviceService wyxdeviceService;
    
    @Autowired
    private IServiceOrderService serviceOrderService;
    
    @Autowired
    private IActivityService activityService;
    
    @Autowired
    private IMembersService membersService;

    /**
     * 获取大屏统计数据
     */
    @GetMapping("/stats")
    public AjaxResult getStats()
    {
        Map<String, Object> stats = new HashMap<>();
        
        // 1. 设备统计
        Wyxdevice deviceQuery = new Wyxdevice();
        List<Wyxdevice> devices = wyxdeviceService.selectWyxdeviceList(deviceQuery);
        int totalDevices = devices.size();
        int onlineDevices = 0;
        int offlineDevices = 0;
        for (Wyxdevice device : devices) {
            String online = device.getOnline();
            if ("1".equals(online) || "true".equalsIgnoreCase(online)) {
                onlineDevices++;
            } else {
                offlineDevices++;
            }
        }
        stats.put("totalDevices", totalDevices);
        stats.put("onlineDevices", onlineDevices);
        stats.put("offlineDevices", offlineDevices);
        stats.put("deviceOnlineRate", totalDevices > 0 ? Math.round(onlineDevices * 100.0 / totalDevices) : 0);
        
        // 2. 预警统计
        Warning warningQuery = new Warning();
        List<Warning> warnings = warningService.selectWarningList(warningQuery);
        int totalWarnings = warnings.size();
        Map<String, Integer> warningLevelCount = new HashMap<>();
        
        for (Warning warning : warnings) {
            // 统计预警等级
            String level = warning.getLevel();
            if (level == null || level.isEmpty()) {
                level = "其他";
            }
            warningLevelCount.put(level, warningLevelCount.getOrDefault(level, 0) + 1);
        }
        
        stats.put("totalWarnings", totalWarnings);
        stats.put("warningLevelCount", warningLevelCount);
        
        // 3. 服务订单统计
        ServiceOrder orderQuery = new ServiceOrder();
        List<ServiceOrder> orders = serviceOrderService.selectServiceOrderList(orderQuery);
        int totalOrders = orders.size();
        int pendingOrders = 0;
        int processingOrders = 0;
        int completedOrders = 0;
        
        for (ServiceOrder order : orders) {
            Integer status = order.getStatus();
            if (status == null || status == 0) {
                pendingOrders++;
            } else if (status == 1) {
                processingOrders++;
            } else if (status == 2) {
                completedOrders++;
            }
        }
        
        stats.put("totalOrders", totalOrders);
        stats.put("pendingOrders", pendingOrders);
        stats.put("processingOrders", processingOrders);
        stats.put("completedOrders", completedOrders);
        
        // 4. 成员统计
        Members memberQuery = new Members();
        List<Members> members = membersService.selectMembersList(memberQuery);
        int totalMembers = members.size();
        
        stats.put("totalMembers", totalMembers);
        stats.put("elderlyCount", 0); // 暂时设为0，因为Members实体没有type字段
        stats.put("childCount", 0);
        stats.put("adultCount", totalMembers);
        
        // 5. 活动统计
        Activity activityQuery = new Activity();
        List<Activity> activities = activityService.selectActivityList(activityQuery);
        int totalActivities = activities.size();
        int ongoingActivities = 0;
        
        for (Activity activity : activities) {
            Integer status = activity.getStatus();
            if (status != null && status == 1) {
                ongoingActivities++;
            }
        }
        
        stats.put("totalActivities", totalActivities);
        stats.put("ongoingActivities", ongoingActivities);
        
        return success(stats);
    }
    
    /**
     * 获取设备类型分布
     */
    @GetMapping("/deviceTypeStats")
    public AjaxResult getDeviceTypeStats()
    {
        Wyxdevice query = new Wyxdevice();
        List<Wyxdevice> devices = wyxdeviceService.selectWyxdeviceList(query);
        
        Map<String, Integer> typeCount = new HashMap<>();
        for (Wyxdevice device : devices) {
            String type = device.getDeviceType();
            if (type == null || type.isEmpty()) {
                type = "其他";
            }
            typeCount.put(type, typeCount.getOrDefault(type, 0) + 1);
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : typeCount.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", entry.getKey());
            item.put("value", entry.getValue());
            result.add(item);
        }
        
        return success(result);
    }
    
    /**
     * 获取最近7天预警趋势
     */
    @GetMapping("/warningTrend")
    public AjaxResult getWarningTrend()
    {
        // 模拟最近7天的预警数据
        List<Map<String, Object>> trend = new ArrayList<>();
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        
        for (int i = 0; i < 7; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("day", days[i]);
            dayData.put("count", (int)(Math.random() * 10) + 1); // 模拟数据
            trend.add(dayData);
        }
        
        return success(trend);
    }
    
    /**
     * 获取实时预警列表（最近5条）
     */
    @GetMapping("/recentWarnings")
    public AjaxResult getRecentWarnings()
    {
        Warning query = new Warning();
        List<Warning> warnings = warningService.selectWarningList(query);
        
        // 取最近5条
        List<Warning> recentWarnings = new ArrayList<>();
        int count = 0;
        for (int i = warnings.size() - 1; i >= 0 && count < 5; i--) {
            recentWarnings.add(warnings.get(i));
            count++;
        }
        
        return success(recentWarnings);
    }
    
    /**
     * 获取房间设备分布
     */
    @GetMapping("/roomDeviceStats")
    public AjaxResult getRoomDeviceStats()
    {
        Wyxdevice query = new Wyxdevice();
        List<Wyxdevice> devices = wyxdeviceService.selectWyxdeviceList(query);
        
        Map<String, Integer> roomCount = new HashMap<>();
        for (Wyxdevice device : devices) {
            String roomName = device.getRoomName();
            if (roomName == null || roomName.isEmpty()) {
                roomName = "未分配";
            }
            roomCount.put(roomName, roomCount.getOrDefault(roomName, 0) + 1);
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : roomCount.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("room", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        
        return success(result);
    }
}
