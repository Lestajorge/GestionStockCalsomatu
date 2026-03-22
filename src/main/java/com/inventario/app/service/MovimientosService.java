/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.app.service;

import com.inventario.app.model.Movimientos;
import com.inventario.app.repository.MovimientosRepository;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class MovimientosService {

    private final MovimientosRepository movRepository;

    public Movimientos crearMovimiento(Movimientos m) {

        return movRepository.save(m);

    }

    public Movimientos obtenerMovimientoPorId(@PathVariable Integer id) {

        return movRepository.findById(id).orElse(null);

    }

    public List<Movimientos> obtenerMovimientos(@PathVariable
            Integer id) {

        return movRepository.findAll();

    }

    public MovimientosService(MovimientosRepository movRepository) {
        this.movRepository = movRepository;
    }

    public Movimientos guardarMovimiento(Movimientos m) {
            
        return movRepository.save(m); 
    }

    public List<Movimientos> listarTodos(){

return movRepository.findAll(); 

    }

}
