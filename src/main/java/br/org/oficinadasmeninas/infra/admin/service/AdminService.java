package br.org.oficinadasmeninas.infra.admin.service;

import static br.org.oficinadasmeninas.domain.admin.mapper.AdminMapper.toDto;

import java.util.UUID;

import br.org.oficinadasmeninas.infra.auth.service.JwtService;
import br.org.oficinadasmeninas.infra.session.service.SessionService;
import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;
import br.org.oficinadasmeninas.presentation.shared.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
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
import br.org.oficinadasmeninas.infra.shared.exception.EmailAlreadyExistsException;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

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
    public PageDTO<Admin> findByFilter(String searchTerm, int page, int pageSize){
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
