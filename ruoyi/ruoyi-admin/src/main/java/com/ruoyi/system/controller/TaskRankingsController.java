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
import com.ruoyi.system.domain.TaskRankings;
import com.ruoyi.system.service.ITaskRankingsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 任务排行榜Controller
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
@RestController
@RequestMapping("/system/rankings")
public class TaskRankingsController extends BaseController
{
    @Autowired
    private ITaskRankingsService taskRankingsService;

    /**
     * 查询任务排行榜列表
     */
    @PreAuthorize("@ss.hasPermi('system:rankings:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaskRankings taskRankings)
    {
        startPage();
        List<TaskRankings> list = taskRankingsService.selectTaskRankingsList(taskRankings);
        return getDataTable(list);
    }

    /**
     * 导出任务排行榜列表
     */
    @PreAuthorize("@ss.hasPermi('system:rankings:export')")
    @Log(title = "任务排行榜", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaskRankings taskRankings)
    {
        List<TaskRankings> list = taskRankingsService.selectTaskRankingsList(taskRankings);
        ExcelUtil<TaskRankings> util = new ExcelUtil<TaskRankings>(TaskRankings.class);
        util.exportExcel(response, list, "任务排行榜数据");
    }

    /**
     * 获取任务排行榜详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:rankings:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(taskRankingsService.selectTaskRankingsById(id));
    }

    /**
     * 新增任务排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:rankings:add')")
    @Log(title = "任务排行榜", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaskRankings taskRankings)
    {
        return toAjax(taskRankingsService.insertTaskRankings(taskRankings));
    }

    /**
     * 修改任务排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:rankings:edit')")
    @Log(title = "任务排行榜", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaskRankings taskRankings)
    {
        return toAjax(taskRankingsService.updateTaskRankings(taskRankings));
    }

    /**
     * 删除任务排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:rankings:remove')")
    @Log(title = "任务排行榜", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(taskRankingsService.deleteTaskRankingsByIds(ids));
    }
}
