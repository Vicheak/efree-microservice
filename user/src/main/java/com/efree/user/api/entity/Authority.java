package com.efree.user.api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    Integer id;

    @Column(name = "authority_name", length = 120, unique = true, nullable = false)
    String name;

    @ManyToMany(mappedBy = "authorities")
    Set<Role> roles;

}
