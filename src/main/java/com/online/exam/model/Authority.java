package com.online.exam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="authorities_table")

public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="authority_id")
    private Long authorityId;
    @Column(name = "authority_name")
    private String authorityName;
    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;

    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }
}
