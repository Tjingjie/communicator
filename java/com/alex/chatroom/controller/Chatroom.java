package com.alex.chatroom.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alex.chatroom.utils.MapSessionControll;
import com.alex.chatroom.utils.deckOfCard;
import com.alex.chatroom.websocket.MyWebSocketHandler;
import com.google.gson.GsonBuilder;
import com.poker.GameParser;
import com.poker.PokerCommunicator;
import com.poker.PokerRoom;
import com.poker.RoomManager;
import com.alex.chatroom.pojo.User;

import bridge.domain.CallContract;
import bridge.domain.Card;
import bridge.domain.PlayerPosition;
import bridge.domain.Rank;
import bridge.domain.Suit;

@Controller
public class Chatroom {

	@Autowired
	private MyWebSocketHandler socketHandler;
	private MapSessionControll mapsessioncontroll;

	@RequestMapping("chat")
	public String chat(HttpServletRequest request) throws IOException {
		// 跳转到chat.jsp
		// request.getSession().setAttribute("userId",user.getUserId());
		String roomName = request.getParameter("roomName");
		request.getSession().setAttribute("roomName", roomName);
		// 将房主标记保存到session中
		String ishost = request.getParameter("roomchoice");
		// 从创建房间页面中获取userId
		String userId = request.getParameter("userId");
		if(ishost!=null&&ishost.equals("1")) {//选项为创建房间时进行创建房间操作
			Boolean success=RoomManager.creatRoom(userId, roomName);
			if(!success) return "createOrenterRoom";
		}else {
			PokerRoom room=RoomManager.findRoom(roomName);//查找房间
			if(room==null) return "createOrenterRoom";
		}
		request.getSession().setAttribute("ishost", ishost);
		/*/ 从对应房间名中取得会话
		Map<String, WebSocketSession> mapSession = socketHandler.roomUserMap.get(roomName);
		// WebSocketSession websocketsession=mapSession.get(userId);
		if (mapSession != null) {
			int roomsize = mapSession.size();
			System.out.println(roomsize + "人");
			request.getSession().setAttribute("roomsize", roomsize);
			socketHandler.sendMessageToAll(request.getParameter("roomName"),
					new TextMessage("这是给所有用户发送的消息，来自controller"));
		} else {
			System.out.println("房间为空");
		}*/
		return "chat";
	}

	// createOrenterRoom
	@RequestMapping("createOrenterRoom")
	public String createOrenterRoom(User user, HttpServletRequest request) throws IOException {
		// 跳转到createOrenterRoom.jsp
		request.getSession().setAttribute("userId", user.getUserId());
		return "createOrenterRoom";
	}

	@RequestMapping("login")
	public String login() {
		// 跳转到login.jsp
		return "login";
	}

	@RequestMapping("ShuffleDeck")
	@ResponseBody
	public String ShuffleDeck(HttpServletRequest request) throws IOException {
		String roomName = request.getParameter("roomname");
		String userId = request.getParameter("userid");
		deckOfCard deckofcards = new deckOfCard();
		deckofcards.setPoker();// 按顺序初始化牌
		// test.printInitPoker();//输出初始化的牌
		deckofcards.Shuffle_deck();// 洗牌
		deckofcards.Sort();//给每个玩家的牌进行排序
		deckofcards.print_result();// 输出发出的牌结果
		//从对应房间名中取得会话
        Map<String, WebSocketSession> mapSessions = socketHandler.roomUserMap.get(roomName);
        //MapSessionControll mapsessioncontroll=new MapSessionControll();
        ////将不同位置的玩家的牌发个对应的玩家,并将对应的牌堆放入到userDeckMap中去
        mapsessioncontroll.sendDeck(mapSessions, deckofcards);
		return "1";
	}
 
	@RequestMapping("sendmessage")
	@ResponseBody
	public String sendmessage(HttpServletRequest request) throws IOException {
		String message = request.getParameter("msg");
		String roomName = request.getParameter("roomname");
		String toId = request.getParameter("toid");
		System.out.println(message + roomName + "啊");
		if (toId.equals("") || toId == "-1") {
			// 如果没有指定用户名，则群发
			Card card[] = new Card[2];
			card[0] = new Card(Rank.TWO, Suit.CLUBS);
			card[1] = new Card(Rank.TWO, Suit.CLUBS);
			// new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(card);
			System.out.println("json格式的牌:");
			System.out.println(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(card));
			message = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(card);
			if (socketHandler.sendMessageToAll(roomName, new TextMessage(message)))
				return "1";// 如果发送成功，则返回“1”
		}
		// 跳转到login.jsp
		return "0";
	}

	@RequestMapping("CallContract")
	@ResponseBody
	public String CallContract(HttpServletRequest request) throws IOException {
		String isend="0";//用来标记叫品是否结束,为"1"则结束，发送给前台
		//从前台获取到用户所在的房间名和用户名来获取用户所在的websocketsession,从而获得pokercomunicator构造函数参数进行创建
		//String roomName = request.getParameter("roomname");
		String roomName="武大";
		String uid = request.getParameter("userId");
		WebSocketSession session = socketHandler.roomUserMap.get(roomName).get(uid);
		PokerCommunicator pokercommunicator=new PokerCommunicator(session);
		String contract=request.getParameter("contract");
		GameParser gameparser=new GameParser();
		//从前台获取叫品，判断是否为是指向叫品，通过pokercomunicator方法发送该叫品，这里可以通过参数构造该叫品
		String calltype=request.getParameter("calltype");
		CallContract callcontract;
		if(calltype==null) {//如果是非实质性叫品
			callcontract=new CallContract(contract);
		}else {//如果是实质性叫品
			PlayerPosition playerposition=socketHandler.userPositionMap.get(uid);
			callcontract=new CallContract(Integer.parseInt(calltype),playerposition);
		}
		//将叫品放入callcontractList
		//socketHandler.callcontractList.add(callcontract);
		//发送该叫品显示给玩家
		pokercommunicator.send(callcontract);
		//if(mapsessioncontroll.findendOfCall()) {
			//如果出现连续三次非实质性叫品，则通知玩家叫品结束，并将最高叫品发给玩家（最后出的叫品即为最高叫品）
			//int loc=mapsessioncontroll.findHighestCall();
			//CallContract highcallcontract=socketHandler.callcontractList.get(loc);
			//pokercommunicator.send(highcallcontract);
			isend="1";
		//}
		return isend;
	}
}
