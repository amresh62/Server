package com.kamjritztex.aoms.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kamjritztex.aoms.components.JwtUtil;
import com.kamjritztex.aoms.models.Employee;
import com.kamjritztex.aoms.models.User;
import com.kamjritztex.aoms.services.impl.CustomUserDetailsServiceImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    public LoginController(
            AuthenticationManager authenticationManager,
            CustomUserDetailsServiceImpl userDetailsService,
            JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Employee userInfo, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            log.info("Authenticating user: {}", userInfo.getEmail());
            UserDetails user = userDetailsService.loadUserByUsername(userInfo.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userInfo.getEmail(), userInfo.getPassword()));
            log.info("Authentication : ", authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (authentication.isAuthenticated()) {
                return generateAuthResponse(user, request, response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed");
            }
        } catch (AuthenticationException e) {
            log.error("Authentication error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    private ResponseEntity<?> generateAuthResponse(UserDetails username, HttpServletRequest request,
            HttpServletResponse response) {

        String token = jwtUtil.generateToken(username);
        // CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
        // csrfTokenRepository.saveToken(csrfToken, request, response);

        addCookie(response, "JWT-TOKEN", token);
        // addCookie(response, "XSRF-TOKEN", csrfToken.getToken());
        User user = new User();
        user.setUsername(username.getUsername());
        user.setEmail(username.getUsername());
        user.setRole(username.getAuthorities().iterator().next().getAuthority());
        return ResponseEntity.ok(user);
    }

    private void addCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(3600); // 1 hour
        response.addHeader("Set-Cookie",
                String.format("%s=%s; Path=/; HttpOnly; Secure; SameSite=Strict", name, value));
    }

    @GetMapping("/check-auth")
public ResponseEntity<?> checkAuthentication(HttpServletRequest request, HttpServletResponse response) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken);

    if (isAuthenticated && authentication != null) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        User user = new User();
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getUsername());
        user.setRole(role);
        return ResponseEntity.ok(user);
    } else {
        return ResponseEntity.ok().build(); // Return empty response for unauthenticated users
    }
}
    @PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
    // Clear the security context
    SecurityContextHolder.clearContext();

    // Remove all cookies
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    // Invalidate the session if it exists
    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate();
    }

    // Return a success response
    return ResponseEntity.ok().body("Logged out successfully");
}

}
