package com.arena.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequestDTO(
        @NotBlank String name,
        @NotBlank @Email String email
) {}
