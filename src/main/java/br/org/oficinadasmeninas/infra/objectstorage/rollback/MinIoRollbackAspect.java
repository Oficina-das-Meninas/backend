package br.org.oficinadasmeninas.infra.objectstorage.rollback;


import br.org.oficinadasmeninas.domain.objectstorage.IObjectStorage;
import br.org.oficinadasmeninas.infra.shared.exception.ObjectStorageException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MinIoRollbackAspect {

    private final MinIoRollbackContext context;
    private final IObjectStorage objectStorage;

    public MinIoRollbackAspect(MinIoRollbackContext context, IObjectStorage objectStorage) {
        this.context = context;
        this.objectStorage = objectStorage;
    }

    @AfterReturning("@annotation(br.org.oficinadasmeninas.infra.objectstorage.rollback.MinIoTransactional)")
    public void onSuccess() {
        context.getAndClear();
    }


    @AfterThrowing(value = "@annotation(br.org.oficinadasmeninas.infra.objectstorage.rollback.MinIoTransactional)", throwing = "ex")
    public void onError(Exception ex) {

        var files = context.getAndClear();

        for (var file : files) {

            try {
                objectStorage.deleteFile(file);
            } catch (ObjectStorageException objectStorageException) {
                log.warn("Erro ao remover arquivo do bucket após exception. path={}", file, objectStorageException);
            }
        }
    }
}


