package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

import java.util.List;

public record ResponseCreateCheckoutPagbank(
   String id,
   List<ResponseCreateCheckoutLink> links,
   String status
) {}
