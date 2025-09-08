package br.org.oficinadasmeninas.domain.event.repository;

import br.org.oficinadasmeninas.domain.event.Event;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

public interface IEventRepository {
    PageDTO<Event> findAll(int page, int pageSize);
}