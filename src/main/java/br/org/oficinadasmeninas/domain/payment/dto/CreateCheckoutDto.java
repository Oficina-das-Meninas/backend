package br.org.oficinadasmeninas.domain.payment.dto;

import java.util.List;

public record CreateCheckoutDto(String reference_id, CustomerDto customer, List<ItemDto> items) {

}
