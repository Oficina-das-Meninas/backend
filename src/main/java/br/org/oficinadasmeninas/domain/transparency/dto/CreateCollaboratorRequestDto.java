package br.org.oficinadasmeninas.domain.transparency.dto;

import org.springframework.web.multipart.MultipartFile;

public record CreateCollaboratorRequestDto(
        MultipartFile file,
        String name,
        String role,
        String description,
        String categoryId
) { }

