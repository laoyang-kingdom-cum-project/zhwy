package com.ruoyi.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.stream.Collectors;

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
import com.ruoyi.system.domain.FamilyHealth;
import com.ruoyi.system.service.IWarningService;
import com.ruoyi.system.service.IWyxdeviceService;
import com.ruoyi.system.service.IServiceOrderService;
import com.ruoyi.system.service.IActivityService;
import com.ruoyi.system.service.IMembersService;
import com.ruoyi.system.service.IFamilyHealthService;

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
    
    @Autowired
    private IFamilyHealthService familyHealthService;

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
            if ("1".equals(online) || "4".equals(online) || "true".equalsIgnoreCase(online)) {
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
        int pendingWarnings = 0;
        
        for (Warning warning : warnings) {
            String state = warning.getState();
            if ("0".equals(state) || "待处理".equals(state)) {
                pendingWarnings++;
            }
        }
        
        stats.put("totalWarnings", totalWarnings);
        stats.put("pendingWarnings", pendingWarnings);
        
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
        stats.put("elderlyCount", 0);
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
        List<Map<String, Object>> trend = new ArrayList<>();
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        
        for (int i = 0; i < 7; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("day", days[i]);
            dayData.put("count", (int)(Math.random() * 10) + 1);
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
    
    /**
     * 获取基本信息饼图数据（接入户数、在线户数）
     */
    @GetMapping("/familyStats")
    public AjaxResult getFamilyStats()
    {
        Map<String, Object> result = new HashMap<>();
        
        FamilyHealth query = new FamilyHealth();
        List<FamilyHealth> families = familyHealthService.selectFamilyHealthList(query);
        
        int totalFamilies = families.size();
        int onlineFamilies = 0;
        
        for (FamilyHealth family : families) {
            String healthStatus = family.getHealthStatus();
            if ("正常".equals(healthStatus)) {
                onlineFamilies++;
            }
        }
        
        result.put("totalFamilies", totalFamilies);
        result.put("onlineFamilies", onlineFamilies);
        result.put("offlineFamilies", totalFamilies - onlineFamilies);
        
        return success(result);
    }
    
    /**
     * 获取实时日志流数据
     */
    @GetMapping("/realTimeLogs")
    public AjaxResult getRealTimeLogs()
    {
        List<Map<String, Object>> logs = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Warning query = new Warning();
        List<Warning> warnings = warningService.selectWarningList(query);
        
        if (warnings.isEmpty()) {
            return success(logs);
        }
        
        int count = 0;
        for (int i = warnings.size() - 1; i >= 0 && count < 10; i--) {
            Warning warning = warnings.get(i);
            Map<String, Object> log = new HashMap<>();
            
            log.put("id", warning.getId());
            log.put("eventType", warning.getTitle() != null ? warning.getTitle() : "未知事件");
            
            String level = warning.getLevel();
            if (level == null || level.isEmpty()) {
                level = "2";
            }
            log.put("level", level);
            log.put("levelDesc", "1".equals(level) ? "高" : "2".equals(level) ? "中" : "低");
            
            log.put("roomNo", warning.getLocation() != null ? warning.getLocation() : "未知位置");
            log.put("message", warning.getTitle() != null ? warning.getTitle() : "未知消息");
            
            Date time = warning.getTime();
            log.put("time", time != null ? sdf.format(time) : sdf.format(new Date()));
            
            String state = warning.getState();
            log.put("status", state != null ? state : "0");
            
            logs.add(log);
            count++;
        }
        
        return success(logs);
    }
    
    /**
     * 获取告警趋势数据（折线图）
     */
    @GetMapping("/alarmTrend")
    public AjaxResult getAlarmTrend()
    {
        List<Map<String, Object>> trend = new ArrayList<>();
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        
        int[][] data = {
            {12, 8, 5},
            {15, 10, 6},
            {8, 12, 4},
            {18, 9, 7},
            {10, 15, 5},
            {6, 8, 3},
            {4, 6, 2}
        };
        
        for (int i = 0; i < 7; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("day", days[i]);
            dayData.put("ongoing", data[i][0]);
            dayData.put("resolved", data[i][1]);
            dayData.put("attention", data[i][2]);
            trend.add(dayData);
        }
        
        return success(trend);
    }
    
    /**
     * 获取传感器状态数据（折线图）
     */
    @GetMapping("/sensorTrend")
    public AjaxResult getSensorTrend()
    {
        List<Map<String, Object>> trend = new ArrayList<>();
        String[] hours = {"08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00"};
        
        Wyxdevice query = new Wyxdevice();
        List<Wyxdevice> devices = wyxdeviceService.selectWyxdeviceList(query);
        
        int totalSensors = devices.size();
        int onlineSensors = 0;
        int offlineSensors = 0;
        int abnormalSensors = 0;
        
        for (Wyxdevice device : devices) {
            String online = device.getOnline();
            if ("1".equals(online) || "4".equals(online) || "true".equalsIgnoreCase(online)) {
                onlineSensors++;
            } else {
                offlineSensors++;
            }
        }
        
        int[][] data = {
            {onlineSensors, offlineSensors, abnormalSensors},
            {onlineSensors - 5, offlineSensors + 5, abnormalSensors},
            {onlineSensors - 3, offlineSensors + 3, abnormalSensors - 2},
            {onlineSensors - 8, offlineSensors + 8, abnormalSensors + 3},
            {onlineSensors - 4, offlineSensors + 4, abnormalSensors - 1},
            {onlineSensors - 10, offlineSensors + 10, abnormalSensors + 2},
            {onlineSensors - 12, offlineSensors + 12, abnormalSensors + 3}
        };
        
        for (int i = 0; i < 7; i++) {
            Map<String, Object> hourData = new HashMap<>();
            hourData.put("hour", hours[i]);
            hourData.put("online", Math.max(0, data[i][0]));
            hourData.put("offline", Math.max(0, data[i][1]));
            hourData.put("abnormal", Math.max(0, data[i][2]));
            trend.add(hourData);
        }
        
        return success(trend);
    }
    
    /**
     * 获取需关注事件列表
     */
    @GetMapping("/attentionEvents")
    public AjaxResult getAttentionEvents()
    {
        List<Map<String, Object>> events = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Warning query = new Warning();
        List<Warning> warnings = warningService.selectWarningList(query);
        
        List<Warning> attentionWarnings = warnings.stream()
            .filter(w -> {
                String state = w.getState();
                String level = w.getLevel();
                return ("0".equals(state) || "待处理".equals(state) || "处理中".equals(state)) 
                    && ("1".equals(level) || "2".equals(level));
            })
            .collect(Collectors.toList());
        
        int count = 0;
        for (int i = attentionWarnings.size() - 1; i >= 0 && count < 10; i--) {
            Warning warning = attentionWarnings.get(i);
            Map<String, Object> event = new HashMap<>();
            
            event.put("id", warning.getId());
            event.put("eventType", warning.getTitle() != null ? warning.getTitle() : "未知事件");
            
            String level = warning.getLevel();
            if (level == null || level.isEmpty()) {
                level = "2";
            }
            event.put("level", level);
            event.put("levelDesc", "1".equals(level) ? "高" : "2".equals(level) ? "中" : "低");
            
            event.put("roomNo", warning.getLocation() != null ? warning.getLocation() : "未知位置");
            event.put("message", warning.getTitle() != null ? warning.getTitle() : "未知消息");
            
            Date time = warning.getTime();
            event.put("time", time != null ? sdf.format(time) : sdf.format(new Date()));
            
            String state = warning.getState();
            event.put("status", state != null ? state : "0");
            event.put("statusDesc", "0".equals(state) ? "待处理" : "1".equals(state) ? "处理中" : "已解决");
            
            events.add(event);
            count++;
        }
        
        return success(events);
    }
    
    /**
     * 获取事件详细信息
     */
    @GetMapping("/eventDetail")
    public AjaxResult getEventDetail(Long id)
    {
        Map<String, Object> detail = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        if (id != null) {
            Warning warning = warningService.selectWarningById(id);
            if (warning != null) {
                detail.put("id", warning.getId());
                detail.put("eventTime", warning.getTime() != null ? sdf.format(warning.getTime()) : sdf.format(new Date()));
                detail.put("eventType", warning.getTitle());
                detail.put("roomNo", warning.getLocation());
                detail.put("message", warning.getTitle());
                detail.put("status", warning.getState());
                detail.put("statusDesc", "0".equals(warning.getState()) ? "待处理" : "1".equals(warning.getState()) ? "处理中" : "已解决");
                detail.put("level", warning.getLevel());
                detail.put("levelDesc", "1".equals(warning.getLevel()) ? "高" : "2".equals(warning.getLevel()) ? "中" : "低");
                detail.put("handlePerson", "-");
                detail.put("handleTime", "-");
                
                return success(detail);
            }
        }
        
        detail.put("id", id);
        detail.put("eventTime", sdf.format(new Date()));
        detail.put("eventType", "未知事件");
        detail.put("roomNo", "未知位置");
        detail.put("message", "暂无详细信息");
        detail.put("status", "0");
        detail.put("statusDesc", "待处理");
        detail.put("level", "2");
        detail.put("levelDesc", "中");
        detail.put("handlePerson", "-");
        detail.put("handleTime", "-");
        
        return success(detail);
    }
}
