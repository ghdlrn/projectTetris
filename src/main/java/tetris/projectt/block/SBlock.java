package tetris.projectt.block;

public class SBlock extends Block {
    public SBlock() {
        super(new int[][][]{
                {{0, 1}, {1, 1}, {1, 0}, {2, 0}}, // 0도
                {{1, 0}, {1, 1}, {2, 1}, {2, 2}}, // 90도
                {{2, 1}, {1, 1}, {1, 2}, {0, 2}}, // 180도
                {{0, 0}, {0, 1}, {1, 1}, {1, 2}}  // 270도
        });
        this.type = "S";
    }
   
}