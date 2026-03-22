/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.inventario.app.repository;

/**
 *
 * @author jorge
 */
import com.inventario.app.model.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Integer> {
    // Aquí podrías crear métodos personalizados, por ejemplo:
    // List<Articulo> findByNombreContaining(String nombre);
}