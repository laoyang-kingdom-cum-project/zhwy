package com.ruoyi.framework.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket服务
 */
@ServerEndpoint("/websocket/device")
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    // 存放每个客户端对应的MyWebSocket对象
    private static final CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("有新连接加入！当前在线人数为: {}", webSocketSet.size());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("有一连接关闭！当前在线人数为: {}", webSocketSet.size());
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到客户端消息: {}", message);
        if ("ping".equalsIgnoreCase(message)) {
            try {
                sendMessage("{\"type\":\"pong\"}");
            } catch (IOException e) {
                log.warn("WebSocket pong发送失败: {}", e.getMessage());
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        webSocketSet.remove(this);
        log.error("WebSocket发生错误，当前在线人数为: {}", webSocketSet.size(), error);
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        if (this.session == null || !this.session.isOpen()) {
            throw new IOException("WebSocket session已关闭");
        }
        synchronized (this.session) {
            this.session.getBasicRemote().sendText(message);
        }
    }

    public static int getOnlineCount() {
        return webSocketSet.size();
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) {
        int successCount = 0;
        log.info("推送消息到全体客户端，当前在线人数: {}，推送内容: {}", webSocketSet.size(), message);
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
                successCount++;
            } catch (IOException e) {
                webSocketSet.remove(item);
                log.warn("WebSocket消息发送失败，已移除失效连接: {}", e.getMessage());
            }
        }
        log.info("WebSocket消息推送完成，成功: {}，剩余在线人数: {}", successCount, webSocketSet.size());
    }
}
