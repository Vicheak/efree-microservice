package com.efree.gateway.security.globalentity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRole {

    Long id;
    @JsonBackReference
    User user;
    @JsonBackReference
    Role role;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
