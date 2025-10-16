package br.org.oficinadasmeninas.domain.transparency.service;

import br.org.oficinadasmeninas.domain.transparency.dto.CreateCollaboratorRequestDto;

import java.util.UUID;

public interface ICollaboratorsService {

    UUID uploadCollaborator(CreateCollaboratorRequestDto request);

    UUID deleteCollaborator(UUID id);
}
