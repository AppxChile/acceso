package com.acceso.acceso.dto;



public class IngresosByHorasDto {

    private String fecha;
    private Integer hora;
    private Integer total;
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public Integer getHora() {
        return hora;
    }
    public void setHora(Integer hora) {
        this.hora = hora;
    }
    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer cantidad) {
        this.total = cantidad;
    }

    



}
