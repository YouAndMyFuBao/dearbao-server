package com.fubao.dearbao.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import com.fubao.dearbao.domain.member.MemberRole;
import com.fubao.dearbao.global.config.security.jwt.JwtAuthenticationCheckFilter;
import com.fubao.dearbao.global.config.security.jwt.JwtAuthenticationEntryPoint;
import com.fubao.dearbao.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] GET_AUTHENTICATED_URLS = {
    };
    private final String[] GUEST_URLS = {
        "/api/v1/auth/init"
    };
    private final String[] GET_MEMBER_URLS = {
        "/api/v1/mission/**"
    };
    private final String[] POST_MEMBER_URLS = {
    };
    private final String[] GET_PERMITTED_URLS = {
        "/api/v1/auth/kakao/code","/","/docs/index.html"
    };
    private final String[] POST_PERMITTED_URLS = {
        "/api/v1/auth/kakao", "/api/v1/auth/token/refresh",
    };
    @Value("${security.cors-urls}")
    private final List<String> CORS_URLS;

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http, JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter,
        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
        http
            .cors(
                httpSecurityCorsConfigurer -> corsConfigurationSource())//CORS 허용 정책(Front , Back 사이에 도메인이 달라지는 경우)
            .csrf(
                AbstractHttpConfigurer::disable) //서버에 인증정보를 저장하지 않기 때문에 굳이 불필요한 csrf 코드들을 작성할 필요가 없다.
            .sessionManagement(
                httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                    .sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)) //스프링시큐리티가 생성하지도않고 기존것을 사용하지도 않음 ->JWT 같은토큰방식을 쓸때 사용하는 설정
            .formLogin(AbstractHttpConfigurer::disable)// 서버에서  View를 배포하지 안으므로 disable
            .httpBasic(AbstractHttpConfigurer::disable) // JWT 인증 방식을 사용하기에 httpBasic을 이용한 인증방식 사용X
            .addFilterAfter(jwtAuthenticationCheckFilter,
                UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(config -> config
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                //인증 중 예외 발생 시 jwtAuthenticationEntryPoint 호출
            )
            .authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.GET, GUEST_URLS).hasRole(MemberRole.ROLE_GUEST.getName())
                .requestMatchers(HttpMethod.GET, GET_MEMBER_URLS).hasRole(MemberRole.ROLE_MEMBER.getName())
                .requestMatchers(HttpMethod.POST, POST_MEMBER_URLS).hasRole(MemberRole.ROLE_MEMBER.getName())
                .requestMatchers(HttpMethod.GET, GET_PERMITTED_URLS).permitAll()
                .requestMatchers(HttpMethod.POST, POST_PERMITTED_URLS).permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }

    // CORS 허용 적용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(CORS_URLS);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(ApplicationContext context)
        throws Exception {
        AuthenticationManagerFactoryBean authenticationManagerFactoryBean = new AuthenticationManagerFactoryBean();
        authenticationManagerFactoryBean.setBeanFactory(context);
        return authenticationManagerFactoryBean.getObject();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter(
        JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationCheckFilter(jwtTokenProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
