package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="role_table")
public class Role {
    @Id
    @Column(name="role_id")
    private Long roleId;
    @Column(name="role_name")
    private String roleName;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "role")
    @JsonManagedReference(value = "role_table")
    private List<UserRole> userRoles;


}
