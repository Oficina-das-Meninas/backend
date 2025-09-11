package br.org.oficinadasmeninas.domain.donation.dto.checkout;

import java.util.List;

public record ResponseCreateCheckoutDTO(
        String id,
        List<ResponseLinksDTO> links,
        CheckoutStatusEnum status
) {}
