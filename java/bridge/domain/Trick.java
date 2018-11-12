package bridge.domain;

public class Trick {
    private PlayerPosition trickDealer;

    public PlayerPosition getTrickDealer() {// 获得第一个出牌的人的位置。
        return trickDealer;
    }

    public void setTrickDealer(PlayerPosition trickDealer) {// 设置第一个出牌的人的位置。
        this.trickDealer = trickDealer;
    }

    private Deck deck;

    public Deck getDeck() {// 获得这一回合中出过的牌。
        return deck;
    }

    public void setDeck(Deck deck) {// 设置这一回合中出过的牌。
        this.deck = deck;
    }

    private PlayerPosition trickWinner;

    public PlayerPosition getTrickWinner() {// 获得本回合赢家。
        return trickWinner;
    }

    public void setTrickWinner(PlayerPosition trickWinner) {// 设置本回合赢家。
        this.trickWinner = trickWinner;
    }

    public Suit getTrickDealerSuit() {// 获得第一个出牌的人所出的牌的花色。
        return deck.getBottomCard().getSuit();
    }

    public Trick() {
        deck = new Deck();
    }
}
