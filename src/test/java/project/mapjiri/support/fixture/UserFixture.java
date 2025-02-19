package project.mapjiri.support.fixture;

import lombok.Getter;
import project.mapjiri.domain.user.model.User;

@Getter
public enum UserFixture {
    USER_FIXTURE_1("test@gmail.com", "password1234@", "홍길동"),
    USER_FIXTURE_2("test2@gmail.com", "password1234@", "존도"),
    USER_FIXTURE_3("test3@gmail.com", "password1234@", "제인도");

    private final String email;
    private final String password;
    private final String username;

    UserFixture(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public User createUser() {
        return new User(email, password, username);
    }
}
