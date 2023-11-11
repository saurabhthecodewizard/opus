package com.opus.service;

import com.opus.dto.response.ProfileDetailsDTO;
import com.opus.dto.response.UserDetailsDTO;
import com.opus.dto.response.UserDTO;
import com.opus.dto.request.UserUpdateDTO;
import com.opus.entity.User;
import com.opus.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<UserDetailsDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(UserDetailsDTO::fromEntity).collect(Collectors.toList());
    }

    public UserDetailsDTO getUser(Long userId) {
        User user = getUserById(userId);

        return UserDetailsDTO.fromEntity(user);
    }

    public UserDetailsDTO createUser(UserDTO userDto) {
        User user = new User();
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setFirstName(userDto.firstName());
        user.setMiddleName(userDto.middleName());
        user.setLastName(userDto.lastName());
        user.setDateOfBirth(userDto.dateOfBirth());
        user.setGender(userDto.gender());

        user = userRepository.save(user);
        return UserDetailsDTO.fromEntity(user);
    }

    public UserDetailsDTO updateUser(Long userId, UserUpdateDTO userDto) {
        User userToBeUpdated = getUserById(userId);

        userToBeUpdated.setEmail(userDto.getEmail());
        userToBeUpdated.setFirstName(userDto.getFirstName());
        userToBeUpdated.setMiddleName(userDto.getMiddleName());
        userToBeUpdated.setLastName(userDto.getLastName());
        userToBeUpdated.setPhoneNumber(userDto.getPhoneNumber());
        userToBeUpdated.setAddress(userDto.getAddress());
        userToBeUpdated.setDateOfBirth(userDto.getDateOfBirth());
        userToBeUpdated.setGender(userDto.getGender());
        userToBeUpdated.setNationality(userDto.getNationality());
        userToBeUpdated.setMaritalStatus(userDto.getMaritalStatus());

        User updatedUser = userRepository.save(userToBeUpdated);

        return UserDetailsDTO.fromEntity(updatedUser);
    }

    public Long deleteUser(Long userId) {
        userRepository.deleteById(userId);

        return userId;
    }

    public ProfileDetailsDTO getUserProfile(Long userId) {
        User user = getUserById(userId);

        return ProfileDetailsDTO.fromEntity(user);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }
}
