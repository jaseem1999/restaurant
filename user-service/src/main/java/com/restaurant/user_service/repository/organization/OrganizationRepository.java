package com.restaurant.user_service.repository.organization;

import com.restaurant.user_service.entity.organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Long> {
    boolean existsByName(String s);
}
