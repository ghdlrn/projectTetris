//------------------------------게임시작, 게임재시작, 일시정지 버튼---------------------------------------
var pause = false;
document.getElementById('pause-button').addEventListener('click', function() {
    pause = !pause;

    var button = document.getElementById('pause-button');
    if (pause) {
        button.textContent = '게임 재개'; // 버튼 텍스트를 '게임 재개'로 변경
        stompClient.send("/app/pause", {}, {}); // 서버에 일시정지 메시지 전송
    } else {
        button.textContent = '일시정지'; // 버튼 텍스트를 '일시정지'로 변경
        stompClient.send("/app/resume", {}, {}); // 서버에 게임 재개 메시지 전송
    }
});


function restartGame() {
    if (confirm('재시작 하시겠습니까?')) {
        stompClient.send("/app/restart", {}, {});
    }
}
document.addEventListener('DOMContentLoaded', (event) => {
    const restartButton = document.getElementById('restart-game');
    if (restartButton) {
        restartButton.addEventListener('click', restartGame);
    }
});

function startGame() {
    fetch('/start-game')
        .then(() => {
            document.getElementById('gameStartButton').style.display = 'none';
        })
        .catch(error => console.error('Error starting the game:', error));

    connect();
}

// ---------------------------미리보기, 홀드, 고스트 블록 구성-----------------------------------------------
function updateNextBlocks(blockTypes) {
    blockTypes.forEach((type, index) => {
        const blockElement = document.getElementById(`next-block-${index+1}`);
        blockElement.className = 'next-block';
        blockElement.classList.add(`block-type-${type}`);
    });
}

function updateHoldBlockDisplay(holdBlockState) {
    const holdBlockElement = document.getElementById('hold-block-display');
    if (holdBlockElement) {
        holdBlockElement.style.backgroundImage = '';
        if (holdBlockState && holdBlockState.type) {
            holdBlockElement.style.backgroundImage = `url('./css/img/${holdBlockState.type}Block.png')`;
        }
    } else {
        console.error('Hold block display element not found.');
    }
}

function updateGhostBlockDisplay(ghostBlockState) {
    gameState.ghostBlock = ghostBlockState;
}
// -----------------------------------------게임 보드 및 블록 구성 ---------------------------------
var gameState = {
    gameBoard: [],
    controlBlock: null,
    ghostBlock: null,
};

function initGameBoard() {
    gameState.gameBoard = createEmptyGameBoard(22, 10); // 세로 22칸, 가로 10칸
    renderGameBoard();
}

function createEmptyGameBoard(rows, cols) {
    return Array.from({ length: rows }, () => Array(cols).fill(false));
}

function updateGameBoard(newGameBoard) {
    gameState.gameBoard = newGameBoard;
    renderGameBoard();
}

function updateControlBlock(blockState) {
    gameState.controlBlock = blockState;
    renderGameBoard(); // 컨트롤 블록 업데이트 시 게임 보드 전체를 다시 렌더링
}

var lastRenderedState = null;

function gameLoop() {
    requestAnimationFrame(gameLoop);
    // 게임 상태가 변경되었는지 확인
    if (JSON.stringify(gameState) !== lastRenderedState) {
        renderGameBoard();
        lastRenderedState = JSON.stringify(gameState);
    }
}

function renderGameBoard() {
    const canvas = document.getElementById('gameCanvas');
    const ctx = canvas.getContext('2d');

    const cellSize = canvas.width / 10; // 캔버스 너비를 기준으로 셀 크기 계산
    // 캔버스 초기화
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    // 게임 보드 배경 및 고정된 셀 그리기
    gameState.gameBoard.forEach((row, rowIndex) => {
        row.forEach((cell, colIndex) => {
            if (cell) { // 고정된 셀 렌더링
                drawCell(ctx, colIndex, rowIndex, cellSize, '#aaaaaa', false);
            } else {          // 비어있는 셀에 대한 렌더링
                drawCell(ctx, colIndex, rowIndex, cellSize, '#ffffff', false, true); // 비어있는 셀
            }
        });
    });
    if (gameState.controlBlock) {  // controlBlock 그리기
        gameState.controlBlock.shape.forEach(([partX, partY]) => {
            drawCell(ctx, gameState.controlBlock.x + partX, gameState.controlBlock.y + partY, cellSize, getBlockColor(gameState.controlBlock.type), false);
        });
    }
    if (gameState.ghostBlock) { // 고스트 블록 렌더링
        gameState.ghostBlock.shape.forEach(([partX, partY]) => {
            drawCell(ctx, gameState.ghostBlock.x + partX, gameState.ghostBlock.y + partY, cellSize, 'rgba(128, 128, 128, 0.6)', true);
        });
    }

}


