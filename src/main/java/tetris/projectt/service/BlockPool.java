package tetris.projectt.service;

import tetris.projectt.block.*;
import tetris.projectt.service.BlockFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class BlockPool {
    private Map<String, Stack<Block>> pool;

    public BlockPool() {
        pool = new HashMap<>();
        // Initialize the pool with stacks for each block type
        pool.put("I", new Stack<>());
        pool.put("J", new Stack<>());
        pool.put("L", new Stack<>());
        pool.put("O", new Stack<>());
        pool.put("S", new Stack<>());
        pool.put("T", new Stack<>());
        pool.put("Z", new Stack<>());
    }

    public Block getBlock(String type) {
        Stack<Block> blocks = pool.get(type);
        if (blocks == null || blocks.isEmpty()) {
            // Create a new block if none are available in the pool
            return BlockFactory.createBlock(type);
        }
        return blocks.pop();
    }

    public void returnBlock(Block block) {
        // Reset the state of the block before returning it to the pool
        block.reset();
        Stack<Block> blocks = pool.get(block.getType());
        if (blocks != null) {
            blocks.push(block);
        }
    }
}