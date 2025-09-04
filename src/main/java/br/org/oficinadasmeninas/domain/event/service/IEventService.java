package br.org.oficinadasmeninas.domain.event.service;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

public interface IEventService {
    PageDTO<Event> findAll(int page, int pageSize);
}