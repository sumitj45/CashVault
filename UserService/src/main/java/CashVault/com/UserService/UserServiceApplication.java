package CashVault.com.UserService;

import CashVault.com.UserService.models.User;
import CashVault.com.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication

public class UserServiceApplication implements CommandLineRunner{

	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	// Check if the user already exists
	@Override
	public void run(String... args) throws Exception {
		String transactionUser = "txn-service";
		String password = "1234";

		// Check if the user already exists
		if (!userRepository.existsByUsername(transactionUser)) {
			userRepository.save(User.builder()
					.username(transactionUser)
					.password(new BCryptPasswordEncoder().encode(password))
					.authorities("svc")
					.build());
		}
	}



}
