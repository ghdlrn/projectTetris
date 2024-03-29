package tetris.projectt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class firstpageController {
        @GetMapping("/login.html")
        public String loginPage() {
            return "login"; // resources/templates/login.html을 가리킵니다.
        }

}
