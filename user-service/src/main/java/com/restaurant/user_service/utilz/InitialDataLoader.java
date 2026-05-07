package com.restaurant.user_service.utilz;

import com.restaurant.user_service.entity.organization.Organization;
import com.restaurant.user_service.entity.user.Roles;
import com.restaurant.user_service.entity.user.UserCredential;
import com.restaurant.user_service.entity.user.UserDetails;
import com.restaurant.user_service.repository.organization.OrganizationRepository;
import com.restaurant.user_service.repository.user.RoleRepository;
import com.restaurant.user_service.repository.user.UserCredentialRepository;
import com.restaurant.user_service.repository.user.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitialDataLoader implements CommandLineRunner {

    private final UserCredentialRepository userCredentialRepository;
    private final RoleRepository roleRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final OrganizationRepository organizationRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Value("${organization.name}")
    private String organizationName;
    @Value("${organization.address}")
    private String organizationAddress;
    @Value("${organization.contactEmail}")
    private String organizationContactEmail;
    @Value("${organization.contactPhone}")
    private String organizationContactPhone;
    @Value("${organization.website}")
    private String organizationWebsite;

    @Value("${user.email}")
    private String email;

    @Value("${user.password}")
    private String password;
    @Value("${user.role}")
    private String role;
    @Value("${user.first_name}")
    private String firstName;
    @Value("${user.last_name}")
    private String lastName;
    @Value("${user.designation}")
    private String designation;
    @Value("${user.phoneNumber}")
    private String phoneNumber;
    @Value("${user.address}")
    private String userAddress;


    @Override
    public void run(String... args) throws Exception {
        boolean isOrganizationDataPresent = organizationRepository.existsByName(organizationName);
        long organizationId = 0L;
        if (!isOrganizationDataPresent) {
            log.info("Loading initial organization data...");
            Organization organization=Organization.builder()
                    .name(organizationName)
                    .address(organizationAddress)
                    .contactEmail(organizationContactEmail)
                    .contactPhone(organizationContactPhone)
                    .website(organizationWebsite)
                    .build();
            organization.setCreatedAt(Instant.now());
            organization.setCreatedBy(0L);
            organization=organizationRepository.save(
                    organization
            );
            organizationId=organization.getId();
            log.info("Initial organization data loaded successfully.");
        }else {
            log.info("Organization data already exists. Skipping initial data load.");
        }

        boolean isUserExist = userCredentialRepository.existsByEmail(email);
        
        if (!isUserExist) {
            log.info("Loading initial user data...");
            if (organizationId == 0L) {
               log.error("Organization ID is not set. Cannot create user without organization. Please check the organization data load.");
                return;
            }
            UserCredential userCredential = UserCredential.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .organizationId(organizationId)
                    .build();
            userCredential.setCreatedAt(Instant.now());
            userCredential.setCreatedBy(0L);
            userCredentialRepository.save(
                    userCredential
            );
            log.info("Initial user data loaded successfully.");

            Roles roles=Roles.builder().role(role)
                    .isActive(true)
                    .description("Super Admin role with all permissions")
                    .userCredential(userCredential).build();
            
            roles.setCreatedBy(0L);
            roles.setCreatedAt(Instant.now());
            
            roleRepository.save(roles);

            UserDetails userDetails = UserDetails.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .designation(designation)
                    .phoneNumber(phoneNumber)
                    .address(userAddress)
                    .userCredential(userCredential)
                    .build();
            
            userDetails.setCreatedAt(Instant.now());
            userDetails.setCreatedBy(0L);
            userDetailsRepository.save(userDetails);

            userCredential.setRoles(List.of(roles));
            userCredential.setUserDetails(userDetails);
            userCredentialRepository.save(userCredential);
            
        } else {
            log.info("User data already exists. Skipping initial data load.");
        }

    }
}
