package tetris.projectt.controller;

import tetris.projectt.model.User;
import tetris.projectt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String signupPage(){
        return "signuppage";
    }

    @PostMapping("/signup")
    public String registerUser(User user){
        userRepository.save(user);  // 사용자 정보 저장
        return "redirect:/login";   // 로그인 페이지로 리디렉션
    }
}
