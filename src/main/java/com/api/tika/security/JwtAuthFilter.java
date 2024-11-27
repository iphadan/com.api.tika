package com.api.tika.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImp;
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
       String authHeader= request.getHeader(HttpHeaders.AUTHORIZATION);
       if(request.getServletPath().contains("/api/pdfbox/auth")){
           filterChain.doFilter(request,response);
           return;
       }

       if(authHeader == null || !authHeader.startsWith("Bearer ")){
           filterChain.doFilter(request,response);
           return;
       }
       String jwt=authHeader.substring(7);
       String userEmail = jwtService.extractUserName(jwt);
        System.out.println(userEmail);
       if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
           UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(userEmail);
           if(jwtService.isValidToken(jwt,userDetails)){
           // required later to update security holder context
               UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                       userDetails,null,userDetails.getAuthorities());
               authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authToken);


           }

       }
       filterChain.doFilter(request,response);
    }
}
