package CashVault.com.UserService.controller;

import CashVault.com.UserService.models.User;
import CashVault.com.UserService.dto.CreateUserRequest;
import CashVault.com.UserService.dto.GetUserResponse;
import CashVault.com.UserService.service.UserService;

import CashVault.com.UserService.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

// Sign-Up
    @PostMapping("/create")
    public void createUser(@RequestBody @Valid CreateUserRequest createUserRequest){
     userService.create(Utils.convertUserCreateRequest(createUserRequest));
    }


//    Profile Information

    @GetMapping("/profile-info")
    public GetUserResponse getProfile(){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user=userService.getById(user.getId());
        return Utils.convertToGetUserResponse(user);
    }

    @GetMapping(value = "/username/{username}",produces= MediaType.APPLICATION_JSON_VALUE)
    public UserDetails getUserByUsername(@PathVariable("username") String username){
        return userService.loadUserByUsername(username);
    }


}
