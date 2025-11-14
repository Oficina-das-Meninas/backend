package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.partner.dto.CreatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.dto.UpdatePartnerDto;
import br.org.oficinadasmeninas.domain.partner.service.IPartnerService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/partners")
public class PartnerController extends BaseController {

    private final IPartnerService partnerService;

    public PartnerController(IPartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @Operation(summary = "Cria um novo parceiro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parceiro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação do parceiro"),
            @ApiResponse(responseCode = "503", description = "Erro ao executar o Bucket")
    })
    @PostMapping
    public ResponseEntity<?> createPartner(
            @ModelAttribute @Valid CreatePartnerDto createPartnerDto
    ) {
        return handle(
                () -> partnerService.insert(createPartnerDto),
                Messages.PARTNER_CREATED_SUCCESSFULLY,
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Atualiza um parceiro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parceiro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização"),
            @ApiResponse(responseCode = "404", description = "Parceiro não encontrado"),
            @ApiResponse(responseCode = "503", description = "Erro ao executar o Bucket")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePartner(
            @PathVariable UUID id,
            @ModelAttribute @Valid UpdatePartnerDto updatePartnerDto
    ) {
        return handle(
                () -> partnerService.update(id, updatePartnerDto),
                Messages.PARTNER_UPDATED_SUCCESSFULLY
        );
    }

    @Operation(summary = "Remove um parceiro pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parceiro removido com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePartner(@PathVariable UUID id) {

        return handle(
                () -> partnerService.deleteById(id),
                Messages.PARTNER_DELETED_SUCCESSFULLY
        );
    }

    @Operation(summary = "Lista parceiros filtrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parceiros encontrados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam @Nullable String searchTerm,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize
    ) {
        return handle(() -> partnerService.findAll(searchTerm, page, pageSize));
    }

    @Operation(summary = "Busca um parceiro pelo identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parceiro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Parceiro não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findPartnerById(@PathVariable UUID id) {
        return handle(() -> partnerService.findById(id));
    }
}