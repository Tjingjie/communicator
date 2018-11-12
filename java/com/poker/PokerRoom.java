package com.poker;

import java.io.IOException;
/**
 * 管理玩家的房间类，提供与房间处理有关的接口
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.web.socket.WebSocketSession;
import com.alex.chatroom.controller.StateManager;

import bridge.domain.PlayerPosition;

public class PokerRoom {
	private Set<User> players=new HashSet<User>();//保存房间内所有玩家的session的变量
	private StateManager manager;//与房间绑定的游戏状态处理机
	private Map<String, PlayerPosition> positionMap=new HashMap<>();//处理玩家在牌桌中的对应位置
	private String hostId;//保存房主id的方法
	
	public PokerRoom(String hostId) {//房主创建房间的构造方法
		this.hostId=hostId;
		manager=new StateManager(this);
	}
	
	public boolean add(User user) {//添加玩家的方法
		if(players.size()>4||players.contains(user)) return false;
		players.add(user);
		if(positionMap.size()==0) {//对房间人数进行判断，按照玩家加入房间的顺序依次添加到北东南西的位置
        	positionMap.put(user.getUserId(), PlayerPosition.NORTH);
        }else if(positionMap.size()==1) {
        	positionMap.put(user.getUserId(), PlayerPosition.EAST);
        }else if(positionMap.size()==2) {
        	positionMap.put(user.getUserId(), PlayerPosition.SOUTH);
        }else if(positionMap.size()==3) {
        	positionMap.put(user.getUserId(), PlayerPosition.WEST);
        }
		user.setRoom(this);
		return true;
	}
	
	public PlayerPosition findPosition(String userId) {
		/**
		 * 查找用户位置的方法
		 */
		return positionMap.get(userId);
	}
	
	public Set<User> getPlayers(){
		return players;
	}
	public int size() {
		/**
		 * 获取房间人数的方法
		 */
		return players.size();
	} 
	
	public String getHostId(){
		/**
		 * 获取房主id的方法
		 */
		return hostId;
	}
	
	public void handelMessage(WebSocketSession session,String message) {//处理与session绑定的message的方法
		manager.handleMessage(matchUser(session),message);
	}
	
	public void sendToAll(Object object) {
		for (User user : players) {
			try {
				user.receive(object);
			} catch (IOException e) {
				// TODO 添加断线处理方法
				e.printStackTrace();
			}
		}
	}
	public User matchUser(WebSocketSession session) {
		/**
		 * 根据session匹配房间内玩家的方法，由于房间内只有4个人，故费时较少
		 */
		for (User user : players) {
			if(user.getSession().getId()==session.getId()) {
				return user;
			}
		}
		return null;
	}
	

	public StateManager getManager() {//获取房间状态机的方法
		return manager;
	}
}
