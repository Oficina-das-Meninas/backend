package br.org.oficinadasmeninas.domain.donation.service;

import br.org.oficinadasmeninas.domain.donation.dto.checkout.RequestCheckoutDTO;
import br.org.oficinadasmeninas.domain.donation.dto.checkout.ResponseCreateCheckoutDTO;

/**
 * Interface para operações relacionadas a doações.
 *
 * Define contratos para a criação de processos de checkout de doações.
 */
public interface IDonationService {

    /**
     * Cria um processo de checkout para efetivação de uma doação.
     *
     * @param request objeto contendo os dados necessários para a criação do checkout
     *                (ex: valor da doação, forma de pagamento, informações do doador)
     * @return objeto de resposta contendo os dados do checkout gerado,
     *         incluindo possíveis links ou identificadores externos
     */
    ResponseCreateCheckoutDTO createCheckout(RequestCheckoutDTO request);

}
