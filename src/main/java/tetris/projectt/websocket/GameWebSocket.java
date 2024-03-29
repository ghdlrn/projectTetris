package tetris.projectt.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import tetris.projectt.block.Block;
import tetris.projectt.model.BlockState;
import tetris.projectt.model.BoardState;
import tetris.projectt.model.GhostBlockState;
import tetris.projectt.model.HoldBlockState;
import tetris.projectt.service.GameService;

import java.util.List;

@Component
public class GameWebSocket {        //클라이언트에게 게임 보드의 상태와 현재 블록의 상태를 전송
    // GameService와 연결되어 현재 게임 상태를 얻어오고, SimpMessagingTemplate을 사용하여 해당 상태를 WebSocket으로 클라이언트에게 전송
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private GameService gameService;

    @Autowired
    public void setGameService(@Lazy GameService gameService) {
        this.gameService = gameService;
    }

    public void sendBoardState() {
        List<String> nextBlockTypes = gameService.getNextBlockTypes();

        BoardState boardState = BoardState.builder()
                .gameBoard(gameService.getBoard().getGameBoard())
                .totalClearLines(gameService.getTotalClearLines())
                .level(gameService.getLevel())
                .nextBlocks(nextBlockTypes)
                .build();

        messagingTemplate.convertAndSend("/topic/boardState", boardState);
    }

    public void sendControlBlockState() {
        Block controlBlock = gameService.getControlBlock();
        if (controlBlock != null) {
            BlockState blockState = new BlockState(
                    controlBlock.getX(),
                    controlBlock.getY(),
                    controlBlock.getType(),
                    controlBlock.getShape(),
                    controlBlock.getCurrentRotation(),
                    controlBlock.getShapes()
            );
            messagingTemplate.convertAndSend("/topic/controlBlockState", blockState);
        }
    }

    public void sendHoldBlockState(String holdBlockType) {
        HoldBlockState holdBlockState = new HoldBlockState(gameService.getHoldBlockType());
        messagingTemplate.convertAndSend("/topic/holdBlockState", holdBlockState);
    }

    public void sendGhostBlockState(GhostBlockState ghostBlockState) {
        if (ghostBlockState != null) {
            messagingTemplate.convertAndSend("/topic/ghostBlock", ghostBlockState);
        }
    }
    public void sendRestartGame() {
        messagingTemplate.convertAndSend("/topic/restartGame", "Game has restarted");
    }
}