package com.example.yurnero.demo.controller;

import com.example.yurnero.demo.controller.auth.AuthController;
import com.example.yurnero.demo.dto.AuthenticationRequest;
import com.example.yurnero.demo.dto.AuthenticationResponse;
import com.example.yurnero.demo.dto.SignupRequest;
import com.example.yurnero.demo.dto.UserDto;
import com.example.yurnero.demo.enums.UserRole;
import com.example.yurnero.demo.model.User;
import com.example.yurnero.demo.repository.UserRepository;
import com.example.yurnero.demo.service.admin.UsersServiceImpl;
import com.example.yurnero.demo.service.auth.AuthService;
import com.example.yurnero.demo.service.jwt.UserService;
import com.example.yurnero.demo.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthControllerTest {
    @Mock
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserDetailsService userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;
    @Mock
    private UsersServiceImpl usersServiceCreate;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @Mock
    private SecurityContextLogoutHandler logoutHandler;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signupUser_Success() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");

        when(authService.hasUserWithEmail(anyString())).thenReturn(false);

        UserDto createdUserDto = new UserDto();
        createdUserDto.setEmail("test@example.com");
        createdUserDto.setId(1L);

        when(authService.signupUser(any(SignupRequest.class))).thenReturn(createdUserDto);

        ResponseEntity<?> responseEntity = authController.signupUser(signupRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdUserDto, responseEntity.getBody());
    }

    @Test
    public void testSignupUser_Success() {
        // Mock data
        SignupRequest signupRequest = new SignupRequest("yura", "yura", "test@example.com", "password123");
        UserDto createdUser = new UserDto(1L,"yura","yura", "test@example.com","password123", UserRole.USER);

        // Mock behavior
        when(authService.hasUserWithEmail(signupRequest.getEmail())).thenReturn(false);
        when(authService.signupUser(signupRequest)).thenReturn(createdUser);

        // Call controller method
        ResponseEntity<?> response = authController.signupUser(signupRequest);

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof UserDto);
        assertEquals(createdUser, response.getBody());
    }

    @Test
    void signupUser_UserAlreadyExists() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("existing@example.com");

        when(authService.hasUserWithEmail(anyString())).thenReturn(true);

        ResponseEntity<?> responseEntity = authController.signupUser(signupRequest);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof String);
        assertEquals("User already exist by this email!", responseEntity.getBody());
    }

    @Test
    void login_Success() {
        UserDetails userDetails = mock(UserDetails.class);

        UserDetailsService userDetailsServiceMock = mock(UserDetailsService.class);
        when(userService.userDetailsService()).thenReturn(userDetailsServiceMock);

        when(userDetailsServiceMock.loadUserByUsername(any(String.class))).thenReturn(userDetails);


        User user = new User();
        user.setId(1L);
        user.setUserRole(UserRole.USER);
        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findFirstByEmail(any(String.class))).thenReturn(optionalUser);


        String mockJwtToken = "mock.jwt.token";
        when(jwtUtil.generateToken(userDetails)).thenReturn(mockJwtToken);


        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setPassword("password");
        AuthenticationResponse authenticationResponse = authController.login(authenticationRequest);


        assertNotNull(authenticationResponse);
        assertEquals(mockJwtToken, authenticationResponse.getJwt());
        assertEquals(user.getId(), authenticationResponse.getUserId());
        assertEquals(user.getUserRole(), authenticationResponse.getUserRole());
    }

    @Test
    void login_BadCredentials() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("nonexistent@example.com");
        authenticationRequest.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Incorrect username or password"));

        assertThrows(BadCredentialsException.class, () -> authController.login(authenticationRequest));
    }

    @Test
    public void testSignupUser_UserExists() {
        // Mock data
        SignupRequest signupRequest = new SignupRequest("yura", "yura", "test@example.com", "password123");

        // Mock behavior
        when(authService.hasUserWithEmail(signupRequest.getEmail())).thenReturn(true);

        // Call controller method
        ResponseEntity<?> response = authController.signupUser(signupRequest);

        // Verify
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("User already exist by this email!", response.getBody());
    }

    @Test
    void logout_NoAuthentication() {
        MockitoAnnotations.openMocks(this);

        SecurityContextHolder.clearContext();

        ResponseEntity<Void> responseEntity = authController.logout(request, response);


        verifyNoInteractions(logoutHandler); // logoutHandler.logout should not be called
        assertEquals(ResponseEntity.ok().build(), responseEntity);
    }

}
