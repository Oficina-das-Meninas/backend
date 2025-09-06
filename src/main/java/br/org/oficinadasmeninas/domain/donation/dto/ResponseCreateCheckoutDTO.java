package br.org.oficinadasmeninas.domain.donation.dto;

import br.org.oficinadasmeninas.domain.donation.PaymentMethodEnum;

import java.util.List;

public record ResponseCreateCheckoutDTO(
        String id,
        List<ResponseLinksDTO> links,
        PaymentMethodEnum status
) {}
