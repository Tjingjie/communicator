package com.poker;
/**
 * 客户端消息解析类，使用静态方法对客户端的消息进行解析，产生相应的对象
 */

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
		String rank=message.substring(1, message.length());
		int score=Integer.parseInt(rank);
		return new Card(new Rank(message.charAt(1)), new Suit(score));
	}
	
	public static List<Card>sort(List<Card> cards){
		Collections.sort(cards, new Comparator<Card>() {
			public int compare(Card a,Card b) {
				int aOrder=a.getSuit().getOrder();
				int bOrder=b.getSuit().getOrder();
				int aScore=a.getRank().getScore();
				int bScore=b.getRank().getScore();
				if(aOrder>bOrder) {//比较花色
					return 1;
				}else if(aOrder<bOrder) {
					return -1;
				}else if(aScore<bScore){//比较点力
					return 1;
				}else if(aScore>bScore) {
					return -1;
				}else {
					return 0;
				}
			}
		});
		
		return cards;
	}
}
