/**
 * 此处定义前端牌局处理的全局变量，将必要的属性保存在相应变量中，用于界面进行相应显示
 */

var MyCardGroup;
	/*保存玩家手牌的变量，类型为Poker数组。*/
var position;
   /*当前行动玩家的位置，字符类型，E代表东,W代表西，S代表南，N代表北。*/
var myPosition;
	/*保存玩家的位置。类型同position*/
var nowCallContract;
		/*保存当前行动玩家的叫品的变量。类型为callContract类型，包含属性：
		call_type:boolean类型，true时表示为实质性叫品，否则为非实质性叫品；
		suit：（表示叫牌将牌的花色,字符类型，S代表黑桃，H代表红桃，D代表方块，C代表梅花，N表示无将定约）；
		rank（表示叫牌的阶次，整数类型，1表示1阶，2表示2阶，依次类推，7表示7阶）；*/
var CallContract;
		/*叫牌是否结束，boolean类型，true表示结束*/
var FinallyContract;
		/*保存最终定约的变量，包含属性：
		suit：（表示定约将牌的花色,字符类型，S代表黑桃，H代表红桃，D代表方块，C代表梅花，N表示无将定约）；
		rank（表示定约的阶次，整数类型，1表示1阶，2表示2阶，依次类推，7表示7阶）；
		banker ：表示庄家的位置，类型同position*/
//var banker;      //庄家
var nowCard;
	/*保存当前出牌的变量，类型为poker，包含以下属性：
	suit：（表示牌的花色,字符类型，S代表黑桃，H代表红桃，D代表方块，C代表梅花）；
	rank: （表示牌数值，整数类型，因桥牌'A'最大，方便排序从2开始计数，数值'2'代表牌'2'，数值'3'代表牌'3'，依次排序，数值          
         '10'代表牌'10'，数值'11'代表牌'J'，数值'12'代表牌'Q'，数值'13'代表牌'K'，数值'14'代表牌'A'。）；
	界面负责将该牌显示在position对应的位置*/
var NSwin;
	/*保存南北方的赢墩数的变量*/
var EWwin
	/*保存东西方的赢墩数的变量*/
var ViewCardGroup;
		/*保存明牌人的手牌的变量，类型同MyCardGroup*/
var finish;
		/*一局牌是否打完，boolean类型*/

var basePath="http:localhost:8080/chatroomdemo/";//公共路径，引用资源基于此公共路径保证能显示
