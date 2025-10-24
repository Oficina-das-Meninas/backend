package br.org.oficinadasmeninas.infra.recaptcha;

import br.org.oficinadasmeninas.infra.recaptcha.dto.CaptchaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CaptchaService {

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean isCaptchaValid(String captchaToken) {
        if (captchaToken == null || captchaToken.isBlank()) {
            return false;
        }

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> requestBody = Map.of(
                "secret", recaptchaSecret,
                "response", captchaToken
        );

        CaptchaResponse response = restTemplate.postForObject(
                VERIFY_URL + "?secret={secret}&response={response}",
                null,
                CaptchaResponse.class,
                requestBody
        );

        return response != null && response.success();
    }
}