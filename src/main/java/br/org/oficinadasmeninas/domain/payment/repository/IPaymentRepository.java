package br.org.oficinadasmeninas.domain.payment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.payment.Payment;
import br.org.oficinadasmeninas.domain.payment.PaymentStatusEnum;

/**
 * Repositório responsável pelas operações de persistência de pagamentos.
 * <p>
 * Define métodos para consulta, criação e atualização de status de pagamentos,
 * garantindo rastreabilidade e consistência das transações associadas a doações.
 * </p>
 */
public interface IPaymentRepository {

    /**
     * Busca um pagamento pelo seu identificador único.
     *
     * @param id identificador {@link UUID} do pagamento; não deve ser {@code null}
     * @return {@link Optional} contendo o {@link Payment} caso encontrado, ou {@link Optional#empty()} se não houver correspondência
     * @throws IllegalArgumentException se {@code id} for {@code null}
     */
    Optional<Payment> findById(UUID id);

    /**
     * Recupera todos os pagamentos associados a uma doação específica.
     *
     * @param donationId identificador {@link UUID} da doação; não deve ser {@code null}
     * @return lista de {@link Payment} relacionados à doação; pode estar vazia caso não haja pagamentos
     * @throws IllegalArgumentException se {@code donationId} for {@code null}
     */
    List<Payment> findByDonationId(UUID donationId);

    /**
     * Persiste um novo pagamento e retorna o identificador gerado.
     *
     * @param payment entidade {@link Payment} a ser criada; não deve ser {@code null}
     * @return {@link UUID} gerado para o pagamento criado
     * @throws IllegalArgumentException se {@code payment} for {@code null}
     */
    UUID create(Payment payment);

    /**
     * Atualiza o status de um pagamento existente.
     *
     * @param id identificador {@link UUID} do pagamento a ser atualizado; não deve ser {@code null}
     * @param status novo {@link PaymentStatusEnum} a ser aplicado; não deve ser {@code null}
     * @throws IllegalArgumentException se {@code id} ou {@code status} forem {@code null}
     */
    void updateStatus(UUID id, PaymentStatusEnum status);
}
