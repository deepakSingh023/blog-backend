package com.example.blog_backend.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;


    @NotBlank
    private String email;


}
