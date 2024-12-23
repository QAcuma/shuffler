package ru.acuma.shuffler.service.extentions;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class NamingService {

    public static final String WORD = "/word";

    private final RestTemplate restTemplate;

    @Value("${application.client.word-generator.url}")
    private String generatorUrl;

    public String getWord() {
//        var response = restTemplate.getForEntity(generatorUrl + WORD, String.class);
        return RandomStringUtils.random(6, true, true);

//        return Objects.equals(response.getStatusCode().value(), 200) && response.getBody() != null
//               ? response.getBody().replaceAll("\\W", "")
//               : RandomStringUtils.randomAlphanumeric(6);
    }
}
