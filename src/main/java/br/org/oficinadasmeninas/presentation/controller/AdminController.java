package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.CreateAdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.UpdateAdminDto;
import br.org.oficinadasmeninas.domain.admin.service.IAdminService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admins")
public class AdminController extends BaseController {

    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<?> createAdmin(
            @Valid @RequestBody CreateAdminDto request
    ) {
        return handle(
                () -> {
                    var id = adminService.insert(request);

                    var response = new AdminDto();
                    response.setId(id);
                    response.setEmail(request.getEmail());
                    response.setName(request.getName());

                    return response;
                },
                Messages.ADMIN_CREATED_SUCCESSFULLY,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAdminDto request
    ) {
        return handle(
                () -> adminService.update(id, request),
                Messages.ADMIN_UPDATED_SUCCESSFULLY
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllAdmin() {
        return handle(adminService::findAll);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable UUID id) {

        return handle(() -> adminService.findById(id));
    }
}
