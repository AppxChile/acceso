package com.acceso.acceso.services;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.acceso.acceso.config.ApiProperties;
import com.acceso.acceso.dto.ListDepartamentosDto;
import com.acceso.acceso.dto.PersonaResponse;
import com.acceso.acceso.dto.UsuarioResponse;

import reactor.core.publisher.Mono;

@Service
public class ApiService {

    private final WebClient webClientPersonas;

    private final WebClient webClientUsuarios;

    public ApiService(WebClient.Builder webClientBuilder, ApiProperties apiProperties) {
        this.webClientPersonas = webClientBuilder.baseUrl(apiProperties.getPersonaUrl()).build();
        this.webClientUsuarios = webClientBuilder.baseUrl(apiProperties.getUsuariosUrl()).build();
    }

    public PersonaResponse getPersonaInfo(Integer rut) {
        return webClientPersonas.get()
                .uri("/{rut}", rut)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(PersonaResponse.class)
                .onErrorResume(Exception.class, e -> Mono.empty())
                .block();
    }

    public UsuarioResponse getUsuario(String username) {

        return webClientUsuarios.get()
                .uri("/buscar/{username}", username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(UsuarioResponse.class)
                .onErrorResume(Exception.class, e -> Mono.empty())
                .block();

    }

    public List<ListDepartamentosDto> getDepartamentos() {
        return webClientUsuarios.get()
                .uri("/departamentos/list")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(new ParameterizedTypeReference<List<ListDepartamentosDto>>() {
                })
                .onErrorResume(Exception.class, e -> Mono.empty())
                .block();
    }

}
