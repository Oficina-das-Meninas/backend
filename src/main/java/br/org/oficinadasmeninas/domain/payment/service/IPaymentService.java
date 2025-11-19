package br.org.oficinadasmeninas.domain.payment.service;

import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;
import br.org.oficinadasmeninas.domain.payment.dto.CreatePaymentDto;
import br.org.oficinadasmeninas.domain.payment.dto.PaymentDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Define as operações relacionadas ao gerenciamento de pagamentos no sistema.
 * <p>
 * Essa interface é responsável por criar, atualizar e consultar informações
 * sobre pagamentos associados a doações, integrando-se aos gateways de pagamento.
 */
public interface IPaymentService {

	/**
	 * Cria e insere um novo pagamento no sistema.
	 * <p>
	 * Essa operação é utilizada para registrar uma tentativa de pagamento
	 * vinculada a uma doação e a um gateway de pagamento específico.
	 *
	 * @param payment objeto contendo os dados necessários para criação do pagamento,
	 *                incluindo o identificador da doação, gateway, método e status inicial
	 * @return objeto {@link PaymentDto} representando o pagamento criado
	 */
	PaymentDto insert(CreatePaymentDto payment);

	/**
	 * Atualiza o status de um pagamento existente.
	 * <p>
	 * Essa operação é geralmente utilizada para refletir mudanças de estado
	 * notificadas pelo gateway de pagamento (ex.: de “WAITING” para “PAID”).
	 *
	 * @param id     identificador único do pagamento
	 * @param status novo status do pagamento ({@link PaymentStatusEnum})
	 */
	void updateStatus(UUID id, PaymentStatusEnum status);
	
	/**
	 * Atualiza o dia de pagamento de um registro existente.
	 * <p>
	 * Pode ser utilizada para atribuir a data de pagamento de um registro existente
	 *
	 * @param id     identificador único do pagamento
	 * @param date data  ({@link LocalDateTime})
	 */
    
    void updatePaymentDate(UUID id, LocalDateTime date);

	/**
	 * Busca todos os pagamentos associados a uma doação específica.
	 *
	 * @param donationId identificador único da doação
	 * @return lista contendo os {@link PaymentDto} vinculados à doação,
	 *         podendo estar vazia caso não existam pagamentos
	 */
	List<PaymentDto> findByDonationId(UUID donationId);

	/**
	 * Busca um pagamento pelo seu identificador único.
	 *
	 * @param id identificador único do pagamento
	 * @return objeto {@link PaymentDto} representando o pagamento encontrado,
	 *         ou {@code null} caso não exista
	 */
	PaymentDto findById(UUID id);
}