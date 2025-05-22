package com.example.demo.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.example.demo.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	private JwtRequestFilter jwtRequestFilter;

	public WebSecurityConfig(JwtRequestFilter jwtRequestFilter) {
		this.jwtRequestFilter = jwtRequestFilter;
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider(MyUserDetailsService myUserDetailsService) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
		return daoAuthenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(request -> {
			CorsConfiguration corsConfig = new CorsConfiguration();
			corsConfig.setAllowedOrigins(Arrays.asList(EndPoint.ALLOWED_ORIGINS));
			corsConfig.setAllowedMethods(Arrays.asList(EndPoint.ALLOWED_METHODS));
			corsConfig.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
			corsConfig.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
			corsConfig.setAllowCredentials(true);
			corsConfig.setMaxAge(3600L);
			return corsConfig;
		}));
		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll())
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
				
		http.formLogin(formLogin -> formLogin.disable());
		http.httpBasic(httpBasic -> httpBasic.disable());
		http.csrf(csrf -> csrf.disable());
		http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}
}
