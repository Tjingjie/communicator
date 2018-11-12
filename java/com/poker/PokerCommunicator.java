package com.poker;
/**
 * poker game 通信类，包装了网页客户端与后台的通信
 * @author 陶荆杰
 */

import java.io.IOException;
import java.util.Map;
import java.util.Set;

//import javax.websocket.RemoteEndpoint.Basic;
//import javax.websocket.Session;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.sockjs.transport.SockJsSession;

import com.alex.chatroom.websocket.MyWebSocketHandler;
import com.alibaba.fastjson.JSON;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import bridge.domain.CallContract;
import bridge.domain.Card;
import bridge.domain.Deck;
import bridge.domain.PlayerPosition;
import bridge.domain.utils.BridgeHelper;


public class PokerCommunicator {
	private WebSocketSession sender;
	public PokerCommunicator(WebSocketSession session) {
		/**
		 * 构造方法，需传入与客户通信的session
		 */
		this.sender=session;
	}
	public void send(Object obj)  throws IOException{
		/**
		 * 将对象转换为json发送给客户端的方法
		 */
		String gson=new GsonBuilder().create().toJson(obj);
		sender.sendMessage(new TextMessage(gson));
	}
	public void sendMessage(String message) throws IOException{
		/**
		 * 发送牌局信息以外的消息时使用的方法
		 */
		sender.sendMessage(new TextMessage(message));
	}
	public boolean sendtoUser(WebSocketSession session,Deck deck) throws IOException {
		/**
		 * 将玩家手牌信息发送给指定玩家
		 */
		if (session == null) return false;
        //WebSocketSession session = userSocketSessionMap.get(uid);
        if (session != null && session.isOpen()) {
        	String gson=new GsonBuilder().create().toJson(deck);
            session.sendMessage(new TextMessage(gson));
        }
        return true;
	}
	
	public static void sendToAll(Object object,String roomName) throws IOException{//向房间中所有玩家发送消息的静态方法
		Map<String, WebSocketSession> map=MyWebSocketHandler.roomUserMap.get(roomName);//获取房间
		Set<String> set=map.keySet();//获取房间内所有用户
		for (String string : set) {
			WebSocketSession session=map.get(string);
			String gson=new GsonBuilder().create().toJson(object);
			session.sendMessage(new TextMessage(gson));//向每个用户发送消息
		}
	}
	/*此处为使用旧的通讯方式所使用的方法，已废弃
	public void send(Deck deck)  throws IOException{
		/**
		 * 将玩家手牌信息发给该玩家的方法
		 *
		sender.sendMessage(new TextMessage(BridgeHelper.deckToPBNHand(deck)));
	}
	
	public void send(PlayerPosition position) throws IOException {
		/**
		 * 将当前行动玩家方信息发给玩家的方法
		 *
		sender.sendMessage(new TextMessage(position.getFullName().substring(0, 1)));
	}
	public void send(Card card) throws IOException{//向对应玩家发送其他玩家出的牌的方法
		sender.sendMessage(new TextMessage(card.getSuit().getShortName()+card.getRank().toString()));
	}
	
	public void send(CallContract contract)  throws IOException{
		/**
		 * 发送玩家叫品的方法
		 *
		sender.sendMessage(new TextMessage(contract.getShortString()));
	}
	

	public void send(String message) throws IOException{
		/**
		 * 发送牌局信息以外的消息时使用的方法
		 *
		sender.sendMessage(new TextMessage(message));
	}
	*/
}
