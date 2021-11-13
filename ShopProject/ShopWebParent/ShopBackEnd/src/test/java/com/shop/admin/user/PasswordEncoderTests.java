package com.shop.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTests {
	@Test
	public void testEncodePassword() {
		BCryptPasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();
		String rawPassword = "dat2021";
		String encodedPassword = passwordEncoder.encode(rawPassword);
		
		System.out.println(encodedPassword);
		
		boolean matches =passwordEncoder.matches(rawPassword, encodedPassword);
		assertThat(matches).isTrue();
	}
	
}
