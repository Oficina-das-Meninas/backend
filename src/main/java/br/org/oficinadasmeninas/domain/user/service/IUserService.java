package br.org.oficinadasmeninas.domain.user.service;

import br.org.oficinadasmeninas.domain.user.User;
import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UpdateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;

import java.util.List;
import java.util.UUID;

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
     * Ativa o usuário.
     *
     * @param id identificador único do usuário
     */
    void markUserAsVerified(UUID id);
    
    /**
     * Atualiza a senha do usuário.
     *
     * @param accountId identificador único da conta do usuário
     * @param encodedPassword nova senha encriptada
     */
    void updatePassword(UUID accountId, String encodedPassword);
  
    /**
     * Busca um usuário pela sessão.
     *
     * @param token token de sessão
     * @return objeto contendo os dados do usuário encontrado,
     *         ou {@code null} se não existir
     */
    UserDto findByUserSession();

    /**
     *
     * @param password
     * @return void se a senha passada no no parâmetro é igual à senha do usuário logado na sessão
     */
    Void verifyUserPassword(String password);


    /**
     * Ativa um usuário inativo com base no ID, e-mail e documento fornecidos.
     *
     * @param userId   o ID do usuário a ser ativado
     * @param email    o e-mail do usuário a ser ativado
     * @param document o documento do usuário a ser ativado
     */
    void activateUser(UUID userId, String email, String document);

    /**
     * Exclui um usuário inativo com base no ID fornecido.
     *
     * @param userId o ID do usuário a ser excluído
     */
    void deleteInactiveUser(UUID userId);

    /**
     * Busca usuários inativos pelo e-mail fornecido.
     *
     * @param email o e-mail dos usuários a serem buscados
     * @return uma lista de usuários inativos correspondentes ao e-mail fornecido
     */
    List<User> findInactiveUsersByDocument(String document);

    /**
     * Verifica se um documento está duplicado entre as contas ativas, excluindo um usuário específico.
     *
     * @param document      o documento a ser verificado
     * @param excludeUserId o ID do usuário a ser excluído da verificação
     * @return {@code true} se o documento estiver duplicado, {@code false} caso contrário
     */
    boolean isDocumentDuplicatedInActiveAccounts(String document, UUID excludeUserId);

    /**
     * Verifica se um e-mail está duplicado entre as contas ativas, excluindo um usuário específico.
     *
     * @param email         o e-mail a ser verificado
     * @param excludeUserId o ID do usuário a ser excluído da verificação
     * @return {@code true} se o e-mail estiver duplicado, {@code false} caso contrário
     */
    boolean isEmailDuplicatedInActiveAccounts(String email, UUID excludeUserId);
}