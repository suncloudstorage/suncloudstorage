package com.suncloudstorage;

import com.suncloudstorage.model.Role;
import com.suncloudstorage.model.Status;
import com.suncloudstorage.model.User;
import com.suncloudstorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SunCloudStorageApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SunCloudStorageApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = User.builder()
				.id("1")
				.username("sunadmin")
				.email("sunadmin@gmail.com")
				.firstName("sun")
				.lastName("admin")
				.password("$2y$12$dkVBoYx4ueEE50V8jHHRQuxqt3W7eacge7UZgIBbE7AI4vKEDF7m6")
				.role(Role.ADMIN)
				.status(Status.ACTIVE)
				.build();
		userRepository.save(user);

	}
}
