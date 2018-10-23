package com.poker;
/**
 * poker game 通信类，包装了网页客户端与后台的通信
 * @author 陶荆杰
 */

import java.io.IOException;

import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

import bridge.domain.CallContract;
import bridge.domain.Card;
import bridge.domain.Deck;
import bridge.domain.PlayerPosition;

public class PokerCommunicator {
	private Basic sender;
	public PokerCommunicator(Session session) {
		/**
		 * 构造方法，需传入与客户通信的session
		 */
		this.sender=session.getBasicRemote();
	}
	
	public void send(Deck deck)  throws IOException{
		/**
		 * 将玩家手牌信息发给该玩家的方法
		 */
	}
	
	public void send(PlayerPosition position) throws IOException {
		/**
		 * 将当前行动玩家方信息发给玩家的方法
		 */
	}
	
	public void send(String message) throws IOException{
		/**
		 * 发送牌局信息以外的消息时使用的方法
		 */
		sender.sendText(message);
	}
	public void send(Card card) throws IOException{//向对应玩家发送其他玩家出的牌的方法
		sender.sendText(card.getSuit().getShortName()+card.getRank().toString());
	}
	public void send(CallContract contract)  throws IOException{
		/**
		 * 发送玩家叫品的方法
		 */
		System.out.println(contract.toString());
		//sender.sendText(contract);
	}
	
}
