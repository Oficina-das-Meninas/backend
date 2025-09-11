package br.org.oficinadasmeninas.presentation.shared;

import java.util.List;

public record PageDTO<T>(
        List<T> data,
        long totalElements,
        int totalPages
) {
}
