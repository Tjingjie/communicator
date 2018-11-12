var state = new Object();

state.cardCount=0;//计算当前出牌数的变量
state.unstart = 0;//"游戏未开始";
state.start = 1;//"游戏开始";
state.callPlay=2;//叫牌方询问状态
state.call = 3//叫牌状态
state.playPos=4;//出牌方询问状态
state.ownCard = 5;//出牌状态
state.gameover = 6;//出牌结束状态;
//state.continueGame = 7;//"继续游戏";
state.currentState  = 0;//置当前状态为游戏未开始;


function onMessage(message)
{
	console.log(message.data);
    switch(state.currentState){//根据当前游戏状态对服务器消息进行合适的解析
        case state.unstart:
            myPosition=getPlayerPosition(message);//将服务器发回的玩家位置进行保存
            state.currentState = state.start;//状态变为游戏开始
			break;
        case state.start:
            MyCardGroup=getDeck(message);//将玩家手牌保存至全局变量
			getAddress(MyCardGroup);//调用显示函数显示手牌
            state.currentState=state.callPlay;//置游戏状态为叫牌
			initGame();//初始化游戏相关变量
			break;
		case state.callPlay:
			position=getPlayerPosition(message);//获取当前叫牌方的位置
			state.currentState=state.call;
			break;
        case state.call:
			if(!CallContract){//叫牌未结束时
				CallContract=isCallEnd(message);//检测服务器是否发来叫牌结束消息
				if(!CallContract){
					nowCallContract=getCallContract(message);//获取前玩家的叫品保存至全局变量
					state.currentState=state.callPlay;//置状态为获取叫牌方位置
				}
			}else{
				FinallyContract=getContract(message);//获取最终定约
				state.currentState=state.playPos;//置游戏状态为打牌
				CallContract=true;//置叫牌结束标志
			}
             break;
		case state.playPos:
			if(state.cardCount==1&&ViewCardGroup==null){//若刚刚出完第一张牌则获取明手手牌
				ViewCardGroup=getDeck(message);
				playView(FinallyContract.banker,)
			}else {
			position=getPlayerPosition(message);//获取当前出牌方位置
			state.currentState=state.ownCard;//置状态为出牌
			}
			break;
        case state.ownCard:
			nowCard=getCard(message);
			state.cardCount++;
			if(state.cardCount==54){
				state.currentState=state.gameover;
				finish=true;
			}else{
				state.currentState=state.playPos;
			};
			break;
        case state.gameover:
            break;
		default:

    }
}

function initGame(){
	state.cardCount=0;//置出牌数为0
	CallContract=false;//置叫牌结束标志为false
	ViewCardGroup=null;
}
