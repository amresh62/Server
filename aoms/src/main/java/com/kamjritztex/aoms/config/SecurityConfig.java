// package com.kamjritztex.aoms.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import com.kamjritztex.aoms.repositories.EmployeeRepository;
// import com.kamjritztex.aoms.security.JwtAuthenticationFilter;
// import com.kamjritztex.aoms.services.impl.CustomUserDetailsServiceImpl;

// @Configuration
// public class SecurityConfig {

//     private final CustomUserDetailsServiceImpl customUserDetailsService;
//     private final JwtAuthenticationFilter jwtAuthenticationFilter;

//     public SecurityConfig(CustomUserDetailsServiceImpl customUserDetailsService,
//             JwtAuthenticationFilter jwtAuthenticationFilter) {
//         this.customUserDetailsService = customUserDetailsService;
//         this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//     }

//     @Bean
//     public CustomUserDetailsServiceImpl customUserDetailsService(EmployeeRepository userRepository) {
//         return new CustomUserDetailsServiceImpl(userRepository);
//     }   

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .csrf(csrf -> csrf.disable())
//                 .authorizeHttpRequests(authorize -> authorize
//                         .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                         .requestMatchers("/api/superadmin/**").hasRole("SUPER_ADMIN")
//                         .requestMatchers("/api/offboarding/**").hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")
//                         .anyRequest().authenticated())
//                 .formLogin(form -> form
//                         .loginPage("/login")
//                         .permitAll())
//                 .logout(logout -> logout
//                         .logoutUrl("/logout")
//                         .permitAll())
//                 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
//             throws Exception {
//         return authenticationConfiguration.getAuthenticationManager();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public UserDetailsService userDetailsService() {
//         return customUserDetailsService;
//     }
// }
