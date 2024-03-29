package tetris.projectt.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tetris.projectt.block.*;
import tetris.projectt.board.Board;
import tetris.projectt.model.BoardState;
import tetris.projectt.model.GhostBlockState;
import tetris.projectt.websocket.GameWebSocket;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Getter
public class GameService {

    private Board board;    //게임이루어지는 board
    private BlockPool blockPool;
    private Block controlBlock; //사용자조작 블록
    private final LinkedList<Block> bag = new LinkedList<>();
    private MoveBlock moveBlock;
    private GhostBlockState ghostBlockState;
    private ScheduledExecutorService executorService;
    private volatile boolean gameStateUpdated = false;
    private int totalClearLines = 0;    //클리어한 총 라인 수
    private int clearLine = 0;       //클리어한라인수    (20라인클리어시 level1증가)
    private int level = 1;      //게임레벨    (기본설정 레벨 1)
    private long dropInterval = 1000;       //블록떨어지는 속도(기본속도 1초에 1칸 -> 1레벨 증가시 1/10씩 감소
    private final Queue<Block> nextBlocks = new LinkedList<>();
    private String holdBlockType = null;
    private boolean canHold = true;
    @Getter
    private boolean isPaused = false;


    @Autowired
    private GameWebSocket gameWebSocket;

    @PostConstruct  //객체 생성 후 자동으로 init메서드 호출
    public void init() {
        this.blockPool = new BlockPool();
        this.board = new Board();
        this.moveBlock = new MoveBlock(board);
        fillBag();  // 7-bag규칙에 따른 블럭배열 생성
        createNextBlocks();    //미리보기 블록 준비
    }

    private void startGameLoop() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {   // 게임 루프 로직 시작
            try {
                if (!isPaused) {
                    gameUpdate();
                    if (gameStateUpdated) {
                        sendGameStateUpdates();
                        gameStateUpdated = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                stopGame();
            }
        }, 0, dropInterval, TimeUnit.MILLISECONDS); // updateStatus에 따라 게임의 속도를 조절
    }


