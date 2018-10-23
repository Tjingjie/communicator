package com.poker;

import bridge.domain.CallContract;
import bridge.domain.Card;
import bridge.domain.Suit;

public class GameParser {

	
	public static CallContract getCallContract(String message) {
		/**
		 * 解析玩家发出的叫牌信息方法
		 */
		return new CallContract(0);
	}
	
	public static Card getCard(String message) {
		/**
		 * 获取该玩家出的牌的方法
		 */
		int rankScore='2';
		Suit suit=new Suit('S');
		return new Card(rankScore, suit);
	}
}
