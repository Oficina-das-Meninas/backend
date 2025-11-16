package br.org.oficinadasmeninas.domain.sponsor.service;

import br.org.oficinadasmeninas.domain.sponsor.dto.SponsorDto;
import br.org.oficinadasmeninas.domain.sponsor.dto.UpdateSponsorDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Define as operações relacionadas aos patrocinadores (sponsors) do sistema.
 * <p>
 * Essa interface é responsável pela criação, atualização, ativação e consulta
 * de informações sobre patrocínios associados a usuários.
 */
@Repository
public interface ISponsorService {

    /**
     * Cria e insere um novo registro de patrocinador no sistema.
     *
     * @param sponsor objeto contendo os dados necessários para criação do patrocinador,
     *                incluindo o valor mensal, dia de cobrança e identificador do usuário
     * @return identificador único (UUID) do patrocinador criado
     */
    UUID insert(SponsorDto sponsor);

    /**
     * Atualiza os dados de um patrocinador existente.
     *
     * @param sponsor objeto contendo as informações atualizadas do patrocinador,
     *                incluindo o identificador, data de término, status e ID da assinatura
     */
    void update(UpdateSponsorDto sponsor);

    /**
     * Ativa o patrocínio associado a um usuário específico.
     *
     * @param userId identificador único do usuário cujo patrocínio deve ser ativado
     */
    void activateByUserId(UUID userId);

    /**
     * Busca um patrocinador pelo seu identificador único.
     *
     * @param id identificador único do patrocinador
     * @return um {@link Optional} contendo o {@link SponsorDto} encontrado,
     *         ou {@link Optional#empty()} se não existir
     */
    Optional<SponsorDto> findById(UUID id);

    /**
     * Busca todos os patrocínios associados a um usuário específico.
     *
     * @param id identificador único do usuário
     * @return lista contendo todos os patrocínios vinculados ao usuário,
     *         podendo estar vazia caso o usuário não possua registros
     */
    List<SponsorDto> findByUserId(UUID id);

    /**
     * Busca o patrocínio ativo de um usuário.
     *
     * @param id identificador único do usuário
     * @return um {@link Optional} contendo o {@link SponsorDto} ativo,
     *         ou {@link Optional#empty()} se não houver patrocínio ativo
     */
    Optional<SponsorDto> findActiveByUserId(UUID id);

    /**
     * Busca um patrocinador a partir do identificador de assinatura.
     *
     * @param subscriptionId identificador único da assinatura
     * @return um {@link Optional} contendo o {@link SponsorDto} correspondente,
     *         ou {@link Optional#empty()} se não existir
     */
    Optional<SponsorDto> findBySubscriptionId(UUID subscriptionId);
}