    private void stopGameLoop() {   //게임루프 중단
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
            try {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void startGameManually() {
        if (this.executorService == null || this.executorService.isShutdown()) {
            startGameLoop();
        }
    }
    private void fillBag() {        //7-bag블록 섞는규칙
        List<String> types = Arrays.asList("I", "J", "L", "O", "S", "T", "Z");
        Collections.shuffle(types); // Shuffle the list of types for the 7-bag rule.
        for (String type : types) {
            Block block = blockPool.getBlock(type);
            bag.add(block);
        }
    }

    private Block createNewBlock() {
        if (bag.isEmpty()) {
            fillBag(); // 봉지가 비었다면 다시 채우기
        }
        return bag.poll(); // 봉지에서 블록 하나를 꺼내고 반환
    }

    private void createNextBlocks() {   //미리보기블록생성
        while (nextBlocks.size() < 5) {     //미리보기 갯수만큼 다음블록 준비
            nextBlocks.add(createNewBlock());
        }
    }

    public List<String> getNextBlockTypes() {   //NextBlock의 정보를 String형 배열로 변환하여 저장
        return nextBlocks.stream()
                .map(Block::getType)
                .collect(Collectors.toList());
    }

    private void createControlBlock() {      //조작할 블록 배치
        if (controlBlock != null) {
            releaseControlBlock(controlBlock);
        }
        if (nextBlocks.isEmpty()) {
            fillBag();
            createNextBlocks();
        }
        controlBlock = nextBlocks.poll();
        createNextBlocks();

        controlBlock.setX(3);   //배치블록 x좌표
        controlBlock.setY(0); // 배치블록 y좌표
        canHold = true;     //홀드기능 사용가능 여부 초기화
        gameWebSocket.sendControlBlockState();
        gameWebSocket.sendGhostBlockState(ghostBlockState());
    }
    public void holdBlock() {   //블록 홀드기능
        if (!canHold) return;   //홀드 연속사용방지

        if (holdBlockType == null) {    //홀드블록이 없을때 컨트롤블록을 홀드블록으로 교체후 새 컨트롤블록 생성
            holdBlockType = controlBlock.getType();
            releaseControlBlock(controlBlock);
            createControlBlock();
        } else {                    //기존 홀드블록이 있을때, 컨트롤블록과 홀드블록 교체
            String tempType = controlBlock.getType();
            releaseControlBlock(controlBlock);
            controlBlock = BlockFactory.createBlock(holdBlockType);
            holdBlockType = tempType;

            controlBlock.setX(3);       //컨트롤블록은 다시 시작위치로 배정, 회전상태 리셋
            controlBlock.setY(0);
        }
        canHold = false; // 연속홀드 방지
        gameWebSocket.sendHoldBlockState(holdBlockType);
    }

    public GhostBlockState ghostBlockState() {  //떨어질위치 미리보기
        if (controlBlock == null) {
            return null;
        }
        Block ghostBlock = BlockFactory.createBlock(controlBlock.getType());
        ghostBlock.setX(controlBlock.getX());
        ghostBlock.setY(controlBlock.getY());
        ghostBlock.setCurrentRotation(controlBlock.getCurrentRotation());
        ghostBlock.setShape(controlBlock.getShape());

        while (moveBlock.softDrop(ghostBlock)) {
        }

        return new GhostBlockState(
                ghostBlock.getX(),
                ghostBlock.getY(),
                ghostBlock.getShape(),
                ghostBlock.getCurrentRotation()
        );
    }

    public void releaseControlBlock(Block block) {  //블록 다시 Pool로 반환
        blockPool.returnBlock(block);
    }

    private boolean checkGameOver() {
        return board.gameOverLineCheck(0);
    }


    private void stopGame() {       //게임 정지(게임오버)
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
            executorService = null;
        }
        // 여기에 게임 중단 시 필요한 추가 로직을 구현
    }

    private void updateStatus(int linesCleared) {   //클리어라인, 레벨, 드랍속도 변경 업데이트
        this.totalClearLines += linesCleared;
        this.clearLine += linesCleared;
        if (this.clearLine >= 5) {
            this.level++;           //레벨 1증가
            this.clearLine -= 5;
            this.dropInterval = (long) (this.dropInterval * 0.9); // 떨어지는속도증가
            updateDropSpeed(); // 게임루프 반영된정보로 재시작
        }

        GhostBlockState ghostBlockState = ghostBlockState();
        if (ghostBlockState != null) {
            gameWebSocket.sendGhostBlockState(ghostBlockState);
        }
    }

    private void updateDropSpeed() {    //게임루프 업데이트
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                executorService.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        startGameLoop(); //dropInterval업데이트 된 새 게임루프 시작
    }

    public void restartGame() {   //재시작기능
        stopGameLoop();
        this.bag.clear();
        this.totalClearLines = 0;
        this.clearLine = 0;
        this.level = 1;
        this.dropInterval = 1000;
        this.holdBlockType = null;
        this.controlBlock = null;
        nextBlocks.forEach(this::releaseControlBlock);
        this.nextBlocks.clear();
        this.canHold = true;
        init();
        startGameManually();
    }

    public void pauseGame() {   //일시정지 기능
        isPaused = true;
        stopGameLoop(); // 게임 루프 중단
    }

    public void resumeGame() {
        if (isPaused) {
            isPaused = false;
            startGameLoop();
        }
    }

    private void sendGameStateUpdates() {
        gameWebSocket.sendControlBlockState();
        gameWebSocket.sendBoardState();
        gameWebSocket.sendGhostBlockState(ghostBlockState());
    }

    private void gameUpdate() {
        if (controlBlock == null || !moveBlock.softDrop(controlBlock)) {
            if (controlBlock != null) {
                board.stackedBlock(controlBlock);
                int clearedLines = board.lineClear();
                updateStatus(clearedLines);
                gameStateUpdated = true;

                if (checkGameOver()) {
                    stopGame();
                    return;
                }
            }

            createControlBlock();
            gameStateUpdated = true;
        }

        ghostBlockState = ghostBlockState();
        if (ghostBlockState != null) {
            gameStateUpdated = true;
        }
        if (gameStateUpdated) {
            sendGameStateUpdates();
            gameStateUpdated = false;
        }
    }

}