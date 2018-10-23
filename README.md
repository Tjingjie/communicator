# communicator
桥牌网页端与服务器端的通信接口

## bridge.domain.CallContract
为bridge.domain扩增的类
该类继承于bridge.domain.Contract，用于表示玩家的叫品。
该类包含的方法如下：
```java
public CallContract(String contract) {
		/**
		 * 叫品为实质性叫品时使用的构造函数，此时该类表示的意义与其父类基本相同
		 */
		super(contract);
		meaningful=true;
	}
	public CallContract(int type) {
		/**
		 * 叫品不为实质性叫品时使用的构造函数，此时该类只有callType属性有意义。
		 * callType属性只有三种取值：CallContact.PASS、CallContact.DOUBLE、CallContact.REDOUBLE分别对应相应叫品
		 */
		super();
		meaningful=false;
		callType=type;
	}
	int getCallType() {//获取玩家叫品的方法（仅当meaningful为false时使用有效,否则请使用父类的方法）
		return callType;
	}
```

## com.poker.PokerCommunicator
供后台使用的通信类，提供了后台向客户端发送数据时的方法，内部对数据进行了格式转换
该类提供的接口方法如下

```java
public PokerCommunicator(Session session) // 构造方法，需传入与客户通信的session 
public void send(Deck deck) throws IOException//将玩家手牌信息发给该玩家的方法，通信出错时将抛出IOException,下述方法同此说明 
public void send(PlayerPosition position) throws IOException// 将当前行动玩家方信息发给玩家的方法 
public void send(String message) throws IOException//发送牌局信息以外的消息时使用的方法 
public void send(Card card) throws IOException//向对应玩家发送其他玩家出的牌的方法 
public void send(CallContract contract) throws IOException//发送玩家叫品的方法 
```

## com.poker.GameParser
供后台使用的客户端信息解析类。使用时将客户端的信息传入，直接调用对应静态方法即可
```java
public static CallContract getCallContract(String message)//解析玩家发出的叫牌信息方法
public static Card getCard(String message)//获取该玩家出的牌的方法
```

## commnunicator.js
供前端调用的js
