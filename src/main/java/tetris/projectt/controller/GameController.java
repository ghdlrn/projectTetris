package tetris.projectt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tetris.projectt.service.GameService;

@Controller     //웹 요청을 처리하는 컨트롤러를 나타냄(클라이언트의 요청에 대응하여 적절한 뷰를 반환
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")      // "/index'루트에 대해 get요청 처리, 사용자가 홈페이지 요청시 이 메서드가 호출됨
    public String index() {
        // 게임 페이지를 반환합니다.
        return "forward:/index.html";        //서버 내부에서 해당경로로 요청을 포워드
    }

    @GetMapping("/start-game")
    public ResponseEntity<Void> startGame() {
        gameService.startGameManually();
        return ResponseEntity.ok().build();
    }
}