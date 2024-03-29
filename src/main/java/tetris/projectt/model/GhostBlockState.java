package tetris.projectt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class GhostBlockState {
    private int x;
    private int y;
    private int[][] shape;
    private int currentRotation;
}
