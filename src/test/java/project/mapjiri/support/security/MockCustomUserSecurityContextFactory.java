package project.mapjiri.support.security;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import project.mapjiri.domain.user.model.User;
import project.mapjiri.support.annotation.WithMockCustom;
import project.mapjiri.support.fixture.UserFixture;


import java.util.List;

public class MockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustom> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustom customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User mockUser = UserFixture.USER_FIXTURE_1.createUser();

        context.setAuthentication(new UsernamePasswordAuthenticationToken(mockUser,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + customUser.role()))));

        return context;
    }
}
