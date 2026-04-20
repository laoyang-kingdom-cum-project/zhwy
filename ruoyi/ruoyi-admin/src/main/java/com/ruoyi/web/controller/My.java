package com.ruoyi.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;


import java.util.stream.Collectors; // 导入 Collectors

@RestController
public class My {

    @Autowired
    private RestTemplate restTemplate;

    /* 把 RestTemplate 扔进容器 */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /* ====== 1. 原来的 /ping ====== */
    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }

    /* ====== 2. 带 token 调自己 /system/team/list ====== */
    private List<Map<String, Object>> getTeamList() {
        /*  从当前请求头里拿真正的 JWT  */
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String bearer = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.isNotBlank(bearer) && bearer.startsWith("Bearer ")) {
            token = bearer.substring(7);
        }
        if (token == null) {
            throw new RuntimeException("请求头缺少 JWT");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "http://localhost/por-api/system/team/list";
        ResponseEntity<Map> resp = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class);

        return (List<Map<String, Object>>) resp.getBody().get("rows");
    }

    /* ====== 3. 对外接口：根据 id 返回对应团队 ====== */
    @GetMapping("/system/team/list/xq/{id}")
    public AjaxResult getTeam(@PathVariable Integer id) {
        Map<String, Object> team = getTeamList().stream()
                .filter(t -> id.equals(t.get("id")))
                .findFirst()
                .orElse(null);
        return team == null ? AjaxResult.error("团队不存在") : AjaxResult.success(team);
    }


    /* ====== 4. 发送验证码 ====== */

    /* ====== 4. 发送验证码（POST 版） ====== */

    @PostMapping("/system/mycaptcha/send")
    public AjaxResult sendCaptcha(@RequestBody Map<String,String> req) {
        String target = req.get("target");

        // 1. 生成 6 位随机验证码
        String code = String.valueOf((int)((Math.random()*9+1)*100000));

        // 2. 5 分钟后的过期时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expireAt = sdf.format(new Date(System.currentTimeMillis() + 5*60*1000));

        // 3. 组装请求体
        HashMap<String,Object> body = new HashMap<>();
        body.put("id", null);
        body.put("target", target);
        body.put("code", code);
        body.put("expireAt", expireAt);

        // 4. 调用后端服务
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<HashMap<String,Object>> entity = new HttpEntity<>(body, headers);

        // >>> 打印原始返回（捕获响应） <<<
        ResponseEntity<String> resp = restTemplate.postForEntity(
                "http://localhost/por-api/system/mycaptcha", entity, String.class);
        System.out.println("【/system/mycaptcha】原始返回：" + resp.getBody());

        // 5. 返回验证码
        HashMap<String,String> result = new HashMap<>();
        result.put("code", code);
        return AjaxResult.success(result);
    }

    /* ====== 5. 校验验证码（带明细提示 & 打印原始返回） ====== */
    @PostMapping("/system/mycaptcha/verify")
    public AjaxResult verifyCaptcha(@RequestBody Map<String,String> req) {
        String target = req.get("target");
        String code   = req.get("code");

        // 1. 调后台列表接口
        String url = "http://localhost/por-api/system/mycaptcha/list?target={target}&code={code}";
        ResponseEntity<Map> resp = restTemplate.getForEntity(url, Map.class, target, code);

        // >>> 打印原始返回 <<<
        System.out.println("【/system/mycaptcha/list】原始返回：" + resp.getBody());

        List<Map<String,Object>> rows = (List<Map<String,Object>>) resp.getBody().get("rows");
        if (rows == null || rows.isEmpty()) {
            return AjaxResult.error("验证码不存在或已失效");
        }

        // 2. 检查是否至少有一条未过期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowStr = sdf.format(new Date());
        for (Map<String,Object> r : rows) {
            String expireAt = (String) r.get("expireAt");
            if (expireAt != null && nowStr.compareTo(expireAt) < 0) {
                return AjaxResult.success("验证码正确", true);
            }
        }
        return AjaxResult.error("验证码已过期");
    }

}