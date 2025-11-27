package br.org.oficinadasmeninas.presentation.controller;

import br.org.oficinadasmeninas.domain.statistics.service.IStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/statistics")
@PreAuthorize("hasRole('ADMIN')")
public class StatisticsController extends BaseController {

    private final IStatisticsService statisticsService;

    public StatisticsController(IStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Operation(summary = "Obter big numbers sobre as doações e doadores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros de data inválidos")
    })
    @PreAuthorize("isAnonymous()")
    @GetMapping("indicators")
    public ResponseEntity<?> getIndicatorsByPeriod(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalDate endDate
    ) {
        return handle(() -> statisticsService.getIndicatorsByPeriod(startDate, endDate));
    }

    @Operation(summary = "Obter doações categorizadas pelo tipo de doação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros de data inválidos")
    })
    @PreAuthorize("isAnonymous()")
    @GetMapping("donations/distribution")
    public ResponseEntity<?> getDonationTypeDistributionByPeriod(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalDate endDate
    ) {
        return handle(() -> statisticsService.getDonationTypeDistributionByPeriod(startDate, endDate));
    }

    @Operation(summary = "Obter histórico de doações agrupadas por tipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Série temporal retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    @PreAuthorize("isAnonymous()")
    @GetMapping("donations")
    public ResponseEntity<?> getDonationsByPeriod(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "month") String groupBy
    ) {
        return handle(() -> statisticsService.getDonationsByPeriod(startDate, endDate, groupBy));
    }
}
