package project.mapjiri.domain.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, updatable = false)
    private String kakaoId;

    @Column(nullable = true, unique = true, updatable = false) //이메일 로그인 사용자는 필수, 카카오 로그인 사용자는 선택
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(nullable = true)
    private String profileImage;

    public User(String email, String encodedPassword, String username) {
        this.email = email;
        this.password = encodedPassword;
        this.username = username;
        this.role = Role.USER;
        this.loginType = LoginType.JWT;
        this.profileImage = null;
    }

    // 카카오 로그인 사용자 (이메일 남겨놓을지 보류)
    public User(String kakaoId, String email, String username, String profileImage) {
        this.kakaoId = kakaoId;
        this.email = email;
        this.username = username;
        this.profileImage = profileImage;
        this.role = Role.USER;
        this.loginType = LoginType.KAKAO;
    }

    public void updateKakaoInfo(String kakaoId, String profileImage) {
        this.kakaoId = kakaoId;
        this.profileImage = profileImage;
    }

}