package com.acceso.acceso.services;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.acceso.acceso.config.ApiProperties;
import com.acceso.acceso.dto.UsuarioResponse;
import com.acceso.acceso.services.interfaces.ApiUsuariosService;

import reactor.core.publisher.Mono;

@Service
public class ApiUsuarioServiceImpl implements ApiUsuariosService {

    private final WebClient webClientUsuarios;

    public ApiUsuarioServiceImpl(WebClient.Builder webClientBuilder, ApiProperties apiProperties) {
        this.webClientUsuarios = webClientBuilder.baseUrl(apiProperties.getUsuariosUrl()).build();
    }

    @Override
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
