package br.org.oficinadasmeninas.domain.donation.service;

import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationDto;
import br.org.oficinadasmeninas.domain.payment.PaymentMethodEnum;

import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável pelo gerenciamento de doações.
 * <p>
 * Define operações para criação, consulta e atualização de status de doações,
 * garantindo o controle e rastreabilidade das contribuições no sistema.
 * </p>
 */
public interface IDonationService {

    /**
     * Retorna todas as doações cadastradas no sistema.
     *
     * @return uma lista de {@link DonationDto} representando as doações encontradas;
     * pode ser vazia se não houver registros
     */
    List<DonationDto> findAll();

    /**
     * Recupera os detalhes de uma doação específica com base em seu identificador.
     *
     * @param id identificador único ({@link UUID}) da doação a ser consultada; não deve ser {@code null}
     * @return um {@link DonationDto} com os dados da doação, ou {@code null} se não encontrada
     * @throws IllegalArgumentException se {@code id} for {@code null}
     */
    DonationDto findById(UUID id);

    /**
     * Cria uma nova doação a partir dos dados fornecidos.
     *
     * @param donation objeto {@link CreateDonationDto} contendo as informações da nova doação; não deve ser {@code null}
     * @return um {@link DonationDto} representando a doação criada, incluindo seu identificador gerado
     * @throws IllegalArgumentException se {@code donation} for {@code null} ou contiver dados inválidos
     */
    DonationDto insert(CreateDonationDto donation);


    /**
     * Atualiza o método de pagamento de uma doação existente.
     *
     * @param id identificador único ({@link UUID}) da doação a ser atualizada; não deve ser {@code null}
     * @param method novo método {@link PaymentMethodEnum} a ser atribuído; não deve ser {@code null}
     */
    void updateMethod(UUID id, PaymentMethodEnum method);
}
