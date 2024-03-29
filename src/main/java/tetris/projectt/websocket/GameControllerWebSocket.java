package tetris.projectt.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import tetris.projectt.service.GameService;

@Controller
public class GameControllerWebSocket {

    private final GameService gameService;
    private final GameWebSocket gameWebSocket;


    @Autowired
    public GameControllerWebSocket(GameService gameService, GameWebSocket gameWebSocket) {
        this.gameService = gameService;
        this.gameWebSocket = gameWebSocket;
    }

    @MessageMapping("/moveLeft")
    public void moveLeft() {
        if(gameService.getMoveBlock().moveLeft(gameService.getControlBlock())) {
            gameWebSocket.sendControlBlockState();
            gameWebSocket.sendBoardState();
            gameWebSocket.sendGhostBlockState(gameService.ghostBlockState());
        }
    }

    @MessageMapping("/moveRight")
    public void moveRight() {
        if(gameService.getMoveBlock().moveRight(gameService.getControlBlock())) {
            gameWebSocket.sendControlBlockState();
            gameWebSocket.sendBoardState();
            gameWebSocket.sendGhostBlockState(gameService.ghostBlockState());
        }
    }

    @MessageMapping("/rotation")
    public void rotation() {
        if(gameService.getMoveBlock().rotation(gameService.getControlBlock())) { // Assuming true for clockwise
            gameWebSocket.sendControlBlockState();
            gameWebSocket.sendBoardState();
            gameWebSocket.sendGhostBlockState(gameService.ghostBlockState());
        }
    }
    @MessageMapping("/reverseRotation")
    public void reverseRotation() {
        if(gameService.getMoveBlock().reverseRotation(gameService.getControlBlock())) { // Assuming true for clockwise
            gameWebSocket.sendControlBlockState();
            gameWebSocket.sendBoardState();
            gameWebSocket.sendGhostBlockState(gameService.ghostBlockState());
        }
    }

    @MessageMapping("/softDrop")
    public void softDrop() {
        if(gameService.getMoveBlock().softDrop(gameService.getControlBlock())) {
            gameWebSocket.sendControlBlockState();
            gameWebSocket.sendBoardState();
            gameWebSocket.sendGhostBlockState(gameService.ghostBlockState());
        }
    }

    @MessageMapping("/hardDrop")
    public void hardDrop() {
        gameService.getMoveBlock().hardDrop(gameService.getControlBlock());
        gameWebSocket.sendControlBlockState();
        gameWebSocket.sendBoardState();
    }

    @MessageMapping("/hold")
    public void holdBlock() {
        gameService.holdBlock();
        gameWebSocket.sendControlBlockState();
        gameWebSocket.sendHoldBlockState(gameService.getHoldBlockType());
    }

    @MessageMapping("/restart")
    public void restartGame() {
        gameService.restartGame();
        gameWebSocket.sendRestartGame();
        gameWebSocket.sendControlBlockState();
        gameWebSocket.sendBoardState();
        gameWebSocket.sendHoldBlockState(gameService.getHoldBlockType());
    }

    @MessageMapping("/pause")
    public void pauseGame() {
        gameService.pauseGame();
    }

    @MessageMapping("/resume")
    public void resumeGame() {
        gameService.resumeGame();
    }
}