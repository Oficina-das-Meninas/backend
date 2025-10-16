package br.org.oficinadasmeninas.infra.admin.service;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.admin.Admin;
import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.CreateAdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.UpdateAdminDto;
import br.org.oficinadasmeninas.domain.admin.repository.IAdminRepository;
import br.org.oficinadasmeninas.domain.admin.service.IAdminService;
import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.infra.shared.exception.EmailAlreadyExistsException;

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
	public List<AdminDto> getAllAdmin() {
		return adminRepository.findAll().stream()
				.map(admin -> new AdminDto(admin.getId(), admin.getName(), admin.getEmail())).toList();
	}

	@Override
	public AdminDto getAdminById(UUID id) {
		AdminDto adminDto = new AdminDto();

		Admin admin = adminRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Admin não encontrado com id: " + id));

		adminDto.setId(admin.getId());
		adminDto.setEmail(admin.getEmail());
		adminDto.setName(admin.getName());

		return adminDto;
	}
	
	@Override
	public AdminDto getAdminByEmail(String email) {
		AdminDto adminDto = new AdminDto();

		Admin admin = adminRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("Admin não encontrado com o email: " + email));

		adminDto.setId(admin.getId());
		adminDto.setEmail(admin.getEmail());
		adminDto.setName(admin.getName());

		return adminDto;
	}

	@Override
	public UUID createAdmin(CreateAdminDto admin) {
		Admin newAdmin = new Admin();
		newAdmin.setName(admin.getName());
		newAdmin.setEmail(admin.getEmail());
		newAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));

		try {
			return adminRepository.create(newAdmin);
		} catch (DataIntegrityViolationException e) {
			throw new EmailAlreadyExistsException();
		}
	}

	@Override
	public void updateAdmin(UUID adminId, UpdateAdminDto admin) {
		Admin existingAdmin = adminRepository.findById(adminId)
				.orElseThrow(() -> new EntityNotFoundException("Admin não encontrado com id: " + adminId));

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
		} catch (DataIntegrityViolationException e) {
			throw new EmailAlreadyExistsException();
		}
	}

}
