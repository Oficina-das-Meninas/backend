package br.org.oficinadasmeninas.domain.donation.dto;

import java.util.UUID;

public record DonorInfoDto(String name, String email, String document, String phone, UUID id) {

}
