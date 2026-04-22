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
import com.ruoyi.system.domain.SportTeam;
import com.ruoyi.system.service.ISportTeamService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 运动团队列Controller
 * 
 * @author ruoyi
 * @date 2026-01-15 15:26:47
 */
@RestController
@RequestMapping("/system/team")
public class SportTeamController extends BaseController
{
    @Autowired
    private ISportTeamService sportTeamService;

    /**
     * 查询运动团队列列表
     */
    @PreAuthorize("@ss.hasPermi('system:team:list')")
    @GetMapping("/list")
    public TableDataInfo list(SportTeam sportTeam)
    {
        startPage();
        List<SportTeam> list = sportTeamService.selectSportTeamList(sportTeam);
        return getDataTable(list);
    }

    /**
     * 导出运动团队列列表
     */
    @PreAuthorize("@ss.hasPermi('system:team:export')")
    @Log(title = "运动团队列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SportTeam sportTeam)
    {
        List<SportTeam> list = sportTeamService.selectSportTeamList(sportTeam);
        ExcelUtil<SportTeam> util = new ExcelUtil<SportTeam>(SportTeam.class);
        util.exportExcel(response, list, "运动团队列数据");
    }

    /**
     * 获取运动团队列详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:team:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sportTeamService.selectSportTeamById(id));
    }

    /**
     * 新增运动团队列
     */
    @PreAuthorize("@ss.hasPermi('system:team:add')")
    @Log(title = "运动团队列", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SportTeam sportTeam)
    {
        return toAjax(sportTeamService.insertSportTeam(sportTeam));
    }

    /**
     * 修改运动团队列
     */
    @PreAuthorize("@ss.hasPermi('system:team:edit')")
    @Log(title = "运动团队列", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SportTeam sportTeam)
    {
        return toAjax(sportTeamService.updateSportTeam(sportTeam));
    }

    /**
     * 删除运动团队列
     */
    @PreAuthorize("@ss.hasPermi('system:team:remove')")
    @Log(title = "运动团队列", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sportTeamService.deleteSportTeamByIds(ids));
    }
}
