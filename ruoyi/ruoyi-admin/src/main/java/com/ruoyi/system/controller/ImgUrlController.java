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
import com.ruoyi.system.domain.ImgUrl;
import com.ruoyi.system.service.IImgUrlService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图片上传Controller
 * 
 * @author ruoyi
 * @date 2026-01-08
 */
@RestController
@RequestMapping("/system/url")
public class ImgUrlController extends BaseController
{
    @Autowired
    private IImgUrlService imgUrlService;

    /**
     * 查询图片上传列表
     */
    @PreAuthorize("@ss.hasPermi('system:url:list')")
    @GetMapping("/list")
    public TableDataInfo list(ImgUrl imgUrl)
    {
        startPage();
        List<ImgUrl> list = imgUrlService.selectImgUrlList(imgUrl);
        return getDataTable(list);
    }

    /**
     * 导出图片上传列表
     */
    @PreAuthorize("@ss.hasPermi('system:url:export')")
    @Log(title = "图片上传", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ImgUrl imgUrl)
    {
        List<ImgUrl> list = imgUrlService.selectImgUrlList(imgUrl);
        ExcelUtil<ImgUrl> util = new ExcelUtil<ImgUrl>(ImgUrl.class);
        util.exportExcel(response, list, "图片上传数据");
    }

    /**
     * 获取图片上传详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:url:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(imgUrlService.selectImgUrlById(id));
    }

    /**
     * 新增图片上传
     */
    @PreAuthorize("@ss.hasPermi('system:url:add')")
    @Log(title = "图片上传", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ImgUrl imgUrl)
    {
        return toAjax(imgUrlService.insertImgUrl(imgUrl));
    }

    /**
     * 修改图片上传
     */
    @PreAuthorize("@ss.hasPermi('system:url:edit')")
    @Log(title = "图片上传", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ImgUrl imgUrl)
    {
        return toAjax(imgUrlService.updateImgUrl(imgUrl));
    }

    /**
     * 删除图片上传
     */
    @PreAuthorize("@ss.hasPermi('system:url:remove')")
    @Log(title = "图片上传", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(imgUrlService.deleteImgUrlByIds(ids));
    }
}
