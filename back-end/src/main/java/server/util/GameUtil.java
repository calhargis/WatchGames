package server.util;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import model.response.MakeMoveResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameUtil {
    private final Table table;

    public GameUtil(Table table) {
        this.table = table;
    }

    public void saveGameStateToDynamoDB(Map<String, Object> gameState) {
        // Convert the board from 2D array to List of Lists
        List<List<String>> boardList = convertBoardToList((String[][]) gameState.get("board"));

        Item item = new Item()
                .withPrimaryKey("gameId", gameState.get("gameId"))
                .withString("playerXId", (String) gameState.get("playerXId"))
                .withString("playerOId", (String) gameState.get("playerOId"))
                .withString("currentTurn", (String) gameState.get("currentTurn"))
                .withString("creationTimestamp", (String) gameState.get("creationTimestamp"))
                .withList("board", boardList); // Use withList for List of Lists

        PutItemOutcome outcome = table.putItem(item);
    }

    public List<List<String>> convertBoardToList(String[][] board) {
        List<List<String>> boardList = new ArrayList<>();
        for (String[] row : board) {
            boardList.add(Arrays.asList(row));
        }
        return boardList;
    }

    public List<List<String>> boardInit() {
        List<List<String>> board = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                row.add("");  // Add empty string to represent an empty cell
            }
            board.add(row);
        }
        return board;
    }

    public boolean isValidRequest(String gameId, String playerXId, String playerOId, String currentTurn, String creationTimestamp) {
        if (gameId == null || gameId.isEmpty()) {
            return false;
        }
        if (playerXId == null || playerXId.isEmpty()) {
            return false;
        }
        if (playerOId == null || playerOId.isEmpty()) {
            return false;
        }
        if (currentTurn == null || currentTurn.isEmpty()) {
            return false;
        }
        if (creationTimestamp == null || creationTimestamp.isEmpty()) {
            return false;
        }
        return true;
    }

    public Map<String, Object> getGameState(String gameId) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("gameId", gameId);
        return table.getItem(spec).asMap();
    }

    public MakeMoveResponse createErrorResponse(String message) {
        MakeMoveResponse response = new MakeMoveResponse();
        response.setStatus("Error");
        response.setMessage(message);
        return response;
    }
}
