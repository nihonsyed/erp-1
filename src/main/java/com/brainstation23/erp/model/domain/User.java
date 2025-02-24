/*
* Md.Abu Syed
* 8:27PM 14.04.2023
* */
package com.brainstation23.erp.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String firstName;
    private String lastName;

    private String email;

    private String password;

    private String role;
}
