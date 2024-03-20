package com.inha.endgame.config;

import com.inha.endgame.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@ComponentScan("com.inha.endgame")
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() //
				.authorizeRequests().antMatchers("/**").permitAll().and()//.access("hasRole('ROLE_USER')").and()
				.httpBasic();
	}
}
