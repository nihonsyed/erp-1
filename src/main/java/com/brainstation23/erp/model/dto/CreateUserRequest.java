package com.brainstation23.erp.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
public class CreateUserRequest {
    @NotNull
    @Schema(description = "User's Name", example = "Abdullah")
    private String firstName;
    private String lastName;

    private String email;

    private String password;
}
