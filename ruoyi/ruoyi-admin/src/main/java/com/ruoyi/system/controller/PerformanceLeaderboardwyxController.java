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
import com.ruoyi.system.domain.PerformanceLeaderboardwyx;
import com.ruoyi.system.service.IPerformanceLeaderboardwyxService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 成绩排行榜Controller
 * 
 * @author ruoyi
 * @date 2026-01-19 16:27:45
 */
@RestController
@RequestMapping("/system/leaderboardwyx")
public class PerformanceLeaderboardwyxController extends BaseController
{
    @Autowired
    private IPerformanceLeaderboardwyxService performanceLeaderboardwyxService;

    /**
     * 查询成绩排行榜列表
     */
    @PreAuthorize("@ss.hasPermi('system:leaderboardwyx:list')")
    @GetMapping("/list")
    public TableDataInfo list(PerformanceLeaderboardwyx performanceLeaderboardwyx)
    {
        startPage();
        List<PerformanceLeaderboardwyx> list = performanceLeaderboardwyxService.selectPerformanceLeaderboardwyxList(performanceLeaderboardwyx);
        return getDataTable(list);
    }

    /**
     * 导出成绩排行榜列表
     */
    @PreAuthorize("@ss.hasPermi('system:leaderboardwyx:export')")
    @Log(title = "成绩排行榜", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PerformanceLeaderboardwyx performanceLeaderboardwyx)
    {
        List<PerformanceLeaderboardwyx> list = performanceLeaderboardwyxService.selectPerformanceLeaderboardwyxList(performanceLeaderboardwyx);
        ExcelUtil<PerformanceLeaderboardwyx> util = new ExcelUtil<PerformanceLeaderboardwyx>(PerformanceLeaderboardwyx.class);
        util.exportExcel(response, list, "成绩排行榜数据");
    }

    /**
     * 获取成绩排行榜详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:leaderboardwyx:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(performanceLeaderboardwyxService.selectPerformanceLeaderboardwyxById(id));
    }

    /**
     * 新增成绩排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:leaderboardwyx:add')")
    @Log(title = "成绩排行榜", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PerformanceLeaderboardwyx performanceLeaderboardwyx)
    {
        return toAjax(performanceLeaderboardwyxService.insertPerformanceLeaderboardwyx(performanceLeaderboardwyx));
    }

    /**
     * 修改成绩排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:leaderboardwyx:edit')")
    @Log(title = "成绩排行榜", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PerformanceLeaderboardwyx performanceLeaderboardwyx)
    {
        return toAjax(performanceLeaderboardwyxService.updatePerformanceLeaderboardwyx(performanceLeaderboardwyx));
    }

    /**
     * 删除成绩排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:leaderboardwyx:remove')")
    @Log(title = "成绩排行榜", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(performanceLeaderboardwyxService.deletePerformanceLeaderboardwyxByIds(ids));
    }
}
