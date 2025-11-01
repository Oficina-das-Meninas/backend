package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UpdateUserDto;
import br.org.oficinadasmeninas.domain.user.service.IUserService;
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

    @GetMapping
    public ResponseEntity<?> findAll() {
        return handle(userService::findAll);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByUserId(
            @PathVariable UUID id
    ) {
        return handle(() -> userService.findByUserId(id));
    }
}
