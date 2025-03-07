package com.acceso.acceso.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.acceso.acceso.dto.PersonaResponse;
import com.acceso.acceso.dto.UsuarioResponse;

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

}
