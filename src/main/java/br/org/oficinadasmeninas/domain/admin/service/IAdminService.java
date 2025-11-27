package br.org.oficinadasmeninas.domain.admin.service;

import java.util.UUID;

import br.org.oficinadasmeninas.domain.admin.Admin;
import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.CreateAdminDto;
import br.org.oficinadasmeninas.domain.admin.dto.UpdateAdminDto;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

/**
 * Interface para operações relacionadas à gestão de administradores.
 * <p>
 * Define contratos para criação, consulta e atualização de administradores.
 */
public interface IAdminService {

    /**
     * Cria e insere um novo administrador no sistema.
     *
     * @param admin objeto contendo os dados necessários para criação
     * @return identificador único do administrador criado
     */
    UUID insert(CreateAdminDto admin);

    /**
     * Atualiza os dados de um administrador existente.
     *
     * @param id    identificador único do administrador a ser atualizado
     * @param admin objeto contendo os novos dados do administrador
     */
    UUID update(UUID id, UpdateAdminDto admin);

    /**
     * Exclui um admin com base no seu identificador.
     *
     * @param id identificador único ({@link UUID}) do admin a ser removida; não deve ser {@code null}
     * @return o identificador único ({@link UUID}) do admin excluída
     * @throws IllegalArgumentException se {@code id} for {@code null}
     * @throws IllegalStateException se a categoria não existir
     */
    UUID deleteById(UUID id);
    
    /**
     * Retorna os administradores filtrados conforme os critérios de pesquisa.
     *
     * @param searchTerm string de pesquisa
     * @param page número da página
     * @param pageSize quantidade de resultados por página
     * @return página de administradores filtrados conforme os critérios informados
     */
    PageDTO<Admin> findByFilter(String searchTerm, int page, int pageSize);

    /**
     * Busca um administrador pelo seu identificador único.
     *
     * @param id identificador único do administrador
     * @return objeto de resposta com os dados do administrador encontrado,
     * ou {@code null} se não existir
     */
    AdminDto findById(UUID id);

    /**
     * Busca um administrador pelo endereço de e-mail.
     *
     * @param email endereço de e-mail do administrador
     * @return objeto de resposta com os dados do administrador encontrado,
     * ou {@code null} se não existir
     */
    AdminDto findByEmail(String email);
    
    /**
     * Atualiza a senha do administrador.
     *
     * @param id identificador único do administrador
     * @param encodedPassword nova senha encriptada
     */
    void updatePassword(UUID id, String encodedPassword);
}
