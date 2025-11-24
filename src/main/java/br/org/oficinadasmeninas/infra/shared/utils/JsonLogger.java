package br.org.oficinadasmeninas.infra.shared.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonLogger {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void logRequest(String endpoint, Object requestBody) {
        try {
            String json = objectMapper.writeValueAsString(requestBody);
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║ PAGBANK REQUEST - " + endpoint);
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println(json);
            System.out.println("╚════════════════════════════════════════════════════════════════╝");
        } catch (Exception e) {
            System.out.println("Erro ao serializar request: " + e.getMessage());
        }
    }

    public static void logResponse(String endpoint, Object responseBody) {
        try {
            String json = objectMapper.writeValueAsString(responseBody);
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║ PAGBANK RESPONSE - " + endpoint);
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println(json);
            System.out.println("╚════════════════════════════════════════════════════════════════╝");
        } catch (Exception e) {
            System.out.println("Erro ao serializar response: " + e.getMessage());
        }
    }

    public static void logError(String endpoint, String error) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║ PAGBANK ERROR - " + endpoint);
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println(error);
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }
}

