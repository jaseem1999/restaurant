package com.restaurant.user_service.repository.user;

import com.restaurant.user_service.entity.user.UserCredential;
import com.restaurant.user_service.projection.use_credential.UserCredentialProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    @Query("SELECT u.id as id, u.email AS email, u.password AS password FROM UserCredential u WHERE u.email = :email")
    UserCredentialProjection findByEmailProjection(String email);

    boolean existsByEmail(String email);
}