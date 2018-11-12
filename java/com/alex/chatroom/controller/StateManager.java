package com.alex.chatroom.controller;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.Position.Bias;

import org.apache.ibatis.annotations.Case;
import org.aspectj.runtime.internal.cflowstack.ThreadStackFactoryImpl;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alex.chatroom.utils.MapSessionControll;
import com.alex.chatroom.utils.deckOfCard;
import com.alex.chatroom.websocket.MyWebSocketHandler;
import com.alibaba.druid.pool.GetConnectionTimeoutException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.FixedSpaceIndenter;
import com.google.gson.Gson;
import com.poker.GameParser;
import com.poker.PokerCommunicator;
import com.poker.PokerRoom;
import com.poker.User;

import bridge.domain.BridgeGame;
import bridge.domain.CallContract;
import bridge.domain.Card;
import bridge.domain.Contract;
import bridge.domain.Deck;
import bridge.domain.PlayerPosition;
import bridge.domain.Trump;
import bridge.domain.utils.BridgeHelper;

public class StateManager {
	
	public static final Map<String, BridgeGame> gameMap=new HashMap<>();//房间与游戏的对应关系暂存此处
	
	/**
	 * 以下为游戏中状态的定义
	 */
	public static final int BERORE_START=0;//游戏未开始状态
	public static final int CALLING=1;//叫牌状态
	public static final int PLAYING=2;//打牌状态
	public static final int END=3;//牌打完的状态，此时可接受重新开始消息
		
	private int now=BERORE_START;//表示当前游戏状态的变量
	private PokerRoom room;//与游戏绑定的房间，需通过房间提供的接口发送消息
	private BridgeGame bridgegame=null;//与状态机绑定的牌局
	private PlayerPosition firstPlayer;//保存每局中首叫玩家的变量
	private List<CallContract> callcontractList=new ArrayList<>();//保存叫品的list
	private deckOfCard deckofcards;//保存玩家手牌的变量
	//private Map<String, WebSocketSession> room;
	
	public StateManager(PokerRoom room) {
		this.room=room;
	}
	
	public void handleMessage(User user,String message) {
		switch(now) {
		case BERORE_START://状态为游戏开始的处理
			if(user.getUserId().equals(room.getHostId())) {//房主才能开始游戏 TODO 处理其他消息
				start();
				now=CALLING;
			}
			break;
		case CALLING:
			if(CallContract(user, message)) {
				now=PLAYING;
			}
			break;
		case PLAYING:
			play(user, message);
			break;
		default:
					
		}
	}
	
	private void start() {//收到游戏开始消息的响应
		deckofcards = new deckOfCard();
		deckofcards.Shuffle_deck();// 洗牌
		deckofcards.Sort();//给每个玩家的牌进行排序
		deckofcards.print_result();// 输出发出的牌结果
        //将不同位置的玩家的牌发个对应的玩家,并将对应的牌堆放入到userDeckMap中去
		Set<User> players=room.getPlayers();
		Deck deck=null;
		for (User user : players) {
			PlayerPosition position=room.findPosition(user.getUserId());
			deck=findDeckByPos(position);
			try {
				user.receive(deck);
			} catch (IOException e) {
				// TODO 
				e.printStackTrace();
			}
		}
		String hostId=room.getHostId();
		firstPlayer=room.findPosition(hostId);//置初始行动方为房主
		/*try {
			Thread.sleep(1000);//尝试增加发送间隔，防止数据丢失
		} catch (InterruptedException e) {
			// TODO: handle exception
		}*/
		room.sendToAll(firstPlayer);
	}
	
	public Deck findDeckByPos(PlayerPosition position) {//根据位置获取牌堆的方法
		Deck deck=null;
		switch(position.getFirstLetter()) {
		case 'N':
			deck=deckofcards.deckN;
			break;
		case 'E':
			deck=deckofcards.deckE;
			break;
		case 'S':
			deck=deckofcards.deckS;
			break;
		case 'W':
			deck=deckofcards.deckW;
		default:
		}
		return deck;
	}
	
