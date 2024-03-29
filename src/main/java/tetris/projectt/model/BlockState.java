package tetris.projectt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BlockState {
    private int x;
    private int y;
    private String type; // 블록 타입 (예: "I", "O", "T", ...)
    private int[][] shape;
    private int currentRotation;
    private int[][][] shapes;
}
