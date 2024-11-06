package com.efree.gateway.security.globalentity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

    Integer id;
    String name;
    List<UserRole> userRoles;
    Set<Authority> authorities;

}
