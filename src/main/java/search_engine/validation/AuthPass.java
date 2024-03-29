    package search_engine.validation;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import search_engine.settings.SearchSettings;

    @Configuration
    @EnableWebSecurity
    public class AuthPass extends WebSecurityConfigurerAdapter {

        private final SearchSettings settings;
        public AuthPass(SearchSettings settings) {

            this.settings = settings;
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

            auth.inMemoryAuthentication()
                    .withUser(settings.getWebinterfaceLogin()).password(passwordEncoder().encode(settings.getWebinterfacePassword()))
                    .authorities("ADMIN");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/test").permitAll()
                    .antMatchers("/**").authenticated()
                    .and()
                    .httpBasic();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {

            return new BCryptPasswordEncoder();
        }
    }
