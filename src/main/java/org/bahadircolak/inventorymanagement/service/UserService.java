package org.bahadircolak.inventorymanagement.service;

import jakarta.transaction.Transactional;
import org.bahadircolak.inventorymanagement.config.JwtService;
import org.bahadircolak.inventorymanagement.config.PasswordEncoderService;
import org.bahadircolak.inventorymanagement.model.Role;
import org.bahadircolak.inventorymanagement.model.User;
import org.bahadircolak.inventorymanagement.repository.InventoryItemRepository;
import org.bahadircolak.inventorymanagement.repository.UserRepository;
import org.bahadircolak.inventorymanagement.web.advice.exception.UserConflictException;
import org.bahadircolak.inventorymanagement.web.advice.exception.UserNotFoundException;
import org.bahadircolak.inventorymanagement.web.dto.UserDto;
import org.bahadircolak.inventorymanagement.web.request.AuthenticationRequest;
import org.bahadircolak.inventorymanagement.web.request.RegisterRequest;
import org.bahadircolak.inventorymanagement.web.request.UpdateUserRequest;
import org.bahadircolak.inventorymanagement.web.response.AuthenticationResponse;
import org.bahadircolak.inventorymanagement.web.response.InventoryItemResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoderService passwordEncoderService;
    private final InventoryService inventoryService;
    private final InventoryItemRepository itemRepository;

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoderService passwordEncoderService, InventoryService inventoryService, InventoryItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoderService = passwordEncoderService;
        this.inventoryService = inventoryService;
        this.itemRepository = itemRepository;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setPassword(user.getPassword());
            userDto.setEmail(user.getEmail());
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDto(
                        user.getId(),
                        user.getPassword(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName()))
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (doesUserExist(request.getEmail())) {
            throw new UserConflictException("User with email: " + request.getEmail() + " already exists!");
        }

        String salt = passwordEncoderService.generateSalt();
        String hashedPassword = passwordEncoderService.hash(request.getPassword(), salt);

        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(hashedPassword)
                .salt(salt)
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        var updatedEmail = request.getEmail();
        var user = userRepository.findByEmail(updatedEmail)
                .orElseThrow(() -> new BadCredentialsException("Invalid email"));

        var passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(request.getPassword() + user.getSalt(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private boolean doesUserExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto updateUser(Long id, UpdateUserRequest request) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            existingUser.setEmail(request.getEmail());
            existingUser = userRepository.save(existingUser);

            UserDto updatedUserDto = new UserDto();
            updatedUserDto.setId(existingUser.getId());
            updatedUserDto.setFirstName(existingUser.getFirstName());
            updatedUserDto.setLastName(existingUser.getLastName());
            updatedUserDto.setEmail(existingUser.getEmail());

            return updatedUserDto;
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    @Transactional
    public List<InventoryItemResponse> getUserInventory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return user.getItems().stream()
                .map(inventoryService::mapToInventoryItemResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public double calculateTotalInventoryValue(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return user.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
    }
}
