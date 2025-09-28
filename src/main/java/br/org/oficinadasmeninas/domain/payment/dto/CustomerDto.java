package br.org.oficinadasmeninas.domain.payment.dto;

public record CustomerDto(String name, String email, String tax_id, CustomerPhoneDto phone) {

}
