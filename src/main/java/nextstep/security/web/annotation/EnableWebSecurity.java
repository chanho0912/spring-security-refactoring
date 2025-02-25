package nextstep.security.web.annotation;

import nextstep.security.autoconfigure.HttpSecurityConfiguration;
import nextstep.security.autoconfigure.WebSecurityConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({HttpSecurityConfiguration.class, WebSecurityConfiguration.class})
public @interface EnableWebSecurity {
}
