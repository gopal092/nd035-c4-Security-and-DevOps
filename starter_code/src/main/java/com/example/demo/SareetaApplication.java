package com.example.demo;

import com.splunk.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@EnableJpaRepositories("com.example.demo.model.persistence.repositories")
@EntityScan("com.example.demo.model.persistence")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SareetaApplication {
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	public static Receiver getSplunkService() {
		HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
		Map<String, Object> connectionArgs = new HashMap<>();
		connectionArgs.put("host", "127.0.0.1");
		connectionArgs.put("port", 8089);
		connectionArgs.put("scheme", "https");
		connectionArgs.put("username", "gopal1991");
		connectionArgs.put("password", "G0p@l!!00!");
		Service splunkService = Service.connect(connectionArgs);
		return splunkService.getReceiver();
	}
	public static void main(String[] args) throws IOException{
		SpringApplication.run(SareetaApplication.class, args);
	}
}
