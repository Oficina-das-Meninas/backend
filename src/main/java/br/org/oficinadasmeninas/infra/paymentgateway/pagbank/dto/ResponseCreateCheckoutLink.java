package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;

public record ResponseCreateCheckoutLink(String rel, String href, String method) {
}
