

package com.inventario.app.controller;

import com.inventario.app.model.Empleado;
import com.inventario.app.service.EmpleadoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    
    
    private final EmpleadoService empleadoService ; 

    public EmpleadoController(EmpleadoService e ) {
        this.empleadoService = e ; 
    }
    
    
    @GetMapping("/listar")
    public List<Empleado> listarEmpleados(){
        
        
        return empleadoService.listarEmpleados(); 
        
    }
    
    @PostMapping("/crear")
    public Empleado crearEmpleado(@RequestBody Empleado e){
        
        return empleadoService.crearEmpleado(e);
        
        
        
    }
    
    
    
    
    
    
}
