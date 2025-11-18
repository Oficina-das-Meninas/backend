package br.org.oficinadasmeninas.infra.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca métodos ou classes para que tenham logging automático através do {@link LoggingAspect}.
 * <p>
 * Quando aplicada:
 * <ul>
 *   <li>Se adicionada em um método → somente aquele método será interceptado para logging.</li>
 *   <li>Se adicionada em uma classe → todos os métodos públicos da classe serão interceptados.</li>
 * </ul>
 *
 * O uso desta anotação evita a necessidade de chamadas explícitas de log dentro dos serviços.
 *
 * @see LoggingAspect
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Logging {
}