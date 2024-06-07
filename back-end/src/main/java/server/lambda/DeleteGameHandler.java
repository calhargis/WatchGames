package server.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import model.request.DeleteGameRequest;
import model.response.DeleteGameResponse;
import model.response.MakeMoveResponse;

import java.util.Map;

public class DeleteGameHandler implements RequestHandler<DeleteGameRequest, DeleteGameResponse> {

    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private final DynamoDB dynamoDB = new DynamoDB(client);
    private final String TABLE_NAME = "TicTacToeGames";

    @Override
    public DeleteGameResponse handleRequest(DeleteGameRequest request, Context context) {
        String gameId = request.getGameId();

        if (gameId == null || gameId.isEmpty()) {
            return createErrorResponse("Game ID is missing");
        }

        Table table = dynamoDB.getTable(TABLE_NAME);

        try {
            table.deleteItem("gameId", gameId);
            return createSuccessResponse("Game with gameId: \"" + gameId + "\" deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return createErrorResponse("\"Error deleting game: \" + e.getMessage()");
        }
    }

    public DeleteGameResponse createErrorResponse(String message) {
        DeleteGameResponse response = new DeleteGameResponse();
        response.setStatus("Error");
        response.setMessage(message);
        return response;
    }

    public DeleteGameResponse createSuccessResponse(String message) {
        DeleteGameResponse response = new DeleteGameResponse();
        response.setStatus("Success");
        response.setMessage(message);
        return response;
    }
}
