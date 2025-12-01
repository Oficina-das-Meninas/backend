package br.org.oficinadasmeninas.domain.user.dto;

import java.util.Optional;

public record UpdateUserDto(
        Optional<String> name,
        Optional<String> email,
        Optional<String> document,
        Optional<String> password,
        Optional<String> phone
) {
}