package tetris.projectt.service;

import tetris.projectt.model.User;
import tetris.projectt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean authenicate(String userid, String password){
        Optional<User> userOptional = userRepository.findByUserid(userid);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().equals(password);
        }
        return false;
    }

    public User registerNewUser(User user){
        // 아이디 중복 체크
        if(userRepository.findByUserid(user.getUserid()).isPresent()){
            throw new DataIntegrityViolationException("입력하신 ID : " + user.getUserid() + " 은(는) 이미 존재합니다.");
        }
        return userRepository.save(user);
    }

}
