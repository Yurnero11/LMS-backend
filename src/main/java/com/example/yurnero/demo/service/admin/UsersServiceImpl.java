package com.example.yurnero.demo.service.admin;

import com.example.yurnero.demo.dto.UserDto;
import com.example.yurnero.demo.enums.UserRole;
import com.example.yurnero.demo.exception.ResourceNotFoundException;
import com.example.yurnero.demo.mapper.UserMapper;
import com.example.yurnero.demo.model.User;
import com.example.yurnero.demo.repository.UserRepository;
import com.example.yurnero.demo.service.jwt.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UserRepository userRepository;
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setUserRole(UserRole.USER);

        User createdUser = userRepository.save(user);
        logger.log(Level.INFO, "User created with id: {0}", createdUser.getId());
        return createdUser.getUserDto();
    }

    @Override
    public UserDto createTeacher(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setUserRole(UserRole.TEACHER);

        User createdUser = userRepository.save(user);
        logger.log(Level.INFO, "Teacher created with id: {0}", createdUser.getId());
        return createdUser.getUserDto();
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        boolean exists = userRepository.findFirstByEmail(email).isPresent();
        if (exists) {
            logger.log(Level.INFO, "User with email {0} already exists", email);
        }
        return exists;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> users = userRepository.findAll()
                .stream()
                .filter(user -> user.getUserRole() == UserRole.USER || user.getUserRole() == UserRole.TEACHER)
                .map(User::getUserDto)
                .collect(Collectors.toList());
        logger.log(Level.INFO, "Retrieved {0} users", users.size());
        return users;
    }

    @Override
    public List<UserDto> getAllTeachers() {
        List<UserDto> teachers = userRepository.findAll()
                .stream()
                .filter(user -> user.getUserRole() == UserRole.TEACHER)
                .map(User::getUserDto)
                .collect(Collectors.toList());
        logger.log(Level.INFO, "Retrieved {0} teachers", teachers.size());
        return teachers;
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        if (userDto.getPassword() != null && !Objects.equals(userDto.getPassword(), user.getPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        logger.log(Level.INFO, "User updated with id: {0}", updatedUser.getId());
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.deleteById(id);
        logger.log(Level.INFO, "User deleted with id: {0}", id);
        return UserMapper.mapToUserDto(user);
    }


}
