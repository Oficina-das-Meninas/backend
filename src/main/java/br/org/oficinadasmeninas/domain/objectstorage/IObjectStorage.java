package br.org.oficinadasmeninas.domain.objectstorage;

import br.org.oficinadasmeninas.infra.exceptions.ObjectStorageException;
import org.springframework.web.multipart.MultipartFile;

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
     */
    String sanitizeFileName(String fileName);

    /**
     * Realiza o upload de um arquivo multipart, permitindo definir se ele deve ser público.
     * <p>
     * A implementação é responsável por definir a chave ou caminho de armazenamento.
     * </p>
     *
     * @param file arquivo multipart a ser enviado; não deve ser {@code null}
     * @param isPublic indica se o arquivo deve ser acessível publicamente; não deve ser {@code null}
     * @throws ObjectStorageException se ocorrer falha de leitura ou escrita durante o upload
     */
    void upload(MultipartFile file, Boolean isPublic);

    /**
     * Realiza o upload de um arquivo multipart utilizando um nome de arquivo explícito.
     *
     * @param file arquivo multipart a ser enviado; não deve ser {@code null}
     * @param fileName nome que será utilizado no armazenamento; não deve ser {@code null}
     * @param isPublic indica se o arquivo deve ser acessível publicamente; não deve ser {@code null}
     * @throws ObjectStorageException se ocorrer falha de leitura ou escrita durante o upload
     */
    void uploadWithName(MultipartFile file, String fileName, Boolean isPublic);

    /**
     * Realiza o upload de um arquivo com a rota de pastas e o nome do arquivo (por exemplo, imagem PNG com canal alfa)
     * e retorna a URL ou chave gerada no armazenamento.
     *
     * @param file arquivo multipart contendo a imagem ou conteúdo a ser armazenado; não deve ser {@code null}
     * @param isImage indica se o arquivo deve ser tratado como imagem; se {@code false}, outro processamento pode ser aplicado
     * @return URL ou chave única do arquivo armazenado; nunca {@code null}
     * @throws ObjectStorageException se ocorrer falha de leitura, escrita ou processamento durante o upload
     */
    String uploadWithFilePath(MultipartFile file, boolean isImage);

    /**
     * Remove um arquivo previamente armazenado.
     *
     * @param fileUrl URL ou chave do arquivo a ser removido; não deve ser {@code null}
     * @throws ObjectStorageException se ocorrer falha durante a remoção
     */
    void deleteFileByPath(String fileUrl);
}
