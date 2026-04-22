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
import com.ruoyi.system.domain.PerformanceLeaderboard;
import com.ruoyi.system.service.IPerformanceLeaderboardService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 成绩排行榜Controller
 * 
 * @author ruoyi
 * @date 2026-01-10 20:09:55
 */
@RestController
@RequestMapping("/system/performance_leaderboard")
public class PerformanceLeaderboardController extends BaseController
{
    @Autowired
    private IPerformanceLeaderboardService performanceLeaderboardService;

    /**
     * 查询成绩排行榜列表
     */
    @PreAuthorize("@ss.hasPermi('system:performance_leaderboard:list')")
    @GetMapping("/list")
    public TableDataInfo list(PerformanceLeaderboard performanceLeaderboard)
    {
        startPage();
        List<PerformanceLeaderboard> list = performanceLeaderboardService.selectPerformanceLeaderboardList(performanceLeaderboard);
        return getDataTable(list);
    }

    /**
     * 导出成绩排行榜列表
     */
    @PreAuthorize("@ss.hasPermi('system:performance_leaderboard:export')")
    @Log(title = "成绩排行榜", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PerformanceLeaderboard performanceLeaderboard)
    {
        List<PerformanceLeaderboard> list = performanceLeaderboardService.selectPerformanceLeaderboardList(performanceLeaderboard);
        ExcelUtil<PerformanceLeaderboard> util = new ExcelUtil<PerformanceLeaderboard>(PerformanceLeaderboard.class);
        util.exportExcel(response, list, "成绩排行榜数据");
    }

    /**
     * 获取成绩排行榜详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:performance_leaderboard:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(performanceLeaderboardService.selectPerformanceLeaderboardById(id));
    }

    /**
     * 新增成绩排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:performance_leaderboard:add')")
    @Log(title = "成绩排行榜", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PerformanceLeaderboard performanceLeaderboard)
    {
        return toAjax(performanceLeaderboardService.insertPerformanceLeaderboard(performanceLeaderboard));
    }

    /**
     * 修改成绩排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:performance_leaderboard:edit')")
    @Log(title = "成绩排行榜", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PerformanceLeaderboard performanceLeaderboard)
    {
        return toAjax(performanceLeaderboardService.updatePerformanceLeaderboard(performanceLeaderboard));
    }

    /**
     * 删除成绩排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:performance_leaderboard:remove')")
    @Log(title = "成绩排行榜", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(performanceLeaderboardService.deletePerformanceLeaderboardByIds(ids));
    }
}
