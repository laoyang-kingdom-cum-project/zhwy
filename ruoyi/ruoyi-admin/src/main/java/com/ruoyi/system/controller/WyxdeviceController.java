package com.ruoyi.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Wyxdevice;
import com.ruoyi.system.service.IWyxdeviceService;
import com.ruoyi.framework.websocket.WebSocketServer;
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
    private static final Set<String> PUSH_CLIENT_IDS = ConcurrentHashMap.newKeySet();

    @Autowired
    private IWyxdeviceService wyxdeviceService;

    @Value("${unipush.webhook-url:}")
    private String uniPushWebhookUrl;

    @Value("${unipush.webhook-secret:}")
    private String uniPushWebhookSecret;

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
     * 注册uni-push客户端cid
     */
    @Anonymous
    @PostMapping("/pushClientId")
    public AjaxResult registerPushClientId(@RequestBody PushClientRegisterRequest request)
    {
        if (request == null || request.getCid() == null || request.getCid().trim().isEmpty()) {
            return error("push client id不能为空");
        }

        PUSH_CLIENT_IDS.add(request.getCid().trim());
        System.out.println("收到Push客户端注册，cid=" + request.getCid() + "，platform=" + request.getPlatform()
                + "，当前数量=" + PUSH_CLIENT_IDS.size());
        return success("注册成功");
    }

    /**
     * 唤起鸿蒙设备智慧生活
     */
    @Anonymous
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
            Process connectProcess = Runtime.getRuntime().exec("hdc tconn " + ip);
            int connectExitCode = connectProcess.waitFor();
            if (connectExitCode != 0) {
                return error("无线调试连接失败，IP：" + ip + "，返回码：" + connectExitCode);
            }

            // 3. 打开智慧生活
            Process openProcess = Runtime.getRuntime().exec("hdc shell aa start -b com.huawei.hmos.ailife -a EntryAbility");
            int openExitCode = openProcess.waitFor();
            if (openExitCode != 0) {
                return error("打开智慧生活指令执行失败，返回码：" + openExitCode);
            }

            // 4. 异步倒计时10秒，然后通过WebSocket通知前端
            new Thread(() -> {
                try {
                    System.out.println("后台线程开始倒计时10秒...");
                    Thread.sleep(10000);
                    String message = "配置已完成，请返回应用";
                    boolean pushed = sendAiLifeFinishedPush(message);
                    if (!pushed) {
                        System.out.println("系统Push未发送成功，继续使用WebSocket兜底");
                    }
                    // 前台在线时仍通过WebSocket兜底
                    WebSocketServer.sendInfo("{\"type\":\"ailife_finished\", \"message\":\"" + message + "\"}");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            return success("执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return error("执行出错：" + e.getMessage());
        }
    }

    private String getHdcDeviceIpFromEnv() {
        try {
            java.io.File currentDir = new java.io.File(System.getProperty("user.dir"));
            while (currentDir != null) {
                java.io.File envFile = new java.io.File(currentDir, ".env");
                if (envFile.exists()) {
                    System.out.println("找到.env文件: " + envFile.getAbsolutePath());
                    java.util.List<String> lines = java.nio.file.Files.readAllLines(envFile.toPath());
                    for (String line : lines) {
                        if (line.trim().startsWith("HDC_DEVICE_IP=")) {
                            return line.split("=", 2)[1].trim();
                        }
                    }
                }
                currentDir = currentDir.getParentFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean sendAiLifeFinishedPush(String message) {
        if (uniPushWebhookUrl == null || uniPushWebhookUrl.trim().isEmpty()) {
            System.out.println("未配置UNIPUSH_WEBHOOK_URL，跳过系统Push");
            return false;
        }
        if (PUSH_CLIENT_IDS.isEmpty()) {
            System.out.println("暂无Push客户端cid，跳过系统Push");
            return false;
        }

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", "return_to_app");
            payload.put("type", "ailife_finished");

            Map<String, Object> body = new HashMap<>();
            body.put("secret", uniPushWebhookSecret);
            body.put("clientIds", new ArrayList<>(PUSH_CLIENT_IDS));
            body.put("title", "智慧社区");
            body.put("content", message);
            body.put("payload", payload);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Map> response = new RestTemplate().postForEntity(
                    uniPushWebhookUrl.trim(),
                    new HttpEntity<>(body, headers),
                    Map.class
            );
            System.out.println("系统Push发送结果: " + response.getStatusCode() + " " + response.getBody());
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.out.println("系统Push发送失败: " + e.getMessage());
            return false;
        }
    }

    public static class PushClientRegisterRequest {
        private String cid;
        private String platform;
        private String appid;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }
    }
}
