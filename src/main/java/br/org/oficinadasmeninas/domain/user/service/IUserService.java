package br.org.oficinadasmeninas.domain.user.service;

import java.util.List;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UpdateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;

/**
 * Interface para operações relacionadas à gestão de usuários.
 *
 * Define contratos para criação, consulta e atualização de usuários.
 */
public interface IUserService {

    /**
     * Retorna todos os usuários cadastrados no sistema.
     *
     * @return lista contendo todos os usuários
     */
    List<UserDto> getAllUsers();

    /**
     * Busca um usuário pelo seu identificador único.
     *
     * @param id identificador único do usuário
     * @return objeto contendo os dados do usuário encontrado,
     *         ou {@code null} se não existir
     */
    UserDto getUserById(UUID id);

    /**
     * Busca um usuário pelo endereço de e-mail.
     *
     * @param email endereço de e-mail do usuário
     * @return objeto contendo os dados do usuário encontrado,
     *         ou {@code null} se não existir
     */
    UserDto getUserByEmail(String email);

    /**
     * Cria e insere um novo usuário no sistema.
     *
     * @param user objeto contendo os dados necessários para criação
     * @return objeto representando o usuário criado
     */
    UserDto createUser(CreateUserDto user);

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id   identificador único do usuário a ser atualizado
     * @param user objeto contendo os novos dados do usuário
     */
    void updateUser(UUID id, UpdateUserDto user);

}
