package com.loafer.blog.config;

import com.loafer.blog.model.security.LoaferUser;
import com.loafer.blog.model.vo.UserVO;
import com.loafer.blog.utils.JwtUtils;
import com.loafer.blog.utils.TokenCache;
import io.jsonwebtoken.lang.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            // 解析用户信息
            UserVO userVO = jwtUtils.getUserIdAndRolesFromToken(token);
            List<String> roleList = userVO.getRoles();
            String[] roles = new String[0];
            if (!Collections.isEmpty(roleList)) {
                roles = new String[roleList.size()];
                for (int i = 0; i < roleList.size(); i++) {
                    roles[i] = roleList.get(i).toUpperCase();
                }
            }

            // 构建用户详情
            UserDetails userDetails = LoaferUser.builder()
                    .userId(userVO.getId())
                    .username(userVO.getUsername())
                    .password("")
                    .roles(roles)
                    .build();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 将userId设置为请求属性
            request.setAttribute("userId", userVO.getId());
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