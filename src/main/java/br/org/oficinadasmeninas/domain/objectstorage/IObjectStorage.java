package br.org.oficinadasmeninas.domain.objectstorage;

import org.springframework.web.multipart.MultipartFile;

import br.org.oficinadasmeninas.infra.shared.exception.ObjectStorageException;

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
     * Realiza o upload de um arquivo multipart utilizando um nome de arquivo explícito.
     *
     * @param file arquivo multipart a ser enviado; não deve ser {@code null}
     * @param isPublic indica se o arquivo deve ser acessível publicamente; não deve ser {@code null}
     * @throws ObjectStorageException se ocorrer falha de leitura ou escrita durante o upload
     * @return URL ou chave única do arquivo armazenado; nunca {@code null}
     */
    String uploadFile(MultipartFile file, Boolean isPublic);

    /**
     * Realiza o upload de um arquivo (documento ou foto) usado na transparencia
     * e retorna a URL ou chave gerada no armazenamento.
     *
     * @param file arquivo multipart contendo a imagem ou conteúdo a ser armazenado; não deve ser {@code null}
     * @param isImage indica se o arquivo deve ser tratado como imagem; se {@code false}, outro processamento pode ser aplicado
     * @return URL ou chave única do arquivo armazenado; nunca {@code null}
     * @throws ObjectStorageException se ocorrer falha de leitura, escrita ou processamento durante o upload
     */
    String uploadTransparencyFile(MultipartFile file, boolean isImage);

    /**
     * Remove um arquivo previamente armazenado.
     *
     * @param fileUrl URL ou chave do arquivo a ser removido; não deve ser {@code null}
     * @throws ObjectStorageException se ocorrer falha durante a remoção
     */
    void deleteFile(String fileUrl);
}
