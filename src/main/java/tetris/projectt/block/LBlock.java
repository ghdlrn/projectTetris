package tetris.projectt.block;

public class LBlock extends Block {

    public LBlock() {
        super(new int[][][]{
                {{0, 1}, {1, 1}, {2, 1}, {2, 0}}, // 0도
                {{1, 0}, {1, 1}, {1, 2}, {2, 2}}, // 90도
                {{0, 2}, {0, 1}, {1, 1}, {2, 1}}, // 180도
                {{0, 0}, {1, 0}, {1, 1}, {1, 2}}  // 270도
        });
        this.type = "L";
    }
}