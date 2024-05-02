package com.sompo.demo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {

    @NotBlank
    @Size(max = 500)
    private String username;

    @NotBlank
    @Size(max = 500)
    private String password;

    @NotBlank
    @Size(max = 500)
    private String longname;
}