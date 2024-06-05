package model.game;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;

import java.util.List;
import java.util.Map;

public class GameState {

    public GameState(String gameId, String playerXId, String playerOId, String currentTurn, String creationTimestamp, List<List<String>> board) {
        this.gameId = gameId;
        this.playerXId = playerXId;
        this.playerOId = playerOId;
        this.currentTurn = currentTurn;
        this.creationTimestamp = creationTimestamp;
        this.board = board;
    }

    String gameId;

    String playerXId;

    String playerOId;

    String currentTurn;

    String creationTimestamp;

    List<List<String>> board;

}
