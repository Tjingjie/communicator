package com.alex.chatroom.utils;

import java.util.List;
import java.util.Random;

import com.alex.chatroom.websocket.MyWebSocketHandler;
import com.poker.GameParser;

//import com.alex.chatroom.pojo.Card;

import bridge.domain.Rank;
import bridge.domain.Suit;
import bridge.domain.utils.BridgeHelper;
import bridge.domain.Card;
import bridge.domain.Deck;
import bridge.domain.PlayerPosition;

public class deckOfCard {
	public Deck deckN = new Deck();// 声明每个玩家的牌堆
	public Deck deckE = new Deck();
	public Deck deckS = new Deck();
	public Deck deckW = new Deck();
	private String[] newPoker = new String[52];// 洗牌后的扑克牌
	public Suit[] suits = { Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS, Suit.SPADES };
	public Rank[] ranks = { Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX, Rank.SEVEN, Rank.EIGHT, Rank.NINE,
			Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING, Rank.ACE };
	Card card[] = new Card[52];
	public Card cardN[] = new Card[13];// 北方玩家
	public Card cardE[] = new Card[13];// 东方玩家
	public Card cardS[] = new Card[13];// 南方玩家
	public Card cardW[] = new Card[13];// 西方玩家

	public void setPoker()// 创建52张扑克牌
	{// 按顺序初始化牌
		int len = 0;
		for (int i = 0; i < suits.length; i++) {
			for (int j = 0; j < ranks.length; j++) {
				card[len++] = new Card(ranks[j], suits[i]);// 按照顺序排列初始化
			}
		}
		for (int i = 0; i < 13; i++) {// 对每一方玩家的桥牌进行方位，花色，数值初始化
			cardN[i] = new Card(ranks[0], suits[0]);
			cardE[i] = new Card(ranks[0], suits[0]);
			cardS[i] = new Card(ranks[0], suits[0]);
			cardW[i] = new Card(ranks[0], suits[0]);
			cardN[i].setPlayerPosition(PlayerPosition.NORTH);
			cardE[i].setPlayerPosition(PlayerPosition.EAST);
			cardS[i].setPlayerPosition(PlayerPosition.SOUTH);
			cardW[i].setPlayerPosition(PlayerPosition.WEST);
		}
	}

	public void printInitPoker() {// 输出初始化的牌
		System.out.println("创建的52张牌为:");
		for (int i = 0; i < card.length; i++) {
			System.out.print(card[i] + " ");
			if ((i + 1) % 13 == 0)
				System.out.println();
		}
	}

	public void Shuffle_deck()// 洗牌
	{
		Random r = new Random();
		/*int[] hash = new int[card.length];// 哈希数组用来判断当前牌是否已经产生
		for (int i = 0; i < hash.length; i++)
			hash[i] = 0;
		int sub = 0;
		while (sub < card.length) {
			int x = r.nextInt(card.length);
			if (hash[x] == 0)// 如果当前牌没有产生
			{
				// newPoker[sub++]=card[x].toString();//将按照顺序初始化的牌的随机的索引对应的牌赋给新的按照顺序排列的牌
				if (sub < 13) {
					cardN[sub++] = new Card(card[x].getRank(), card[x].getSuit());
					deckN.addCard(cardN[sub - 1]);
					// cardN[sub].setpalayerposition();
				} else if (sub < 26) {
					cardE[(sub - 13)] = new Card(card[x].getRank(), card[x].getSuit());
					deckE.addCard(cardE[sub - 13]);
					sub++;
				} else if (sub < 39) {
					cardS[sub - 26] = new Card(card[x].getRank(), card[x].getSuit());
					deckS.addCard(cardS[sub - 26]);
					sub++;
				} else {
					cardW[sub - 39] = new Card(card[x].getRank(), card[x].getSuit());
					deckW.addCard(cardW[sub - 39]);
					sub++;
				}
				// Card card=new Card("1",'S');
				hash[x] = 1;// 标记该索引已经产生过，防止出现重复赋值的牌
			}
		}*/
		
		/*重写随机洗牌的算法，采用效率更高的方法*/
		Deck deck=BridgeHelper.getDeck("23456789TJQKA.23456789TJQKA.23456789TJQKA.23456789TJQKA");//生成一副牌
		Deck[] randomDecks=new Deck[4];
		for (int i=0;i< randomDecks.length;i++) {
			randomDecks[i]=new Deck();
		}
		for(int i=0;i<13;i++) {
			for (int j = 0; j < randomDecks.length; j++) {
				List<Card> cards=deck.getCards();
				int cardPos=r.nextInt(cards.size());//获取随机选取的牌的位置
				randomDecks[j].addCard(cards.get(cardPos));//将牌加入一个玩家牌堆
				cards.remove(cardPos);//移除已取走的牌
			}
		}
		deckN=randomDecks[0];
		deckE=randomDecks[1];
		deckS=randomDecks[2];
		deckW=randomDecks[3];
	}
    //给每个玩家的牌进行排序
	public void Sort() {
            GameParser.sort(deckN.getCards());
            GameParser.sort(deckE.getCards());
            GameParser.sort(deckS.getCards());
            GameParser.sort(deckW.getCards());
	}

	public void print_result()// 输出每个人手中的牌
	{
		System.out.println("北方位置手中的牌为:");
		for (int i = 0; i < 13; i++) {
			System.out.print(deckN.getCards().get(i) + " ");
		}
		System.out.println();
		System.out.println("东方位置手中的牌为:");
		for (int i = 0; i < 13; i++) {
			System.out.print(deckE.getCards().get(i) + " ");
		}
		System.out.println();
		System.out.println("南方位置手中的牌为:");
		for (int i = 0; i < 13; i++) {
			System.out.print(deckS.getCards().get(i) + " ");
		}
		System.out.println();
		System.out.println("西方位置手中的牌为:");
		for (int i = 0; i < 13; i++) {
			System.out.print(deckW.getCards().get(i) + " ");
		}

		System.out.println();
	}
}
