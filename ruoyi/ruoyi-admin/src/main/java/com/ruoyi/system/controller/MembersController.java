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
import com.ruoyi.system.domain.Members;
import com.ruoyi.system.service.IMembersService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 成员Controller
 * 
 * @author ruoyi
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/system/members")
public class MembersController extends BaseController
{
    @Autowired
    private IMembersService membersService;

    /**
     * 查询成员列表
     */
    @PreAuthorize("@ss.hasPermi('system:members:list')")
    @GetMapping("/list")
    public TableDataInfo list(Members members)
    {
        startPage();
        List<Members> list = membersService.selectMembersList(members);
        return getDataTable(list);
    }

    /**
     * 导出成员列表
     */
    @PreAuthorize("@ss.hasPermi('system:members:export')")
    @Log(title = "成员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Members members)
    {
        List<Members> list = membersService.selectMembersList(members);
        ExcelUtil<Members> util = new ExcelUtil<Members>(Members.class);
        util.exportExcel(response, list, "成员数据");
    }

    /**
     * 获取成员详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:members:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(membersService.selectMembersById(id));
    }

    /**
     * 新增成员
     */
    @PreAuthorize("@ss.hasPermi('system:members:add')")
    @Log(title = "成员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Members members)
    {
        return toAjax(membersService.insertMembers(members));
    }

    /**
     * 修改成员
     */
    @PreAuthorize("@ss.hasPermi('system:members:edit')")
    @Log(title = "成员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Members members)
    {
        return toAjax(membersService.updateMembers(members));
    }

    /**
     * 删除成员
     */
    @PreAuthorize("@ss.hasPermi('system:members:remove')")
    @Log(title = "成员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(membersService.deleteMembersByIds(ids));
    }
}
