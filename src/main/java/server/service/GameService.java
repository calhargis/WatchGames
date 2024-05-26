package server.service;

import model.request.CreateGameRequest;
import model.response.CreateGameResponse;

public class GameService {

    public CreateGameResponse createGame(CreateGameRequest request) {
        if (request.getGameId() == null) {
            throw new RuntimeException("gameID is null");
        }
        if (request.getBoard() == null) {
            throw new RuntimeException("board is null");
        }
        if (request.getCreationTimestamp() == null) {
            throw new RuntimeException("creationTimestamp is null");
        }
        if (request.getCurrentTurn() == null) {
            throw new RuntimeException("currentTurn is null");
        }
        if (request.getPlayerXId() == null) {
            throw new RuntimeException("playerXId is null");
        }
        if (request.getPlayerOId() == null) {
            throw new RuntimeException("playerOId is null");
        }

        return new CreateGameResponse("", request.getGameId(), request.getPlayerXId(), request.getPlayerOId(), request.getBoard(), request.getCurrentTurn(), request.getCreationTimestamp(), "");
    }

}
