package br.org.oficinadasmeninas.domain.event.service;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.util.UUID;

/**
 * Interface para operações relacionadas à gestão de eventos.
 * <p>
 * Define contratos para criação, consulta, atualização e exclusão de eventos.
 */
public interface IEventService {

    /**
     * Cria um novo evento no sistema.
     *
     * @param eventDto objeto contendo os dados necessários para criação do evento
     * @return evento criado com seus respectivos identificadores e metadados
     */
    UUID insert(CreateEventDto eventDto);

    /**
     * Atualiza os dados de um evento existente.
     *
     * @param id             identificador único do evento a ser atualizado
     * @param updateEventDto objeto contendo os novos dados do evento
     * @return evento atualizado
     */
    UUID update(UUID id, UpdateEventDto updateEventDto);

    /**
     * Exclui permanentemente um evento pelo seu identificador.
     * <p>
     * Observação: a exclusão pode impactar dados relacionados (ex: inscrições,
     * documentos ou mídias associadas). Recomenda-se validar dependências antes da operação.
     * </p>
     *
     * @param id identificador único do evento
     */
    UUID deleteById(UUID id);

    /**
     * Busca um evento pelo seu identificador único.
     *
     * @param id identificador único do evento
     * @return evento encontrado
     */
    Event findById(UUID id);

    /**
     * Retorna eventos filtrados conforme os critérios de pesquisa.
     *
     * @param getEventDto objeto contendo critérios de filtragem e paginação
     * @return página de eventos filtrados conforme os critérios informados
     */
    PageDTO<Event> findByFilter(GetEventDto getEventDto);
}
