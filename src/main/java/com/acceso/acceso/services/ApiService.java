package com.acceso.acceso.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.acceso.acceso.controllers.UsuarioResponse;
import com.acceso.acceso.dto.PersonaResponse;

import reactor.core.publisher.Mono;

@Service
public class ApiService {

    private final WebClient webClientPersonas;

    private final WebClient webClientUsuarios;

    public ApiService(WebClient.Builder webClientBuilder, @Value("${api.persona.url}") String apiUrlPersona,
            @Value("${api.usuarios.url}") String apiUrlUsuario) {
        this.webClientPersonas = webClientBuilder.baseUrl(apiUrlPersona).build();
        this.webClientUsuarios = webClientBuilder.baseUrl(apiUrlUsuario).build();
    }

    public PersonaResponse obtenerDatosPersona(Integer rut) {
        return webClientPersonas.get()
                .uri("/{rut}", rut)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, _ -> Mono.empty())
                .bodyToMono(PersonaResponse.class)
                .onErrorResume(WebClientResponseException.class, _ -> Mono.empty())
                .block(); // Bloquea hasta recibir la respuesta (sincrónico)
    }

    public UsuarioResponse obtenerUsuario(String username) {

        return webClientUsuarios.get()
                .uri("/buscar/{username}", username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, _ -> Mono.empty())
                .bodyToMono(UsuarioResponse.class)
                .onErrorResume(WebClientResponseException.class, _ -> Mono.empty())
                .block(); // Bloquea hasta recibir la respuesta (sincrónico)

    }

}
