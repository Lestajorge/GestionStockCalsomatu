/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.inventario.app.repository;

/**
 *
 * @author jorge
 */
import com.inventario.app.model.Movimientos;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
@Repository
public interface MovimientosRepository extends JpaRepository<Movimientos, Integer> {

    List<Movimientos> findAllByOrderByIdMovimientosDesc();

    // Cambia ?1, ?2, ?3 por nombres claros para evitar confusiones de posición
    @Query("SELECT m FROM Movimientos m WHERE m.idEmpleado.idEmpleado = :idEmp AND m.fecha BETWEEN :ini AND :fin")
    List<Movimientos> findByEmpleadoAndFechaBetween(
        @Param("idEmp") Integer idEmp, 
        @Param("ini") Date ini, 
        @Param("fin") Date fin);
}