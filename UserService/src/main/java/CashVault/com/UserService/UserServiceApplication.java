package CashVault.com.UserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		String transactionUser = "txn-service";
//		String password = "1234";
//		userRepository.save(User.builder()
//				.username(transactionUser)
//				.password(new BCryptPasswordEncoder().encode(password))
//				.authorities("svc")
//				.build());

	}

}
