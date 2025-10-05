package br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto;


public record RequestCreateCheckoutCustomer(String name, String email, String tax_id, RequestCreateCheckoutCustomerPhone phone) {}