	private boolean CallContract(User user,String message){//叫牌过程中对用户消息的响应,若叫牌结束返回true，否则返回false
		
		//从前台获取到用户所在的房间名和用户名来获取用户所在的websocketsession,从而获得pokercomunicator构造函数参数进行创建
		//String roomName = request.getParameter("roomname");

		CallContract callcontract=GameParser.getCallContract(message);//解析玩家的叫品
		//将叫品放入callcontractList
		callcontractList.add(callcontract);
		//发送该叫品显示给玩家
		room.sendToAll(callcontract);
		if(findendOfCall()) {
			//如果出现连续三次非实质性叫品，则通知玩家叫品结束，并将最高叫品发给玩家（最后出的叫品即为最高叫品）
			int loc=findHighestCall();
			CallContract highcallcontract=callcontractList.get(loc);
			room.sendToAll("call end");//通知玩家叫牌结束
			room.sendToAll(highcallcontract);
			// 设置牌局
			initGame(deckofcards,highcallcontract);
			return true;
		}
		PlayerPosition position=BridgeHelper.getNextPlayerPosition(room.findPosition(user.getUserId()));//获取下一个玩家的位置
		room.sendToAll(position);
		return false;
	}
	
	private boolean findendOfCall() {//内部检测叫牌是否结束的方法
		int flag=0;
		for(int i=callcontractList.size()-1;i>=callcontractList.size()-3;i--) {
			//将该叫品序列集合从后往前倒序遍历四个元素，以flag标记非实质性叫品个数
			if(callcontractList.get(i).meaningful==false) {
				flag++;
			}
		}
		if(flag==3)//如果找到连续三个非实质性叫品，则返回true
			return true;
		return false;
	}
	
	private int findHighestCall() {
		int loc=-1;
		for(int i=callcontractList.size()-1;i>=0;i--) {
			//找出最后加进去的实质性叫品的位置
			if(callcontractList.get(i).meaningful==true) {
				loc=i;
				break;
			}
		}
		return loc;
	}
	
	private void initGame(deckOfCard deckofcards,Contract contract) {//初始化牌局函数
		Dictionary<PlayerPosition, Deck> getGameState = new Hashtable<PlayerPosition, Deck>();//创建用户手牌字典
		Set<User> players=room.getPlayers();//获取房间里所有用户
		for (User user : players) {
			PlayerPosition playerposition = room.findPosition(user.getUserId());
			Deck deck = findDeckByPos(playerposition);
			getGameState.put(playerposition, deck);//将牌对依次加入手牌字典
		}
		// 根据玩家位置和该玩家的纸牌创建牌桌状态
		bridgegame = new BridgeGame(getGameState, contract.getShortString());
	}
	
	private boolean play(User user,String message){//处理玩家出牌消息的方法，若出完则返回true，否则返回false
		//判断是否为第一个出牌的，如果是第一个出牌的，则把明手的牌发给所有玩家
		Card card=GameParser.getCard(message);//将card的就送字符串类型解析为card类
		//将玩家打出的牌发给所有玩家，这里把card类解析成json格式
		room.sendToAll(card);
		if(bridgegame.getTricks().size()==0) {
			PlayerPosition dummy=bridgegame.getDummy();//找到明手位置
			//根据位置获得明手手牌
			Deck dummydeck=bridgegame.getGameState().get(dummy);
			//将明手的手牌发给所有玩家
			room.sendToAll(dummydeck);
		}
		//在bridgegame中的playCard方法实现了该功能，该方法先判断一墩是否结束，如果未结束，则直接返回邻接的下一个位置，
		//如果一墩的牌数为4，则找到赢家，下一个玩家位置即为赢家位置
		PlayerPosition nextplayer=bridgegame.playCard(card, room.findPosition(user.getUserId()));
		if(bridgegame.getCardsRemaining()==0) {
			return true;
		}
		room.sendToAll(nextplayer);
		return false;
	}

	public int getNow() {
		return now;
	}
}
