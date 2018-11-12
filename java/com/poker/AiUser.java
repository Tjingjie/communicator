package com.poker;

import java.io.IOException;

/**
 * 
 *用于提供AI接口的类，继承于com.Poker.User，使用统一的对外通信接口
 *
 */
public class AiUser extends User {
	
	public AiUser(String userId) {//使用唯一id（房间内唯一）构造AiUser
		super(userId, null);
	}
	
	@Override
	public void receive(Object message) throws IOException {
		
		PokerRoom room=getRoom();
		/*
		 * 之后将传入的message进行响应解析，可使用房间的getManager()获取状态机,
		 * 使用状态机的getNow方法获取游戏状态，根据游戏状态解析message的类型，状态包含以下情况
		 * 	public static final int BERORE_START=0;//游戏未开始状态
			public static final int CALLING=1;//叫牌状态
			public static final int PLAYING=2;//打牌状态
			public static final int END=3;//牌打完的状态，此时可接受重新开始消息
		 * 进行合适处理获取Ai的响应结果，使用GameParser类的静态方法fitMessage方法将应
		 * 发回的对象转为String，再使用PokerManager的handleMessage方法发回状态机处理（若不需Ai响应则不发消息）
		 */
	}
	
}
