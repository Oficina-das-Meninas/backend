package br.org.oficinadasmeninas.infra.logspagbank.repository;

public class LogPagbankBuilder {
	public static final String CREATE_LOGPAGBANK = """
				       INSERT INTO public.logs_pagbank(
			 id, label, data_time, body)
			VALUES (?, ?, ?, ?);
				    """;
}
