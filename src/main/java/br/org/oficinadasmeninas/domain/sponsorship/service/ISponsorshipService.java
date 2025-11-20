package br.org.oficinadasmeninas.domain.sponsorship.service;

import br.org.oficinadasmeninas.domain.sponsorship.dto.SponsorshipDto;
import br.org.oficinadasmeninas.domain.sponsorship.dto.UpdateSponsorshipDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Define as operações relacionadas aos patrocínios (sponsorships) do sistema.
 * <p>
 * Essa interface é responsável pela criação, atualização, ativação e consulta
 * de informações sobre patrocínios associados a usuários.
 */
public interface ISponsorshipService {

    /**
     * Cria e insere um novo registro de patrocínio no sistema.
     *
     * @param sponsorship objeto contendo os dados necessários para criação do patrocínio
     * @return identificador único (UUID) do patrocínio criado
     */
    UUID insert(SponsorshipDto sponsorship);

    /**
     * Atualiza os dados de um patrocínio existente.
     *
     * @param sponsorship objeto contendo as informações atualizadas do patrocínio
     */
    void update(UpdateSponsorshipDto sponsorship);

    /**
     * Ativa o patrocínio mais recente associado a um usuário específico.
     * <p>
     * <strong>Comportamento:</strong> Este método ativa apenas o último patrocínio criado
     * (com base no ID mais recente) para o usuário. Todos os outros patrocínios do mesmo
     * usuário permanecerão com seu status atual.
     *
     * @param userId identificador único do usuário cujo patrocínio mais recente deve ser ativado
     */
    void activateByUserId(UUID userId);

    /**
     * Busca um patrocínio pelo seu identificador único.
     *
     * @param id identificador único do patrocínio
     * @return um {@link Optional} contendo o {@link SponsorshipDto} encontrado,
     *         ou {@link Optional#empty()} se não existir
     */
    Optional<SponsorshipDto> findById(UUID id);

    /**
     * Busca todos os patrocínios associados a um usuário específico.
     *
     * @param id identificador único do usuário
     * @return lista contendo todos os patrocínios vinculados ao usuário
     */
    List<SponsorshipDto> findByUserId(UUID id);

    /**
     * Busca o patrocínio ativo de um usuário.
     *
     * @param id identificador único do usuário
     * @return um {@link Optional} contendo o {@link SponsorshipDto} ativo,
     *         ou {@link Optional#empty()} se não houver patrocínio ativo
     */
    Optional<SponsorshipDto> findActiveByUserId(UUID id);

    /**
     * Busca um patrocínio a partir do identificador da assinatura.
     *
     * @param subscriptionId identificador único da assinatura
     * @return um {@link Optional} contendo o {@link SponsorshipDto} correspondente,
     *         ou {@link Optional#empty()} se não existir
     */
    Optional<SponsorshipDto> findBySubscriptionId(String subscriptionId);
}
