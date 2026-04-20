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
import com.ruoyi.system.domain.CheckInLeaderboard;
import com.ruoyi.system.service.ICheckInLeaderboardService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 打卡排行榜Controller
 * 
 * @author ruoyi
 * @date 2026-01-10 17:25:39
 */
@RestController
@RequestMapping("/system/leaderboard")
public class CheckInLeaderboardController extends BaseController
{
    @Autowired
    private ICheckInLeaderboardService checkInLeaderboardService;

    /**
     * 查询打卡排行榜列表
     */
    @PreAuthorize("@ss.hasPermi('system:leaderboard:list')")
    @GetMapping("/list")
    public TableDataInfo list(CheckInLeaderboard checkInLeaderboard)
    {
        startPage();
        List<CheckInLeaderboard> list = checkInLeaderboardService.selectCheckInLeaderboardList(checkInLeaderboard);
        return getDataTable(list);
    }

    /**
     * 导出打卡排行榜列表
     */
    @PreAuthorize("@ss.hasPermi('system:leaderboard:export')")
    @Log(title = "打卡排行榜", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CheckInLeaderboard checkInLeaderboard)
    {
        List<CheckInLeaderboard> list = checkInLeaderboardService.selectCheckInLeaderboardList(checkInLeaderboard);
        ExcelUtil<CheckInLeaderboard> util = new ExcelUtil<CheckInLeaderboard>(CheckInLeaderboard.class);
        util.exportExcel(response, list, "打卡排行榜数据");
    }

    /**
     * 获取打卡排行榜详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:leaderboard:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(checkInLeaderboardService.selectCheckInLeaderboardById(id));
    }

    /**
     * 新增打卡排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:leaderboard:add')")
    @Log(title = "打卡排行榜", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CheckInLeaderboard checkInLeaderboard)
    {
        return toAjax(checkInLeaderboardService.insertCheckInLeaderboard(checkInLeaderboard));
    }

    /**
     * 修改打卡排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:leaderboard:edit')")
    @Log(title = "打卡排行榜", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CheckInLeaderboard checkInLeaderboard)
    {
        return toAjax(checkInLeaderboardService.updateCheckInLeaderboard(checkInLeaderboard));
    }

    /**
     * 删除打卡排行榜
     */
    @PreAuthorize("@ss.hasPermi('system:leaderboard:remove')")
    @Log(title = "打卡排行榜", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(checkInLeaderboardService.deleteCheckInLeaderboardByIds(ids));
    }
}
