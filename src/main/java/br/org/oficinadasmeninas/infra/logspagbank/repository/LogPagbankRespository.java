package br.org.oficinadasmeninas.infra.logspagbank.repository;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.oficinadasmeninas.infra.event.repository.EventQueryBuilder;
import br.org.oficinadasmeninas.infra.logspagbank.dto.CreateLogPagbank;


@Repository
public class LogPagbankRespository {
	
    private final JdbcTemplate Jdbc;
    private final ObjectMapper mapper = new ObjectMapper();
			
	public LogPagbankRespository(JdbcTemplate jdbc) {
		this.Jdbc = jdbc;
	}

	public UUID insert(CreateLogPagbank log) {
		UUID id = UUID.randomUUID();

				
        Jdbc.update(
                LogPagbankBuilder.CREATE_LOGPAGBANK,
                id,
                log.label(),
                log.data(),
                log.body().toString()         
        );

        return id;
    }
	
}
