package com.inventario.app.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Movimientos")

public class Movimientos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMovimientos;

    private Integer cantidad;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public Integer getIdMovimientos() {
        return idMovimientos;
    }

    public void setIdMovimientos(Integer idMovimientos) {
        this.idMovimientos = idMovimientos;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Articulo getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Articulo idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_articulo", referencedColumnName = "idArticulo")
    private Articulo idArticulo;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_empleado", referencedColumnName = "idEmpleado")
    private Empleado idEmpleado;

 
  
}