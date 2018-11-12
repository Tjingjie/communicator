/**
 * 
 */

var socketurl="ws://"+document.domain+":8080/chatroomdemo/ws.action";//使用时需修改为服务器端对应的url
var websocket=null;//与服务器端连接的websocket
var roomname="武大"; //测试时定义的房间号


function initConnect(onGetMessage){//初始化连接的方法，通信前需调用该方法建立连接
	    //判断当前浏览器是否支持WebSocket
	if ('WebSocket' in window) {
	      websocket = new WebSocket(socketurl);
	}else {
	      alert('当前浏览器 Not support websocket')
	}
	websocket.onerror = function () {//连接发生错误的回调方法
	    alert("服务器连接发生错误");
	}
	
	websocket.onopen = function () {//连接成功建立的回调方法
		console.log('连接成功');
	}
	
	websocket.onmessage = onGetMessage;//设置接收到消息的回调方法
	
	websocket.onclose = function () {//连接关闭的回调方法
	    alert("服务器已断开连接");
	}
	
	window.onbeforeunload = function () {//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常
	    closeWebSocket();
	}

}

function getDeck(message)//根据服务器端数据解析为玩家手牌的方法，该方法返回一组牌（明手牌的获取也使用该方法）
{
	var gson=JSON.parse(message.data);
	var pokers=new Array(13);
	var cards=gson["cards"];
	for (var i = 0; i < cards.length; i++) {
		var card=new Object();
		card.suit=cards[i]["suit"]["shortName"];;
		card.rank=cards[i]["rank"]["score"];
		pokers[i]=card;
	}
	return pokers;
}

function getPlayerPosition(message)//根据数据解析为当前行动玩家方的方法，该方法返回一个玩家位置标志（叫牌和出牌玩家方的获取均使用此方法)
{
		var gson=JSON.parse(message.data);
		return gson["firstLetter"];
}

function getCallContract(message) //根据服务器消息解析为玩家叫品的方法，该方法返回一个叫品
{
	var gson=JSON.parse(message.data);
	var callContract=new Object();
	callContract.call_type=gson["meaningful"];
	callContract.suit=gson["trump"]["suit"]["shortName"];
	callContract.rank=gson["value"];
	callContract.position=gson["playerPosition"]["firstLetter"];
	
	switch (gson["callType"]) {
	case 0:
		callContract.type="pass";
		break;
	case 1:
		callConsract.type="double";
		break;
	case 2:
		callContract.type="double";
		break;
	default:	
		callContract.type="pass";
		break;
	}
	return callContract;
}

function getContract(message) //根据服务器消息解析为最终定约的方法，该方法返回一个定约。定约中包含的字段及意义：type、suit、rank字段与前端接口定义相同，banker字段表示庄家位置字母
{
	var gson=JSON.parse(messaage.data);
	var contract=new Object();
	if(gson["trump"]["shortName"]=="N"){
		contract.type=0;
	}else{
		contract.type=1;
	}
	contract.suit=gson["trump"]["suit"]["shortName"];
	contract.rank=gson["value"];
	contract.banker=gson["playerPosition"]["shortName"];
	return contract;
}

function getCard(message)//根据服务器消息解析为玩家所出牌的方法,该方法返回一张牌
{
	cardGson=message.data;
	var cardobj=JSON.parse(cardGson);
	var card=new Object();
	card.suit=cardobj["suit"]["shortName"];
	card.rank=cardobj["rank"]["score"];
	return card;
}

function isCallEnd(message){
		return message.data=="call end";
}
function sendCallContract(callContract)//发送叫牌信息的方法
{
	var contract=myPosition+":";
	if(callContract.call_type==false){
		contract+=callContract.type[0];
	}else {
		contract+="0"+callContract.rank+callContract.suit;
		if(contract[contract.length-1]=='N'){
				contract+="T";
		}
	}
	sendMessage(contract);
}

function sendCard(card)//发送出牌消息的方法
{	
	var cardmsg=card.suit+card.rank;
	sendMessage(cardmsg);
}

function sendMessage(message) {//发送牌局外的其他消息的方法
	console.log(message);
	websocket.send(message);
	/*var data={
			msg:message,
			roomname:roomName,
			//toid:toId
	};
	//data['roomName']=roomName;
	$.post("<%=basePath%>ShuffleDeck.action",data,function(data){
		//alert(data);
		if(data=="1"){
			alert("发送成功");
		}
		}); */
}
function closeWebSocket() {//关闭WebSocket连接
	websocket.close();
}
