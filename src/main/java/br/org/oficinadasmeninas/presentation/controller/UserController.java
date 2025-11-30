package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.pontuation.dto.GetUserPontuationsDto;
import br.org.oficinadasmeninas.domain.pontuation.service.IPontuationService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UpdateUserDto;
import br.org.oficinadasmeninas.domain.user.service.IUserService;
import jakarta.annotation.Nullable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
public class UserController extends BaseController {

    private final IUserService userService;
    private final IPontuationService pontuationService;

    public UserController(IUserService userService, IPontuationService pontuationService) {
        super();
        this.userService = userService;
        this.pontuationService = pontuationService;
    }

    @Operation(summary = "Cria um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do usuário"),
    })
    @PostMapping
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> insert(
            @Valid @RequestBody CreateUserDto request
    ) {
        return handle(
                () -> userService.insert(request),
                Messages.USER_CREATED_SUCCESSFULLY,
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Atualiza um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização do usuário"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserDto request
    ) {
        return handle(
                () -> userService.update(id, request),
                Messages.USER_UPDATED_SUCCESSFULLY
        );
    }

    @Operation(summary = "Lista todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAll() {
        return handle(userService::findAll);
    }

    @Operation(summary = "Busca um usuário pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> findByUserId(
            @PathVariable UUID id
    ) {
        return handle(() -> userService.findByUserId(id));
    }
    
	@GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> findByUserSession() {
		return handle(userService::findByUserSession);
	}

    @GetMapping("/pontuations")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getUserPontuations(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize,
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalDate endDate,
            @RequestParam @Nullable String donationType
    ) {
        var request = GetUserPontuationsDto.FromRequestParams(page, pageSize, startDate, endDate, donationType);

        return handle(() -> pontuationService.getUserPontuations(request));
    }
}
