package com.serkan.lottofun.ticketservice.jwt;

import com.serkan.lottofun.ticketservice.exception.BaseException;
import com.serkan.lottofun.ticketservice.exception.ErrorMessage;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header ;
        String token;
        String username;

        header = request.getHeader("Authorization");
        if (header == null  || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        token = header.substring(7);
        System.out.println("token: "+token);
        try {

            username = jwtService.getUsernameFromToken(token);
            System.out.println("username: " +username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("USERNAME: " + username);
                System.out.println("VALID BEFORE CHECK: " + jwtService.validateToken(token));
                if (!jwtService.validateToken(token)) {
                    System.out.println("validate ic: " +jwtService.validateToken(token));
                    filterChain.doFilter(request, response);
                    return;
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("AUTH SET: " +
                        SecurityContextHolder.getContext().getAuthentication());
                System.out.println("AUTH AFTER: " + SecurityContextHolder.getContext().getAuthentication());

            }

        } catch (ExpiredJwtException e) {
            throw new BaseException(ErrorMessage.TOKEN_EXPIRED, e.getMessage());
        } catch (Exception e) {
            throw new BaseException(ErrorMessage.INVALID_TOKEN, e.getMessage());
        }

        filterChain.doFilter(request, response);
//        try {
//            username = jwtService.getUsernameFromToken(token);
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                if(userDetails != null && jwtService.isTokenExpired(token)) {
////                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    authentication.setDetails(userDetails);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
//        }catch (ExpiredJwtException e){
//            throw new BaseException(ErrorMessage.TOKEN_EXPIRED,e.getMessage());
//        }catch (Exception e){
//            throw new BaseException(ErrorMessage.INVALID_TOKEN,e.getMessage());
//        }
//        filterChain.doFilter(request, response);
    }
}
