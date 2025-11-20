package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.donor.service.IDonorService;
import br.org.oficinadasmeninas.infra.donor.service.DonorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/donors")
@PreAuthorize("hasRole('ADMIN')")
public class DonorController extends BaseController {

    private final IDonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String badge,
            @RequestParam(defaultValue = "totalDonated") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) String searchTerm) {

        return handle(() -> donorService.findAll(
                page, pageSize,
                badge,
                sortField, sortDirection,
                searchTerm)
        );
    }
}