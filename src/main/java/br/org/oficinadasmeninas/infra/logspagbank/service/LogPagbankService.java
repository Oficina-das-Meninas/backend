package br.org.oficinadasmeninas.infra.logspagbank.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.org.oficinadasmeninas.infra.logspagbank.dto.CreateLogPagbank;
import br.org.oficinadasmeninas.infra.logspagbank.repository.LogPagbankRespository;

@Service
public class LogPagbankService {
	
	private final LogPagbankRespository logPagbankRespository;
	
	public LogPagbankService(LogPagbankRespository logPagbankRespository) {
		this.logPagbankRespository = logPagbankRespository;
	}

	public UUID createLogPagbank(CreateLogPagbank log) {
		return logPagbankRespository.insert(log);
	}

}
