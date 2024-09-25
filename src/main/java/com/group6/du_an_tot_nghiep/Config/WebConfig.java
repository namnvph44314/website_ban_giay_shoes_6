package com.group6.du_an_tot_nghiep.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class WebConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf -> csrf.ignoringRequestMatchers("/login")
                .ignoringRequestMatchers("/kh/**")
                .ignoringRequestMatchers("/api/**")
                .ignoringRequestMatchers("/register-user")
                .ignoringRequestMatchers("/api/re-change-password")
                .ignoringRequestMatchers("/api/send-code-verify")
                .ignoringRequestMatchers("/bill/add-product")
                .ignoringRequestMatchers("/select-address/**")
                .ignoringRequestMatchers("/bill/delete-product")
                .ignoringRequestMatchers("/final-checkout/**")
                .ignoringRequestMatchers("/pay-history")
                .ignoringRequestMatchers("/admin/**")
                .ignoringRequestMatchers("/edit-amount/**")
                .ignoringRequestMatchers("/delete-history-pay/**")))
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/uploads/**").permitAll()
//                                .requestMatchers("/khach-hang/gio-hang/index").permitAll()
                                .requestMatchers("/khach-hang/**").hasAuthority("ROLE_Khach_Hang")
                                .requestMatchers("/nhan-vien/**").hasAuthority("ROLE_Nhan_Vien")
                                .requestMatchers("/api/**").permitAll()
                                .requestMatchers("/hdct/index/**").hasAnyAuthority("ROLE_Nhan_Vien", "ROLE_ADMIN")
                                .requestMatchers("/hoa-don/hien-thi").hasAnyAuthority("ROLE_Nhan_Vien", "ROLE_ADMIN")
                                .requestMatchers("/tai-khoan-quan-ly/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_Nhan_Vien")
//                                .requestMatchers("/khach-hang-san-pham/index/").permitAll()
                                .requestMatchers("/list-san-pham/**").permitAll()
                                .requestMatchers("/khach-hang-san-pham/index/**").permitAll()
                                .requestMatchers("/trang-chu/index").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/dang-ky").permitAll()
                                .requestMatchers("/quen-mat-khau").permitAll()
                                .requestMatchers("/dat-lai-mat-khau").permitAll()
                                .requestMatchers("/ban-hang-tai-quay").hasAnyAuthority("ROLE_Nhan_Vien", "ROLE_ADMIN")

                                .anyRequest().permitAll()
                )
                .formLogin(loginConfigurer -> loginConfigurer.loginPage("/login")
                        .defaultSuccessUrl("/trang-chu/index", true)
                        .failureUrl("/login?error=true").permitAll())
                .logout(logoutConfigurer -> logoutConfigurer.logoutSuccessUrl("/user/dang-nhap?logout=true")
                        .invalidateHttpSession(true).permitAll())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
