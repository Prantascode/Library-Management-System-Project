package com.pranta.LibraryMangement.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pranta.LibraryMangement.DTOs.DTO.UserRegistrationDto;
import com.pranta.LibraryMangement.DTOs.DTO.UserResponseDto;
import com.pranta.LibraryMangement.Entity.User;
import com.pranta.LibraryMangement.Exception.ResourceAlreadyExistsException;
import com.pranta.LibraryMangement.Exception.ResourceNotFoundException;
import com.pranta.LibraryMangement.Repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto registerUser(UserRegistrationDto registrationDto){
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email Already in use : "+registrationDto.getEmail());
        }
        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(registrationDto.getRole());
        User savedUser = userRepository.save(user);
        return mapToResponseDto(savedUser);
    }
    public UserResponseDto getUserById(Long userId){
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+userId));

        return mapToResponseDto(user);
        
    }
    public List<UserResponseDto> getAllStudents(){
        List<User> students = userRepository.findAllByRole(User.UserRole.STUDENT);
        return students.stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found email : "+email));
    }
    
    private UserResponseDto mapToResponseDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        responseDto.setRole(user.getRole());
        return responseDto;
    }
}
