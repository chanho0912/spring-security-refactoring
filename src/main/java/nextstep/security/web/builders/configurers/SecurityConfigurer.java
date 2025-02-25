package nextstep.security.web.builders.configurers;

import nextstep.security.web.builders.HttpSecurity;

public interface SecurityConfigurer {

    void init(HttpSecurity http);

    void configure(HttpSecurity http);
}
