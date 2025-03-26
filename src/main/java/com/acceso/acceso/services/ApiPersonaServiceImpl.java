package com.acceso.acceso.services;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.acceso.acceso.config.ApiProperties;
import com.acceso.acceso.dto.PersonaResponse;
import com.acceso.acceso.services.interfaces.ApiPersonaService;

import reactor.core.publisher.Mono;

@Service
public class ApiPersonaServiceImpl implements ApiPersonaService {

    private final WebClient webClientPersonas;

    public ApiPersonaServiceImpl(WebClient.Builder webClientBuilder, ApiProperties apiProperties) {
        this.webClientPersonas = webClientBuilder.baseUrl(apiProperties.getPersonaUrl()).build();
    }

    @Override
    public PersonaResponse getPersonaInfo(Integer rut) {
        return webClientPersonas.get()
                .uri("/{rut}", rut)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(PersonaResponse.class)
                .onErrorResume(Exception.class, e -> Mono.empty())
                .block();
    }

}
