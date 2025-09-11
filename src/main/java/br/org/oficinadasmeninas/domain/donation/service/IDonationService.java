package br.org.oficinadasmeninas.domain.donation.service;

import br.org.oficinadasmeninas.domain.donation.dto.checkout.RequestCheckoutDTO;
import br.org.oficinadasmeninas.domain.donation.dto.checkout.ResponseCreateCheckoutDTO;

public interface IDonationService {
    ResponseCreateCheckoutDTO createCheckout(RequestCheckoutDTO request);
}
