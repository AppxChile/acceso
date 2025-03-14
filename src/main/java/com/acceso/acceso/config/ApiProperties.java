package com.acceso.acceso.config;

import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

    private String personaUrl;

    private String usuariosUrl;

    public String getPersonaUrl() {
        return personaUrl;
    }

    public void setPersonaUrl(String personaUrl) {
        this.personaUrl = personaUrl;
    }

    public String getUsuariosUrl() {
        return usuariosUrl;
    }

    public void setUsuariosUrl(String usuariosUrl) {
        this.usuariosUrl = usuariosUrl;
    }

}
