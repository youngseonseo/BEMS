package energypa.bems.login.config;


import energypa.bems.login.config.security.handler.CustomSimpleUrlAuthenticationFailureHandler;
import energypa.bems.login.config.security.handler.CustomSimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import energypa.bems.login.config.security.token.CustomOncePerRequestFilter;
import energypa.bems.login.repository.CustomAuthorizationRequestRepository;
import energypa.bems.login.service.CustomDefaultOAuth2UserService;
import energypa.bems.login.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig {
    AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomDefaultOAuth2UserService customOAuth2UserService;
    private final CustomSimpleUrlAuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CustomSimpleUrlAuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    @Bean
    public CustomOncePerRequestFilter customOncePerRequestFilter() {
        return new CustomOncePerRequestFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
    @Bean
//    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService);
        authenticationManager = authenticationManagerBuilder.build();
        http.cors(withDefaults());
        http
                .cors()
                .and()
                .authenticationManager(authenticationManager)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/api/movie/**", "/api/genre/**", "/api/comment/**", "/api/credit/**")
                .permitAll()
                .antMatchers("/api/swagger-ui/**", "/api/docs/**")
                .permitAll()
                .antMatchers("/api/login/**", "/api/auth/**", "/api/oauth2/**")
                .permitAll()
                .antMatchers("/api/community/**", "/api/heart/**", "/api/posts/**", "/api/subcomment/**", "/api/auth/**", "/api/posts/**", "/api/postByMovie/**", "/api/postByMember/**", "/api/posts/read/**", "/api/posts/search/**", "/api/tv/**", "/api/gallery/**")
                .permitAll()
                .antMatchers("/api/blog/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()


                .oauth2Login()

                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(customAuthorizationRequestRepository)
                .and()
//                    .redirectionEndpoint()
//                        .baseUri("/oauth2/callback/*")
//                        .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        http.addFilterBefore(customOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)               // 사이트 위변조 요청 방지
                .authorizeHttpRequests((authorizeRequests) -> {      // 특정 URL에 대한 권한 설정.
                    authorizeRequests.requestMatchers("/user/**").authenticated();    // /user/**의 주소가 들어오면 인증이 필요함
                    authorizeRequests.requestMatchers("/manager/**")
                            .hasAnyRole("ADMIN", "MANAGER");   // ROLE_은 붙이면 안 된다. hasAnyRole()을 사용할 때 자동으로 ROLE_이 붙기 때문이다.
                    authorizeRequests.requestMatchers("/admin/**")
                            .hasRole("ADMIN");                       // ROLE_은 붙이면 안 된다. hasRole()을 사용할 때 자동으로 ROLE_이 붙기 때문이다.
                    authorizeRequests.anyRequest().permitAll();
                })

                .formLogin((formLogin) -> {
                    formLogin.loginPage("/login");        /* 권한이 필요한 요청은 해당 url로 리다이렉트 */
                })
                .build();
    }

}