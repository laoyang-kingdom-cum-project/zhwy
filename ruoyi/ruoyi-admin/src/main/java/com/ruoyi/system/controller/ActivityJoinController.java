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
import com.ruoyi.system.domain.ActivityJoin;
import com.ruoyi.system.service.IActivityJoinService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 活动报名Controller
 * 
 * @author ruoyi
 * @date 2026-04-17 15:48:32
 */
@RestController
@RequestMapping("/system/join")
public class ActivityJoinController extends BaseController
{
    @Autowired
    private IActivityJoinService activityJoinService;

    /**
     * 查询活动报名列表
     */
    @PreAuthorize("@ss.hasPermi('system:join:list')")
    @GetMapping("/list")
    public TableDataInfo list(ActivityJoin activityJoin)
    {
        startPage();
        List<ActivityJoin> list = activityJoinService.selectActivityJoinList(activityJoin);
        return getDataTable(list);
    }

    /**
     * 导出活动报名列表
     */
    @PreAuthorize("@ss.hasPermi('system:join:export')")
    @Log(title = "活动报名", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ActivityJoin activityJoin)
    {
        List<ActivityJoin> list = activityJoinService.selectActivityJoinList(activityJoin);
        ExcelUtil<ActivityJoin> util = new ExcelUtil<ActivityJoin>(ActivityJoin.class);
        util.exportExcel(response, list, "活动报名数据");
    }

    /**
     * 获取活动报名详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:join:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(activityJoinService.selectActivityJoinById(id));
    }

    /**
     * 新增活动报名
     */
    @PreAuthorize("@ss.hasPermi('system:join:add')")
    @Log(title = "活动报名", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ActivityJoin activityJoin)
    {
        return toAjax(activityJoinService.insertActivityJoin(activityJoin));
    }

    /**
     * 修改活动报名
     */
    @PreAuthorize("@ss.hasPermi('system:join:edit')")
    @Log(title = "活动报名", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ActivityJoin activityJoin)
    {
        return toAjax(activityJoinService.updateActivityJoin(activityJoin));
    }

    /**
     * 删除活动报名
     */
    @PreAuthorize("@ss.hasPermi('system:join:remove')")
    @Log(title = "活动报名", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(activityJoinService.deleteActivityJoinByIds(ids));
    }
}
