package com.swagger.service;


import com.swagger.dto.ResponseDTO;
import com.swagger.dto.SignInRequestDTO;
import com.swagger.dto.SignInResponseDTO;
import com.swagger.dto.SignUpRequestDTO;
import com.swagger.entity.Admin;
import com.swagger.entity.Authority;
import com.swagger.entity.Employee;
import com.swagger.entity.UserCredential;
import com.swagger.exception.BadRequestServiceAlertException;
import com.swagger.repository.AdminRepository;
import com.swagger.repository.AuthorityRepository;
import com.swagger.repository.EmployeeRepository;
import com.swagger.repository.UserCredentialRepository;
import com.swagger.util.Constants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {

    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthorityRepository authorityRepository;
    private final WebClient webClient;
    private final EmployeeRepository employeeRepository;
    private final AdminRepository adminRepository;

    public ResponseDTO createUser(final SignUpRequestDTO signUpRequestDTO) {

        UserCredential userCredential = UserCredential.builder()
                .authority(Authority.builder().id(signUpRequestDTO.getAuthorityId()).build())
                .firstName(signUpRequestDTO.getFirstName())
                .lastName(signUpRequestDTO.getLastName())
                .isRemoved(false)
                .isActivated(false)
                .email(signUpRequestDTO.getEmail())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .build();

        userCredential = this.userCredentialRepository.save(userCredential);
        if (signUpRequestDTO.getIsEmployee()) {

            Employee employee=Employee.builder()
                    .userCredential(userCredential)
                    .build();
            this.employeeRepository.save( employee);
        } else {
            Admin admin=Admin.builder()
                    .userCredential(userCredential)
                    .build();
            this.adminRepository.save( admin);
        }

        return ResponseDTO.builder()
                .statusCode(200)
                .message(Constants.CREATED)
                .data(userCredential)
                .build();
    }

    public ResponseDTO generateToken(final SignInRequestDTO signInRequestDTO) {
        final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequestDTO.getUsername(), signInRequestDTO.getPassword()));
        if (authenticate.isAuthenticated()) {
            UserCredential userCredential = this.userCredentialRepository.findByEmail(signInRequestDTO.getUsername())
                    .orElseThrow(() -> new BadRequestServiceAlertException(Constants.NOT_FOUND));

            Employee employee= (Employee) this.employeeRepository.findByUserCredential(userCredential)
                    .orElseThrow(() -> new BadRequestServiceAlertException(Constants.NOT_FOUND));
            String userId = userCredential.getId();
            String Role = userCredential.getAuthority().getRole();
            String token = this.jwtService.generateToken(signInRequestDTO.getUsername(), userId, Role);
            return ResponseDTO.builder()
                    .statusCode(200)
                    .message(Constants.ACTIVATED)
                    .data(SignInResponseDTO.builder()
                            .token(token)
                            .expiresIn(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                            .build())
                    .build();
        } else {
            throw new BadRequestServiceAlertException(Constants.CREDENTIALS_MISMATCH);
        }
    }

//    public ResponseDTO activateUser(String email) {
//        UserCredential userCredential = this.userCredentialRepository.findByEmail(email).orElseThrow(() -> new BadRequestServiceAlertException(Constants.USER_NOT_FOUND));
//        userCredential.setUpdatedAt(Instant.now());
//        userCredential.setIsActivated(true);
//        userCredential = this.userCredentialRepository.save(userCredential);
//        return ResponseDTO.builder()
//                .statusCode(200)
//                .message(Constants.ACTIVATED)
//                .data(userCredential)
//                .build();
//    }

//    public ResponseDTO diActivateUser(String email) {
//        UserCredential userCredential = this.userCredentialRepository.findByEmail(email).orElseThrow(() -> new BadRequestServiceAlertException("No user found"));
//        userCredential.setUpdatedAt(Instant.now());
//        userCredential.setIsActivated(false);
//        userCredential = this.userCredentialRepository.save(userCredential);
//        return ResponseDTO.builder()
//                .statusCode(200)
//                .message(Constants.DE_ACTIVATED)
//                .data(userCredential)
//                .build();
//    }

//    public ResponseDTO removeUser(String email) {
//        UserCredential userCredential = this.userCredentialRepository.findByEmail(email).orElseThrow(() -> new BadRequestServiceAlertException("No user found"));
//        userCredential.setUpdatedAt(Instant.now());
//        userCredential.setIsActivated(false);
//        userCredential.setIsRemoved(true);
//        userCredential = this.userCredentialRepository.save(userCredential);
//        return ResponseDTO.builder()
//                .statusCode(200)
//                .message(Constants.REMOVED)
//                .data(userCredential)
//                .build();
//    }

//    public ResponseDTO retrieveRoles() {
//        final List<Authority> authorityList = this.authorityRepository.findAll();
//        return ResponseDTO.builder()
//                .statusCode(200)
//                .data(authorityList)
//                .message(Constants.RETRIEVED)
//                .build();
//    }

//    public ResponseDTO retrieveUser(String id) {
//        final UserCredential userCredential = this.userCredentialRepository.findById(id).orElseThrow(() -> new BadRequestServiceAlertException(Constants.USER_NOT_FOUND));
//        return ResponseDTO.builder()
//                .statusCode(200)
//                .data(userCredential)
//                .message(Constants.RETRIEVED)
//                .build();
//    }

    public ResponseDTO validateToken(String token) {
        this.jwtService.validateToken(token);
        return ResponseDTO.builder()
                .statusCode(200)
                .data("Provided token is valid")
                .message(Constants.VALID_TOKEN)
                .build();
    }

//    public ResponseDTO account(String token) {
//        String email;
//        try {
//            email = jwtService.extractUsername(token);
//        } catch (Exception e) {
//            throw new BadRequestServiceAlertException("Invalid token..");
//        }
//        UserCredential userCredential = this.userCredentialRepository.findByEmail(email)
//                .orElseThrow(() -> new BadRequestServiceAlertException("User not found.."));
//        OrganizationUser organizationUser = (OrganizationUser) this.organizationUserRepository.findByUserCredential(userCredential)
//                .orElseThrow(() -> new BadRequestServiceAlertException("User not found in any organization."));
//        Organization organization1 = organizationUser.getOrganization();
//        AccountDetailsDTO accountDetailsDTO =AccountDetailsDTO.builder()
//                .userid(userCredential.getId())
//                .email(userCredential.getEmail())
//                .firstName(userCredential.getFirstName())
//                .LastName(userCredential.getLastName())
//                .isRemoved(userCredential.getIsRemoved())
//                .isActivated(userCredential.getIsActivated())
//                .createdAt(userCredential.getCreatedAt().toString())
//                .updatedAt(userCredential.getUpdatedAt().toString())
//                .authority(userCredential.getAuthority().getRole())
//                .orgId(organization1.getId())
//                .orgEmail(organization1.getOrgEmail())
//                .OrgName(organization1.getName())
//                .isCustomer(organization1.getIsCustomer().toString())
//                .orgUserId(organizationUser.getId()).build();
//        return
//                ResponseDTO.builder()
//                        .message(Constants.RETRIEVED)
//                        .statusCode(200)
//                        .data(accountDetailsDTO)
//                        .build();
//    }
}
