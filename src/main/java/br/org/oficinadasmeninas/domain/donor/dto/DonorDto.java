package br.org.oficinadasmeninas.domain.donor.dto;

import java.util.UUID;

public record DonorDto(UUID id,
                       String name,
                       String email,
                       String phone,
                       String badge,
                       int points,
                       double totalDonated) {

}