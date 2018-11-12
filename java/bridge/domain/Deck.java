package bridge.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public List<Card> getCards() {// 获得牌堆中所有的牌。
        return cards;
    }

    public void setCards(List<Card> cards) {// 设置牌堆中所有的牌。
        this.cards = cards;
    }

    public Deck() {
        cards = new ArrayList<>();
    }

    public Card getTopCard() {
        return cards.isEmpty() ? null : cards.get(cards.size() - 1);
    }

    public Card getBottomCard() {
        return cards.isEmpty() ? null : cards.get(0);
    }

    public Card getCardWithHighestRank() {// 获得分值最大的牌。
        return cards.isEmpty() ? null : Collections.max(cards);
    }

    public boolean notEmpty() {
        return !cards.isEmpty();
    }

    public void removeCard(Card card) {// 从牌堆里移走指定的牌。
        cards.remove(card);
    }

    public void addCard(Card card) {// 向牌堆里加入指定的牌。
        cards.add(card);
    }

    public Card getCard(Rank rank, Suit suit) {
    	// 获得牌堆里具有指定分值和花色的牌。
        return cards.stream().filter(card -> card.getRank().equals(rank) && card.getSuit().equals(suit)).findFirst().orElse(null);
    }

    public Card getCard(int score, Suit suit) {
        return getCard(new Rank(score), suit);
    }

    public boolean has(Rank rank, Suit suit) {// 判断牌堆里是否具有指定分值和花色的牌。
        return getCard(rank, suit) != null;
    }

    public boolean has(int score, Suit suit) {
        return getCard(score, suit) != null;
    }

    public int getCount() {
    	// 获得牌堆里牌的总数。
        return cards.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getCount());
        sb.append(":");
        for (Card card : cards) {
            sb.append(card);
        }
        return sb.toString();
    }
}
