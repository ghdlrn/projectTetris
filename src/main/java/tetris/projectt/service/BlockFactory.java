package tetris.projectt.service;

import tetris.projectt.block.*;
public class BlockFactory {

    public static Block createBlock(String type) {
        switch (type) {
            case "I":
                return new IBlock();
            case "J":
                return new JBlock();
            case "L":
                return new LBlock();
            case "O":
                return new OBlock();
            case "S":
                return new SBlock();
            case "T":
                return new TBlock();
            case "Z":
                return new ZBlock();
            default:
                throw new IllegalArgumentException("Unknown block type: " + type);
        }
    }
}
