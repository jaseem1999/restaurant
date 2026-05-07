package com.restaurant.user_service.repository.user;

import com.restaurant.user_service.entity.user.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {

}
