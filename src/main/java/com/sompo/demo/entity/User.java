package com.sompo.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mst_user")
public class User {

    @Id
    private String username;

    private String password;

    private String longname;

    private String token;

    @Column(name = "token_expired")
    private Long tokenExpired;

}