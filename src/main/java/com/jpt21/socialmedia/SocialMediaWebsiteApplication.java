package com.jpt21.socialmedia;

import com.jpt21.socialmedia.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class SocialMediaWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaWebsiteApplication.class, args);
	}

}
