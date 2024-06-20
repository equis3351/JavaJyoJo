package com.sparta.javajyojo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // JwtUtil 추가

    // 필터 추가

    // 시큐리티를 사용할 때 특정 URL 통과, 기능 선택
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 설정 (사용하지 않아서 disable)
        http.csrf((csrf) -> csrf.disable()); // yellow: 람다는 메소드 참조로 대체 가능

        // 기본 설정인 Session 방식에서 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정 (이게 왜 필요할까?)
                .requestMatchers("/api/**").permitAll() // "/api" 로 시작하는 요청 모두 접근 허가 (스웨거의 접근도 여기서 허용 가능) (ex 특정 권한이 있는 사용자만 접근 가능하게도 설정 가능)
                .anyRequest().authenticated() // 위의 요청 제외 모든 요청은 인증처리가 필요
            );

        // 필터 관리 (동작 순서 지정)

        return http.build();
    }
}
