package br.org.oficinadasmeninas.domain.payment.repository;

import br.org.oficinadasmeninas.domain.payment.Payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
     * @return {@link Payment} Objeto Payment com ID gerado
     */
    Payment insert(Payment payment);

    /**
     * Atualiza o status de um pagamento existente.
     *
     * @param payment Entidade {@link Payment} a ser atualiza o status;
     */
    Payment updateStatus(Payment payment);

    void cancelPaymentByDonationId(UUID donationId);

    /**
     * Atualiza a data de um pagamento existente.
     *
     * @param payment Entidade {@link Payment} a ser atualiza a data de pagamento;
     */
    Payment updateDate(Payment payment);
}
