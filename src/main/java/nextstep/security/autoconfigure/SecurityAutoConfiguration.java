package nextstep.security.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(SpringBootWebSecurityConfiguration.class)
public class SecurityAutoConfiguration {
}
