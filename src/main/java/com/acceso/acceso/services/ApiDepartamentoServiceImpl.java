package com.acceso.acceso.services;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.acceso.acceso.config.ApiProperties;
import com.acceso.acceso.dto.DepartamentoResponse;
import com.acceso.acceso.dto.ListDepartamentosDto;
import com.acceso.acceso.services.interfaces.ApiDepartamentoService;

import reactor.core.publisher.Mono;

@Service
public class ApiDepartamentoServiceImpl implements ApiDepartamentoService {

    private final WebClient webClientUsuarios;

    public ApiDepartamentoServiceImpl(WebClient.Builder webClientBuilder, ApiProperties apiProperties) {
        this.webClientUsuarios = webClientBuilder.baseUrl(apiProperties.getUsuariosUrl()).build();
    }

    @Override
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

    @Override
    public DepartamentoResponse getDepartamentoById(Long id) {
        try {
            return webClientUsuarios.get()
                    .uri("/departamentos/byId/{id}", id) // Corregido el paréntesis en la URL
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                    .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.empty())
                    .bodyToMono(DepartamentoResponse.class)
                    .onErrorResume(Exception.class, e -> Mono.empty())
                    .block();

        } catch (Exception e) {
            return null; // O puedes devolver un valor por defecto, dependiendo de tu lógica
        }
    }

}
