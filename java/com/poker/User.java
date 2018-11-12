package com.poker;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.GsonBuilder;

import bridge.domain.Deck;

public class User {

	private String userId;//用户id
	private WebSocketSession session;//用户的session
	private PokerRoom room;//用户所在房间
	public User(String userId,WebSocketSession session) {
		this.userId=userId;
		this.session=session;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public WebSocketSession getSession() {
		return session;
	}
	
	public void setRoom(PokerRoom room) {
		this.room=room;
	}
	public PokerRoom getRoom() {
		return room;
	}
	public void receive(Object object) throws IOException{
		// TODO Auto-generated method stub
		String gson=new GsonBuilder().create().toJson(object);
		session.sendMessage(new TextMessage(gson));
		
	}
}
