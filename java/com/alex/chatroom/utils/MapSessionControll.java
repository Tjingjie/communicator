package com.alex.chatroom.utils;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.alex.chatroom.websocket.MyWebSocketHandler;
import com.poker.PokerCommunicator;

import bridge.domain.CallContract;
import bridge.domain.PlayerPosition;

@Component
public class MapSessionControll {

	//MyWebSocketHandler MyWebSocketHandler;//=new MyWebSocketHandler();
	
	public static void sendDeck(Map<String, WebSocketSession> mapSessions,deckOfCard deckofcards) throws IOException {
		 //将不同位置的玩家的牌发个对应的玩家,并将对应的牌堆放入到userDeckMap中去
        for (Map.Entry<String, WebSocketSession> mapsession : mapSessions.entrySet()) {
			if(MyWebSocketHandler.userPositionMap.get(mapsession.getKey())==PlayerPosition.NORTH) {
				//如果用户名对应的位置为北,将对应北方的牌发送给房间第一个用户
				PokerCommunicator pokercommunicator=new PokerCommunicator(mapsession.getValue());
				pokercommunicator.sendtoUser(mapsession.getValue(), deckofcards.deckN);
				MyWebSocketHandler.userDeckMap.put(mapsession.getKey(), deckofcards.deckN);
			}
			if(MyWebSocketHandler.userPositionMap.get(mapsession.getKey())==PlayerPosition.EAST) {
				//如果用户名对应的位置为东,将对应东方的牌发送给房间第一个用户
				PokerCommunicator pokercommunicator=new PokerCommunicator(mapsession.getValue());
				pokercommunicator.sendtoUser(mapsession.getValue(), deckofcards.deckE);
				MyWebSocketHandler.userDeckMap.put(mapsession.getKey(), deckofcards.deckE);
			}
			if(MyWebSocketHandler.userPositionMap.get(mapsession.getKey())==PlayerPosition.SOUTH) {
				//如果用户名对应的位置为南,将对应南方的牌发送给房间第一个用户
				PokerCommunicator pokercommunicator=new PokerCommunicator(mapsession.getValue());
				pokercommunicator.sendtoUser(mapsession.getValue(), deckofcards.deckS);
				MyWebSocketHandler.userDeckMap.put(mapsession.getKey(), deckofcards.deckS);
			}
			if(MyWebSocketHandler.userPositionMap.get(mapsession.getKey())==PlayerPosition.WEST) {
				//如果用户名对应的位置为西,将对应西方的牌发送给房间第一个用户
				PokerCommunicator pokercommunicator=new PokerCommunicator(mapsession.getValue());
				pokercommunicator.sendtoUser(mapsession.getValue(), deckofcards.deckW);
				MyWebSocketHandler.userDeckMap.put(mapsession.getKey(), deckofcards.deckW);
			}
		}
		
	}
	//将叫品放入callcontractList，
	//判断有没有连续三次出现非实质性叫品，如果有，则通知玩家叫品结束，并将最高叫品发给玩家（最后出的叫品即为最高叫品）
	
	//这里找出最后的叫品
//	public static int findHighestCall() {
//		int loc=-1;
//		for(int i=MyWebSocketHandler.callcontractList.size()-1;i>=0;i--) {
//			//找出最后加进去的实质性叫品的位置
//			if(MyWebSocketHandler.callcontractList.get(i).meaningful==true) {
//				loc=i;
//				break;
//			}
//		}
//		return loc;
//	}
	//通过map值获取键
	public static Object findkey(Map map,Object value) {
		Object key=new Object();
		for(Object key1: map.keySet()){
	        if(map.get(key).equals(value)){
	        	key=key1;
	        }
	    }
		return key;
	}
}
