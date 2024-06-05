package server.util;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.google.gson.Gson;
import model.game.GameState;
import model.response.MakeMoveResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameUtil {
    private final Table table;
    private final Gson gson;

    public GameUtil(Table table) {
        this.table = table;
        gson = new Gson();
    }

    public PutItemOutcome saveGameStateToDynamoDB(GameState gameState) {
        String gameId = gameState.getGameId();
        String gameStateJson = gson.toJson(gameState);
        return table.putItem(new PutItemSpec().withItem(new Item()
                .withPrimaryKey("gameId", gameId)
                .withString("gameState", gameStateJson)));
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

    public GameState getGameState(String gameId) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("gameId", gameId);
        Item item = table.getItem(spec);

        if (item != null) {
            String gameStateJson = item.getString("gameState");
            return gson.fromJson(gameStateJson, GameState.class);
        }
        return null;
    }

    public MakeMoveResponse createErrorResponse(String message) {
        MakeMoveResponse response = new MakeMoveResponse();
        response.setStatus("Error");
        response.setMessage(message);
        return response;
    }


}
