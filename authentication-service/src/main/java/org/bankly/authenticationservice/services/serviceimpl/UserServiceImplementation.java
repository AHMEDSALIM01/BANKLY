package org.bankly.authenticationservice.services.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.bankly.authenticationservice.dtos.WalletRequestDto;
import org.bankly.authenticationservice.entities.Users;
import org.bankly.authenticationservice.repositories.UserRepository;
import org.bankly.authenticationservice.services.UserService;
import org.bankly.authenticationservice.services.WalletServiceClient;
import org.bankly.authenticationservice.validators.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final WalletServiceClient walletServiceClient;

    @Override
    public Users updateUser(Long id, Users user) {
        Users userExist = this.getUserById(id);
        if (userExist == null) {
            throw new IllegalStateException("utilisateur non trouvé");
        }

        if (Boolean.FALSE.equals(userValidator.validate(user))) {
            throw new IllegalStateException(userValidator.getErrorMessage());
        }
        userExist.setName(user.getName());

        userExist.setAddress(user.getAddress());
        userExist.setPhoneNumber(user.getPhoneNumber());
        if(!user.getPassword().equals(userExist.getPassword())){
            userExist.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(userExist);
    }


    @Override
    public Users signUp(Users user) {
        boolean isValidUser = userValidator.validate(user);
        if (!isValidUser) {
            throw new IllegalStateException(userValidator.getErrorMessage());
        }
        Users userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail != null) {
            throw new IllegalStateException("utilisateur existe déja");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        WalletRequestDto walletRequestDto = new WalletRequestDto();
        Users userSaved = userRepository.save(user);
        walletRequestDto.setUserId(userSaved.getId());
        walletRequestDto.setBalance((double) 0);
        walletServiceClient.create(walletRequestDto);
        return userSaved;
    }

    @Override
    public Users getUserById(Long id) {
        Optional<Users> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new IllegalStateException("utilisateur non trouvée");
        }
    }

    @Override
    public Users loadUserByEmail(String email) {
        boolean isValidEmail = userValidator.validateEmail(email);
        if (!isValidEmail) {
            throw new IllegalStateException(userValidator.getErrorMessage());
        }
        Users userByEmail = userRepository.findByEmail(email);
        if (userByEmail == null) {
            throw new IllegalStateException("l'adresse email invalid");
        }
        return userByEmail;
    }

}