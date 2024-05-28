package ses.tictactoe;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.google.gson.JsonObject;

public class MakeMoveLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String TABLE_NAME = "TicTacToeGames";

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
            DynamoDB dynamoDBClient = new DynamoDB(dynamoDB);

            // Parse the request body to get move details
            JsonObject requestBody = new JsonObject(); // Parse your request body JSON here

            String gameId = requestBody.get("gameId").getAsString();
            int row = requestBody.get("row").getAsInt();
            int col = requestBody.get("col").getAsInt();
            String player = requestBody.get("player").getAsString();

            // Create a DynamoDB item for the move
            Item moveItem = new Item()
                    .withPrimaryKey(new PrimaryKey("GameId", gameId))
                    .withString("Player", player)
                    .withNumber("Row", row)
                    .withNumber("Col", col);

            // Update the game state in DynamoDB
            UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                    .withPrimaryKey(new PrimaryKey("GameId", gameId))
                    .withUpdateExpression("SET #r[#c] = :p")
                    .withNameMap(new NameMap().with("#r", "Board").with("#c", String.valueOf(col)))
                    .withValueMap(new ValueMap().withString(":p", player));

            dynamoDBClient.getTable(TABLE_NAME).updateItem(updateItemSpec);

            // Check for a win or a draw
            // Implement your game logic here to determine the game outcome

            // Build a response
            JsonObject responseBody = new JsonObject();
            responseBody.addProperty("message", "Move successful");
            // Include game outcome information in the response

            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(200);
            response.setBody(responseBody.toString());
            return response;
        } catch (Exception e) {
            APIGatewayProxyResponseEvent errorResponse = new APIGatewayProxyResponseEvent();
            errorResponse.setStatusCode(500);
            errorResponse.setBody("Error: " + e.getMessage());
            return errorResponse;
        }
    }

    //Tic-tac-toe functions

    public boolean checkForWin(String[][] board, String player, int row, int col) {
        // Check horizontally
        if (board[row][0].equals(player) && board[row][1].equals(player) && board[row][2].equals(player)) {
            return true;
        }

        // Check vertically
        if (board[0][col].equals(player) && board[1][col].equals(player) && board[2][col].equals(player)) {
            return true;
        }

        // Check diagonally (top-left to bottom-right)
        if (row == col && board[0][0].equals(player) && board[1][1].equals(player) && board[2][2].equals(player)) {
            return true;
        }

        // Check diagonally (top-right to bottom-left)
        return row + col == 2 && board[0][2].equals(player) && board[1][1].equals(player) && board[2][0].equals(player); // No win detected
    }

    public boolean checkForDraw(String[][] board) {
        for (String[] strings : board) {
            for (String string : strings) {
                // If any cell is empty, the game is not a draw
                if (string.equals(" ")) {
                    return false;
                }
            }
        }
        return true; // All cells are filled; it's a draw
    }

    public String switchPlayer(String currentPlayer) {
        if (currentPlayer.equals("X")) {
            return "O";
        } else if (currentPlayer.equals("O")) {
            return "X";
        } else {
            throw new IllegalArgumentException("Invalid player symbol: " + currentPlayer);
        }
    }
    


    public boolean validateMove(String[][] board, int row, int col) {
        int numRows = board.length;
        int numCols = board[0].length;

        // Check if the cell is within the bounds of the game board
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return false; // Move is out of bounds
        }

        // Check if the cell is empty
        return board[row][col].equals(" ");
    }




}

