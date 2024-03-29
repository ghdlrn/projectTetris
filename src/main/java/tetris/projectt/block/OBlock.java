package tetris.projectt.block;

public class OBlock extends Block {
    public OBlock() {
        super(new int[][][]{
                {{0, 0}, {0, 1}, {1, 0}, {1, 1}}, // 0도
                {{0, 0}, {0, 1}, {1, 0}, {1, 1}}, // 90도
                {{0, 0}, {0, 1}, {1, 0}, {1, 1}}, // 180도
                {{0, 0}, {0, 1}, {1, 0}, {1, 1}}  // 270도
        });
        this.type = "O";
    }
}
