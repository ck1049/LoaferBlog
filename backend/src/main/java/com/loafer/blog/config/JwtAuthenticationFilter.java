package com.loafer.blog.config;

import com.loafer.blog.utils.JwtUtils;
import com.loafer.blog.utils.TokenCache;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final TokenCache tokenCache;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, TokenCache tokenCache) {
        this.jwtUtils = jwtUtils;
        this.tokenCache = tokenCache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        if (token != null && jwtUtils.validateToken(token) && !tokenCache.containsToken(token)) {
            Long userId = jwtUtils.getUserIdFromToken(token);
            // 这里需要实现UserDetailsService来加载用户信息
            // UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());
            // 暂时使用一个简单的实现 - 假设所有登录用户都是管理员
            // TODO: 实现真正的角色管理
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(userId.toString())
                    .password("")
                    .authorities("ROLE_USER", "ROLE_ADMIN")
                    .build();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 将userId设置为请求属性
            request.setAttribute("userId", userId);
        }

        chain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}