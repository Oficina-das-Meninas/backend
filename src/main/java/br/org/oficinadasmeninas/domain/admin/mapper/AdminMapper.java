package br.org.oficinadasmeninas.domain.admin.mapper;

import br.org.oficinadasmeninas.domain.admin.Admin;
import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;

public final class AdminMapper {

    public static AdminDto toDto(Admin admin) {

        return new AdminDto(admin.getId(), admin.getName(), admin.getEmail(), admin.getAccountId());
    }
}