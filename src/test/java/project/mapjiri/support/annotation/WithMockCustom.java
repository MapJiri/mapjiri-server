package project.mapjiri.support.annotation;

import org.springframework.security.test.context.support.WithSecurityContext;
import project.mapjiri.support.security.MockCustomUserSecurityContextFactory;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WithSecurityContext(factory = MockCustomUserSecurityContextFactory.class)
public @interface WithMockCustom {
    long id() default 1L;
    String role();
}
