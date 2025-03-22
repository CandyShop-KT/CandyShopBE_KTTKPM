package com.example.demo.service.imp;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MyUserDetailsService;

@Service
public class MyUserDetailsServiceImp implements MyUserDetailsService {

	private final UserRepository userRepository;

	public MyUserDetailsServiceImp(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().toString());
		
		UserDetails userDetails = org.springframework.security.core.userdetails.User
				.withUsername(user.getUserName())
				.password(user.getPassword()).authorities(authority)
				.build();

		return userDetails;
	}

}
