package br.org.oficinadasmeninas.domain.donation.service;

import java.util.List;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.donation.DonationStatusEnum;
import br.org.oficinadasmeninas.domain.donation.dto.CreateDonationDto;
import br.org.oficinadasmeninas.domain.donation.dto.DonationDto;

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
    List<DonationDto> getAllDonations();

    /**
     * Recupera os detalhes de uma doação específica com base em seu identificador.
     *
     * @param id identificador único ({@link UUID}) da doação a ser consultada; não deve ser {@code null}
     * @return um {@link DonationDto} com os dados da doação, ou {@code null} se não encontrada
     * @throws IllegalArgumentException se {@code id} for {@code null}
     */
    DonationDto getDonationById(UUID id);

    /**
     * Cria uma nova doação a partir dos dados fornecidos.
     *
     * @param donation objeto {@link CreateDonationDto} contendo as informações da nova doação; não deve ser {@code null}
     * @return um {@link DonationDto} representando a doação criada, incluindo seu identificador gerado
     * @throws IllegalArgumentException se {@code donation} for {@code null} ou contiver dados inválidos
     */
    DonationDto createDonation(CreateDonationDto donation);

    /**
     * Atualiza o status de uma doação existente.
     *
     * @param id identificador único ({@link UUID}) da doação a ser atualizada; não deve ser {@code null}
     * @param status novo status {@link DonationStatusEnum} a ser atribuído; não deve ser {@code null}
     * @throws IllegalArgumentException se {@code id} ou {@code status} forem {@code null}
     * @throws IllegalStateException se a doação não existir ou não puder ter o status alterado
     */
    void updateDonationStatus(UUID id, DonationStatusEnum status);
}
