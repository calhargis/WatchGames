package server.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.google.gson.Gson;

public class TicTacToeService {

    private final AmazonDynamoDB dynamoDBClient;
    private final String tableName; // Name of your DynamoDB table

    public TicTacToeService(AmazonDynamoDB dynamoDBClient, String tableName) {
        this.dynamoDBClient = dynamoDBClient;
        this.tableName = tableName;
    }

    public void updateGameState(String gameId, String[][] board) {
        DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
        Table table = dynamoDB.getTable(tableName);

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("GameId", gameId)
                .withUpdateExpression("SET Board = :b")
                .withValueMap(new ValueMap().withString(":b", serializeBoard(board)));

        UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
        // You can handle the outcome as needed, e.g., check for success or handle errors
    }

    // Helper method to serialize the game board to a string (e.g., JSON)
    private String serializeBoard(String[][] board) {
        // Implement your serialization logic here, e.g., convert the 2D array to a JSON string
        return new Gson().toJson(board);
    }

    public String[][] initializeGame(int numRows, int numCols) {
        String[][] board = new String[numRows][numCols];

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                board[row][col] = " "; // Initialize each cell as empty
            }
        }

        return board;
    }

}

