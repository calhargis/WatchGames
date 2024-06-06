package server.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class DeleteGameHandler implements RequestHandler<Map<String, Object>, String> {

    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private final DynamoDB dynamoDB = new DynamoDB(client);
    private final String TABLE_NAME = "TicTacToeGames";

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {
        String gameId = (String) input.get("gameId");

        if (gameId == null || gameId.isEmpty()) {
            return "Game ID is missing";
        }

        Table table = dynamoDB.getTable(TABLE_NAME);

        try {
            table.deleteItem("gameId", gameId);
            return "Game deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error deleting game: " + e.getMessage();
        }
    }
}
