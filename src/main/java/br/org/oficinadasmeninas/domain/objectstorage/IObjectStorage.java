package br.org.oficinadasmeninas.domain.objectstorage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Serviço responsável pelas operações de armazenamento de objetos,
 * incluindo upload, remoção e manipulação de arquivos relacionados
 * ao módulo de transparência.
 * <p>
 * Define utilitários para sanitização de nomes de arquivos e tratamento
 * de uploads específicos (como imagens com transparência).
 * </p>
 */
public interface IObjectStorage {

    /**
     * Sanitiza o nome de um arquivo, removendo ou substituindo caracteres inválidos
     * para garantir compatibilidade com sistemas de arquivos e URLs.
     *
     * @param fileName nome original do arquivo; não deve ser {@code null}
     * @return nome do arquivo sanitizado, nunca {@code null}
     * @throws IllegalArgumentException se {@code fileName} for {@code null} ou vazio
     */
    String sanitizeFileName(String fileName);

    /**
     * Realiza o upload de um fluxo de dados para o armazenamento, utilizando a chave informada.
     *
     * @param key identificador único (chave) do objeto no armazenamento; não deve ser {@code null}
     * @param data fluxo de dados contendo o conteúdo a ser enviado; não deve ser {@code null}
     * @param contentType tipo MIME do conteúdo (ex.: {@code "image/png"}); pode ser {@code null} se não aplicável
     * @throws IOException se ocorrer falha de leitura ou escrita durante o upload
     * @throws IllegalArgumentException se {@code key} ou {@code data} forem {@code null}
     */
    void upload(String key, InputStream data, String contentType) throws IOException;

    /**
     * Realiza o upload de um arquivo multipart, permitindo definir se ele deve ser público.
     * <p>
     * A implementação é responsável por definir a chave ou caminho de armazenamento.
     * </p>
     *
     * @param file arquivo multipart a ser enviado; não deve ser {@code null}
     * @param isPublic indica se o arquivo deve ser acessível publicamente; não deve ser {@code null}
     * @throws IOException se ocorrer falha de leitura ou escrita durante o upload
     * @throws IllegalArgumentException se {@code file} ou {@code isPublic} forem {@code null}
     */
    void upload(MultipartFile file, Boolean isPublic) throws IOException;

    /**
     * Realiza o upload de um arquivo multipart utilizando um nome de arquivo explícito.
     *
     * @param file arquivo multipart a ser enviado; não deve ser {@code null}
     * @param fileName nome que será utilizado no armazenamento; não deve ser {@code null}
     * @param isPublic indica se o arquivo deve ser acessível publicamente; não deve ser {@code null}
     * @throws IOException se ocorrer falha de leitura ou escrita durante o upload
     * @throws IllegalArgumentException se algum parâmetro obrigatório for {@code null}
     */
    void upload(MultipartFile file, String fileName, Boolean isPublic) throws IOException;

    /**
     * Realiza o upload de um arquivo de transparência (por exemplo, imagem PNG com canal alfa)
     * e retorna a URL ou chave gerada no armazenamento.
     *
     * @param file arquivo multipart contendo a imagem ou conteúdo a ser armazenado; não deve ser {@code null}
     * @param isImage indica se o arquivo deve ser tratado como imagem; se {@code false}, outro processamento pode ser aplicado
     * @return URL ou chave única do arquivo armazenado; nunca {@code null}
     * @throws IOException se ocorrer falha de leitura, escrita ou processamento durante o upload
     * @throws IllegalArgumentException se {@code file} for {@code null}
     */
    String uploadTransparencyFile(MultipartFile file, boolean isImage) throws IOException;

    /**
     * Remove um arquivo de transparência previamente armazenado.
     *
     * @param fileUrl URL ou chave do arquivo a ser removido; não deve ser {@code null}
     * @throws IOException se ocorrer falha durante a remoção
     * @throws IllegalArgumentException se {@code fileUrl} for {@code null} ou vazio
     */
    void deleteTransparencyFile(String fileUrl) throws IOException;
}
