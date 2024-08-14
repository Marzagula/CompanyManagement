package com.gminds.auth_server;

import com.gminds.auth_server.config.RsaKeyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class AuthServerApplication {


	static
	PasswordEncoder encoder = new BCryptPasswordEncoder();
	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
		System.out.println(encoder.encode("password"));
	}

}
