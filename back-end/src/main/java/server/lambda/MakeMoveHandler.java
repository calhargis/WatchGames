package server.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import model.game.GameState;
import model.request.MakeMoveRequest;
import model.response.MakeMoveResponse;
import server.util.GameUtil;

import java.util.ArrayList;
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

        // Fetch the current game state from DynamoDB
        GameState gameState = gameUtil.getGameState(gameId);

        if (gameState == null) {
            return gameUtil.createErrorResponse("Game not found");
        }

        List<List<String>> board = gameState.getBoard();
        String currentTurn = gameState.getCurrentTurn();

        // Validate the move
        if (!isValidMove(board, row, col, currentTurn, playerId)) {
            return gameUtil.createErrorResponse("Invalid move");
        }

        // Update the board with the player's move
        makeMove(board, row, col, currentTurn);


        // Check for win or draw (null)
        String winner = checkWinner(board);

        // Update the turn
        if (winner == null) {
            currentTurn = currentTurn.equals("X") ? "O" : "X";
        }
        gameState.setBoard(board);
        gameState.setCurrentTurn(currentTurn);

        // Save the updated game state to DynamoDB
        gameUtil.saveGameStateToDynamoDB(gameState);

        // Create the response
        MakeMoveResponse response = new MakeMoveResponse();
        response.setStatus("Move made successfully");
        response.setBoard(board);
        response.setWinner(winner);

        // Next turn
        if (winner == null) {
            response.setMessage("Next player's turn");
            return response;
        }
        // Draw
        else if (winner.equals("Draw")){
            response.setDraw(true);
            return response;
        }

        // Game won
        response.setMessage("Player " + winner + " wins!");
        return response;
    }

    private boolean isValidMove(List<List<String>> board, int row, int col, String currentTurn, String playerId) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3) {
            return false;
        }
        if (!board.get(row).get(col).isEmpty()) {
            return false;
        }
        return currentTurn.equals(playerId);
    }

    public static String checkWinner(List<List<String>> board) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!board.get(i).get(0).isEmpty() &&
                    board.get(i).get(0).equals(board.get(i).get(1)) &&
                    board.get(i).get(1).equals(board.get(i).get(2))) {
                return board.get(i).get(0);
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (!board.get(0).get(i).isEmpty() &&
                    board.get(0).get(i).equals(board.get(1).get(i)) &&
                    board.get(1).get(i).equals(board.get(2).get(i))) {
                return board.get(0).get(i);
            }
        }

        // Check diagonals
        if (!board.get(0).get(0).isEmpty() &&
                board.get(0).get(0).equals(board.get(1).get(1)) &&
                board.get(1).get(1).equals(board.get(2).get(2))) {
            return board.get(0).get(0);
        }

        if (!board.get(0).get(2).isEmpty() &&
                board.get(0).get(2).equals(board.get(1).get(1)) &&
                board.get(1).get(1).equals(board.get(2).get(0))) {
            return board.get(0).get(2);
        }

        // Check if there are any empty cells
        for (List<String> row : board) {
            for (String cell : row) {
                if (cell.isEmpty()) {
                    return null; // Game is not finished
                }
            }
        }

        // If no winner and no empty cells, it's a draw
        return "Draw";
    }

    // Method to change the value at a specific row and column
    public void makeMove(List<List<String>> board, int row, int col, String player) {
        if (row < 0 || row >= board.size() || col < 0 || col >= board.get(row).size()) {
            throw new IllegalArgumentException("Invalid move: out of board bounds.");
        }

        if (!board.get(row).get(col).isEmpty()) {
            throw new IllegalArgumentException("Invalid move: cell already occupied.");
        }

        board.get(row).set(col, player);
    }

    // Method to initialize an empty Tic Tac Toe board
    public List<List<String>> initializeEmptyBoard() {
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
