package tetris.projectt.block;

public class TBlock extends Block {
    public TBlock() {
        super(new int[][][]{
                {{0, 1}, {1, 1}, {2, 1}, {1, 0}}, // 0도
                {{1, 0}, {1, 1}, {1, 2}, {2, 1}}, // 90도
                {{0, 1}, {1, 1}, {2, 1}, {1, 2}}, // 180도
                {{0, 1}, {1, 1}, {1, 0}, {1, 2}}  // 270도
        });
        this.type = "T";
    }
}