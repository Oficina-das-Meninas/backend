package br.org.oficinadasmeninas.domain.event.service;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.domain.event.dto.CreateEventDto;
import br.org.oficinadasmeninas.domain.event.dto.GetEventDto;
import br.org.oficinadasmeninas.domain.event.dto.UpdateEventDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

import java.io.IOException;
import java.util.UUID;

/**
 * Interface para operações relacionadas à gestão de eventos.
 *
 * Define contratos para criação, consulta, atualização e exclusão de eventos.
 */
public interface IEventService {

    /**
     * Cria um novo evento no sistema.
     *
     * @param eventDto objeto contendo os dados necessários para criação do evento
     * @return evento criado com seus respectivos identificadores e metadados
     * @throws IOException em caso de erro de leitura/gravação de dados (ex: upload de arquivos relacionados)
     */
    Event insert(CreateEventDto eventDto) throws IOException;

    /**
     * Atualiza os dados de um evento existente.
     *
     * @param id              identificador único do evento a ser atualizado
     * @param updateEventDto  objeto contendo os novos dados do evento
     * @return evento atualizado
     * @throws Exception em caso de erro de validação, inexistência do evento ou falha durante a atualização
     */
    Event update(UUID id, UpdateEventDto updateEventDto) throws Exception;

    /**
     * Exclui permanentemente um evento pelo seu identificador.
     * <p>
     * Observação: a exclusão pode impactar dados relacionados (ex: inscrições,
     * documentos ou mídias associadas). Recomenda-se validar dependências antes da operação.
     * </p>
     *
     * @param id identificador único do evento
     * @throws Exception em caso de inexistência do evento ou falha durante a exclusão
     */
    void deleteById(UUID id) throws Exception;

    /**
     * Busca um evento pelo seu identificador único.
     *
     * @param id identificador único do evento
     * @return evento encontrado
     * @throws Exception caso o evento não seja encontrado ou ocorra algum erro inesperado
     */
    Event findById(UUID id) throws Exception;

    /**
     * Retorna eventos filtrados conforme os critérios de pesquisa.
     *
     * @param getEventDto objeto contendo critérios de filtragem e paginação
     * @return página de eventos filtrados conforme os critérios informados
     */
    PageDTO<Event> getFilteredEvents(GetEventDto getEventDto);
}
