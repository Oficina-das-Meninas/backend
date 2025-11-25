package br.org.oficinadasmeninas.domain.user.service;

import java.util.List;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UpdateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserRecurrencyDto;

/**
 * Interface para operações relacionadas à gestão de usuários.
 * <p>
 * Define contratos para criação, consulta e atualização de usuários.
 */
public interface IUserService {

    /**
     * Cria e insere um novo usuário no sistema.
     *
     * @param user objeto contendo os dados necessários para criação
     * @return objeto representando o usuário criado
     */
    UserDto insert(CreateUserDto user);

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id   identificador único do usuário a ser atualizado
     * @param user objeto contendo os novos dados do usuário
     */
    UUID update(UUID id, UpdateUserDto user);

    /**
     * Retorna todos os usuários cadastrados no sistema.
     *
     * @return lista contendo todos os usuários
     */
    List<UserDto> findAll();


    /**
     * Busca um usuário pelo seu identificador único.
     *
     * @param id identificador único do usuário
     * @return objeto contendo os dados do usuário encontrado,
     *         ou {@code null} se não existir
     */
    UserDto findByUserId(UUID id);

    /**
     * Busca um usuário pelo endereço de e-mail.
     *
     * @param email endereço de e-mail do usuário
     * @return objeto contendo os dados do usuário encontrado,
     *         ou {@code null} se não existir
     */
    UserDto findByEmail(String email);

    /**
     * Busca um usuário pelo documento.
     *
     * @param document documento do usuário
     * @return objeto contendo os dados do usuário encontrado,
     *         ou {@code null} se não existir
     */
    UserDto findByDocument(String document);
    
    /**
     * Busca um usuário pela sessão.
     *
     * @return objeto contendo os dados do usuário encontrado,
     *         ou {@code null} se não existir
     */
    UserDto findByUserSession();
    
    /**
     * Busca o apadrinhamento ativo do usuário da sessão
     * 
     * @return objeto contendo os dados do usuário encontrado,
     *         ou {@code null} se não existir
     */
    UserRecurrencyDto findActiveSponsorshipByUserSession();
}