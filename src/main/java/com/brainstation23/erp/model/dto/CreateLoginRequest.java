package com.brainstation23.erp.model.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CreateLoginRequest {
    private String email;
    private String password;
}
