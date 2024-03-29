package tetris.projectt.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tetris.projectt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.http.HttpRequest;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String userid, @RequestParam String password, Model model, RedirectAttributes redirectAttributes){
        if(userService.authenicate(userid, password)){
            // 트리거??
            // 사용자가 입력한 정보(username)을 넘겨준다.-> (attributeValue에 대입)

            redirectAttributes.addAttribute("username", userid);
            return "redirect:/"; // 로그인 성공 시 "index" 뷰로 리디렉트
        }
        else {
            model.addAttribute("error", "잘못된 아이디, 비밀번호 입니다.");
            return "login"; // 로그인 실패 시 다시 "login" 뷰로 리디렉트
        }
    }

    @GetMapping("/logout")
    public String logoutPage(){
        return "login";
    }

//    @GetMapping("/signup")
//    public String signinPage(){
//        return "signuppage";
//    }

}
