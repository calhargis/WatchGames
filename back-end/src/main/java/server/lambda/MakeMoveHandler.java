package server.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import model.request.MakeMoveRequest;
import model.response.MakeMoveResponse;
import server.util.GameUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeMoveHandler implements RequestHandler<MakeMoveRequest, MakeMoveResponse> {

    private final DynamoDB dynamoDB;
    private final Table table;
    GameUtil gameUtil;

    public MakeMoveHandler() {
        // Initialize the DynamoDB client
        dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.standard().build());
        table = dynamoDB.getTable("TicTacToeGames");
        gameUtil = new GameUtil(table);
    }
    @Override
    public MakeMoveResponse handleRequest(MakeMoveRequest request, Context context) {
        String gameId = request.getGameId();
        String playerId = request.getPlayerId();
        int row = request.getRow();
        int col = request.getCol();
        // welcome shade


        // Fetch the current game state from DynamoDB
        Map<String, Object> gameState = gameUtil.getGameState(gameId);

        if (gameState == null) {
            return createErrorResponse("Game not found");
        }

        String[][] board = (String[][]) gameState.get("board");
        String currentTurn = (String) gameState.get("currentTurn");

        // Validate the move
        if (!isValidMove(board, row, col, currentTurn, playerId)) {
            return createErrorResponse("Invalid move");
        }

        // Update the board with the player's move
        board[row][col] = currentTurn;

        // Check for win or draw
        String winner = checkWinner(board);
        boolean draw = isDraw(board);

        // Update the turn
        if (winner == null && !draw) {
            currentTurn = currentTurn.equals("X") ? "O" : "X";
        }
        gameState.put("board", board);
        gameState.put("currentTurn", currentTurn);

        // Save the updated game state to DynamoDB
        gameUtil.saveGameStateToDynamoDB(gameState);

        // Create the response
        MakeMoveResponse response = new MakeMoveResponse();
        response.setStatus("Move made successfully");
        response.setBoard(board);
        response.setWinner(winner);
        response.setDraw(draw);
        response.setMessage(winner != null ? "Player " + winner + " wins!" : draw ? "The game is a draw." : "Next player's turn");

        return response;
    }

    private boolean isValidMove(String[][] board, int row, int col, String currentTurn, String playerId) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3) {
            return false;
        }
        if (!board[row][col].isEmpty()) {
            return false;
        }
        return currentTurn.equals(playerId);
    }

    private String checkWinner(String[][] board) {
        // Check rows, columns, and diagonals for a winner
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].isEmpty() && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return board[i][0];
            }
            if (!board[0][i].isEmpty() && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                return board[0][i];
            }
        }
        if (!board[0][0].isEmpty() && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            return board[0][0];
        }
        if (!board[0][2].isEmpty() && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            return board[0][2];
        }
        return null;
    }

    private boolean isDraw(String[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private MakeMoveResponse createErrorResponse(String message) {
        MakeMoveResponse response = new MakeMoveResponse();
        response.setStatus("Error");
        response.setMessage(message);
        return response;
    }

    private void saveGameStateToDynamoDB(String gameId, String[][] board, String currentTurn) {
        List<List<String>> boardList = gameUtil.convertBoardToList(board);

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("gameId", gameId)
                .withUpdateExpression("set board = :b, currentTurn = :t")
                .withValueMap(new ValueMap()
                        .withList(":b", boardList)
                        .withString(":t", currentTurn));

        table.updateItem(updateItemSpec);
    }
}
