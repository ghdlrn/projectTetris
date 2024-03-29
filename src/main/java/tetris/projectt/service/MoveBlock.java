package tetris.projectt.service;

import tetris.projectt.block.Block;
import tetris.projectt.board.Board;

public class MoveBlock {
    private final Board board;
    private final int[][][] rotationWallKickDataJLSTZ = {   //JLSTZ블록 시계방향회전 Wallkickdata
            { {0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2} },//0->1
            { {0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2} },      //1->2
            { {0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2} },   //2->3
            { {0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2} } //3->0
    };
    private final int[][][] reverseRotationWallKickDataJLSTZ = {   //JLSTZ블록 반시계방향회전 Wallkickdata
            { {0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2} },//0->3
            { {0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2} },     //1->0
            { {0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2} },     //2->1
            { {0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2} }    //3->2
    };
    private final int[][][] rotationWallKickDataI = {       //I블록 시계방향회전 Wallkickdata
            { {0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2} },//0->1
            { {0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1} },      //1->2
            { {0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2} },   //2->3
            { {0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1} } //3->0
    };

    private final int[][][] reverseRotationWallKickDataI = {       //I블록 반시계방향회전 Wallkickdata
            { {0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1} },    //0->3
            { {0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2} },     //1->0
            { {0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1} },     //2->1
            { {0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2} },    //3->2
    };

    public MoveBlock(Board board) {
        this.board = board;
    }

    public boolean moveLeft(Block block) { // 블록을 좌측으로 이동
        return move(block, -1, 0);
    }

    public boolean moveRight(Block block) { // 블록을 우측으로 이동
        return move(block, 1, 0);
    }

    public boolean softDrop(Block block) {  // 블록을 아래로 이동
        return move(block, 0, 1);
    }

    public void hardDrop(Block block) {  // 블록을 한번에 아래로 이동
        while (move(block, 0, 1)) {
        }
    }
    private boolean move(Block block, int dx, int dy) { //이동해보고 안되면 원위치로되돌리는 로직
        block.setX(block.getX() + dx);
        block.setY(block.getY() + dy);
        if (!canMove(block)) {
            block.setX(block.getX() - dx); // 원래 위치로 되돌림
            block.setY(block.getY() - dy);
            return false;
        }
        return true;
    }

    protected boolean canMove(Block block) {
        for (int[] part : block.getShape()) {
            int x = block.getX() + part[0];
            int y = block.getY() + part[1];

            if (x < 0 || x >= Board.GAME_BOARD_WIDTH || y < 0 || y >= Board.GAME_BOARD_HEIGHT || board.getGameBoard()[y][x]) {
                return false;
            }
        }
        return true;
    }

    public boolean rotation(Block block) {
        if (block.getType().equals("O")) {      //O블록 바로회전
            block.rotation();
            return true;
        }
        int currentRotation = block.getCurrentRotation(); //현재 블록회전상태
        block.rotation();       //블록회전
        int[][][] wallKickData = block.getType().equals("I") ? rotationWallKickDataI : rotationWallKickDataJLSTZ;
        if (canMove(block)) {   //1차이동시도
            return true;
        }
        for (int i = 0; i < wallKickData[currentRotation].length; i++) {    //월킥데이터만큼  회전 반복
            int[] kick = wallKickData[currentRotation][i];
            block.setX(block.getX() + kick[0]);
            block.setY(block.getY() + kick[1]);
            if (canMove(block)) {      //도중에 회전성공하면 true
                return true;
            }
            block.setX(block.getX() - kick[0]);     //회전실패시 원상태로
            block.setY(block.getY() - kick[1]);
        }
        block.reverseRotation();    //월킥 모두 실패시 다시 원상태로
        return false;   //회전실패
    }

    public boolean reverseRotation(Block block) {
        if (block.getType().equals("O")) {      //O블록 바로회전
            block.reverseRotation();
            return true;
        }
        int currentRotation = block.getCurrentRotation(); //현재 블록회전상태
        block.reverseRotation();       //블록회전
        int[][][] wallKickData = block.getType().equals("I") ? reverseRotationWallKickDataI : reverseRotationWallKickDataJLSTZ;
        if (canMove(block)) {   //1차이동시도
            return true;
        }
        for (int i = 0; i < wallKickData[currentRotation].length; i++) {    //월킥데이터만큼  회전 반복
            int[] kick = wallKickData[currentRotation][i];
            block.setX(block.getX() - kick[0]);
            block.setY(block.getY() - kick[1]);
            if (canMove(block)) {      //도중에 회전성공하면 true
                return true;
            }
            block.setX(block.getX() + kick[0]);     //회전실패시 원상태로
            block.setY(block.getY() + kick[1]);
        }
        block.rotation();    //월킥 모두 실패시 다시 원상태로
        return false;   //회전실패
    }
}