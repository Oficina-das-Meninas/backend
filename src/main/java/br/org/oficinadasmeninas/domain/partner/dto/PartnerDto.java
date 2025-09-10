package br.org.oficinadasmeninas.domain.partner.dto;

import br.org.oficinadasmeninas.domain.partner.Partner;

import java.util.UUID;

public record PartnerDto (
        UUID id,
        String previewUrl,
        String name
) {

    public static PartnerDto fromPartner(Partner partner) {
        return new PartnerDto(
                partner.getId(),
                partner.getPreviewUrl(),
                partner.getName()
        );
    }
}