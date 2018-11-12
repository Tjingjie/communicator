package com.alex.chatroom.pojo;

public class Card {

	private String color;//颜色（红桃，黑桃，梅花，方块）
    private String point;//点数（A,2,3,4,5,6,7,8,9,10,J,Q,K）
    public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public Card(String color,String point)//构造函数给颜色点数赋值
    {
        this.color=color;
        this.point=point;
    }
    public String getPoker()//返回当前对象的牌
    {
        return this.color+this.point;
    }
}
