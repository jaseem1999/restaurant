package com.restaurant.user_service.repository.user;

import com.restaurant.user_service.dto.user_credential.UserCredentialDto;
import com.restaurant.user_service.entity.user.UserCredential;
import com.restaurant.user_service.projection.use_credential.UserCredentialProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

    @Query(value = """
    SELECT 
        u.id as id,
        u.email as email,
        u.password as password,
        GROUP_CONCAT(r.role) as roles
    FROM user_credential u
    LEFT JOIN roles r 
        ON u.id = r.user_credential_id
    WHERE u.email = :email
    GROUP BY u.id, u.email, u.password
    """, nativeQuery = true)
    Optional<UserCredentialProjection> findUserCredentialByEmail(String email);

    boolean existsByEmail(String email);
}