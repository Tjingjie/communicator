package com.alex.chatroom.websocket;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

//import com.alex.chatroom.utils.MyHandShakeInterceptor;

/**
 * Component注解告诉SpringMVC该类是一个SpringIOC容器下管理的类
 * 其实@Controller, @Service, @Repository是@Component的细化
 * @Component注解，就是相当于告诉SpringMVC这是SpringIOC容器下管理的类，和@Controller注解其实是一样的，
 * 通过localhost:8080/web/能访问到Controller并做映射，
 * 通过localhost:8080/web/同样可以访问MyWebSocketConfig这个类在SpringIOC下的对象，从而服务端进行WebSocket服务。
 */

@Component
@Configuration
@EnableWebSocket
public class MyWebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer{

	//注入处理器
	/*
	 *MyWebSocketHandler  handler规定了服务端WebSocket的处理。而MyHandShakeInterceptor是客户端与服务端握手连接前后拦截器。
	 */
	@Resource
    private MyWebSocketHandler handler;
	//MyHandShakeInterceptor myHandShakeInterceptor;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
		// TODO Auto-generated method stub
		//添加websocket处理器，添加握手拦截器
        webSocketHandlerRegistry.addHandler(handler, "/ws.action").addInterceptors(new MyHandShakeInterceptor());

        //添加websocket处理器，添加握手拦截器
        webSocketHandlerRegistry.addHandler(handler, "/ws/sockjs.action").addInterceptors(new MyHandShakeInterceptor()).withSockJS();
        
	}

}
