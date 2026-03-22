/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.app.service;

import com.inventario.app.model.Empleado;
import com.inventario.app.repository.EmpleadoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService {

   
  
    
    private final EmpleadoRepository empleadoRepository ; 

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }
    
    
    public Empleado crearEmpleado(Empleado e){
        
        
        return empleadoRepository.save(e); 
        
    }
    
    public List<Empleado> listarEmpleados(){
        
        
        return empleadoRepository.findAll(); 
        
    }
    
    
    
    
    
    
    
}
