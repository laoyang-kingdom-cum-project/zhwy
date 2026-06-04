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
import com.ruoyi.system.domain.Wyxdevice;
import com.ruoyi.system.service.IWyxdeviceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备列表Controller
 * 
 * @author ruoyi
 * @date 2026-04-16 19:31:38
 */
@RestController
@RequestMapping("/system/wyxdevice")
public class WyxdeviceController extends BaseController
{
    @Autowired
    private IWyxdeviceService wyxdeviceService;

    /**
     * 查询设备列表列表
     */
    @PreAuthorize("@ss.hasPermi('system:wyxdevice:list')")
    @GetMapping("/list")
    public TableDataInfo list(Wyxdevice wyxdevice)
    {
        startPage();
        List<Wyxdevice> list = wyxdeviceService.selectWyxdeviceList(wyxdevice);
        return getDataTable(list);
    }

    /**
     * 导出设备列表列表
     */
    @PreAuthorize("@ss.hasPermi('system:wyxdevice:export')")
    @Log(title = "设备列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Wyxdevice wyxdevice)
    {
        List<Wyxdevice> list = wyxdeviceService.selectWyxdeviceList(wyxdevice);
        ExcelUtil<Wyxdevice> util = new ExcelUtil<Wyxdevice>(Wyxdevice.class);
        util.exportExcel(response, list, "设备列表数据");
    }

    /**
     * 获取设备列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:wyxdevice:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wyxdeviceService.selectWyxdeviceById(id));
    }

    /**
     * 新增设备列表
     */
    @PreAuthorize("@ss.hasPermi('system:wyxdevice:add')")
    @Log(title = "设备列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Wyxdevice wyxdevice)
    {
        return toAjax(wyxdeviceService.insertWyxdevice(wyxdevice));
    }

    /**
     * 修改设备列表
     */
    @PreAuthorize("@ss.hasPermi('system:wyxdevice:edit')")
    @Log(title = "设备列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Wyxdevice wyxdevice)
    {
        return toAjax(wyxdeviceService.updateWyxdevice(wyxdevice));
    }

    /**
     * 删除设备列表
     */
    @PreAuthorize("@ss.hasPermi('system:wyxdevice:remove')")
    @Log(title = "设备列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wyxdeviceService.deleteWyxdeviceByIds(ids));
    }

    /**
     * 唤起鸿蒙设备智慧生活
     */
    @GetMapping("/openAiLife")
    public AjaxResult openAiLife()
    {
        try {
            // 1. 获取无线调试IP
            String ip = getHdcDeviceIpFromEnv();
            if (ip == null || ip.isEmpty()) {
                return error("未在.env文件中找到HDC_DEVICE_IP配置");
            }

            // 2. 连接设备
            Process connectProcess = Runtime.getRuntime().exec("./hdc tconn " + ip);
            int connectExitCode = connectProcess.waitFor();
            if (connectExitCode != 0) {
                return error("无线调试连接失败，IP：" + ip + "，返回码：" + connectExitCode);
            }

            // 3. 打开智慧生活
            Process openProcess = Runtime.getRuntime().exec("./hdc shell aa start -b com.huawei.hmos.ailife -a EntryAbility");
            int openExitCode = openProcess.waitFor();
            if (openExitCode != 0) {
                return error("打开智慧生活指令执行失败，返回码：" + openExitCode);
            }

            // 4. 倒计时10秒
            for (int i = 10; i > 0; i--) {
                System.out.println("倒计时：" + i + "秒");
                Thread.sleep(1000);
            }

            // 5. 返回原应用
            Process returnProcess = Runtime.getRuntime().exec("./hdc shell aa start -b com.wyx.lianfanshuang.zhsq -a EntryAbility");
            int returnExitCode = returnProcess.waitFor();
            if (returnExitCode != 0) {
                return error("返回原应用执行失败，返回码：" + returnExitCode);
            }

            return success("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return error("执行出错：" + e.getMessage());
        }
    }

    private String getHdcDeviceIpFromEnv() {
        try {
            // 优先查找上级目录（如果是在ruoyi目录下运行）
            java.io.File envFile = new java.io.File("../.env");
            if (!envFile.exists()) {
                envFile = new java.io.File(".env");
            }
            if (envFile.exists()) {
                java.util.List<String> lines = java.nio.file.Files.readAllLines(envFile.toPath());
                for (String line : lines) {
                    if (line.trim().startsWith("HDC_DEVICE_IP=")) {
                        return line.split("=", 2)[1].trim();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
