package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.WorkoutLog;
import com.ruoyi.system.service.IWorkoutLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 运动记录Controller
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/system/log")
public class WorkoutLogController extends BaseController
{
    @Autowired
    private IWorkoutLogService workoutLogService;

    /**
     * 查询运动记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(WorkoutLog workoutLog)
    {
        startPage();
        List<WorkoutLog> list = workoutLogService.selectWorkoutLogList(workoutLog);
        return getDataTable(list);
    }

    /**
     * 导出运动记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:export')")
    @Log(title = "运动记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WorkoutLog workoutLog)
    {
        List<WorkoutLog> list = workoutLogService.selectWorkoutLogList(workoutLog);
        ExcelUtil<WorkoutLog> util = new ExcelUtil<WorkoutLog>(WorkoutLog.class);
        util.exportExcel(response, list, "运动记录数据");
    }

    /**
     * 获取运动记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(workoutLogService.selectWorkoutLogById(id));
    }

    /**
     * 新增运动记录
     */
    @PreAuthorize("@ss.hasPermi('system:log:add')")
    @Log(title = "运动记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WorkoutLog workoutLog)
    {
        return toAjax(workoutLogService.insertWorkoutLog(workoutLog));
    }

    /**
     * 修改运动记录
     */
    @PreAuthorize("@ss.hasPermi('system:log:edit')")
    @Log(title = "运动记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WorkoutLog workoutLog)
    {
        return toAjax(workoutLogService.updateWorkoutLog(workoutLog));
    }

    /**
     * 删除运动记录
     */
    @PreAuthorize("@ss.hasPermi('system:log:remove')")
    @Log(title = "运动记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(workoutLogService.deleteWorkoutLogByIds(ids));
    }
}
