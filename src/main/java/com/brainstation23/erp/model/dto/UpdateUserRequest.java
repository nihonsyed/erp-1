package com.brainstation23.erp.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@ToString
@Getter
@Setter
public class UpdateUserRequest {

    @Schema(description = "Updates user's first name")
    private String firstName;


    @Schema(description = "Updates user's last name")
    private String lastName;


    @Schema(description = "Updates user's role")
    private String role;


    @Schema(description = "Updates user's email")
    private String email;

    @Schema(description = "Updates user's password")
    private String password;



}
