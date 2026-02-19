package com.master.springboot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@Service
public class AuthCaptchaService {
    @Value("${recaptcha.secret.key}")
    private String secretKey;

    @Value("${recaptcha.url}")
    private String recaptchaUrl;

    public boolean verifyRecaptcha(String recaptchaResponse) {
        System.out.println("\n=== VERIFICANDO CAPTCHA ===");

        if (recaptchaResponse == null || recaptchaResponse.isEmpty()) {
            System.out.println("❌ Token vacío");
            return false;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Crear el cuerpo de la petición
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("secret", secretKey);
            map.add("response", recaptchaResponse);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            System.out.println("📤 Enviando petición POST a Google...");
            System.out.println("Secret: " + secretKey);
            System.out.println("Response length: " + recaptchaResponse.length());

            // Hacer la petición POST
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    recaptchaUrl, request, Map.class);

            System.out.println("📥 Código de respuesta HTTP: " + response.getStatusCode());
            System.out.println("Respuesta: " + response.getBody());

            Map<String, Object> body = response.getBody();
            Boolean success = (Boolean) body.get("success");

            if (body.containsKey("error-codes")) {
                System.out.println("❌ Error codes: " + body.get("error-codes"));
            }

            return success != null && success;

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}