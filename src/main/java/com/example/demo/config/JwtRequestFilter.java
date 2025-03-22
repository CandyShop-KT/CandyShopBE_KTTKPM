package com.example.demo.config;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.exception.AuthenticationException;
import com.example.demo.service.MyUserDetailsService;
import com.example.demo.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private MyUserDetailsService myUserDetailsService;
	private JwtUtil jwtUtil;

	public JwtRequestFilter(MyUserDetailsService userDetailsService, JwtUtil jwtUtil) {
		this.myUserDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			
			String requestURI = request.getRequestURI();
	        String requestMethod = request.getMethod();

	        if (shouldBypassFilter(requestURI, requestMethod)) {
	            filterChain.doFilter(request, response);
	            return;
	        }

			final String authorizationHeader = request.getHeader("Authorization");

			String username = null;
			String jwt = null;

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				username = jwtUtil.extractUsername(jwt);
			} else {
				throw new AuthenticationException("Invalid Token or Token expired");
			}

			if (username != null) {
				UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
				if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				} else {
					throw new AuthenticationException("Invalid Token or Token expired");
				}
			} else {
				throw new AuthenticationException("Invalid Token or Token expired");
			}
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");

			Map<String, Object> errors = new LinkedHashMap<String, Object>();
			errors.put("status", HttpServletResponse.SC_UNAUTHORIZED);
			errors.put("message", "Unauthorized access");

			response.getWriter().write(new ObjectMapper().writeValueAsString(errors));
		}
	}
	
	private boolean shouldBypassFilter(String requestURI, String requestMethod) {
		
		PathMatcher pathMatcher = new AntPathMatcher();
		
		if(requestMethod.equals("POST")) {
			for (String endpoint : EndPoint.PUBLIC_METHODS_POST) {
				if (pathMatcher.match(endpoint, requestURI) && requestMethod.equals("POST")) return true;
			}
		}
		
		if(requestMethod.equals("GET")) {
			for (String endpoint : EndPoint.PUBLIC_METHODS_GET) {
				if (pathMatcher.match(endpoint, requestURI) && requestMethod.equals("GET")) return true;
			}
		}
		return false;
	}

}
