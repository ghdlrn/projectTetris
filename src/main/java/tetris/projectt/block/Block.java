package tetris.projectt.block;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public abstract class Block {

    protected int[][][] shapes; // 모든 회전 상태를 저장
    protected int currentRotation = 0;          // 0 : 초기블록,  1 : 90도 회전, 2 : 180도회전, 3 : 270도 회전 (시계방향기준)
    protected int[][] shape;    //블록 현재의 모양
    protected int x, y; // 블록의  위치
    protected String type; // 블록 타입

    public Block(int[][][] shapes) {
        this.shapes = shapes;
        this.shape = shapes[currentRotation]; // 초기 모양 설정
        this.x = 0;
        this.y = 0;
    }


    public void rotation() {  //블록회전(시계)
        currentRotation = (currentRotation + 1) % shapes.length; // 다음 회전 상태
        this.shape = shapes[currentRotation]; // 모양 업데이트
    }

    public void reverseRotation() {  //블록회전(반시계)
        currentRotation = (currentRotation - 1 + shapes.length) % shapes.length; // 다음 회전 상태
        this.shape = shapes[currentRotation]; // 모양 업데이트
    }

    public void reset() {
        currentRotation = 0;
        shape = shapes[currentRotation];
        x = 0;
        y = 0;
    }
}