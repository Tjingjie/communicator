package com.poker;
/**
 * 客户端消息解析类，使用静态方法对客户端的消息进行解析，产生相应的对象
 */

import bridge.domain.CallContract;
import bridge.domain.Card;
import bridge.domain.PlayerPosition;
import bridge.domain.Rank;
import bridge.domain.Suit;

public class GameParser {

	
	public static CallContract getCallContract(String message) {
		/**
		 * 解析玩家发出的叫牌信息方法
		 */
		CallContract callContract;
		PlayerPosition position=new PlayerPosition(message.charAt(0));
		switch (message.charAt(2)) {
		case 'P':
			callContract=new CallContract(CallContract.PASS, position);
			break;
		case 'D':
			callContract=new CallContract(CallContract.DOUBLE,position);
			break;
		case 'R':
			callContract=new CallContract(CallContract.REDOUBLE,position);
			break;
		default:
			callContract=new CallContract(message);
			break;
		}
		return callContract;
	}
	
	public static Card getCard(String message) {
		/**
		 * 获取该玩家出的牌的方法
		 */
		return new Card(new Rank(message.charAt(1)), new Suit(message.charAt(0)));
	}
}
