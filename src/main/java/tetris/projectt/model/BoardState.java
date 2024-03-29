package tetris.projectt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static tetris.projectt.board.Board.GAME_BOARD_HEIGHT;
import static tetris.projectt.board.Board.GAME_BOARD_WIDTH;
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class BoardState {
    private boolean[][] gameBoard = new boolean[GAME_BOARD_HEIGHT][GAME_BOARD_WIDTH];
    private int totalClearLines;
    private int level; // 현재 레벨
    private List<String> nextBlocks; // 다음 블록 타입
}
