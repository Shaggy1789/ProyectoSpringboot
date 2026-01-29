package com.master.springboot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.util.Map;

@Service
public class AuthCaptchaService {
    @Value("${recaptcha.secret.key}")
    private String secretKey;

    @Value("${recaptcha.url}")
    private String recaptchaUrl;

    public boolean verifyRecaptcha(String recaptchaResponse){
        if(recaptchaResponse==null || recaptchaResponse.isEmpty()){
            return false;
        }

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("secret",secretKey);
        params.add("response", recaptchaResponse);

        try{
            Map<String, Object> response = restTemplate.postForObject(
                    recaptchaUrl,params,Map.class);
            return response != null && (Boolean) response.get("success");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
