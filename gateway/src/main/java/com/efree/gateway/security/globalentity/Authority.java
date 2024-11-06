package com.efree.gateway.security.globalentity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Authority {

    Integer id;
    String name;
    Set<Role> roles;

}
