package br.org.oficinadasmeninas.infra.admin.service;

import static br.org.oficinadasmeninas.domain.admin.mapper.AdminMapper.toDto;
import java.util.UUID;
import br.org.oficinadasmeninas.presentation.exceptions.ConflictException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.org.oficinadasmeninas.domain.admin.Admin;
import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.CreateAdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.UpdateAdminDto;
import br.org.oficinadasmeninas.domain.admin.repository.IAdminRepository;
import br.org.oficinadasmeninas.domain.admin.service.IAdminService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.infra.session.service.SessionService;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static br.org.oficinadasmeninas.domain.admin.mapper.AdminMapper.toDto;

@Service
public class AdminService implements IAdminService {

    private final IAdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    public AdminService(IAdminRepository adminRepository, PasswordEncoder passwordEncoder, SessionService sessionService) {
        super();
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionService = sessionService;
    }

    @Override
    public UUID insert(CreateAdminDto request) {

        var existsAdmin = adminRepository.findByEmail(request.getEmail());

        if (existsAdmin.isPresent())
            throw new ValidationException(Messages.EMAIL_ALREADY_EXISTS);

        var admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        try {
            adminRepository.insert(admin);
            return admin.getId();
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(Messages.EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    public UUID update(UUID adminId, UpdateAdminDto request) {
        var adminA = adminRepository.findById(adminId);

        if (adminA.isEmpty())
            throw new NotFoundException(Messages.ADMIN_NOT_FOUND_BY_ID + adminId);

        var admin = adminA.get();

        if (request.getName() != null && !request.getName().isBlank() && !admin.getName().equals(request.getName())) {
            admin.setName(request.getName());
        }

        if (request.getEmail() != null && !admin.getEmail().equals(request.getEmail())) {
            admin.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (!request.getEmail().equals(admin.getEmail())) {
            var adminByEmail = adminRepository.findByEmail(request.getEmail());

            if (adminByEmail.isPresent())
                throw new ConflictException(Messages.EMAIL_ALREADY_EXISTS);
        }

        adminRepository.update(admin);
        return admin.getId();
    }

    @Override
    public UUID deleteById(UUID id) {
        checkAdminExists(id);

        var admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.ADMIN_NOT_FOUND_BY_ID + id));

        var username = sessionService.getSession().getUsername();

        if (admin.getEmail().equals(username)) {
            throw new ValidationException(Messages.CANNOT_DELETE_LOGGED_USER);
        }

        adminRepository.deleteById(id);

        return id;
    }

    @Override
    public PageDTO<Admin> findByFilter(String searchTerm, int page, int pageSize) {
        return adminRepository.findByFilter(searchTerm, page, pageSize);
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

    @Override
    public void updatePassword(UUID uuid, String encodedPassword) {
        adminRepository.updatePassword(uuid, encodedPassword);
    }

    private void checkAdminExists(UUID id) {
        if (!adminRepository.existsById(id)) {
            throw new NotFoundException(Messages.ADMIN_NOT_FOUND_BY_ID);
        }
    }
}
