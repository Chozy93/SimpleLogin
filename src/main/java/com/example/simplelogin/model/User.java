package com.example.simplelogin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * packageName : com.example.simplelogin.model
 * fileName : User
 * author : Chozy93
 * date : 22-11-28(028)
 * description :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 22-11-28(028)         Chozy93          최초 생성
 */
@Entity
@SequenceGenerator(
        name = "SQ_USER_GENERATOR"
        ,sequenceName = "SQ_USER"
        ,initialValue = 1
        ,allocationSize = 1
)
@Table(name="TB_USER"
    ,uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            ,generator="SQ_USER_GENERATOR"
    )
    @Column
    private Long id;

//    유효성 체크 : validation 라이브러리
//          대상 : 컨트롤러 매개변수로 들어오는 변수값에 대해서 체크함
    @NotBlank
    @Size(max=20)
    private String username;

    @NotBlank
    @Size(max=50)
    private String email;

    @NotBlank
    @Size(max=120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_USER_ROLE"
                ,joinColumns = @JoinColumn(name = "USER_ID"),
                inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<Role> roles = new HashSet<>();


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
