package com.acceso.acceso.dto;

public class AssignRequest {

    private Long id;
    private String login;
    private Long moduloId;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public Long getModuloId() {
        return moduloId;
    }
    public void setModuloId(Long moduloId) {
        this.moduloId = moduloId;
    }

    

}
