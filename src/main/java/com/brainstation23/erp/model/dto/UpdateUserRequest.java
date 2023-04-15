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

    @NotNull
    @Id
    private UUID id;
    @NotNull
    @Schema(description = "User's first name", example = "Mahedi ")
    private String firstName;

    @NotNull
    @Schema(description = "User's last name", example = "Mahedi ")
    private String lastName;

    @NotNull
    @Schema(description = "User's email", example = "Mahedi ")
    private String email;

}
