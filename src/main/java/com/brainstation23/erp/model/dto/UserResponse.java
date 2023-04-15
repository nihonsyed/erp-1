package com.brainstation23.erp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
@Setter
public class UserResponse {
    @Schema(description = "User ID", example = "3F41A301-25ED-4F0F-876F-7657BEABB00F")
    private UUID id;

    @Schema(description = "User's First Name", example = "Avash")
    private String firstName;

    @Schema(description = "User's Last Name", example = "ORG000001")
    private String lastName;

    @Schema(description = "User's Email", example = "avash@gmail.com")
    private String email;
}
