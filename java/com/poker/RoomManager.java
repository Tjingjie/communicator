package com.poker;
/**
 * 用于管理各个房间的方法，属性均为静态属性，方法均为静态方法
 * @author 陶荆杰
 *
 */

import java.util.HashMap;
import java.util.Map;

import com.alex.chatroom.controller.StateManager;

public class RoomManager {
	private static final Map<String, PokerRoom> roomMap=new HashMap<>();//保存房间号对应的房间
	private static final Map<String, PokerRoom> userMap=new HashMap<>();//保存用户id对应的房间
	public static boolean creatRoom(String hostId,String roomId) {
		/**
		 * 创建房间的方法，若创建成功返回true，否则返回false
		 */
		if(userMap.containsKey(hostId)||roomMap.containsKey(roomId)) return false;//用户已在房间或房间已存在时返回false
		PokerRoom room=new PokerRoom(hostId);
		roomMap.put(roomId, room);//添加相应绑定
		userMap.put(hostId, room);
		return true;
	}
	
	public static PokerRoom findRoom(String roomName) {//根据房间名获取房间的方法
		return roomMap.get(roomName);
	}
}
