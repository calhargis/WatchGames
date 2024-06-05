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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerXId() {
        return playerXId;
    }

    public void setPlayerXId(String playerXId) {
        this.playerXId = playerXId;
    }

    public String getPlayerOId() {
        return playerOId;
    }

    public void setPlayerOId(String playerOId) {
        this.playerOId = playerOId;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public List<List<String>> getBoard() {
        return board;
    }

    public void setBoard(List<List<String>> board) {
        this.board = board;
    }
}
