package tetris.projectt.repository;

import tetris.projectt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//    User findByUserid(String userid);   // 사용자 ID로 사용자 찾기
    Optional<User> findByUserid(String userid);
}
