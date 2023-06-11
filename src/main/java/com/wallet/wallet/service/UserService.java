package com.wallet.wallet.service;

import com.wallet.wallet.model.UserObj;
import com.wallet.wallet.model.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService {
    private WalletService walletService;
    private UserRepository userRepo;


    public UserService(UserRepository userRepo, WalletService walletService) {
        this.userRepo = userRepo;
        this.walletService = walletService;
    }

    public void createUser(String username, String email, int age, String password) {
        if (userRepo.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (age < 18) {
            throw new IllegalArgumentException("Age must be at least 18");
        }
        if (!isPasswordValid(password)) {
            throw new IllegalArgumentException("Invalid password format");
        }

        UserObj userObj = new UserObj();
        userObj.setUsername(username);
        userObj.setEmail(email);
        userObj.setAge(age);
        userObj.setPassword(password);
        userObj.setUserWallet(walletService.createNewWallet());
        userRepo.save(userObj);
    }

    public UserObj getUserById(Long id) {
        UserObj userObj = userRepo.findById(id).orElse(null);
        userObj.getUserWallet().setBitcoinValue(walletService.getBitcoinValue());
        return userObj;
    }

    public void deleteUserById(Long id) {
        userRepo.deleteById(id);
    }

    public UserObj updateUser(Long id, UserObj updatedUserObj) {
        UserObj userObj = userRepo.findById(id).orElse(null);
        if (userObj != null) {
            userObj.setUsername(updatedUserObj.getUsername());
            userObj.setPassword(updatedUserObj.getPassword());
            userObj = userRepo.save(userObj);
        }
        return userObj;
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isPasswordValid(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.matches(passwordRegex, password);
    }


}
