package tetris.projectt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Member")
public class User {

    @Id
    private String userid;

    private String password;

    @Column(name = "username")
    private String username;
}
