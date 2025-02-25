package nextstep.security.autoconfigure;

import nextstep.security.config.SecurityFilterChain;
import nextstep.security.web.builders.HttpSecurity;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

class DefaultWebSecurityCondition extends AllNestedConditions {

    DefaultWebSecurityCondition() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnClass({ SecurityFilterChain.class, HttpSecurity.class })
    static class Classes {
    }

    @ConditionalOnMissingBean({ SecurityFilterChain.class })
    static class Beans {
    }

}
