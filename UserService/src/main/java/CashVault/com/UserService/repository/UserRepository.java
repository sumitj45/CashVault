package CashVault.com.UserService.repository;


import CashVault.com.UserService.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String phone);

    boolean existsByUsername(String transactionUser);
}
