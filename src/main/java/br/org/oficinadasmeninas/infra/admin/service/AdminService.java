package br.org.oficinadasmeninas.infra.admin.service;

import br.org.oficinadasmeninas.domain.admin.Admin;
import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.CreateAdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.UpdateAdminDto;
import br.org.oficinadasmeninas.domain.admin.mapper.AdminMapper;
import br.org.oficinadasmeninas.domain.admin.repository.IAdminRepository;
import br.org.oficinadasmeninas.domain.admin.service.IAdminService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.infra.shared.exception.EmailAlreadyExistsException;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static br.org.oficinadasmeninas.domain.admin.mapper.AdminMapper.toDto;

@Service
public class AdminService implements IAdminService {

    private final IAdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(IAdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        super();
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UUID insert(CreateAdminDto request) {

        var admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        try {
            adminRepository.insert(admin);
            return admin.getId();
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException();
        }
    }

    @Override
    public UUID update(UUID adminId, UpdateAdminDto admin) {
        var existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException(Messages.ADMIN_NOT_FOUND_BY_ID + adminId));

        if (admin.getName() != null && !admin.getName().isBlank() && !existingAdmin.getName().equals(admin.getName())) {
            existingAdmin.setName(admin.getName());
        }

        if (admin.getEmail() != null && !existingAdmin.getEmail().equals(admin.getEmail())) {
            existingAdmin.setEmail(admin.getEmail());
        }

        if (admin.getPassword() != null && !admin.getPassword().isBlank()) {
            existingAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }

        try {
            adminRepository.update(existingAdmin);
            return existingAdmin.getId();
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException();
        }
    }

    @Override
    public List<AdminDto> findAll() {

        return adminRepository
                .findAll().stream()
                .map(AdminMapper::toDto)
                .toList();
    }

    @Override
    public AdminDto findById(UUID id) {

        var admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.ADMIN_NOT_FOUND_BY_ID + id));

        return toDto(admin);
    }

    @Override
    public AdminDto findByEmail(String email) {

        var admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Messages.ADMIN_NOT_FOUND_BY_EMAIL + email));

        return toDto(admin);
    }
}
