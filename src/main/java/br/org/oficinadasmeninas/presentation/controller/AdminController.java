package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.CreateAdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.UpdateAdminDto;
import br.org.oficinadasmeninas.domain.admin.service.IAdminService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admins")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController extends BaseController {

    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Cria um novo administrador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrador criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados")
    })
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

    @Operation(summary = "Atualiza um administrador existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrador atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados"),
            @ApiResponse(responseCode = "404", description = "Administrador não encontrado")
    })
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

    @Operation(summary = "Lista todos os administradores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administradores listados com sucesso")
    })
    @GetMapping
    public ResponseEntity<?> getAllAdmin() {
        return handle(adminService::findAll);
    }

    @Operation(summary = "Busca um administrador pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrador encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Administrador não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable UUID id) {

        return handle(() -> adminService.findById(id));
    }
}
