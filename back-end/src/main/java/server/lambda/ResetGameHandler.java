package server.lambda;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import model.response.CreateGameResponse;
import server.util.GameUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResetGameHandler implements RequestHandler<Map<String, Object>, CreateGameResponse>  {

    private final AmazonDynamoDB client;
    private final DynamoDB dynamoDB;
    private final Table table;

    public ResetGameHandler() {
        // Initialize the DynamoDB client
        client = AmazonDynamoDBClientBuilder.standard().build();
        dynamoDB = new DynamoDB(client);
        // Reference the correct table name
        table = dynamoDB.getTable("TicTacToeGames");
    }

    @Override
    public CreateGameResponse handleRequest(Map<String, Object> input, Context context) {
        context.getLogger().log("Input: " + input);
        GameUtil gameUtil = new GameUtil(table);

        // Extract data from input
        String gameId = (String) input.get("gameId");
        String playerXId = (String) input.get("playerXId");
        String playerOId = (String) input.get("playerOId");
        String currentTurn = (String) input.get("currentTurn");
        String creationTimestamp = (String) input.get("creationTimestamp");

        if (!gameUtil.isValidRequest(gameId, playerXId, playerOId, currentTurn, creationTimestamp)) {
            throw new RuntimeException("input has empty variables (CreateGameHandler)");
        }

        // Initialize the board
        List<List<String>> board = gameUtil.boardInit();

        // Create game state
        Map<String, Object> gameState = new HashMap<>();
        gameState.put("gameId", gameId);
        gameState.put("playerXId", playerXId);
        gameState.put("playerOId", playerOId);
        gameState.put("board", board);
        gameState.put("currentTurn", currentTurn);
        gameState.put("creationTimestamp", creationTimestamp);

        // Save the game state to DynamoDB (pseudo code, implement DynamoDB logic here)
        gameUtil.saveGameStateToDynamoDB(gameState);

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
}
