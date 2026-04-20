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
import com.ruoyi.system.domain.RunningRoutes;
import com.ruoyi.system.service.IRunningRoutesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 跑步路线Controller
 * 
 * @author ruoyi
 * @date 2026-01-10 15:38:30
 */
@RestController
@RequestMapping("/system/routes")
public class RunningRoutesController extends BaseController
{
    @Autowired
    private IRunningRoutesService runningRoutesService;

    /**
     * 查询跑步路线列表
     */
    @PreAuthorize("@ss.hasPermi('system:routes:list')")
    @GetMapping("/list")
    public TableDataInfo list(RunningRoutes runningRoutes)
    {
        startPage();
        List<RunningRoutes> list = runningRoutesService.selectRunningRoutesList(runningRoutes);
        return getDataTable(list);
    }

    /**
     * 导出跑步路线列表
     */
    @PreAuthorize("@ss.hasPermi('system:routes:export')")
    @Log(title = "跑步路线", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RunningRoutes runningRoutes)
    {
        List<RunningRoutes> list = runningRoutesService.selectRunningRoutesList(runningRoutes);
        ExcelUtil<RunningRoutes> util = new ExcelUtil<RunningRoutes>(RunningRoutes.class);
        util.exportExcel(response, list, "跑步路线数据");
    }

    /**
     * 获取跑步路线详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:routes:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(runningRoutesService.selectRunningRoutesById(id));
    }

    /**
     * 新增跑步路线
     */
    @PreAuthorize("@ss.hasPermi('system:routes:add')")
    @Log(title = "跑步路线", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RunningRoutes runningRoutes)
    {
        return toAjax(runningRoutesService.insertRunningRoutes(runningRoutes));
    }

    /**
     * 修改跑步路线
     */
    @PreAuthorize("@ss.hasPermi('system:routes:edit')")
    @Log(title = "跑步路线", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RunningRoutes runningRoutes)
    {
        return toAjax(runningRoutesService.updateRunningRoutes(runningRoutes));
    }

    /**
     * 删除跑步路线
     */
    @PreAuthorize("@ss.hasPermi('system:routes:remove')")
    @Log(title = "跑步路线", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(runningRoutesService.deleteRunningRoutesByIds(ids));
    }
}