function drawCell(ctx, x, y, cellSize, fillColor, isGhost, isEmpty = false) {
    const realX = x * cellSize;
    const realY = y * cellSize;

    if (!isEmpty) { // 셀 배경 색상
        ctx.fillStyle = fillColor;
        ctx.fillRect(realX, realY, cellSize, cellSize);
        // 입체감을 위한 그림자 추가
        if (!isGhost) { // 고스트 블록에는 그림자를 적용하지 않음
            ctx.shadowColor = 'rgba(0, 0, 0, 0.4)';
            ctx.shadowBlur = 3;
            ctx.shadowOffsetX = 2;
            ctx.shadowOffsetY = 2;

            // 그림자가 적용된 배경을 다시 그림
            ctx.fillRect(realX, realY, cellSize, cellSize);

            // 다음 그림 그리기 전에 그림자 설정 초기화
            ctx.shadowColor = 'transparent';
            ctx.shadowBlur = 0;
            ctx.shadowOffsetX = 0;
            ctx.shadowOffsetY = 0;
        }
    } else {    //빈셀 처리
        ctx.fillStyle = 'rgba(0, 0, 0, 0.4)';
        ctx.shadowColor = 'rgba(0, 0, 0, 0.2)';
        ctx.shadowBlur = 5;
        ctx.shadowOffsetX = 2;
        ctx.shadowOffsetY = 2;
        ctx.fillRect(realX, realY, cellSize, cellSize);
    }
    // 셀 테두리
    ctx.strokeStyle = isGhost ? 'rgba(0, 0, 0, 0.5)' : '#ffffff';
    ctx.lineWidth = isGhost ? 1 : 2;

    if (isGhost) {    // 고스트 블록에는 점선 테두리 적용
        ctx.setLineDash([5, 5]);
    } else {       // 일반 셀에는 점선 없음
        ctx.setLineDash([]);
    }
    ctx.strokeRect(realX, realY, cellSize, cellSize);

    if (isEmpty) {  // 비어있는 셀을 위한 처리
        ctx.strokeStyle = 'rgba(0, 0, 0, 0)';
        ctx.strokeRect(realX, realY, cellSize, cellSize);
    }

    if (!isGhost && !isEmpty) {    // 명암 처리를 위한 그라데이션
        const gradient = ctx.createLinearGradient(realX, realY, realX + cellSize, realY + cellSize);
        gradient.addColorStop(0, 'rgba(255, 255, 255, 0.4)');
        gradient.addColorStop(1, 'rgba(0, 0, 0, 0.2)');
        ctx.fillStyle = gradient;
        ctx.fillRect(realX, realY, cellSize, cellSize);
    }
}

function getBlockColor(type) {
    switch (type) {
        case 'I': return 'cyan';
        case 'J': return 'blue';
        case 'L': return 'orange';
        case 'O': return 'yellow';
        case 'S': return 'green';
        case 'T': return 'purple';
        case 'Z': return 'red';
        default: return '#c0c0c0';
    }
}

// 게임 루프 시작
gameLoop();

// 게임 초기화 및 게임 보드 생성
initGameBoard();
//---------------------------웹소켓 ---------------------------------------
var stompClient = null;

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/boardState', function (boardState) {
            const state = JSON.parse(boardState.body);
            updateGameBoard(state.gameBoard);
            updateNextBlocks(state.nextBlocks);
            document.getElementById('totalClearLines').innerText = 'Line: ' + state.totalClearLines;
            document.getElementById('level').innerText = 'Level: ' + state.level;
        });
        stompClient.subscribe('/topic/controlBlockState', function (controlBlockState) {
            updateControlBlock(JSON.parse(controlBlockState.body));
        });
        stompClient.subscribe('/topic/holdBlockState', function(message) {
            var holdBlockState = JSON.parse(message.body);
            updateHoldBlockDisplay(holdBlockState);
        });
        stompClient.subscribe('/topic/ghostBlock', function (ghostBlockState) {
            updateGhostBlockDisplay(JSON.parse(ghostBlockState.body));
        });
        stompClient.subscribe('/topic/restartGame', function (message) {
            initGameBoard();
        });
    });
}

document.addEventListener('keydown', function(event) {
    if (pause) return;
    const key = event.key.toLowerCase();
    switch(event.key) {
        case 'ArrowLeft': stompClient.send("/app/moveLeft", {}, {}); break;
        case 'ArrowRight': stompClient.send("/app/moveRight", {}, {}); break;
        case 'ArrowUp': stompClient.send("/app/rotation", {}, {}); break;
        case 'ArrowDown': stompClient.send("/app/softDrop", {}, {}); break;
        case ' ': stompClient.send("/app/hardDrop", {}, {}); break;
        case 'z':stompClient.send("/app/reverseRotation", {}, {}); break;
        case 'Z':stompClient.send("/app/reverseRotation", {}, {}); break;
        case 'c':stompClient.send("/app/hold", {}, {}); break;
        case 'C':stompClient.send("/app/hold", {}, {}); break;
    }
});