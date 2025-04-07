package com.example.oauth.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder makePassword(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain myFilter(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // csrf비활성화 <-- 찾아보기
                .httpBasic(AbstractHttpConfigurer::disable) // Basic인증 비활성화
                                                            // Basic인증은 사용자이름과 비밀번호를 Base64로 인코딩하여 인증값으로 활용
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션방식을 비활성화
                .authorizeHttpRequests(a->a.requestMatchers("/member/create", "/member/login").permitAll().anyRequest().authenticated())
                // 특정 url패턴에 대해서는 인증처리 제외, Authentication 객체생성 제외
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 추가 필요하면 추가하면 된다.
        configuration.setAllowedMethods(Arrays.asList("*")); // 모든 HTTP메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // 자격증명허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 url 패턴에 대해서 cors 허용 설정
                                                                       // ** 디렉터리까지 포함 되는 것
        return source;
    }
}
