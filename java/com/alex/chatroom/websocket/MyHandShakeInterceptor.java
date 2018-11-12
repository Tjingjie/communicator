package com.alex.chatroom.websocket;


import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.poker.User;

/**
 * websocket握手拦截器
 * 拦截握手前，握手后的两个切面
 * 主要是拦截连接并且设置用户session标识作用，这样在最后一个处理请求类可以分清是哪个已连接用户发出的请求~，
 */
//加Component注解是为了在MyWebSocketConfig中才可以通过@Autowired将其自动装载
//@Component
@Service
public class MyHandShakeInterceptor implements HandshakeInterceptor{

	@Override
	public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
		// TODO Auto-generated method stub
		System.out.println("======握手成功啦");
	}

	/*
	 *客户端与服务端握手连接前将键名"uid"，值为用户id的这个键值对加入到指定参数map中。
	 *为服务器建立与相应客户端连接的WebSocketSession打下基础。 
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler,
			Map<String, Object> map) throws Exception {
		System.out.println("Websocket:用户[ID:" + ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getSession().getAttribute("userId") + "]已经建立连接");
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            String userId=(String) ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getSession().getAttribute("userId");
            String roomName=(String) ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getSession().getAttribute("roomName");
            map.put("roomName", roomName);//将房间名存储在map中，在WebSocketHandler取出
            if(userId!=null){
                map.put("uid",userId);//为服务器创建WebSocketSession做准备
                System.out.println("用户id："+userId+" 被加入");
            }else{
                System.out.println("user为空");
                //return false;
            }
        }
        return true;
	}

}
