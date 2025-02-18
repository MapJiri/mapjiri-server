package project.mapjiri.domain.user.model;

import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@Builder
@AllArgsConstructor(access = PUBLIC)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, updatable = false)
    private String kakaoId;

    @Column(nullable = true, unique = true, updatable = false) //이메일 로그인 사용자는 필수, 카카오 로그인 사용자는 선택
    private String email;

    @Column(nullable = true)
    private String password = "";

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
        this.password = ""; // 빈 문자열로 설정해 NULL방지
    }

    public void updateKakaoInfo(String kakaoId, String profileImage) {
        this.kakaoId = kakaoId;
        this.profileImage = profileImage;
    }

}