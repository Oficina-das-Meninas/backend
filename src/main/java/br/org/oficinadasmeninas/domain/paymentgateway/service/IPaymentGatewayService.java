package br.org.oficinadasmeninas.domain.paymentgateway.service;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.CheckoutNotificationDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentNotificationDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.RequestCreateCheckoutDto;
import br.org.oficinadasmeninas.domain.paymentgateway.dto.checkout.ResponseCreateCheckoutDto;
 import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.RequestSubscriptionIdCustomer;
import br.org.oficinadasmeninas.infra.paymentgateway.pagbank.dto.ResponseWebhookCustomer;

import java.util.UUID;

/**
 * Define as operações relacionadas à integração com o gateway de pagamentos do sistema.
 * <p>
 * Essa interface é responsável por gerenciar o ciclo de vida dos checkouts e pagamentos,
 * incluindo a criação, cancelamento e atualização de status de transações.
 */
public interface IPaymentGatewayService {

	/**
	 * Cria um novo checkout no gateway de pagamentos.
	 * <p>
	 * O checkout é responsável por iniciar o processo de pagamento de uma doação,
	 * vinculando informações do cliente, assinatura e valores.
	 *
	 * @param responseCreateCheckoutDto objeto contendo os dados necessários
	 *                                  para criação do checkout, incluindo cliente,
	 *                                  assinatura e valor da doação
	 * @return objeto {@link ResponseCreateCheckoutDto} contendo o link de pagamento
	 *         e o identificador único do checkout criado
	 */
	ResponseCreateCheckoutDto createCheckout(RequestCreateCheckoutDto responseCreateCheckoutDto);

	/**
	 * Cancela um checkout existente no gateway de pagamentos.
	 * <p>
	 * Essa operação interrompe o processo de pagamento antes de sua conclusão.
	 *
	 * @param checkoutId identificador único do checkout a ser cancelado
	 */
	void cancelCheckout(String checkoutId);

	/**
	 * Atualiza o status de um pagamento específico.
	 * <p>
	 * Essa operação é geralmente chamada após o recebimento de notificações (webhooks)
	 * do gateway de pagamento, refletindo mudanças no status da transação.
	 *
	 * @param paymentId     identificador único do pagamento no sistema interno
	 * @param paymentStatus novo status do pagamento ({@link PaymentStatusEnum})
	 * @param paymentMethod método de pagamento utilizado ({@link PaymentMethodEnum})
	 * @param recurring     indica se o pagamento faz parte de uma recorrência
	 * @param customer      informações do cliente associadas ao pagamento
	 */
	void updatePaymentStatus(UUID paymentId, PaymentStatusEnum paymentStatus, PaymentMethodEnum paymentMethod,
							 boolean recurring, ResponseWebhookCustomer customer);

	/**
	 * Atualiza o status de um checkout existente no gateway de pagamentos.
	 * <p>
	 * Essa operação permite sincronizar o estado do checkout com o sistema interno
	 * após o processamento de um pagamento.
	 *
	 * @param checkoutId    identificador único do checkout
	 * @param paymentId     identificador do pagamento associado
	 * @param paymentStatus novo status do pagamento ({@link PaymentStatusEnum})
	 */
	void updateCheckoutStatus(String checkoutId, UUID paymentId, PaymentStatusEnum paymentStatus);
	
	/**
	 * Busca junto a plataforma de pagamentos o ID intenro da assinatura deles
	 * <p>
	 *	 *
	 * @param customer customer
	 */
	
	String findSubscriptionId(RequestSubscriptionIdCustomer customer);

    void notifyPayment(PaymentNotificationDto request);

    void notifyCheckout(CheckoutNotificationDto request);
	
}