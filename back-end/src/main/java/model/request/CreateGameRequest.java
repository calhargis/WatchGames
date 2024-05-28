package model.request;

public class CreateGameRequest {
    private String gameId;
    private String playerXId;
    private String playerOId;
    private String[][] board;
    private String currentTurn;
    private String creationTimestamp;

    // Getters and setters

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
}

