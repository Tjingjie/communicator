package bridge.domain;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import bridge.domain.utils.BridgeHelper;

public class BridgeGame {
    private Trick currentTrick;

    public Trick getCurrentTrick() {// 返回当前墩（可以用来判断上一回合是否已经结束）。
        return currentTrick;
    }

    private PlayerPosition nextPlayer;

    private Dictionary<PlayerPosition, Deck> gameState;

    public Dictionary<PlayerPosition, Deck> getGameState() {
    	// 返回当前牌桌状态，其中每一个PlayerPosition都有一个Deck。
        return gameState;
    }

    private List<Trick> tricks;

    public List<Trick> getTricks() {// 获取已经完成的所有的墩（时间顺序）。
        return tricks;
    }

    private PlayerPosition declarer;

    public PlayerPosition getDeclarer() {// 获取庄家。
        return declarer;
    }

    private PlayerPosition dummy;

    public PlayerPosition getDummy() {// 获取明手。
        return dummy;
    }

    private Contract contract;

    public Contract getContract() { // 获取定约。
        return contract;
    }

    public BridgeGame(Dictionary<PlayerPosition, Deck> gameState, String contractShortStr)
    {
        this.gameState = gameState;
        contract = new Contract(contractShortStr);
        tricks = new ArrayList<>();
        declarer = contract.getPlayerPosition();
        dummy = BridgeHelper.getNextPlayerPosition(BridgeHelper.getNextPlayerPosition(declarer));
        currentTrick = new Trick();
        currentTrick.setTrickDealer(BridgeHelper.getNextPlayerPosition(declarer));
    }

    private static PlayerPosition findWinner(Trick trick, Trump trump) {
        Card highestTrump = trick.getDeck().getCards().stream().filter(card -> card.getSuit().equals(trump.getSuit())).max(Card::compareTo).orElse(null);
        Card highestInTrickDealerSuit = trick.getDeck().getCards().stream().filter(card -> card.getSuit().equals(trick.getTrickDealerSuit())).max(Card::compareTo).orElse(null);
        return highestTrump != null ? highestTrump.getPlayerPosition() : highestInTrickDealerSuit.getPlayerPosition();
    }

    public int getCardsRemaining() {// 获得牌桌上剩余的牌的数量。
        int total = 0;
        Enumeration<Deck> decks = gameState.elements();
        while (decks.hasMoreElements()) {
            total += decks.nextElement().getCount();
        }
        return total;
    }

    public PlayerPosition playCard(Card card, PlayerPosition playerPosition) {
    	// 出一张牌，参数card是牌，playerPosition是出牌人的位置，返回下一个出牌人的位置。
        nextPlayer = BridgeHelper.getNextPlayerPosition(playerPosition);
        if (currentTrick.getDeck().getCount() <= 4) {
            card.setPlayerPosition(playerPosition);
            currentTrick.getDeck().addCard(card);
        }
        if (currentTrick.getDeck().getCount() == 4) {
            tricks.add(currentTrick);
            PlayerPosition winner = findWinner(currentTrick, contract.getTrump());
            currentTrick.setTrickWinner(winner);
            nextPlayer = winner;
            currentTrick = new Trick();
            currentTrick.setTrickDealer(winner);
        }
        gameState.get(playerPosition).removeCard(card);
        return nextPlayer;
    }

    public int getNorthSouthTricksMade() {// 获得南北得到的墩数。
        return Integer.parseInt(String.valueOf(tricks.stream().filter(trick -> trick.getTrickWinner().equals(PlayerPosition.NORTH) || trick.getTrickWinner().equals(PlayerPosition.SOUTH)).count()));
    }

    public int getEastWestTricksMade() {// 获得东西得到的墩数。
        return Integer.parseInt(String.valueOf(tricks.stream().filter(trick -> trick.getTrickWinner().equals(PlayerPosition.EAST) || trick.getTrickWinner().equals(PlayerPosition.WEST)).count()));
    }
}
