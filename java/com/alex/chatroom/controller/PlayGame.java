package com.alex.chatroom.controller;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.WebSocketSession;

import com.alex.chatroom.pojo.Message;
import com.alex.chatroom.utils.MapSessionControll;
import com.alex.chatroom.websocket.MyWebSocketHandler;
import com.google.gson.Gson;
import com.poker.GameParser;
import com.poker.PokerCommunicator;

import bridge.domain.BridgeGame;
import bridge.domain.Card;
import bridge.domain.Contract;
import bridge.domain.Deck;
import bridge.domain.PlayerPosition;
import bridge.domain.Trick;
import bridge.domain.Trump;

@Controller
public class PlayGame {

	@Autowired
	private MyWebSocketHandler socketHandler;
	private MapSessionControll mapsessioncontroll;

	private BridgeGame bridgegame;

	@RequestMapping("start")
	@ResponseBody
	public String start(HttpServletRequest request) {
		// 玩家开始游戏前的准备,确定好将牌，定约，庄家,其中庄家在定约中有定义
		String roomName = request.getParameter("roomname");
		// 将前台传入的数据转化为Trump对象 ->Trump
		Trump trump = new Gson().fromJson(request.getParameter("trump"), Trump.class);
		Contract contract = new Gson().fromJson(request.getParameter("contract"), Contract.class);
		socketHandler.roomTrumpMap.put(roomName, trump);
		socketHandler.roomContractMap.put(roomName, contract);
		return "1";
	}

	@RequestMapping("play")
	@ResponseBody
	public String play(HttpServletRequest request) throws IOException {
       
		String userId = request.getParameter("userid");
		String roomName = "武大";//这里先规定只有一个房间
		String contractShortStr = socketHandler.roomContractMap.get(roomName).getShortString();
		PlayerPosition playerposition = socketHandler.userPositionMap.get(userId);
		//Card card=new Gson().fromJson(request.getParameter("card"), Card.class);
		//WebSocketSession session=socketHandler.userSocketSessionMap.get(userId);
		//获取用户会话
		//PokerCommunicator pokercommunicator=new PokerCommunicator(session);
		Deck deck = socketHandler.userDeckMap.get(userId);
		Dictionary<PlayerPosition, Deck> getGameState = new Hashtable<PlayerPosition, Deck>();
		// 根据玩家位置和该玩家的纸牌创建牌桌状态
		getGameState.put(playerposition, deck);
		bridgegame = new BridgeGame(getGameState, contractShortStr);
		//判断是否为第一个出牌的，如果是第一个出牌的，则把明手的牌发给所有玩家
		if(bridgegame.getCardsRemaining()==0) {
			PlayerPosition dummy=bridgegame.getDummy();//找到明手位置
			//根据位置获得明手用户名
			String dummyId=(String) mapsessioncontroll.findkey(socketHandler.userPositionMap, dummy);
			//根据用户名获得明手手牌
			Deck dummydeck=socketHandler.userDeckMap.get(dummyId);
			//将明手的手牌发给所有玩家
			//pokercommunicator.send(dummydeck);
		}
		String stringCard=request.getParameter("card");//获取前台的json字符串
		Card card=GameParser.getCard(stringCard);//将card的就送字符串类型解析为card类
		//将玩家打出的牌发给所有玩家，这里把card类解析成json格式
		//pokercommunicator.send(card);
		//判断一墩有没有打完，如果打完，则找到赢家，发送给所有玩家，该赢家即为下一个出牌人，
		//如果没有打完，则利用bridgegame的方法获取下一个出牌人的位置
		/*if(bridgegame.getCurrentTrick().getDeck().getCount()<4) {//获取这一墩打出的牌数
			//获取下一个出牌人的位置，用于提醒下一个玩家出牌，将下一个出牌人位置发给所有用户
			PlayerPosition nextplayer=bridgegame.playCard(card, playerposition);
			pokercommunicator.send(nextplayer);
		}else {
			Trump trump=socketHandler.roomTrumpMap.get(roomName);//获得该房间的将牌
			Trick trick=socketHandler.roomTrickMap.get(roomName);//获得该房间一墩的牌堆
			PlayerPosition winner= bridgegame.findWinner(trick, trump);
			pokercommunicator.send(winner);
		}*/
		//在bridgegame中的playCard方法实现了该功能，该方法先判断一墩是否结束，如果未结束，则直接返回邻接的下一个位置，
		//如果一墩的牌数为4，则找到赢家，下一个玩家位置即为赢家位置
		PlayerPosition nextplayer=bridgegame.playCard(card, playerposition);
		//pokercommunicator.send(nextplayer);
		//最终将玩家打出去的牌从玩家手中移除
		deck.removeCard(card);
		return "1";
	}
}
