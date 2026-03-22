package com.inventario.app.model;

import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "Empleado")

public class Empleado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmpleado;

    private String numeroEmp;

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNumeroEmp() {
        return numeroEmp;
    }

    public void setNumeroEmp(String numeroEmp) {
        this.numeroEmp = numeroEmp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    private String nombre;
    private String apellidos;
    private boolean activo;
}