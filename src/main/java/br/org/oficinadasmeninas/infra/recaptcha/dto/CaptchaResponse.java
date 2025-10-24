package br.org.oficinadasmeninas.infra.recaptcha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CaptchaResponse(
        boolean success,
        @JsonProperty("challenge_ts") String challengeTs,
        String hostname,
        @JsonProperty("error-codes") List<String> errorCodes
) {}