// Block 관리 인터페이스
public interface BlockManager {
Block generateNewBlock();
void moveLeft();
void moveRight();
void rotate();
void drop();
boolean down();
}

// 게임 보드 관리 인터페이스
public interface BoardManager {
Boolean[][] getBoard();
void clearRow(int row);
boolean isRowFilled(int row);
boolean isBlockPresentInRow(int row);
}

// 게임 서비스 인터페이스
public interface GameService {
void startGame();
void pauseGame();
void resumeGame();
void endGame();
}

// 게임 서비스 구현 클래스
@Service
public class GameServiceImpl implements GameService {
private final BlockManager blockManager;
private final BoardManager boardManager;

    public GameServiceImpl(BlockManager blockManager, BoardManager boardManager) {
        this.blockManager = blockManager;
        this.boardManager = boardManager;
    }

    @Override
    public void startGame() {
        // 게임 시작 로직
    }

    @Override
    public void pauseGame() {
        // 게임 일시 정지 로직
    }

    @Override
    public void resumeGame() {
        // 게임 재개 로직
    }

    @Override
    public void endGame() {
        // 게임 종료 로직
    }
}

// Block 관리 클래스 구현
@Component
public class BlockManagerImpl implements BlockManager {
// 구현 내용 생략
}

// 게임 보드 관리 클래스 구현
@Component
public class BoardManagerImpl implements BoardManager {
// 구현 내용 생략
}