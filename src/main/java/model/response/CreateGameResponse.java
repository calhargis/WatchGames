package model.response;

public class CreateGameResponse {
    private String status;
    private String gameId;
    private String playerXId;
    private String playerOId;
    private String[][] board;
    private String currentTurn;
    private String creationTimestamp;
    private String message;

    // Getters and setters


    public CreateGameResponse() {
    }

    public CreateGameResponse(String status, String gameId, String playerXId, String playerOId, String[][] board, String currentTurn, String creationTimestamp, String message) {
        this.status = status;
        this.gameId = gameId;
        this.playerXId = playerXId;
        this.playerOId = playerOId;
        this.board = board;
        this.currentTurn = currentTurn;
        this.creationTimestamp = creationTimestamp;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerXId() {
        return playerXId;
    }

    public void setPlayerXId(String playerXId) {
        this.playerXId = playerXId;
    }

    public String getPlayerOId() {
        return playerOId;
    }

    public void setPlayerOId(String playerOId) {
        this.playerOId = playerOId;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

