package br.org.oficinadasmeninas.infra.logspagbank.dto;

import java.time.LocalDateTime;

public record CreateLogPagbank(
		String label, LocalDateTime data, Object body
) {}
