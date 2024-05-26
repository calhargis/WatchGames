package ses.tictactoe;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateGameLambda implements RequestHandler<Object, String> {

    private static final String DYNAMODB_TABLE_NAME = "TicTacToeGames"; // Replace with your table name

    @Override
    public String handleRequest(Object input, Context context) {
        // Initialize the DynamoDB client
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

        // Generate a unique game ID
        String gameId = UUID.randomUUID().toString();

        // Create a map of attribute name-value pairs
        Map<String, AttributeValue> attributeMap = new HashMap<>();
        attributeMap.put("GameId", new AttributeValue(gameId));
        attributeMap.put("Status", new AttributeValue("InProgress"));

        // Create a new game item in DynamoDB
        PutItemRequest request = new PutItemRequest()
                .withTableName(DYNAMODB_TABLE_NAME)
                .withItem(attributeMap);
                        // Add more game attributes here as needed

        dynamoDB.putItem(request);

        return gameId; // Return the generated game ID
    }
}

