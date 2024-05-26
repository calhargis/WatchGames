package server.lambda;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import model.request.CreateGameRequest;
import model.response.CreateGameResponse;
import server.service.GameService;

import java.util.*;

public class CreateGameHandler implements RequestHandler<Map<String, Object>, CreateGameResponse> {

    //TODO Make sure DynamoDB table is correctly connected

    private final AmazonDynamoDB client;
    private final DynamoDB dynamoDB;
    private final Table table;

    public CreateGameHandler() {
        // Initialize the DynamoDB client
        client = AmazonDynamoDBClientBuilder.standard().build();
        dynamoDB = new DynamoDB(client);
        // Reference the correct table name
        table = dynamoDB.getTable("TicTacToeGames");
    }
    @Override
    public CreateGameResponse handleRequest(Map<String, Object> input, Context context) {
        context.getLogger().log("Input: " + input);

        // Extract data from input
        String gameId = (String) input.get("gameId");
        String playerXId = (String) input.get("playerXId");
        String playerOId = (String) input.get("playerOId");
        String[][] board = new String[3][3];
        String currentTurn = (String) input.get("currentTurn");
        String creationTimestamp = (String) input.get("creationTimestamp");

        if (gameId == null || gameId.isEmpty()) {
            gameId = "no gameId found in input. (CreateGameHandler)";
        }
        if (playerXId == null || playerXId.isEmpty()) {
            playerXId = "no playerXId found in input. (CreateGameHandler)";
        }
        if (playerOId == null || playerOId.isEmpty()) {
            playerOId = "no playerOid found in input. (CreateGameHandler)";
        }
        if (currentTurn == null || currentTurn.isEmpty()) {
            currentTurn = "no currentTurn found in input. (CreateGameHandler)";
        }
        if (creationTimestamp == null || creationTimestamp.isEmpty()) {
            creationTimestamp = "no creationTimestamp found in input. (CreateGameHandler)";
        }

        // Initialize the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
            }
        }

        // Create game state
        Map<String, Object> gameState = new HashMap<>();
        gameState.put("gameId", gameId);
        gameState.put("playerXId", playerXId);
        gameState.put("playerOId", playerOId);
        gameState.put("board", board);
        gameState.put("currentTurn", currentTurn);
        gameState.put("creationTimestamp", creationTimestamp);

        // Save the game state to DynamoDB (pseudo code, implement DynamoDB logic here)
        saveGameStateToDynamoDB(gameState);

        // Create response
        CreateGameResponse response = new CreateGameResponse();
        response.setStatus("Game created successfully");
        response.setGameId(gameId);
        response.setPlayerXId(playerXId);
        response.setPlayerOId(playerOId);
        response.setBoard(board);
        response.setCurrentTurn(currentTurn);
        response.setCreationTimestamp(creationTimestamp);
        response.setMessage("Enjoy your game!");

        return response;
    }

    private void saveGameStateToDynamoDB(Map<String, Object> gameState) {
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

    private List<List<String>> convertBoardToList(String[][] board) {
        List<List<String>> boardList = new ArrayList<>();
        for (String[] row : board) {
            boardList.add(Arrays.asList(row));
        }
        return boardList;
    }


}
