package tetris.projectt.block;

public class IBlock extends Block {

    public IBlock() {
        super(new int[][][]{
                {{0, 1}, {1, 1}, {2, 1}, {3, 1}}, // 0도
                {{2, 0}, {2, 1}, {2, 2}, {2, 3}}, // 90도
                {{0, 2}, {1, 2}, {2, 2}, {3, 2}}, // 180도
                {{1, 0}, {1, 1}, {1, 2}, {1, 3}}  // 270도
        });
        this.type = "I";
    }

}