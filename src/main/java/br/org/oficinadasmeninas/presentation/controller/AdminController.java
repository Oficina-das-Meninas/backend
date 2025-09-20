package br.org.oficinadasmeninas.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.CreateAdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.UpdateAdminDto;
import br.org.oficinadasmeninas.domain.admin.service.IAdminService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final IAdminService adminService;

	public AdminController(IAdminService adminService) {
		super();
		this.adminService = adminService;
	}

	@PostMapping
	public ResponseEntity<AdminDto> createAdmin(@Valid @RequestBody CreateAdminDto request) {
		UUID id = adminService.createAdmin(request);

		AdminDto adminDto = new AdminDto();
		adminDto.setId(id);
		adminDto.setEmail(request.getEmail());
		adminDto.setName(request.getName());

		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri())
				.body(adminDto);
	}

	@GetMapping
	public ResponseEntity<List<AdminDto>> getAllAdmin() {
		List<AdminDto> dto = adminService.getAllAdmin();

		return ResponseEntity.ok(dto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AdminDto> getAdminById(@PathVariable UUID id) {
		AdminDto dto = adminService.getAdminById(id);

		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateAdmin(@PathVariable UUID id, @Valid @RequestBody UpdateAdminDto request) {
		adminService.updateAdmin(id, request);

		return ResponseEntity.noContent().build();
	}

}
