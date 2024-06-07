package model.request;

public class DeleteGameRequest {

    public DeleteGameRequest(String gameId) {
        this.gameId = gameId;
    }

    String gameId;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
