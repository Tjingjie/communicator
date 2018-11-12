package bridge.domain;
//package bridge.domain;

//import javax.persistence.criteria.CriteriaBuilder.Case;

import bridge.domain.Contract;
import bridge.domain.PlayerPosition;

/**
 * 表示玩家的叫品类
 * 
 * @author 陶荆杰
 *
 */
public class CallContract extends Contract {
	public static final int PASS=0;
	public static final int DOUBLE=1;
	public static final int REDOUBLE=2;
	public final boolean meaningful;//叫品类型标志，若为true则为实质性叫品
	private int callType;//callType属性只有三种取值：CallContact.PASS、CallContact.DOUBLE、CallContact.REDOUBLE分别对应相应叫品（仅当meaningful为false时使用有效）
	public CallContract(String contract) {
		/**
		 * 叫品为实质性叫品时使用的构造函数，此时该类表示的意义与其父类基本相同
		 */
		super(contract);
		meaningful=true;
	}
	public CallContract(int type,PlayerPosition position) {
		/**
		 * 叫品不为实质性叫品时使用的构造函数，此时该类只有callType属性有意义。
		 * callType属性只有三种取值：CallContact.PASS、CallContact.DOUBLE、CallContact.REDOUBLE分别对应相应叫品
		 */
		super();
		meaningful=false;
		callType=type;
		setPlayerPosition(position);
	}
	int getCallType() {//获取玩家叫品的方法（仅当meaningful为false时使用有效）
		return callType;
	}
	
	@Override
	public String getShortString() {//重写了getShortString方法，便于通信转换
		// TODO Auto-generated method stub
		if(meaningful) {
			return super.getShortString();
		}else {
			String callname;
			switch (callType) {
			case PASS:
				callname="P";
				break;
			case DOUBLE:
				callname="D";
				break;
			case REDOUBLE:
				callname="R";
				break;
			default:
				callname="P";
				break;
			}
			return getPlayerPosition().getFirstLetter()+":"+callname;
		}
	}
}
