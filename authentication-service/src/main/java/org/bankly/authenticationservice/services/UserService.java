package org.bankly.authenticationservice.services;
import org.bankly.authenticationservice.entities.Users;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Users signUp(Users user);
    Users updateUser(Long id,Users user);
    Users getUserById(Long id);
    Users loadUserByEmail(String email);
}
