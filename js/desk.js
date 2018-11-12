var pick;//出牌前点击选中的牌
var rank;    //牌值
       var pick=new Object();  //出牌前点击选中的牌
               var hand0;//其他人要出的下一张牌
       
       
               //叫牌结束在牌桌中间显示定约信息
               function showMe(){
                   var myText = document.createTextNode("定约类型："+nowCallContract.type+"/n有将定约下将牌的花色："+nowCallContract.suit+"/n定约的阶次："+nowCallContract.rank);
                   document.getElementById("Message").appendChild(myText);
               }
       
               //选中牌的图片
               function picked(src,id){
                   pick.src=src;
                   pick.id=id;
                   pick.sta=true;
               }
       
               //出牌
               function toggle() {
                   if(pick.sta==true) {
                       var div = document.getElementById(pick.id); //使当前位置的牌隐藏
                       if (div.style.display == "block") {
                           div.style.display = 'none';
                       } else {
                           div.style.display = 'block';
                       }
                       document.getElementById("player4" + position).setAttribute('src', pick.src);//将选中的牌显示在牌桌中间位置
                       //position = (position + 1) % 4;
                   }
                   pick.sta=false;
               }
			   
               function hand(hand0){//其他玩家出牌（hand0为要出的牌）
                   var div = document.getElementById(hand0.id); //使当前位置的牌隐藏
                   if (div.style.display == "block") {
                       div.style.display = 'none';
                   } else {
                       div.style.display = 'block';
                   }
                   document.getElementById("player4" + position).setAttribute('src', hand0.src);//将选中的牌显示在牌桌中间位置
                   position = (position + 1) % 4;
               }
       
               //发牌后显示自己的手牌
               function getAddress(MyCardGroup){
                   for (var i=0; i<13; i++){
						var num1="pkimage/"+MyCardGroup[i].suit+MyCardGroup[i].rank+".jpg";
						document.getElementById('player0'+i).setAttribute('src',num1);
					   
				   }
               }
       
               //叫牌时点击值后的下一步
               function nextCall(value){
                   document.getElementById("btn1").style.display="none";
                   document.getElementById("btn2").style.display="none";
                   document.getElementById("btn3").style.display="none";
                   document.getElementById("btn4").style.display="none";
                   document.getElementById("btn5").style.display="none";
                   document.getElementById("btn6").style.display="none";
                   document.getElementById("btn7").style.display="none";
                   document.getElementById("btn8").style.display="none";
                   document.getElementById("btn9").style.display="none";
                   var rank=value;
       
                   document.getElementById("btn10").style.display="block";
                   document.getElementById("btn11").style.display="block";
                   document.getElementById("btn12").style.display="block";
                   document.getElementById("btn13").style.display="block";
                   document.getElementById("btn14").style.display="block";
               }
       
               //庄家队友明牌
               function playView(banker){
       
                   //如果南方是庄家，则庄家队友北方明牌
                   if(banker=="S"){
                       for (var i=0; i<13; i++){
                           var num2="style/img/back.jpg";
                           document.getElementById('player1'+i).setAttribute('src',num2);
                       }
                       for (var i=0; i<13; i++){
                           var num3="style/img/back.jpg";
                           document.getElementById('player3'+i).setAttribute('src',num3);
                       }
                       for (var i=0; i<13; i++){
                           var num4="style/img/"+ViewCardGroup[i]+".jpg";
                           document.getElementById('player2'+i).setAttribute('src',num4);
                       }
                   }
                  //如果北方是庄家，则庄家队友南方明牌
                   if(banker=="N"){
                       for (var i=0; i<13; i++){
                           var num5="style/img/back.jpg";
                           document.getElementById('player1'+i).setAttribute('src',num5);
                       }
                       for (var i=0; i<13; i++){
                           var num6="style/img/back.jpg";
                           document.getElementById('player3'+i).setAttribute('src',num6);
                       }
                       for (var i=0; i<13; i++){
                           var num7="style/img/back.jpg";
                           document.getElementById('player2'+i).setAttribute('src',num7);
                       }
                   }
       
                   //如果东方是庄家，则庄家队友西方明牌
                   if(banker=="E"){
                       for (var i=0; i<13; i++){
                           var num8="style/img/back.jpg";
                           document.getElementById('player2'+i).setAttribute('src',num8);
                       }
                       for (var i=0; i<13; i++){
                           var num9="style/img/back.jpg";
                           document.getElementById('player3'+i).setAttribute('src',num9);
                       }
                       for (var i=0; i<13; i++){
                           var num10="style/img/"+ViewCardGroup[i]+".jpg";
                           document.getElementById('player1'+i).setAttribute('src',num10);
                       }
                   }
       
       //如果西方是庄家，则庄家队友东方明牌
                   if(banker=="W"){
                       for (var i=0; i<13; i++){
                           var num11="style/img/back.jpg";
                           document.getElementById('player2'+i).setAttribute('src',num11);
                       }
                       for (var i=0; i<13; i++){
                           var num12="style/img/back.jpg";
                           document.getElementById('player1'+i).setAttribute('src',num12);
                       }
                       for (var i=0; i<13; i++){
                           var num13="style/img/"+ViewCardGroup[i]+".jpg";
                           document.getElementById('player3'+i).setAttribute('src',num13);
                       }
                   }
               }
       
               //一轮叫牌的最后一步
               function call(id){
                   var suit = id; //叫牌花色
                   document.getElementById("btn10").style.display="none";
                   document.getElementById("btn11").style.display="none";
                   document.getElementById("btn12").style.display="none";
                   document.getElementById("btn13").style.display="none";
                   document.getElementById("btn14").style.display="none";
       
                   //轮到下一个人叫牌
                   position = (position+1)%4;
       
       
       
                   if(CallContract==true){   //叫牌结束，有/无庄家产生，移除叫牌按钮
                       document.getElementById("btn1").style.display="none";
                       document.getElementById("btn2").style.display="none";
                       document.getElementById("btn3").style.display="none";
                       document.getElementById("btn4").style.display="none";
                       document.getElementById("btn5").style.display="none";
                       document.getElementById("btn6").style.display="none";
                       document.getElementById("btn7").style.display="none";
                       document.getElementById("btn8").style.display="none";
                       document.getElementById("btn9").style.display="none";
                       document.getElementById("btn10").style.display="none";
                       document.getElementById("btn11").style.display="none";
                       document.getElementById("btn12").style.display="none";
                       document.getElementById("btn13").style.display="none";
                       document.getElementById("btn14").style.display="none";
                          playView(banker,ViewCardGroup);
                           showMe();
                       return;
                   }
                   document.getElementById("btn2").style.display="block";
                   document.getElementById("btn3").style.display="block";
                   document.getElementById("btn4").style.display="block";
                   document.getElementById("btn5").style.display="block";
                   document.getElementById("btn6").style.display="block";
                   document.getElementById("btn7").style.display="block";
                   document.getElementById("btn8").style.display="block";
                   document.getElementById("btn9").style.display="block";
           }
		  
function call(value){//叫牌响应动作,参数暂定为
	if(position==myPosition){//仅当轮到自己叫牌时发出消息
		var callContract=new Object();
		callContract.suit=value;
		callContract.rank=rank;
		if(value=="callpass"){
			callContract.call_type=false;
		}else{
			callContract.call_type=true;
		}
		sendCallContract(callContract);
	}
}