package com.restaurant.user_service.entity.user;

import com.restaurant.user_service.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class UserDetails extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    private UserCredential userCredential;
    private String firstName;
    private String lastName;
    private String designation;
    private String phoneNumber;
    private String address;
}
