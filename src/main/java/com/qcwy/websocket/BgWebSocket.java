package com.qcwy.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by KouKi on 2017/2/25.
 */

@ServerEndpoint(value = "/bgWebSocket/{username}")
@Component
public class BgWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    //private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();
    public static CopyOnWriteArrayList<BgWebSocket> webSockets = new CopyOnWriteArrayList<>();

    private String username;
    private Session session;
    private Map<String, Session> map = new HashMap<>();

    /**
     * 连接建立成功调用的方法
     */

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        this.username = username;
        this.session = session;
        webSockets.add(this);     //加入set中
        map.put(username, session);
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前后台在线人数为" + getOnlineCount());
        try {
            sendMessage(username);
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSockets.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前后台在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
//        System.out.println("来自" + username + "的消息:" + message);
        //群发消息
        for (BgWebSocket item : webSockets) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @OnError
     **/
    public void onError(Session session, Throwable error) {
        System.out.println("后台WebSocket发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
//        this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (BgWebSocket item : webSockets) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    //原子性加1
    public static void addOnlineCount() {
        BgWebSocket.onlineCount.incrementAndGet();
    }

    //原子性减1
    public static void subOnlineCount() {
        BgWebSocket.onlineCount.decrementAndGet();
    }

    //根据openId发送消息
    public static void sendMsgByUsername(String username, String msg) throws IOException {
        for (BgWebSocket socket : webSockets) {
            if (username.equals(socket.getOpenId())) {
                socket.sendMessage(msg);
            }
        }
    }

    public static void setOnlineCount(AtomicInteger onlineCount) {
        BgWebSocket.onlineCount = onlineCount;
    }

    public String getOpenId() {
        return username;
    }

    public void setOpenId(String openId) {
        this.username = openId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Map<String, Session> getMap() {
        return map;
    }

    public void setMap(Map<String, Session> map) {
        this.map = map;
    }
}