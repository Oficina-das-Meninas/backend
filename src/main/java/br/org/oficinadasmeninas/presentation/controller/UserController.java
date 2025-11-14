package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UpdateUserDto;
import br.org.oficinadasmeninas.domain.user.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        super();
        this.userService = userService;
    }

    @Operation(summary = "Cria um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do usuário"),
    })
    @PostMapping
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
    public ResponseEntity<?> findAll() {
        return handle(userService::findAll);
    }

    @Operation(summary = "Busca um usuário pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findByUserId(
            @PathVariable UUID id
    ) {
        return handle(() -> userService.findByUserId(id));
    }
}
