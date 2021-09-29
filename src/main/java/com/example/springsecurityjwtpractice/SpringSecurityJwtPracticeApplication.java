package com.example.springsecurityjwtpractice;

import com.example.springsecurityjwtpractice.application.MemberService;
import com.example.springsecurityjwtpractice.domain.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringSecurityJwtPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtPracticeApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(MemberService memberService) {
		return args -> {
			memberService.save("user1", "user1@gmail.com", "11111111", Role.USER);
			memberService.save("user2", "user2@naver.com", "22222222", Role.USER);
			memberService.save("admin1", "admin1@gmail.com", "33333333", Role.ADMIN);
			memberService.save("user3", "user3@gmail.com", "44444444", Role.USER);
			memberService.save("admin2", "admin2@gmail.com", "55555555", Role.ADMIN);
		};
	}

}
