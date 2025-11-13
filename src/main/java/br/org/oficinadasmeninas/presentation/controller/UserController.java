package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.pontuation.dto.GetUserPontuationsDto;
import br.org.oficinadasmeninas.domain.pontuation.service.IPontuationService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UpdateUserDto;
import br.org.oficinadasmeninas.domain.user.service.IUserService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    private final IUserService userService;
    private final IPontuationService pontuationService;

    public UserController(IUserService userService, IPontuationService pontuationService) {
        super();
        this.userService = userService;
        this.pontuationService = pontuationService;
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

    @GetMapping("/{id}/pontuations")
    public ResponseEntity<?> getUserPontuations(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize,
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalDate endDate,
            @RequestParam @Nullable String donationType
    ) {
        var request = GetUserPontuationsDto.FromRequestParams(page, pageSize, startDate, endDate, donationType);

        return handle(() -> pontuationService.getUserPontuations(id, request));
    }
}
