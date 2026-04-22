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
import com.ruoyi.system.domain.RunningRouteFavoriteStatus;
import com.ruoyi.system.service.IRunningRouteFavoriteStatusService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 路线收藏状态Controller
 * 
 * @author ruoyi
 * @date 2026-01-10 17:31:13
 */
@RestController
@RequestMapping("/system/status")
public class RunningRouteFavoriteStatusController extends BaseController
{
    @Autowired
    private IRunningRouteFavoriteStatusService runningRouteFavoriteStatusService;

    /**
     * 查询路线收藏状态列表
     */
    @PreAuthorize("@ss.hasPermi('system:status:list')")
    @GetMapping("/list")
    public TableDataInfo list(RunningRouteFavoriteStatus runningRouteFavoriteStatus)
    {
        startPage();
        List<RunningRouteFavoriteStatus> list = runningRouteFavoriteStatusService.selectRunningRouteFavoriteStatusList(runningRouteFavoriteStatus);
        return getDataTable(list);
    }

    /**
     * 导出路线收藏状态列表
     */
    @PreAuthorize("@ss.hasPermi('system:status:export')")
    @Log(title = "路线收藏状态", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RunningRouteFavoriteStatus runningRouteFavoriteStatus)
    {
        List<RunningRouteFavoriteStatus> list = runningRouteFavoriteStatusService.selectRunningRouteFavoriteStatusList(runningRouteFavoriteStatus);
        ExcelUtil<RunningRouteFavoriteStatus> util = new ExcelUtil<RunningRouteFavoriteStatus>(RunningRouteFavoriteStatus.class);
        util.exportExcel(response, list, "路线收藏状态数据");
    }

    /**
     * 获取路线收藏状态详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:status:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(runningRouteFavoriteStatusService.selectRunningRouteFavoriteStatusById(id));
    }

    /**
     * 新增路线收藏状态
     */
    @PreAuthorize("@ss.hasPermi('system:status:add')")
    @Log(title = "路线收藏状态", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RunningRouteFavoriteStatus runningRouteFavoriteStatus)
    {
        return toAjax(runningRouteFavoriteStatusService.insertRunningRouteFavoriteStatus(runningRouteFavoriteStatus));
    }

    /**
     * 修改路线收藏状态
     */
    @PreAuthorize("@ss.hasPermi('system:status:edit')")
    @Log(title = "路线收藏状态", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RunningRouteFavoriteStatus runningRouteFavoriteStatus)
    {
        return toAjax(runningRouteFavoriteStatusService.updateRunningRouteFavoriteStatus(runningRouteFavoriteStatus));
    }

    /**
     * 删除路线收藏状态
     */
    @PreAuthorize("@ss.hasPermi('system:status:remove')")
    @Log(title = "路线收藏状态", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(runningRouteFavoriteStatusService.deleteRunningRouteFavoriteStatusByIds(ids));
    }
}
