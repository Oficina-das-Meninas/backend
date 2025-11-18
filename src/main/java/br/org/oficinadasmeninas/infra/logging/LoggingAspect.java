package br.org.oficinadasmeninas.infra.logging;


import br.org.oficinadasmeninas.domain.resources.LogTemplate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspecto responsável por realizar logging automático em métodos ou classes anotados com {@link Logging}.
 * <p>
 * Funcionalidades aplicadas:
 * <ul>
 *   <li>Registra início e término de execução do método</li>
 *   <li>Mede o tempo de execução do método (em milissegundos)</li>
 *   <li>Registra a ocorrência de exceções com stack trace</li>
 * </ul>
 *
 * O comportamento é aplicado quando:
 * <ul>
 *   <li>O método está anotado com {@link Logging}</li>
 *   <li>A classe está anotada com {@link Logging} (aplica a todos os métodos públicos)</li>
 * </ul>
 *
 * A anotação funciona com qualquer método público de um bean gerenciado pelo Spring.
 *
 * @see Logging
 */
@Aspect
@Component
public class LoggingAspect {

    @Around("@annotation(br.org.oficinadasmeninas.infra.logging.Logging) || " +
            "@within(br.org.oficinadasmeninas.infra.logging.Logging)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        var targetClass = joinPoint.getTarget().getClass();
        var log = LoggerFactory.getLogger(targetClass);

        var serviceName = targetClass.getSimpleName();
        var methodName = joinPoint.getSignature().getName();

        var start = System.currentTimeMillis();

        try {
            var object = joinPoint.proceed();

            var duration = System.currentTimeMillis() - start;

            log.info(
                    LogTemplate.END_METHOD_WITH_MESSAGE,
                    serviceName,
                    methodName,
                    "Execução finalizada em " + duration + "ms"
            );

            return object;
        } catch (Throwable ex) {

            log.error(
                LogTemplate.ERROR_METHOD,
                serviceName,
                methodName,
                ex.getMessage(),
                ex
            );

            throw ex;
        }
    }
}
