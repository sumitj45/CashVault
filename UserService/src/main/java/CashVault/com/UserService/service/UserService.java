package CashVault.com.UserService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.simple.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import CashVault.com.UserService.models.User;
import CashVault.com.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper objectMapper =new ObjectMapper();

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
       return userRepository.findByUsername(username);
   }

    public void create(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setAuthorities("usr");
    userRepository.save(user);
    System.out.println(user.getPassword());

    JSONObject userJsonObject = new JSONObject();
    userJsonObject.put("phone", user.getUsername());
    userJsonObject.put("email", user.getEmail());

        // publish an event to kafka with topic -> user_created
        // TODO - move to constants
        try {
            kafkaTemplate.send("user_created",
                    objectMapper.writeValueAsString(userJsonObject));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User getById(Integer userId) {
       return userRepository.findById(userId)
               .orElseThrow(()->new BadCredentialsException(""));
    }


}
