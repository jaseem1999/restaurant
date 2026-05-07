package com.restaurant.user_service.entity.user;

import com.restaurant.user_service.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class UserCredential extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @OneToMany(mappedBy = "userCredential",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Roles> roles;

    @OneToOne(mappedBy = "userCredential", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserDetails userDetails;

    @Column(nullable = false)
    private Long organizationId;
}
