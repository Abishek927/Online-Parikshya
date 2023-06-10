package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name="role_table")

public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="role_id")
    private Long roleId;
    @Column(name="role_name")
    private String roleName;

    @ManyToMany(mappedBy = "userRoles")
    @JsonIgnore
    private Set<User> userSet;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="role_authority",
            joinColumns = @JoinColumn(name="role_id_fk",referencedColumnName = "role_id"),
            inverseJoinColumns = @JoinColumn(name="authority_id_fk",referencedColumnName = "authority_id")

    )
    private Set<Authority> authorities;

}
